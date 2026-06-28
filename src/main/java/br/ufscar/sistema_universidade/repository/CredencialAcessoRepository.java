package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.model.CredencialAcesso;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CredencialAcessoRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<CredencialAcesso> CREDENCIAL_ROW_MAPPER = (rs, rowNum) ->
        new CredencialAcesso(
            rs.getLong("id_pessoa"),
            null,
            rs.getString("login"),
            rs.getString("senha_hash"),
            rs.getBoolean("ativo")
        );

    private static final RowMapper<CredencialAcesso> CREDENCIAL_COM_PESSOA_ROW_MAPPER = (rs, rowNum) ->
        new CredencialAcesso(
            rs.getLong("id_pessoa"),
            rs.getString("nome"),
            rs.getString("login"),
            rs.getString("senha_hash"),
            rs.getBoolean("ativo")
        );

    public CredencialAcessoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<CredencialAcesso> buscarPorLogin(String login) {
        List<CredencialAcesso> credenciais = jdbcTemplate.query(
            """
            SELECT id_pessoa, login, senha_hash, ativo
            FROM credencial_acesso
            WHERE login = ?
            """,
            CREDENCIAL_ROW_MAPPER,
            login
        );
        return credenciais.stream().findFirst();
    }

    public Optional<Long> buscarIdPessoaPorLogin(String login) {
        return buscarPorLogin(login).map(CredencialAcesso::getIdPessoa);
    }

    public CredencialAcesso buscarPorPessoa(Long idPessoa) {
        List<CredencialAcesso> credenciais = jdbcTemplate.query(
            """
            SELECT c.id_pessoa, p.nome, c.login, c.senha_hash, c.ativo
            FROM credencial_acesso c
            INNER JOIN pessoa p ON p.id_pessoa = c.id_pessoa
            WHERE c.id_pessoa = ?
            """,
            CREDENCIAL_COM_PESSOA_ROW_MAPPER,
            idPessoa
        );
        return credenciais.isEmpty() ? null : credenciais.get(0);
    }

    public List<CredencialAcesso> listarTodos() {
        return jdbcTemplate.query(
            """
            SELECT c.id_pessoa, p.nome, c.login, c.senha_hash, c.ativo
            FROM credencial_acesso c
            INNER JOIN pessoa p ON p.id_pessoa = c.id_pessoa
            ORDER BY p.nome
            """,
            CREDENCIAL_COM_PESSOA_ROW_MAPPER
        );
    }

    public int salvar(CredencialAcesso credencial) {
        return jdbcTemplate.update(
            """
            INSERT INTO credencial_acesso (id_pessoa, login, senha_hash, ativo)
            VALUES (?, ?, ?, ?)
            """,
            credencial.getIdPessoa(),
            credencial.getLogin(),
            credencial.getSenhaHash(),
            Boolean.TRUE.equals(credencial.getAtivo())
        );
    }

    public int atualizarDados(Long idPessoa, String login, Boolean ativo) {
        return jdbcTemplate.update(
            """
            UPDATE credencial_acesso
            SET login = ?, ativo = ?
            WHERE id_pessoa = ?
            """,
            login,
            Boolean.TRUE.equals(ativo),
            idPessoa
        );
    }

    public int atualizarSenha(Long idPessoa, String senhaHash) {
        return jdbcTemplate.update(
            "UPDATE credencial_acesso SET senha_hash = ? WHERE id_pessoa = ?",
            senhaHash,
            idPessoa
        );
    }

    public int deletarPorId(Long idPessoa) {
        return jdbcTemplate.update("DELETE FROM credencial_acesso WHERE id_pessoa = ?", idPessoa);
    }

    public Set<String> buscarPerfisPorPessoa(Long idPessoa) {
        Set<String> perfis = new LinkedHashSet<>();

        if (existeEmTabela("usuario", idPessoa)) {
            perfis.add("ROLE_USUARIO");
        }
        if (existeEmTabela("discente", idPessoa)) {
            perfis.add("ROLE_DISCENTE");
        }
        if (existeEmTabela("funcionario", idPessoa)) {
            perfis.add("ROLE_FUNCIONARIO");
        }
        if (existeEmTabela("administrador", idPessoa)) {
            perfis.add("ROLE_ADMINISTRADOR");
        }
        String setorAdministrador = buscarSetorAdministrador(idPessoa);
        if (ehAdministradorMaster(setorAdministrador)) {
            perfis.add("ROLE_ADMIN_MASTER");
        }
        if (ehAdministradorOperacional(setorAdministrador)) {
            perfis.add("ROLE_ADMIN_OPERACIONAL");
        }

        return perfis;
    }

    private boolean existeEmTabela(String tabela, Long idPessoa) {
        Integer quantidade = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM " + tabela + " WHERE id_pessoa = ?",
            Integer.class,
            idPessoa
        );
        return quantidade != null && quantidade > 0;
    }

    private String buscarSetorAdministrador(Long idPessoa) {
        List<String> setores = jdbcTemplate.queryForList(
            """
            SELECT setor
            FROM administrador
            WHERE id_pessoa = ?
            """,
            String.class,
            idPessoa
        );
        return setores.isEmpty() ? null : setores.get(0);
    }

    private boolean ehAdministradorMaster(String setor) {
        if (setor == null) {
            return false;
        }
        return "master".equalsIgnoreCase(setor.trim());
    }

    private boolean ehAdministradorOperacional(String setor) {
        if (setor == null) {
            return false;
        }
        String setorNormalizado = setor.trim().toLowerCase();
        return setorNormalizado.equals("operacional")
            || setorNormalizado.equals("admin operacional")
            || setorNormalizado.equals("biblioteca")
            || setorNormalizado.equals("biblioteca central");
    }
}
