package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.model.Pessoa;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PessoaRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Pessoa> PESSOA_ROW_MAPPER = (rs, rowNum) ->
        new Pessoa(
            rs.getLong("id_pessoa"),
            rs.getString("nome"),
            rs.getString("cpf"),
            rs.getString("email"),
            rs.getString("telefone")
        );

    public PessoaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long salvar(Pessoa pessoa) {
        return jdbcTemplate.queryForObject(
            "INSERT INTO pessoa (nome, cpf, email, telefone) VALUES (?, ?, ?, ?) RETURNING id_pessoa",
            Long.class,
            pessoa.getNome(),
            pessoa.getCpf(),
            pessoa.getEmail(),
            pessoa.getTelefone()
        );
    }

    public int atualizar(Pessoa pessoa) {
        return jdbcTemplate.update(
            "UPDATE pessoa SET nome = ?, cpf = ?, email = ?, telefone = ? WHERE id_pessoa = ?",
            pessoa.getNome(),
            pessoa.getCpf(),
            pessoa.getEmail(),
            pessoa.getTelefone(),
            pessoa.getIdPessoa()
        );
    }

    public int deletarPorId(Long idPessoa) {
        return jdbcTemplate.update("DELETE FROM pessoa WHERE id_pessoa = ?", idPessoa);
    }

    public Pessoa buscarPorId(Long idPessoa) {
        List<Pessoa> pessoas = jdbcTemplate.query(
            "SELECT id_pessoa, nome, cpf, email, telefone FROM pessoa WHERE id_pessoa = ?",
            PESSOA_ROW_MAPPER,
            idPessoa
        );
        return pessoas.isEmpty() ? null : pessoas.get(0);
    }

    public Pessoa buscarPorCpf(String cpf) {
        List<Pessoa> pessoas = jdbcTemplate.query(
            "SELECT id_pessoa, nome, cpf, email, telefone FROM pessoa WHERE cpf = ?",
            PESSOA_ROW_MAPPER,
            cpf
        );
        return pessoas.isEmpty() ? null : pessoas.get(0);
    }

    public List<Pessoa> listarTodos() {
        return jdbcTemplate.query(
            "SELECT id_pessoa, nome, cpf, email, telefone FROM pessoa ORDER BY nome",
            PESSOA_ROW_MAPPER
        );
    }
}
