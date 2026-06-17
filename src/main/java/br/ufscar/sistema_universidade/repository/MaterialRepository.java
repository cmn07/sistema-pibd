package br.ufscar.sistema_universidade.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MaterialRepository {

    private final JdbcTemplate jdbcTemplate;

    public MaterialRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // TODO: Implementar consultas SQL de material_acervo, livro, periodico, outros e autor.
}
