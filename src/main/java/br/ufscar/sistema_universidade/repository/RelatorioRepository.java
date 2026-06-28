package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.dto.RelatorioSalaDTO;
import br.ufscar.sistema_universidade.dto.RelatorioEmprestimoUsuarioDTO;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RelatorioRepository {

    private final JdbcTemplate jdbcTemplate;

    public RelatorioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RelatorioSalaDTO> emitirRelatorioInfraestrutura() {
        String sql = """
            SELECT 
                c.nome AS nome_campus,
                c.cidade AS cidade_campus,
                p.nome AS nome_predio,
                p.bloco AS bloco_predio,
                s.numero_sala,
                s.categoria AS categoria_sala,
                s.capacidade
            FROM sala s
            INNER JOIN predio p ON s.codigo_predio = p.codigo
            INNER JOIN campus c ON p.codigo_campus = c.codigo
            ORDER BY c.nome, p.nome, s.numero_sala
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new RelatorioSalaDTO(
                rs.getString("nome_campus"),
                rs.getString("cidade_campus"),
                rs.getString("nome_predio"),
                rs.getString("bloco_predio"),
                rs.getString("numero_sala"),
                rs.getString("categoria_sala"),
                rs.getInt("capacidade")
        ));
    }

    public List<RelatorioEmprestimoUsuarioDTO> emitirRelatorioEmprestimosPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        String sql = """
            SELECT
                p.id_pessoa AS id_pessoa,
                p.nome AS nome_usuario,
                COUNT(*) AS quantidade_emprestimos,
                MIN(e.data_emprestimo) AS primeiro_emprestimo,
                MAX(e.data_emprestimo) AS ultimo_emprestimo
            FROM emprestimo e
            INNER JOIN pessoa p ON p.id_pessoa = e.codigo_usuario
            WHERE e.data_emprestimo BETWEEN ? AND ?
            GROUP BY p.id_pessoa, p.nome
            ORDER BY quantidade_emprestimos DESC, p.nome ASC
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new RelatorioEmprestimoUsuarioDTO(
                rs.getLong("id_pessoa"),
                rs.getString("nome_usuario"),
                rs.getInt("quantidade_emprestimos"),
                rs.getDate("primeiro_emprestimo").toLocalDate(),
                rs.getDate("ultimo_emprestimo").toLocalDate()
        ), Date.valueOf(dataInicio), Date.valueOf(dataFim));
    }
}
