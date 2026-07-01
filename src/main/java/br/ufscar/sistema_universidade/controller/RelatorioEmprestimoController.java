package br.ufscar.sistema_universidade.controller;

import br.ufscar.sistema_universidade.model.UsuarioLogado;
import br.ufscar.sistema_universidade.repository.RelatorioRepository;
import java.time.LocalDate;
import java.util.Collections;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/relatorios")
public class RelatorioEmprestimoController {

    private final RelatorioRepository relatorioRepository;

    public RelatorioEmprestimoController(RelatorioRepository relatorioRepository) {
        this.relatorioRepository = relatorioRepository;
    }

    @GetMapping("/emprestimos")
    public String relatorioEmprestimos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @AuthenticationPrincipal UsuarioLogado usuarioLogado,
            Model model
    ) {
        exigirAdminOperacional(usuarioLogado);

        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);

        if (dataInicio == null || dataFim == null) {
            model.addAttribute("relatorio", Collections.emptyList());
            model.addAttribute("mensagem", "Informe a data inicial e a data final para gerar o relatorio.");
            return "relatorios/emprestimos";
        }

        if (dataFim.isBefore(dataInicio)) {
            model.addAttribute("relatorio", Collections.emptyList());
            model.addAttribute("mensagem", "A data final deve ser igual ou posterior a data inicial.");
            return "relatorios/emprestimos";
        }

        model.addAttribute("relatorio", relatorioRepository.emitirRelatorioEmprestimosPorPeriodo(dataInicio, dataFim));
        return "relatorios/emprestimos";
    }

    private void exigirAdminOperacional(UsuarioLogado usuarioLogado) {
        if (usuarioLogado == null || !usuarioLogado.temPerfil("ROLE_ADMIN_OPERACIONAL")) {
            throw new AccessDeniedException("Apenas admin operacional pode acessar este relatorio.");
        }
    }
}
