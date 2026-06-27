package br.ufscar.sistema_universidade.controller;

import br.ufscar.sistema_universidade.model.UsuarioLogado;
import br.ufscar.sistema_universidade.repository.MaterialRepository;
import br.ufscar.sistema_universidade.service.EmprestimoService;
import java.time.LocalDate;
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
public class EmprestimoController {

    private static final String MENSAGEM_CONFLITO_INTEGRIDADE =
            "Nao foi possivel realizar o emprestimo. Verifique pendencias, material e vinculo de usuario.";

    private final EmprestimoService emprestimoService;
    private final MaterialRepository materialRepository;

    public EmprestimoController(EmprestimoService emprestimoService, MaterialRepository materialRepository) {
        this.emprestimoService = emprestimoService;
        this.materialRepository = materialRepository;
    }

    @GetMapping("/emprestimos")
    public String emprestimos(
            Model model,
            @RequestParam(required = false) Long material,
            @AuthenticationPrincipal UsuarioLogado usuarioLogado
    ) {
        boolean adminOperacional = usuarioLogado.temPerfil("ROLE_ADMIN_OPERACIONAL");
        boolean usuario = usuarioLogado.temPerfil("ROLE_USUARIO");
        model.addAttribute("emprestimos", adminOperacional
                ? emprestimoService.listarTodosResumidos()
                : emprestimoService.listarAtivosResumidosPorUsuario(usuarioLogado.getIdPessoa()));
        model.addAttribute("materiais", materialRepository.listarTodos());
        model.addAttribute("dataDevolucaoSugerida", LocalDate.now().plusDays(14));
        model.addAttribute("adminOperacional", adminOperacional);
        model.addAttribute("podeRealizarEmprestimo", usuario && !adminOperacional);
        model.addAttribute("materialSelecionado", material);
        return "emprestimos";
    }

    @PostMapping("/emprestimos/salvar")
    public String salvarEmprestimo(
            @RequestParam Long codigoMaterialAcervo,
            @RequestParam LocalDate dataDevolucaoPrevista,
            @AuthenticationPrincipal UsuarioLogado usuarioLogado,
            RedirectAttributes redirectAttributes
    ) {
        try {
            emprestimoService.salvarEmprestimo(
                    usuarioLogado.getIdPessoa(),
                    codigoMaterialAcervo,
                    dataDevolucaoPrevista
            );
            redirectAttributes.addFlashAttribute("mensagem", "Emprestimo realizado com sucesso.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensagem", ex.getMessage());
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("mensagem", MENSAGEM_CONFLITO_INTEGRIDADE);
        }
        return "redirect:/emprestimos";
    }

    @PostMapping("/emprestimos/devolver")
    public String devolverEmprestimo(
            @RequestParam Long idEmprestimo,
            @AuthenticationPrincipal UsuarioLogado usuarioLogado,
            RedirectAttributes redirectAttributes
    ) {
        exigirAdminOperacional(usuarioLogado);
        return executarAcaoAdministrativa(
                "Devolucao registrada com sucesso.",
                redirectAttributes,
                () -> emprestimoService.registrarDevolucao(idEmprestimo, usuarioLogado.getIdPessoa())
        );
    }

    @PostMapping("/emprestimos/pendencia")
    public String criarPendencia(
            @RequestParam Long idEmprestimo,
            @RequestParam String motivo,
            @AuthenticationPrincipal UsuarioLogado usuarioLogado,
            RedirectAttributes redirectAttributes
    ) {
        exigirAdminOperacional(usuarioLogado);
        return executarAcaoAdministrativa(
                "Pendencia criada com sucesso.",
                redirectAttributes,
                () -> emprestimoService.criarPendencia(idEmprestimo, usuarioLogado.getIdPessoa(), motivo)
        );
    }

    private String executarAcaoAdministrativa(
            String mensagemSucesso,
            RedirectAttributes redirectAttributes,
            OperacaoEmprestimo operacao
    ) {
        try {
            operacao.executar();
            redirectAttributes.addFlashAttribute("mensagem", mensagemSucesso);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensagem", ex.getMessage());
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("mensagem", MENSAGEM_CONFLITO_INTEGRIDADE);
        }
        return "redirect:/emprestimos";
    }

    private void exigirAdminOperacional(UsuarioLogado usuarioLogado) {
        if (usuarioLogado == null || !usuarioLogado.temPerfil("ROLE_ADMIN_OPERACIONAL")) {
            throw new AccessDeniedException("Apenas admin operacional pode alterar emprestimos.");
        }
    }

    @FunctionalInterface
    private interface OperacaoEmprestimo {
        void executar();
    }
}
