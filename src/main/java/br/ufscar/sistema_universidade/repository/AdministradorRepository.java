package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.model.Administrador;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AdministradorRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Administrador> ADMINISTRADOR_ROW_MAPPER = (rs, rowNum) ->
        new Administrador(
            rs.getLong("id_pessoa"),
            rs.getString("nome"),
            rs.getString("cpf"),
            rs.getString("email"),
            rs.getString("telefone"),
            rs.getString("id_admin"),
            rs.getString("setor")
        );

    public AdministradorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int salvar(Administrador administrador) {
        return jdbcTemplate.update(
            "INSERT INTO administrador (id_pessoa, id_admin, setor) VALUES (?, ?, ?)",
            administrador.getIdPessoa(),
            administrador.getIdAdmin(),
            administrador.getSetor()
        );
    }

    public int atualizar(Administrador administrador) {
        return jdbcTemplate.update(
            "UPDATE administrador SET id_admin = ?, setor = ? WHERE id_pessoa = ?",
            administrador.getIdAdmin(),
            administrador.getSetor(),
            administrador.getIdPessoa()
        );
    }

    public int deletarPorId(Long idPessoa) {
        return jdbcTemplate.update("DELETE FROM administrador WHERE id_pessoa = ?", idPessoa);
    }

    public Administrador buscarPorId(Long idPessoa) {
        List<Administrador> administradores = jdbcTemplate.query(
            """
            SELECT p.id_pessoa, p.nome, p.cpf, p.email, p.telefone,
                   a.id_admin, a.setor
            FROM administrador a
            INNER JOIN pessoa p ON p.id_pessoa = a.id_pessoa
            WHERE a.id_pessoa = ?
            """,
            ADMINISTRADOR_ROW_MAPPER,
            idPessoa
        );
        return administradores.isEmpty() ? null : administradores.get(0);
    }

    public Administrador buscarPorIdAdmin(String idAdmin) {
        List<Administrador> administradores = jdbcTemplate.query(
            """
            SELECT p.id_pessoa, p.nome, p.cpf, p.email, p.telefone,
                   a.id_admin, a.setor
            FROM administrador a
            INNER JOIN pessoa p ON p.id_pessoa = a.id_pessoa
            WHERE a.id_admin = ?
            """,
            ADMINISTRADOR_ROW_MAPPER,
            idAdmin
        );
        return administradores.isEmpty() ? null : administradores.get(0);
    }

    public List<Administrador> listarTodos() {
        return jdbcTemplate.query(
            """
            SELECT p.id_pessoa, p.nome, p.cpf, p.email, p.telefone,
                   a.id_admin, a.setor
            FROM administrador a
            INNER JOIN pessoa p ON p.id_pessoa = a.id_pessoa
            ORDER BY p.nome
            """,
            ADMINISTRADOR_ROW_MAPPER
        );
    }

    public List<Administrador> listarPorSetor(String setor) {
        return jdbcTemplate.query(
            """
            SELECT p.id_pessoa, p.nome, p.cpf, p.email, p.telefone,
                   a.id_admin, a.setor
            FROM administrador a
            INNER JOIN pessoa p ON p.id_pessoa = a.id_pessoa
            WHERE a.setor = ?
            ORDER BY p.nome
            """,
            ADMINISTRADOR_ROW_MAPPER,
            setor
        );
    }
}
