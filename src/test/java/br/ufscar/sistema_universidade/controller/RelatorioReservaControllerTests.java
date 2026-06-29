package br.ufscar.sistema_universidade.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import br.ufscar.sistema_universidade.repository.RelatorioRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

class RelatorioReservaControllerTests {

    private RelatorioRepository relatorioRepository;
    private RelatorioReservaController controller;

    @BeforeEach
    void configurar() {
        relatorioRepository = org.mockito.Mockito.mock(RelatorioRepository.class);
        controller = new RelatorioReservaController(relatorioRepository);
    }

    @Test
    void deveConsultarReservasComPeriodoEStatusValidos() {
        LocalDate inicio = LocalDate.of(2026, 6, 1);
        LocalDate fim = LocalDate.of(2026, 6, 30);
        Model model = new ExtendedModelMap();
        when(relatorioRepository.emitirRelatorioReservasPorPeriodo(inicio, fim, "aprovada"))
                .thenReturn(List.of());

        String view = controller.relatorioReservas(inicio, fim, "aprovada", model);

        assertEquals("relatorio-reservas", view);
        assertEquals("aprovada", model.asMap().get("statusSelecionado"));
        verify(relatorioRepository).emitirRelatorioReservasPorPeriodo(inicio, fim, "aprovada");
    }

    @Test
    void deveRejeitarPeriodoComDataFinalAnteriorAInicial() {
        LocalDate inicio = LocalDate.of(2026, 6, 30);
        LocalDate fim = LocalDate.of(2026, 6, 1);
        Model model = new ExtendedModelMap();

        String view = controller.relatorioReservas(inicio, fim, null, model);

        assertEquals("relatorio-reservas", view);
        assertTrue(model.asMap().containsKey("erro"));
        verifyNoInteractions(relatorioRepository);
    }
}
