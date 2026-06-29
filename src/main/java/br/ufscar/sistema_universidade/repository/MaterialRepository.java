package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.model.MaterialAcervo;
import br.ufscar.sistema_universidade.dto.MaterialAcervoConsultaDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MaterialRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<MaterialAcervo> MATERIAL_ROW_MAPPER = (rs, rowNum) ->
            new MaterialAcervo(
                    rs.getLong("codigo"),
                    rs.getString("tipo_material"),
                    rs.getString("titulo"),
                    rs.getInt("quantidade_copias"),
                    getIntegerOrNull(rs, "ano_publicacao"),
                    rs.getString("idioma"),
                    rs.getString("categoria"),
                    rs.getString("editora")
            );

    private static final RowMapper<MaterialAcervoConsultaDTO> MATERIAL_CONSULTA_ROW_MAPPER = (rs, rowNum) ->
            new MaterialAcervoConsultaDTO(
                    rs.getLong("codigo_material"),
                    rs.getString("titulo"),
                    rs.getString("tipo_material"),
                    rs.getString("categoria"),
                    rs.getString("editora"),
                    rs.getString("idioma"),
                    getIntegerOrNull(rs, "ano_publicacao"),
                    rs.getInt("quantidade_copias"),
                    getLongOrNull(rs, "id_emprestimo"),
                    rs.getString("status_emprestimo"),
                    rs.getDate("data_devolucao_prevista") == null
                            ? null
                            : rs.getDate("data_devolucao_prevista").toLocalDate(),
                    getLongOrNull(rs, "codigo_usuario"),
                    rs.getString("nome_usuario")
            );

    public MaterialRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int salvar(MaterialAcervo material) {
        return jdbcTemplate.update(
                """
                INSERT INTO material_acervo
                    (tipo_material, titulo, quantidade_copias, ano_publicacao, idioma, categoria, editora)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """,
                material.getTipoMaterial(),
                material.getTitulo(),
                material.getQuantidadeCopias(),
                material.getAnoPublicacao(),
                material.getIdioma(),
                material.getCategoria(),
                material.getEditora()
        );
    }

    public int atualizar(MaterialAcervo material) {
        return jdbcTemplate.update(
                """
                UPDATE material_acervo
                SET tipo_material = ?,
                    titulo = ?,
                    quantidade_copias = ?,
                    ano_publicacao = ?,
                    idioma = ?,
                    categoria = ?,
                    editora = ?
                WHERE codigo = ?
                """,
                material.getTipoMaterial(),
                material.getTitulo(),
                material.getQuantidadeCopias(),
                material.getAnoPublicacao(),
                material.getIdioma(),
                material.getCategoria(),
                material.getEditora(),
                material.getCodigo()
        );
    }

    public int deletarPorId(Long codigo) {
        return jdbcTemplate.update("DELETE FROM material_acervo WHERE codigo = ?", codigo);
    }

    public MaterialAcervo buscarPorId(Long codigo) {
        List<MaterialAcervo> materiais = jdbcTemplate.query(
                """
                SELECT codigo, tipo_material, titulo, quantidade_copias, ano_publicacao, idioma, categoria, editora
                FROM material_acervo
                WHERE codigo = ?
                """,
                MATERIAL_ROW_MAPPER,
                codigo
        );
        return materiais.isEmpty() ? null : materiais.get(0);
    }

    public List<MaterialAcervo> listarTodos() {
        return jdbcTemplate.query(
                """
                SELECT codigo, tipo_material, titulo, quantidade_copias, ano_publicacao, idioma, categoria, editora
                FROM material_acervo
                ORDER BY titulo, codigo
                """,
                MATERIAL_ROW_MAPPER
        );
    }

    public List<MaterialAcervoConsultaDTO> consultarAcervo(String termo, String tipoMaterial, String situacao) {
        String termoFiltro = normalizarFiltroTexto(termo);
        String tipoFiltro = normalizarFiltroTexto(tipoMaterial);
        String situacaoFiltro = normalizarFiltroTexto(situacao);

        return jdbcTemplate.query(
                """
                SELECT
                    m.codigo AS codigo_material,
                    m.titulo,
                    m.tipo_material,
                    m.categoria,
                    m.editora,
                    m.idioma,
                    m.ano_publicacao,
                    m.quantidade_copias,
                    e.id_emprestimo,
                    e.status_emprestimo,
                    e.data_devolucao_prevista,
                    p.id_pessoa AS codigo_usuario,
                    p.nome AS nome_usuario
                FROM material_acervo m
                LEFT JOIN emprestimo e
                       ON e.codigo_material_acervo = m.codigo
                      AND e.status_emprestimo IN ('ativo', 'atrasado')
                LEFT JOIN pessoa p
                       ON p.id_pessoa = e.codigo_usuario
                WHERE (CAST(? AS VARCHAR) IS NULL OR LOWER(m.titulo) LIKE LOWER(CONCAT('%', CAST(? AS VARCHAR), '%'))
                                                OR LOWER(COALESCE(m.categoria, '')) LIKE LOWER(CONCAT('%', CAST(? AS VARCHAR), '%'))
                                                OR LOWER(COALESCE(m.editora, '')) LIKE LOWER(CONCAT('%', CAST(? AS VARCHAR), '%')))
                  AND (CAST(? AS VARCHAR) IS NULL OR m.tipo_material = CAST(? AS VARCHAR))
                  AND (CAST(? AS VARCHAR) IS NULL OR CAST(? AS VARCHAR) = 'TODOS'
                       OR (CAST(? AS VARCHAR) = 'DISPONIVEL' AND e.id_emprestimo IS NULL)
                       OR (CAST(? AS VARCHAR) = 'EMPRESTADO' AND e.id_emprestimo IS NOT NULL))
                ORDER BY m.titulo, m.codigo, e.data_devolucao_prevista
                """,
                MATERIAL_CONSULTA_ROW_MAPPER,
                termoFiltro,
                termoFiltro,
                termoFiltro,
                termoFiltro,
                tipoFiltro,
                tipoFiltro,
                situacaoFiltro,
                situacaoFiltro,
                situacaoFiltro,
                situacaoFiltro
        );
    }

    private static Integer getIntegerOrNull(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        return rs.wasNull() ? null : value;
    }

    private static Long getLongOrNull(ResultSet rs, String columnName) throws SQLException {
        long value = rs.getLong(columnName);
        return rs.wasNull() ? null : value;
    }

    private String normalizarFiltroTexto(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        return valor.trim();
    }
}
