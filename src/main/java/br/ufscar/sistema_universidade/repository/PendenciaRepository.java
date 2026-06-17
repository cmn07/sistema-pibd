package br.ufscar.sistema_universidade.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PendenciaRepository {

    private final JdbcTemplate jdbcTemplate;

    public PendenciaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // TODO: Implementar consultas SQL de pendencias.
}
