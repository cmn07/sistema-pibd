package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.dto.RelatorioSalaDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

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
}