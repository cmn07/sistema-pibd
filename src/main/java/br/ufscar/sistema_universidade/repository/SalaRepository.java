package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.model.Campus;
import br.ufscar.sistema_universidade.model.Predio;
import br.ufscar.sistema_universidade.model.Sala;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SalaRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Sala> SALA_ROW_MAPPER = (rs, rowNum) -> {
        Sala sala = new Sala(
            rs.getLong("codigo"),
            rs.getString("numero_sala"),
            rs.getString("categoria"),
            rs.getInt("capacidade"),
            rs.getLong("codigo_predio")
        );

        if (rs.getObject("predio_nome") != null) {
            Predio predio = new Predio(
                    rs.getLong("predio_codigo"),
                    rs.getString("predio_nome"),
                    rs.getString("predio_bloco"),
                    rs.getLong("predio_codigo_campus")
            );
            sala.setPredio(predio);
        }

        if (rs.getObject("campus_nome") != null) {
            Campus campus = new Campus(
                    rs.getLong("campus_codigo"),
                    rs.getString("campus_nome"),
                    rs.getString("campus_cidade")
            );
            sala.setCampus(campus);
        }
        return sala;
    };

    public SalaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Sala> listarTodos() {
        return jdbcTemplate.query(
            """
            SELECT 
                s.codigo, s.numero_sala, s.categoria, s.capacidade, s.codigo_predio,
                p.codigo as predio_codigo, p.nome as predio_nome, p.bloco as predio_bloco, p.codigo_campus as predio_codigo_campus,
                c.codigo as campus_codigo, c.nome as campus_nome, c.cidade as campus_cidade
            FROM sala s
            LEFT JOIN predio p ON s.codigo_predio = p.codigo
            LEFT JOIN campus c ON p.codigo_campus = c.codigo
            ORDER BY s.numero_sala
            """,
            SALA_ROW_MAPPER
        );
    }

    public Sala buscarPorId(Long id) {
        return jdbcTemplate.queryForObject(
            """
            SELECT 
                s.codigo, s.numero_sala, s.categoria, s.capacidade, s.codigo_predio,
                p.codigo as predio_codigo, p.nome as predio_nome, p.bloco as predio_bloco, p.codigo_campus as predio_codigo_campus,
                c.codigo as campus_codigo, c.nome as campus_nome, c.cidade as campus_cidade
            FROM sala s
            LEFT JOIN predio p ON s.codigo_predio = p.codigo
            LEFT JOIN campus c ON p.codigo_campus = c.codigo
            WHERE s.codigo = ?
            """,
            SALA_ROW_MAPPER,
            new Object[]{id}
        );
    }

    public void salvar(Sala sala) {
        jdbcTemplate.update("INSERT INTO sala (numero_sala, categoria, capacidade, codigo_predio) VALUES (?, ?, ?, ?)",
                sala.getNumeroSala(), sala.getCategoria(), sala.getCapacidade(), sala.getCodigoPredio());
    }

    public void atualizar(Sala sala) {
        jdbcTemplate.update("UPDATE sala SET numero_sala = ?, categoria = ?, capacidade = ?, codigo_predio = ? WHERE codigo = ?",
                sala.getNumeroSala(), sala.getCategoria(), sala.getCapacidade(), sala.getCodigoPredio(), sala.getCodigo());
    }

    public void deletarPorId(Long id) {
        jdbcTemplate.update("DELETE FROM sala WHERE codigo = ?", id);
    }
}
