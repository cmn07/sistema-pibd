package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.dto.RelatorioSalaDTO;
import br.ufscar.sistema_universidade.dto.RelatorioEmprestimoUsuarioDTO;
import br.ufscar.sistema_universidade.dto.RelatorioReservaDTO;
import br.ufscar.sistema_universidade.dto.RelatorioInfraestruturaDTO;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    public List<RelatorioInfraestruturaDTO> emitirRelatorioInfraestruturaCritica() {
        String sql = """
            SELECT 
                c.nome AS campus,
                p.nome AS predio,
                COUNT(s.codigo) AS total_salas,
                COUNT(l.codigo_sala) AS total_laboratorios
            FROM campus c
            JOIN predio p ON c.codigo = p.codigo_campus
            JOIN sala s ON p.codigo = s.codigo_predio
            LEFT JOIN laboratorio l ON s.codigo = l.codigo_sala
            GROUP BY c.nome, p.nome
            HAVING COUNT(s.codigo) > 10 AND COUNT(l.codigo_sala) < 2
            ORDER BY c.nome, p.nome
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new RelatorioInfraestruturaDTO(
                rs.getString("campus"),
                rs.getString("predio"),
                rs.getLong("total_salas"),
                rs.getLong("total_laboratorios")
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

    public List<RelatorioSalaDTO> listarSalasDisponiveis(LocalDate data, LocalTime horarioInicio, LocalTime horarioFim) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                c.nome AS nome_campus,
                c.cidade AS cidade_campus,
                p.nome AS nome_predio,
                p.bloco AS bloco_predio,
                s.numero_sala,
                s.categoria AS categoria_sala,
                s.capacidade
            FROM sala s
            JOIN predio p ON p.codigo = s.codigo_predio
            JOIN campus c ON c.codigo = p.codigo_campus
            WHERE s.codigo NOT IN (
                SELECT r.codigo_sala
                FROM reserva r
                WHERE r.data = ?
                AND r.status_reserva = 'aprovada'
            """);

        List<Object> params = new ArrayList<>();
        params.add(Date.valueOf(data));

        if (horarioInicio != null && horarioFim != null) {
            sql.append(" AND r.horario_inicio < ? AND r.horario_fim > ? ");
            params.add(Time.valueOf(horarioFim));
            params.add(Time.valueOf(horarioInicio));
        }

        sql.append("""
            )
            ORDER BY c.nome, p.nome, s.numero_sala
            """);

        return jdbcTemplate.query(sql.toString(), (rs, rowNum) -> new RelatorioSalaDTO(
                rs.getString("nome_campus"),
                rs.getString("cidade_campus"),
                rs.getString("nome_predio"),
                rs.getString("bloco_predio"),
                rs.getString("numero_sala"),
                rs.getString("categoria_sala"),
                rs.getInt("capacidade")
        ), params.toArray());
    }

    public List<RelatorioReservaDTO> emitirRelatorioReservasPorPeriodo(
            LocalDate dataInicio,
            LocalDate dataFim,
            String status
    ) {
        String sql = """
            SELECT
                r.id_reserva,
                r.data,
                r.horario_inicio,
                r.horario_fim,
                r.tipo_reserva,
                r.status_reserva,
                pessoa.nome AS nome_solicitante,
                s.numero_sala,
                s.categoria AS categoria_sala,
                predio.nome AS nome_predio,
                predio.bloco AS bloco_predio,
                campus.nome AS nome_campus
            FROM reserva r
            INNER JOIN usuario u ON u.id_pessoa = r.codigo_usuario
            INNER JOIN pessoa ON pessoa.id_pessoa = u.id_pessoa
            INNER JOIN sala s ON s.codigo = r.codigo_sala
            INNER JOIN predio ON predio.codigo = s.codigo_predio
            INNER JOIN campus ON campus.codigo = predio.codigo_campus
            WHERE r.data BETWEEN ? AND ?
              AND (CAST(? AS VARCHAR) IS NULL OR r.status_reserva = ?)
            ORDER BY r.data, r.horario_inicio, campus.nome, predio.nome, s.numero_sala
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new RelatorioReservaDTO(
                rs.getLong("id_reserva"),
                rs.getDate("data").toLocalDate(),
                rs.getTime("horario_inicio").toLocalTime(),
                rs.getTime("horario_fim").toLocalTime(),
                rs.getString("tipo_reserva"),
                rs.getString("status_reserva"),
                rs.getString("nome_solicitante"),
                rs.getString("numero_sala"),
                rs.getString("categoria_sala"),
                rs.getString("nome_predio"),
                rs.getString("bloco_predio"),
                rs.getString("nome_campus")
        ), Date.valueOf(dataInicio), Date.valueOf(dataFim), status, status);
    }
}