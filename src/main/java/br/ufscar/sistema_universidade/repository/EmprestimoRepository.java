package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.dto.EmprestimoResumoDTO;
import br.ufscar.sistema_universidade.model.Emprestimo;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class EmprestimoRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Emprestimo> EMPRESTIMO_ROW_MAPPER = (rs, rowNum) ->
            new Emprestimo(
                    rs.getLong("id_emprestimo"),
                    rs.getDate("data_emprestimo").toLocalDate(),
                    rs.getString("status_emprestimo"),
                    getNullableDate(rs, "data_devolucao_real"),
                    rs.getDate("data_devolucao_prevista").toLocalDate(),
                    rs.getLong("codigo_material_acervo"),
                    rs.getLong("codigo_usuario"),
                    getNullableLong(rs, "codigo_administrador")
            );

    private static final RowMapper<EmprestimoResumoDTO> EMPRESTIMO_RESUMO_ROW_MAPPER = (rs, rowNum) ->
            new EmprestimoResumoDTO(
                    rs.getLong("id_emprestimo"),
                    rs.getDate("data_emprestimo").toLocalDate(),
                    rs.getString("status_emprestimo"),
                    getNullableDate(rs, "data_devolucao_real"),
                    rs.getDate("data_devolucao_prevista").toLocalDate(),
                    rs.getLong("codigo_material_acervo"),
                    rs.getString("titulo_material"),
                    rs.getString("tipo_material"),
                    rs.getLong("codigo_usuario"),
                    rs.getString("nome_usuario")
            );

    public EmprestimoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int salvar(Emprestimo emprestimo) {
        return jdbcTemplate.update(
                """
                INSERT INTO emprestimo (
                    data_emprestimo, status_emprestimo, data_devolucao_real,
                    data_devolucao_prevista, codigo_material_acervo, codigo_usuario, codigo_administrador
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """,
                Date.valueOf(emprestimo.getDataEmprestimo()),
                emprestimo.getStatusEmprestimo(),
                emprestimo.getDataDevolucaoReal() == null ? null : Date.valueOf(emprestimo.getDataDevolucaoReal()),
                Date.valueOf(emprestimo.getDataDevolucaoPrevista()),
                emprestimo.getCodigoMaterialAcervo(),
                emprestimo.getCodigoUsuario(),
                emprestimo.getCodigoAdministrador()
        );
    }

    public Emprestimo buscarPorId(Long idEmprestimo) {
        List<Emprestimo> emprestimos = jdbcTemplate.query(
                """
                SELECT id_emprestimo, data_emprestimo, status_emprestimo, data_devolucao_real,
                       data_devolucao_prevista, codigo_material_acervo, codigo_usuario, codigo_administrador
                FROM emprestimo
                WHERE id_emprestimo = ?
                """,
                EMPRESTIMO_ROW_MAPPER,
                idEmprestimo
        );
        return emprestimos.isEmpty() ? null : emprestimos.get(0);
    }

    public List<EmprestimoResumoDTO> listarTodosResumidos() {
        return jdbcTemplate.query(
                sqlResumoEmprestimo() + " ORDER BY e.data_emprestimo DESC, e.id_emprestimo DESC",
                EMPRESTIMO_RESUMO_ROW_MAPPER
        );
    }

    public List<EmprestimoResumoDTO> listarResumidosPorUsuario(Long idPessoa) {
        return jdbcTemplate.query(
                sqlResumoEmprestimo() + """
                WHERE e.codigo_usuario = ?
                ORDER BY e.data_emprestimo DESC, e.id_emprestimo DESC
                """,
                EMPRESTIMO_RESUMO_ROW_MAPPER,
                idPessoa
        );
    }

    public List<EmprestimoResumoDTO> listarAtivosResumidosPorUsuario(Long idPessoa) {
        return jdbcTemplate.query(
                sqlResumoEmprestimo() + """
                WHERE e.codigo_usuario = ?
                  AND e.status_emprestimo IN ('ativo', 'atrasado')
                ORDER BY e.data_emprestimo DESC, e.id_emprestimo DESC
                """,
                EMPRESTIMO_RESUMO_ROW_MAPPER,
                idPessoa
        );
    }

    public int registrarDevolucao(Long idEmprestimo, Long codigoAdministrador) {
        return jdbcTemplate.update(
                """
                UPDATE emprestimo
                SET status_emprestimo = 'devolvido',
                    data_devolucao_real = CURRENT_DATE,
                    codigo_administrador = ?
                WHERE id_emprestimo = ?
                  AND status_emprestimo IN ('ativo', 'atrasado')
                """,
                codigoAdministrador,
                idEmprestimo
        );
    }

    public int marcarComoAtrasado(Long idEmprestimo, Long codigoAdministrador) {
        return jdbcTemplate.update(
                """
                UPDATE emprestimo
                SET status_emprestimo = 'atrasado',
                    codigo_administrador = ?
                WHERE id_emprestimo = ?
                  AND status_emprestimo = 'ativo'
                """,
                codigoAdministrador,
                idEmprestimo
        );
    }

    public int contarEmprestimosAtivosPorMaterial(Long codigoMaterialAcervo) {
        Integer quantidade = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM emprestimo
                WHERE codigo_material_acervo = ?
                  AND status_emprestimo IN ('ativo', 'atrasado')
                """,
                Integer.class,
                codigoMaterialAcervo
        );
        return quantidade == null ? 0 : quantidade;
    }

    private String sqlResumoEmprestimo() {
        return """
               SELECT e.id_emprestimo,
                      e.data_emprestimo,
                      e.status_emprestimo,
                      e.data_devolucao_real,
                      e.data_devolucao_prevista,
                      e.codigo_material_acervo,
                      m.titulo AS titulo_material,
                      m.tipo_material,
                      e.codigo_usuario,
                      p.nome AS nome_usuario
               FROM emprestimo e
               INNER JOIN material_acervo m ON m.codigo = e.codigo_material_acervo
               INNER JOIN pessoa p ON p.id_pessoa = e.codigo_usuario
               """;
    }

    private static java.time.LocalDate getNullableDate(ResultSet rs, String columnName) throws SQLException {
        Date value = rs.getDate(columnName);
        return value == null ? null : value.toLocalDate();
    }

    private static Long getNullableLong(ResultSet rs, String columnName) throws SQLException {
        long value = rs.getLong(columnName);
        return rs.wasNull() ? null : value;
    }
}
