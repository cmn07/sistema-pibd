package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.model.Sala;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SalaRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Sala> SALA_ROW_MAPPER = (rs, rowNum) ->
        new Sala(
            rs.getLong("codigo"),
            rs.getString("numero_sala"),
            rs.getString("categoria"),
            rs.getInt("capacidade"),
            rs.getLong("codigo_predio")
        );

    public SalaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Sala> listarTodos() {
        return jdbcTemplate.query(
            """
            SELECT codigo, numero_sala, categoria, capacidade, codigo_predio
            FROM sala
            ORDER BY numero_sala
            """,
            SALA_ROW_MAPPER
        );
    }

    public Sala buscarPorId(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM sala WHERE codigo = ?", new Object[]{id}, SALA_ROW_MAPPER);
    }

    public void salvar(Sala sala) {
        jdbcTemplate.update("INSERT INTO sala (numero_sala, categoria, capacidade, codigo_predio) VALUES (?, ?, ?, ?)",
                sala.getNumeroSala(), sala.getCategoria(), sala.getCapacidade(), sala.getCodigoPredio());
    }

    public void atualizar(Sala sala) {
        jdbcTemplate.update("UPDATE sala SET numero_sala = ?, categoria = ?, capacidade = ?, codigo_predio = ? WHERE codigo = ?",
                sala.getNumeroSala(), sala.getCategoria(), sala.getCapacidade(), sala.getCodigoPredio(), sala.getCodigo());
    }

    public void deletarPorId(Long id) {
        jdbcTemplate.update("DELETE FROM sala WHERE codigo = ?", id);
    }}
