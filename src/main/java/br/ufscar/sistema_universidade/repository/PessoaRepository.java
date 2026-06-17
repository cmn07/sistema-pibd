package br.ufscar.sistema_universidade.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PessoaRepository {

    private final JdbcTemplate jdbcTemplate;

    public PessoaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // TODO: Implementar consultas SQL de pessoas usando JdbcTemplate.
}
