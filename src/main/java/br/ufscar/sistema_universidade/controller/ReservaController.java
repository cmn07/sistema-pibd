package br.ufscar.sistema_universidade.controller;

import br.ufscar.sistema_universidade.model.Reserva;
import br.ufscar.sistema_universidade.model.UsuarioLogado;
import br.ufscar.sistema_universidade.repository.AdministradorRepository;
import br.ufscar.sistema_universidade.repository.FuncionarioRepository;
import br.ufscar.sistema_universidade.repository.SalaRepository;
import br.ufscar.sistema_universidade.service.ReservaService;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class    ReservaController {

    private static final String MENSAGEM_CONFLITO_INTEGRIDADE =
        "Nao foi possivel inserir ou realizar a acao. Ja existe um registro com a mesma chave ou com dados conflitantes.";

    private final ReservaService reservaService;
    private final FuncionarioRepository funcionarioRepository;
    private final SalaRepository salaRepository;
    private final AdministradorRepository administradorRepository;

    public ReservaController(
        ReservaService reservaService,
        FuncionarioRepository funcionarioRepository,
        SalaRepository salaRepository,
        AdministradorRepository administradorRepository
    ) {
        this.reservaService = reservaService;
        this.funcionarioRepository = funcionarioRepository;
        this.salaRepository = salaRepository;
        this.administradorRepository = administradorRepository;
    }

    @FunctionalInterface
    private interface OperacaoPersistencia {
        void executar();
    }

    private String executarComTratamentoDeErro(
        String destino,
        String mensagemSucesso,
        RedirectAttributes redirectAttributes,
        OperacaoPersistencia operacao
    ) {
        try {
            operacao.executar();
            redirectAttributes.addFlashAttribute("mensagem", mensagemSucesso);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensagem", ex.getMessage());
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("mensagem", MENSAGEM_CONFLITO_INTEGRIDADE);
        }
        return "redirect:" + destino;
    }

    @GetMapping({"/reservas", "/reservas/nova"})
    public String reservas(Model model, @RequestParam(required = false) Long editar,
                           @AuthenticationPrincipal UsuarioLogado usuarioLogado) {
        boolean adminOperacional = usuarioLogado.temPerfil("ROLE_ADMIN_OPERACIONAL");
        model.addAttribute("reservas", adminOperacional
            ? reservaService.listarTodos()
            : reservaService.listarPorUsuario(usuarioLogado.getIdPessoa()));
        model.addAttribute("funcionarios", funcionarioRepository.listarTodos());
        model.addAttribute("salas", salaRepository.listarTodos());
        model.addAttribute("administradores", administradorRepository.listarTodos());
        model.addAttribute("tiposReserva", ReservaService.TIPOS_RESERVA);
        model.addAttribute("statusReserva", ReservaService.STATUS_RESERVA);
        model.addAttribute("adminOperacional", adminOperacional);
        model.addAttribute("reservaSelecionada", adminOperacional && editar != null ? reservaService.buscarPorId(editar) : null);
        return "reservas";
    }

    @PostMapping("/reservas/salvar")
    public String salvarReserva(
        @RequestParam LocalDate data,
        @RequestParam String tipoReserva,
        @RequestParam(required = false) String objetivo,
        @RequestParam LocalTime horarioInicio,
        @RequestParam LocalTime horarioFim,
        @RequestParam(required = false) Long codigoUsuario,
        @RequestParam Long codigoSala,
        @AuthenticationPrincipal UsuarioLogado usuarioLogado,
        RedirectAttributes redirectAttributes
    ) {
        Long solicitante = usuarioLogado.temPerfil("ROLE_ADMIN_OPERACIONAL") && codigoUsuario != null
            ? codigoUsuario
            : usuarioLogado.getIdPessoa();

        return executarComTratamentoDeErro(
            "/reservas",
            "Reserva salva com sucesso.",
            redirectAttributes,
            () -> reservaService.salvar(new Reserva(
                null,
                data,
                tipoReserva,
                "pendente",
                objetivo,
                horarioInicio,
                horarioFim,
                solicitante,
                codigoSala,
                null
            ))
        );
    }

    @PostMapping("/reservas/editar")
    public String editarReserva(
        @RequestParam Long idReserva,
        @RequestParam LocalDate data,
        @RequestParam String tipoReserva,
        @RequestParam String statusReserva,
        @RequestParam(required = false) String objetivo,
        @RequestParam LocalTime horarioInicio,
        @RequestParam LocalTime horarioFim,
        @RequestParam Long codigoUsuario,
        @RequestParam Long codigoSala,
        @AuthenticationPrincipal UsuarioLogado usuarioLogado,
        RedirectAttributes redirectAttributes
    ) {
        exigirAdminOperacional(usuarioLogado);
        return executarComTratamentoDeErro(
            "/reservas",
            "Reserva atualizada com sucesso.",
            redirectAttributes,
            () -> reservaService.atualizar(new Reserva(
                idReserva,
                data,
                tipoReserva,
                statusReserva,
                objetivo,
                horarioInicio,
                horarioFim,
                codigoUsuario,
                codigoSala,
                usuarioLogado.getIdPessoa()
            ))
        );
    }

    @PostMapping("/reservas/excluir")
    public String excluirReserva(
        @RequestParam Long idReserva,
        @AuthenticationPrincipal UsuarioLogado usuarioLogado,
        RedirectAttributes redirectAttributes
    ) {
        exigirAdminOperacional(usuarioLogado);
        return executarComTratamentoDeErro(
            "/reservas",
            "Reserva excluida com sucesso.",
            redirectAttributes,
            () -> reservaService.excluir(idReserva)
        );
    }

    private void exigirAdminOperacional(UsuarioLogado usuarioLogado) {
        if (usuarioLogado == null || !usuarioLogado.temPerfil("ROLE_ADMIN_OPERACIONAL")) {
            throw new AccessDeniedException("Apenas admin operacional pode alterar reservas.");
        }
    }
}
