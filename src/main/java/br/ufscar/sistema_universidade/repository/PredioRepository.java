package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.model.Predio;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PredioRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Predio> PREDIO_ROW_MAPPER = (rs, rowNum) ->
            new Predio(
                    rs.getLong("codigo"),
                    rs.getString("nome"),
                    rs.getString("bloco"),
                    rs.getLong("codigo_campus")
            );

    public PredioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int salvar(Predio predio) {
        return jdbcTemplate.update(
                "INSERT INTO predio (nome, bloco, codigo_campus) VALUES (?, ?, ?)",
                predio.getNome(),
                predio.getBloco(),
                predio.getCodigoCampus()
        );
    }

    public int atualizar(Predio predio) {
        return jdbcTemplate.update(
                "UPDATE predio SET nome = ?, bloco = ?, codigo_campus = ? WHERE codigo = ?",
                predio.getNome(),
                predio.getBloco(),
                predio.getCodigoCampus(),
                predio.getCodigo()
        );
    }

    public int deletarPorId(Long codigo) {
        return jdbcTemplate.update("DELETE FROM predio WHERE codigo = ?", codigo);
    }

    public Predio buscarPorId(Long codigo) {
        List<Predio> predios = jdbcTemplate.query(
                "SELECT codigo, nome, bloco, codigo_campus FROM predio WHERE codigo = ?",
                PREDIO_ROW_MAPPER,
                codigo
        );
        return predios.isEmpty() ? null : predios.get(0);
    }

    public List<Predio> listarTodos() {
        return jdbcTemplate.query(
                "SELECT codigo, nome, bloco, codigo_campus FROM predio ORDER BY nome",
                PREDIO_ROW_MAPPER
        );
    }

    public List<Predio> listarPorCampus(Long codigoCampus) {
        return jdbcTemplate.query(
                "SELECT codigo, nome, bloco, codigo_campus FROM predio WHERE codigo_campus = ? ORDER BY nome",
                PREDIO_ROW_MAPPER,
                codigoCampus
        );
    }
}