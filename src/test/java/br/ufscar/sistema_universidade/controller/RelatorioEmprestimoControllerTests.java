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

class RelatorioEmprestimoControllerTests {

    private RelatorioRepository relatorioRepository;
    private RelatorioEmprestimoController controller;

    @BeforeEach
    void configurar() {
        relatorioRepository = org.mockito.Mockito.mock(RelatorioRepository.class);
        controller = new RelatorioEmprestimoController(relatorioRepository);
    }

    @Test
    void deveConsultarEmprestimosNoPeriodoInformado() {
        LocalDate inicio = LocalDate.of(2026, 6, 1);
        LocalDate fim = LocalDate.of(2026, 6, 30);
        Model model = new ExtendedModelMap();
        when(relatorioRepository.emitirRelatorioEmprestimosPorPeriodo(inicio, fim))
                .thenReturn(List.of());

        String view = controller.relatorioEmprestimos(inicio, fim, model);

        assertEquals("relatorio-emprestimos", view);
        assertEquals(inicio, model.asMap().get("dataInicio"));
        assertEquals(fim, model.asMap().get("dataFim"));
        verify(relatorioRepository).emitirRelatorioEmprestimosPorPeriodo(inicio, fim);
    }

    @Test
    void deveRejeitarPeriodoComDataFinalAnteriorAInicial() {
        LocalDate inicio = LocalDate.of(2026, 6, 30);
        LocalDate fim = LocalDate.of(2026, 6, 1);
        Model model = new ExtendedModelMap();

        String view = controller.relatorioEmprestimos(inicio, fim, model);

        assertEquals("relatorio-emprestimos", view);
        assertTrue(model.asMap().containsKey("erro"));
        verifyNoInteractions(relatorioRepository);
    }
}
