package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.model.Usuario;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Usuario> USUARIO_ROW_MAPPER = (rs, rowNum) ->
        new Usuario(
            rs.getLong("id_pessoa"),
            rs.getString("nome"),
            rs.getString("cpf"),
            rs.getString("email"),
            rs.getString("telefone")
        );

    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int salvar(Long idPessoa) {
        return jdbcTemplate.update(
            "INSERT INTO usuario (id_pessoa) VALUES (?)",
            idPessoa
        );
    }

    public int deletarPorId(Long idPessoa) {
        return jdbcTemplate.update("DELETE FROM usuario WHERE id_pessoa = ?", idPessoa);
    }

    public Usuario buscarPorId(Long idPessoa) {
        List<Usuario> usuarios = jdbcTemplate.query(
            """
            SELECT p.id_pessoa, p.nome, p.cpf, p.email, p.telefone
            FROM usuario u
            INNER JOIN pessoa p ON p.id_pessoa = u.id_pessoa
            WHERE u.id_pessoa = ?
            """,
            USUARIO_ROW_MAPPER,
            idPessoa
        );
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }

    public List<Usuario> listarTodos() {
        return jdbcTemplate.query(
            """
            SELECT p.id_pessoa, p.nome, p.cpf, p.email, p.telefone
            FROM usuario u
            INNER JOIN pessoa p ON p.id_pessoa = u.id_pessoa
            ORDER BY p.nome
            """,
            USUARIO_ROW_MAPPER
        );
    }

    public boolean existePorId(Long idPessoa) {
        Integer quantidade = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM usuario WHERE id_pessoa = ?",
            Integer.class,
            idPessoa
        );
        return quantidade != null && quantidade > 0;
    }
}
