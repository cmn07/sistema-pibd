package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.model.Campus;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CampusRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Campus> CAMPUS_ROW_MAPPER = (rs, rowNum) ->
            new Campus(
                    rs.getLong("codigo"),
                    rs.getString("nome"),
                    rs.getString("cidade")
            );

    public CampusRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int salvar(Campus campus) {
        return jdbcTemplate.update(
                "INSERT INTO campus (nome, cidade) VALUES (?, ?)",
                campus.getNome(),
                campus.getCidade()
        );
    }

    public int atualizar(Campus campus) {
        return jdbcTemplate.update(
                "UPDATE campus SET nome = ?, cidade = ? WHERE codigo = ?",
                campus.getNome(),
                campus.getCidade(),
                campus.getCodigo()
        );
    }

    public int deletarPorId(Long codigo) {
        return jdbcTemplate.update("DELETE FROM campus WHERE codigo = ?", codigo);
    }

    public Campus buscarPorId(Long codigo) {
        List<Campus> campi = jdbcTemplate.query(
                "SELECT codigo, nome, cidade FROM campus WHERE codigo = ?",
                CAMPUS_ROW_MAPPER,
                codigo
        );
        return campi.isEmpty() ? null : campi.get(0);
    }

    public List<Campus> listarTodos() {
        return jdbcTemplate.query(
                "SELECT codigo, nome, cidade FROM campus ORDER BY nome",
                CAMPUS_ROW_MAPPER
        );
    }
}