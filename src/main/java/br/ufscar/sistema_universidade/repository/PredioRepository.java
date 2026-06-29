package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.model.Predio;
import br.ufscar.sistema_universidade.model.Campus;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PredioRepository {

    private final JdbcTemplate jdbcTemplate;

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
                """
                SELECT 
                    p.codigo, p.nome, p.bloco, p.codigo_campus,
                    c.codigo as campus_codigo, c.nome as campus_nome, c.cidade as campus_cidade
                FROM predio p
                LEFT JOIN campus c ON p.codigo_campus = c.codigo
                WHERE p.codigo = ?
                """,
                (rs, rowNum) -> {
                    Predio predio = new Predio(
                            rs.getLong("codigo"),
                            rs.getString("nome"),
                            rs.getString("bloco"),
                            rs.getLong("codigo_campus")
                    );
                    if (rs.getObject("campus_nome") != null) {
                        predio.setCampus(new Campus(
                                rs.getLong("campus_codigo"),
                                rs.getString("campus_nome"),
                                rs.getString("campus_cidade")
                        ));
                    }
                    return predio;
                },
                codigo
        );
        return predios.isEmpty() ? null : predios.get(0);
    }

    public List<Predio> listarTodos() {
        return jdbcTemplate.query(
                """
                SELECT 
                    p.codigo, p.nome, p.bloco, p.codigo_campus,
                    c.codigo as campus_codigo, c.nome as campus_nome, c.cidade as campus_cidade
                FROM predio p
                LEFT JOIN campus c ON p.codigo_campus = c.codigo
                ORDER BY p.nome
                """,
                (rs, rowNum) -> {
                    Predio predio = new Predio(
                            rs.getLong("codigo"),
                            rs.getString("nome"),
                            rs.getString("bloco"),
                            rs.getLong("codigo_campus")
                    );
                    if (rs.getObject("campus_nome") != null) {
                        predio.setCampus(new Campus(
                                rs.getLong("campus_codigo"),
                                rs.getString("campus_nome"),
                                rs.getString("campus_cidade")
                        ));
                    }
                    return predio;
                }
        );
    }

    public List<Predio> listarPorCampus(Long codigoCampus) {
        return jdbcTemplate.query(
                """
                SELECT 
                    p.codigo, p.nome, p.bloco, p.codigo_campus,
                    c.codigo as campus_codigo, c.nome as campus_nome, c.cidade as campus_cidade
                FROM predio p
                LEFT JOIN campus c ON p.codigo_campus = c.codigo
                WHERE p.codigo_campus = ?
                ORDER BY p.nome
                """,
                (rs, rowNum) -> {
                    Predio predio = new Predio(
                            rs.getLong("codigo"),
                            rs.getString("nome"),
                            rs.getString("bloco"),
                            rs.getLong("codigo_campus")
                    );
                    if (rs.getObject("campus_nome") != null) {
                        predio.setCampus(new Campus(
                                rs.getLong("campus_codigo"),
                                rs.getString("campus_nome"),
                                rs.getString("campus_cidade")
                        ));
                    }
                    return predio;
                },
                codigoCampus
        );
    }
}
