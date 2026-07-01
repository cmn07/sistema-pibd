package br.ufscar.sistema_universidade.controller;

import br.ufscar.sistema_universidade.repository.RelatorioRepository;
import java.time.LocalDate;
import java.util.Collections;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RelatorioEmprestimoController {

    private final RelatorioRepository relatorioRepository;

    public RelatorioEmprestimoController(RelatorioRepository relatorioRepository) {
        this.relatorioRepository = relatorioRepository;
    }

    @GetMapping("/relatorios/emprestimos")
    public String relatorioEmprestimos(
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            Model model
    ) {
        LocalDate fim = dataFim == null ? LocalDate.now() : dataFim;
        LocalDate inicio = dataInicio == null ? fim.minusDays(30) : dataInicio;

        model.addAttribute("dataInicio", inicio);
        model.addAttribute("dataFim", fim);

        if (fim.isBefore(inicio)) {
            model.addAttribute("erro", "A data final deve ser igual ou posterior a data inicial.");
            model.addAttribute("relatorio", Collections.emptyList());
            return "relatorio-emprestimos";
        }

        model.addAttribute(
                "relatorio",
                relatorioRepository.emitirRelatorioEmprestimosPorPeriodo(inicio, fim)
        );
        return "relatorio-emprestimos";
    }
}
