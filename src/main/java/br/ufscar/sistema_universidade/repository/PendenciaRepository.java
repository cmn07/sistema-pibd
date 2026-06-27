package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.dto.PendenciaResumoDTO;
import br.ufscar.sistema_universidade.model.Pendencia;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PendenciaRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<PendenciaResumoDTO> PENDENCIA_RESUMO_ROW_MAPPER = (rs, rowNum) ->
            new PendenciaResumoDTO(
                    rs.getLong("codigo"),
                    rs.getString("motivo"),
                    rs.getString("status_pendencia"),
                    rs.getDate("data_inicio_pendencia").toLocalDate(),
                    getNullableDate(rs, "data_fim_pendencia"),
                    rs.getLong("codigo_emprestimo"),
                    rs.getLong("codigo_usuario"),
                    rs.getString("nome_usuario"),
                    rs.getString("titulo_material"),
                    rs.getString("tipo_material"),
                    rs.getString("nome_administrador")
            );

    public PendenciaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int salvar(Pendencia pendencia) {
        return jdbcTemplate.update(
                """
                INSERT INTO pendencia (
                    motivo, status_pendencia, data_inicio_pendencia,
                    data_fim_pendencia, codigo_emprestimo, codigo_administrador
                ) VALUES (?, ?, ?, ?, ?, ?)
                """,
                pendencia.getMotivo(),
                pendencia.getStatusPendencia(),
                Date.valueOf(pendencia.getDataInicioPendencia()),
                pendencia.getDataFimPendencia() == null ? null : Date.valueOf(pendencia.getDataFimPendencia()),
                pendencia.getCodigoEmprestimo(),
                pendencia.getCodigoAdministrador()
        );
    }

    public List<PendenciaResumoDTO> listarTodasResumidas() {
        return jdbcTemplate.query(
                sqlResumoPendencia() + " ORDER BY p.data_inicio_pendencia DESC, p.codigo DESC",
                PENDENCIA_RESUMO_ROW_MAPPER
        );
    }

    public List<PendenciaResumoDTO> listarResumidasPorUsuario(Long idPessoa) {
        return jdbcTemplate.query(
                sqlResumoPendencia() + """
                WHERE e.codigo_usuario = ?
                ORDER BY p.data_inicio_pendencia DESC, p.codigo DESC
                """,
                PENDENCIA_RESUMO_ROW_MAPPER,
                idPessoa
        );
    }

    public boolean existePorEmprestimo(Long codigoEmprestimo) {
        Integer quantidade = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM pendencia WHERE codigo_emprestimo = ?",
                Integer.class,
                codigoEmprestimo
        );
        return quantidade != null && quantidade > 0;
    }

    public int encerrar(Long codigoPendencia) {
        return jdbcTemplate.update(
                """
                UPDATE pendencia
                SET status_pendencia = 'cumprida',
                    data_fim_pendencia = CURRENT_DATE
                WHERE codigo = ?
                  AND status_pendencia = 'ativa'
                """,
                codigoPendencia
        );
    }

    private String sqlResumoPendencia() {
        return """
               SELECT p.codigo,
                      p.motivo,
                      p.status_pendencia,
                      p.data_inicio_pendencia,
                      p.data_fim_pendencia,
                      p.codigo_emprestimo,
                      e.codigo_usuario,
                      pessoa_usuario.nome AS nome_usuario,
                      m.titulo AS titulo_material,
                      m.tipo_material,
                      pessoa_administrador.nome AS nome_administrador
               FROM pendencia p
               INNER JOIN emprestimo e ON e.id_emprestimo = p.codigo_emprestimo
               INNER JOIN material_acervo m ON m.codigo = e.codigo_material_acervo
               INNER JOIN pessoa pessoa_usuario ON pessoa_usuario.id_pessoa = e.codigo_usuario
               LEFT JOIN pessoa pessoa_administrador ON pessoa_administrador.id_pessoa = p.codigo_administrador
               """;
    }

    private static java.time.LocalDate getNullableDate(ResultSet rs, String columnName) throws SQLException {
        Date value = rs.getDate(columnName);
        return value == null ? null : value.toLocalDate();
    }
}
