package br.ufscar.sistema_universidade.repository;
import br.ufscar.sistema_universidade.model.Reserva;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ReservaRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Reserva> RESERVA_ROW_MAPPER = (rs, rowNum) ->
        new Reserva(
            rs.getLong("id_reserva"),
            rs.getDate("data").toLocalDate(),
            rs.getString("tipo_reserva"),
            rs.getString("status_reserva"),
            rs.getString("objetivo"),
            rs.getTime("horario_inicio").toLocalTime(),
            rs.getTime("horario_fim").toLocalTime(),
            rs.getLong("codigo_usuario"),
            rs.getLong("codigo_sala"),
            getNullableLong(rs, "codigo_administrador")
        );

    public ReservaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Long getNullableLong(ResultSet rs, String columnName) throws SQLException {
        long value = rs.getLong(columnName);
        return rs.wasNull() ? null : value;
    }

    public int salvar(Reserva reserva) {
        return jdbcTemplate.update(
            """
            INSERT INTO reserva (
                data, tipo_reserva, status_reserva, objetivo,
                horario_inicio, horario_fim, codigo_usuario, codigo_sala, codigo_administrador
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """,
            Date.valueOf(reserva.getData()),
            reserva.getTipoReserva(),
            reserva.getStatusReserva(),
            reserva.getObjetivo(),
            Time.valueOf(reserva.getHorarioInicio()),
            Time.valueOf(reserva.getHorarioFim()),
            reserva.getCodigoUsuario(),
            reserva.getCodigoSala(),
            reserva.getCodigoAdministrador()
        );
    }

    public int atualizar(Reserva reserva) {
        return jdbcTemplate.update(
            """
            UPDATE reserva
            SET data = ?, tipo_reserva = ?, status_reserva = ?, objetivo = ?,
                horario_inicio = ?, horario_fim = ?, codigo_usuario = ?,
                codigo_sala = ?, codigo_administrador = ?
            WHERE id_reserva = ?
            """,
            Date.valueOf(reserva.getData()),
            reserva.getTipoReserva(),
            reserva.getStatusReserva(),
            reserva.getObjetivo(),
            Time.valueOf(reserva.getHorarioInicio()),
            Time.valueOf(reserva.getHorarioFim()),
            reserva.getCodigoUsuario(),
            reserva.getCodigoSala(),
            reserva.getCodigoAdministrador(),
            reserva.getIdReserva()
        );
    }

    public int deletarPorId(Long idReserva) {
        return jdbcTemplate.update("DELETE FROM reserva WHERE id_reserva = ?", idReserva);
    }

    public Reserva buscarPorId(Long idReserva) {
        List<Reserva> reservas = jdbcTemplate.query(
            """
            SELECT id_reserva, data, tipo_reserva, status_reserva, objetivo,
                   horario_inicio, horario_fim, codigo_usuario, codigo_sala, codigo_administrador
            FROM reserva
            WHERE id_reserva = ?
            """,
            RESERVA_ROW_MAPPER,
            idReserva
        );
        return reservas.isEmpty() ? null : reservas.get(0);
    }

    public List<Reserva> listarTodos() {
        return jdbcTemplate.query(
            """
            SELECT id_reserva, data, tipo_reserva, status_reserva, objetivo,
                   horario_inicio, horario_fim, codigo_usuario, codigo_sala, codigo_administrador
            FROM reserva
            ORDER BY data DESC, horario_inicio DESC, id_reserva DESC
            """,
            RESERVA_ROW_MAPPER
        );
    }

    public List<Reserva> listarPorUsuario(Long idPessoa) {
        return jdbcTemplate.query(
            """
            SELECT id_reserva, data, tipo_reserva, status_reserva, objetivo,
                   horario_inicio, horario_fim, codigo_usuario, codigo_sala, codigo_administrador
            FROM reserva
            WHERE codigo_usuario = ?
            ORDER BY data DESC, horario_inicio DESC, id_reserva DESC
            """,
            RESERVA_ROW_MAPPER,
            idPessoa
        );
    }

    public boolean existeConflito(Reserva reserva) {
        Integer quantidade = jdbcTemplate.queryForObject(
            """
            SELECT COUNT(*)
            FROM reserva
            WHERE codigo_sala = ?
              AND data = ?
              AND status_reserva IN ('pendente', 'aprovada')
              AND horario_inicio < ?
              AND horario_fim > ?
              AND (? IS NULL OR id_reserva <> ?)
            """,
            Integer.class,
            reserva.getCodigoSala(),
            Date.valueOf(reserva.getData()),
            Time.valueOf(reserva.getHorarioFim()),
            Time.valueOf(reserva.getHorarioInicio()),
            reserva.getIdReserva(),
            reserva.getIdReserva()
        );
        return quantidade != null && quantidade > 0;
    }
}

