package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.model.Discente;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DiscenteRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Discente> DISCENTE_ROW_MAPPER = (rs, rowNum) ->
        new Discente(
            rs.getLong("id_pessoa"),
            rs.getString("nome"),
            rs.getString("cpf"),
            rs.getString("email"),
            rs.getString("telefone"),
            rs.getString("ra"),
            rs.getString("curso")
        );

    public DiscenteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int salvar(Discente discente) {
        return jdbcTemplate.update(
            "INSERT INTO discente (id_pessoa, ra, curso) VALUES (?, ?, ?)",
            discente.getIdPessoa(),
            discente.getRa(),
            discente.getCurso()
        );
    }

    public int atualizar(Long idPessoaAtual, Discente discente) {
        return jdbcTemplate.update(
            "UPDATE discente SET id_pessoa = ?, ra = ?, curso = ? WHERE id_pessoa = ?",
            discente.getIdPessoa(),
            discente.getRa(),
            discente.getCurso(),
            idPessoaAtual
        );
    }

    public int deletarPorId(Long idPessoa) {
        return jdbcTemplate.update("DELETE FROM discente WHERE id_pessoa = ?", idPessoa);
    }

    public Discente buscarPorId(Long idPessoa) {
        List<Discente> discentes = jdbcTemplate.query(
            """
            SELECT p.id_pessoa, p.nome, p.cpf, p.email, p.telefone,
                   d.ra, d.curso
            FROM discente d
            INNER JOIN usuario u ON u.id_pessoa = d.id_pessoa
            INNER JOIN pessoa p ON p.id_pessoa = u.id_pessoa
            WHERE d.id_pessoa = ?
            """,
            DISCENTE_ROW_MAPPER,
            idPessoa
        );
        return discentes.isEmpty() ? null : discentes.get(0);
    }

    public List<Discente> listarTodos() {
        return jdbcTemplate.query(
            """
            SELECT p.id_pessoa, p.nome, p.cpf, p.email, p.telefone,
                   d.ra, d.curso
            FROM discente d
            INNER JOIN usuario u ON u.id_pessoa = d.id_pessoa
            INNER JOIN pessoa p ON p.id_pessoa = u.id_pessoa
            ORDER BY p.nome
            """,
            DISCENTE_ROW_MAPPER
        );
    }
}
