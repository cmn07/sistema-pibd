package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.model.MaterialAcervo;
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

    private static Integer getIntegerOrNull(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        return rs.wasNull() ? null : value;
    }
}
