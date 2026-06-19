package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.model.Funcionario;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class FuncionarioRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Funcionario> FUNCIONARIO_ROW_MAPPER = (rs, rowNum) ->
        new Funcionario(
            rs.getLong("id_pessoa"),
            rs.getString("nome"),
            rs.getString("cpf"),
            rs.getString("email"),
            rs.getString("telefone"),
            rs.getString("id_func"),
            rs.getString("tipo_vinculo")
        );

    public FuncionarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int salvar(Funcionario funcionario) {
        return jdbcTemplate.update(
            "INSERT INTO funcionario (id_pessoa, id_func, tipo_vinculo) VALUES (?, ?, ?)",
            funcionario.getIdPessoa(),
            funcionario.getIdFunc(),
            funcionario.getTipoVinculo()
        );
    }

    public int atualizar(Funcionario funcionario) {
        return jdbcTemplate.update(
            "UPDATE funcionario SET id_func = ?, tipo_vinculo = ? WHERE id_pessoa = ?",
            funcionario.getIdFunc(),
            funcionario.getTipoVinculo(),
            funcionario.getIdPessoa()
        );
    }

    public int deletarPorId(Long idPessoa) {
        return jdbcTemplate.update("DELETE FROM funcionario WHERE id_pessoa = ?", idPessoa);
    }

    public Funcionario buscarPorId(Long idPessoa) {
        List<Funcionario> funcionarios = jdbcTemplate.query(
            """
            SELECT p.id_pessoa, p.nome, p.cpf, p.email, p.telefone,
                   f.id_func, f.tipo_vinculo
            FROM funcionario f
            INNER JOIN usuario u ON u.id_pessoa = f.id_pessoa
            INNER JOIN pessoa p ON p.id_pessoa = u.id_pessoa
            WHERE f.id_pessoa = ?
            """,
            FUNCIONARIO_ROW_MAPPER,
            idPessoa
        );
        return funcionarios.isEmpty() ? null : funcionarios.get(0);
    }

    public Funcionario buscarPorIdFunc(String idFunc) {
        List<Funcionario> funcionarios = jdbcTemplate.query(
            """
            SELECT p.id_pessoa, p.nome, p.cpf, p.email, p.telefone,
                   f.id_func, f.tipo_vinculo
            FROM funcionario f
            INNER JOIN usuario u ON u.id_pessoa = f.id_pessoa
            INNER JOIN pessoa p ON p.id_pessoa = u.id_pessoa
            WHERE f.id_func = ?
            """,
            FUNCIONARIO_ROW_MAPPER,
            idFunc
        );
        return funcionarios.isEmpty() ? null : funcionarios.get(0);
    }

    public List<Funcionario> listarTodos() {
        return jdbcTemplate.query(
            """
            SELECT p.id_pessoa, p.nome, p.cpf, p.email, p.telefone,
                   f.id_func, f.tipo_vinculo
            FROM funcionario f
            INNER JOIN usuario u ON u.id_pessoa = f.id_pessoa
            INNER JOIN pessoa p ON p.id_pessoa = u.id_pessoa
            ORDER BY p.nome
            """,
            FUNCIONARIO_ROW_MAPPER
        );
    }

    public List<Funcionario> listarPorTipoVinculo(String tipoVinculo) {
        return jdbcTemplate.query(
            """
            SELECT p.id_pessoa, p.nome, p.cpf, p.email, p.telefone,
                   f.id_func, f.tipo_vinculo
            FROM funcionario f
            INNER JOIN usuario u ON u.id_pessoa = f.id_pessoa
            INNER JOIN pessoa p ON p.id_pessoa = u.id_pessoa
            WHERE f.tipo_vinculo = ?
            ORDER BY p.nome
            """,
            FUNCIONARIO_ROW_MAPPER,
            tipoVinculo
        );
    }
}
