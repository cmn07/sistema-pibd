package br.ufscar.sistema_universidade.controller;

import br.ufscar.sistema_universidade.repository.RelatorioRepository;
import br.ufscar.sistema_universidade.service.ReservaService;
import java.time.LocalDate;
import java.util.Collections;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RelatorioReservaController {

    private final RelatorioRepository relatorioRepository;

    public RelatorioReservaController(RelatorioRepository relatorioRepository) {
        this.relatorioRepository = relatorioRepository;
    }

    @GetMapping("/relatorios/reservas")
    public String relatorioReservas(
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            @RequestParam(required = false) String status,
            Model model
    ) {
        LocalDate inicio = dataInicio == null ? LocalDate.now() : dataInicio;
        LocalDate fim = dataFim == null ? LocalDate.now().plusDays(30) : dataFim;
        String statusSelecionado = status == null || status.isBlank() ? null : status;

        model.addAttribute("dataInicio", inicio);
        model.addAttribute("dataFim", fim);
        model.addAttribute("statusSelecionado", statusSelecionado);
        model.addAttribute("statusDisponiveis", ReservaService.STATUS_RESERVA);

        if (fim.isBefore(inicio)) {
            model.addAttribute("erro", "A data final deve ser igual ou posterior a data inicial.");
            model.addAttribute("relatorio", Collections.emptyList());
            return "relatorio-reservas";
        }

        if (statusSelecionado != null && !ReservaService.STATUS_RESERVA.contains(statusSelecionado)) {
            model.addAttribute("erro", "Status de reserva invalido.");
            model.addAttribute("relatorio", Collections.emptyList());
            return "relatorio-reservas";
        }

        model.addAttribute(
                "relatorio",
                relatorioRepository.emitirRelatorioReservasPorPeriodo(inicio, fim, statusSelecionado)
        );
        return "relatorio-reservas";
    }
}
