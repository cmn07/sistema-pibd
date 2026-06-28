package br.ufscar.sistema_universidade.controller;

import br.ufscar.sistema_universidade.model.UsuarioLogado;
import br.ufscar.sistema_universidade.service.EmprestimoService;
import br.ufscar.sistema_universidade.service.PendenciaService;
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
public class PendenciaController {

    private static final String MENSAGEM_CONFLITO_INTEGRIDADE =
            "Nao foi possivel cadastrar a pendencia. Verifique o emprestimo e o administrador responsavel.";

    private final PendenciaService pendenciaService;
    private final EmprestimoService emprestimoService;

    public PendenciaController(PendenciaService pendenciaService, EmprestimoService emprestimoService) {
        this.pendenciaService = pendenciaService;
        this.emprestimoService = emprestimoService;
    }

    @GetMapping("/pendencias")
    public String pendencias(Model model, @AuthenticationPrincipal UsuarioLogado usuarioLogado) {
        boolean adminOperacional = usuarioLogado.temPerfil("ROLE_ADMIN_OPERACIONAL");
        model.addAttribute("adminOperacional", adminOperacional);
        model.addAttribute("pendencias", adminOperacional
                ? pendenciaService.listarTodasResumidas()
                : pendenciaService.listarResumidasPorUsuario(usuarioLogado.getIdPessoa()));
        model.addAttribute("emprestimos", adminOperacional
                ? emprestimoService.listarTodosResumidos()
                : java.util.List.of());
        return "pendencias";
    }

    @PostMapping("/pendencias/salvar")
    public String salvarPendencia(
            @RequestParam Long codigoEmprestimo,
            @RequestParam String motivo,
            @AuthenticationPrincipal UsuarioLogado usuarioLogado,
            RedirectAttributes redirectAttributes
    ) {
        exigirAdminOperacional(usuarioLogado);
        try {
            emprestimoService.criarPendencia(codigoEmprestimo, usuarioLogado.getIdPessoa(), motivo);
            redirectAttributes.addFlashAttribute("mensagem", "Pendencia cadastrada com sucesso.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensagem", ex.getMessage());
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("mensagem", MENSAGEM_CONFLITO_INTEGRIDADE);
        }
        return "redirect:/pendencias";
    }

    @PostMapping("/pendencias/encerrar")
    public String encerrarPendencia(
            @RequestParam Long codigoPendencia,
            @AuthenticationPrincipal UsuarioLogado usuarioLogado,
            RedirectAttributes redirectAttributes
    ) {
        exigirAdminOperacional(usuarioLogado);
        try {
            pendenciaService.encerrarPendencia(codigoPendencia);
            redirectAttributes.addFlashAttribute("mensagem", "Pendencia encerrada com sucesso.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensagem", ex.getMessage());
        }
        return "redirect:/pendencias";
    }

    private void exigirAdminOperacional(UsuarioLogado usuarioLogado) {
        if (usuarioLogado == null || !usuarioLogado.temPerfil("ROLE_ADMIN_OPERACIONAL")) {
            throw new AccessDeniedException("Apenas admin operacional pode cadastrar pendencias.");
        }
    }
}
