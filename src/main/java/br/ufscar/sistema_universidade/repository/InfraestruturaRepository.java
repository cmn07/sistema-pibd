package br.ufscar.sistema_universidade.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class InfraestruturaRepository {

    private final JdbcTemplate jdbcTemplate;

    public InfraestruturaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // TODO: Implementar consultas SQL de campus, predios, salas e laboratorios.
}
