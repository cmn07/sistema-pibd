package br.ufscar.sistema_universidade.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.ufscar.sistema_universidade.model.Reserva;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

class ReservaRepositoryTests {

    private JdbcTemplate jdbcTemplate;
    private ReservaRepository repository;

    @BeforeEach
    void configurar() {
        jdbcTemplate = mock(JdbcTemplate.class);
        repository = new ReservaRepository(jdbcTemplate);
    }

    @Test
    void deveVerificarNovaReservaSemParametroNuloNaConsulta() {
        Reserva reserva = reserva(null);
        when(jdbcTemplate.queryForObject(
                any(String.class), eq(Integer.class), any(), any(), any(), any()))
            .thenReturn(0);

        assertFalse(repository.existeConflito(reserva));

        verify(jdbcTemplate).queryForObject(
            any(String.class), eq(Integer.class), any(), any(), any(), any());
    }

    @Test
    void deveIgnorarAPropriaReservaDuranteEdicao() {
        Reserva reserva = reserva(42L);
        when(jdbcTemplate.queryForObject(
                contains("id_reserva <> ?"), eq(Integer.class), any(), any(), any(), any(), eq(42L)))
            .thenReturn(1);

        assertTrue(repository.existeConflito(reserva));

        verify(jdbcTemplate).queryForObject(
            contains("id_reserva <> ?"), eq(Integer.class), any(), any(), any(), any(), eq(42L));
    }

    private Reserva reserva(Long id) {
        return new Reserva(
            id,
            LocalDate.of(2026, 7, 2),
            "aula",
            "pendente",
            "Teste",
            LocalTime.of(10, 0),
            LocalTime.of(11, 0),
            1L,
            1L,
            null
        );
    }
}
