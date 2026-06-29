package br.ufscar.sistema_universidade.repository;

import br.ufscar.sistema_universidade.model.Laboratorio;
import br.ufscar.sistema_universidade.model.Predio;
import br.ufscar.sistema_universidade.model.Campus;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LaboratorioRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Laboratorio> LABORATORIO_ROW_MAPPER = (rs, rowNum) -> {
        Laboratorio lab = new Laboratorio(
            rs.getLong("codigo_sala"),
            rs.getString("numero_sala"),
            rs.getString("categoria"),
            rs.getInt("capacidade"),
            rs.getLong("codigo_predio"),
            rs.getString("departamento_setor"),
            rs.getInt("quantidade_equipamentos_informatica")
        );
        
        Predio predio = new Predio(
            rs.getLong("predio_codigo"),
            rs.getString("predio_nome"),
            rs.getString("predio_bloco"),
            rs.getLong("campus_codigo")
        );
        lab.setPredio(predio);
        
        Campus campus = new Campus(
            rs.getLong("campus_codigo"),
            rs.getString("campus_nome"),
            rs.getString("campus_cidade")
        );
        lab.setCampus(campus);
        
        return lab;
    };

    public LaboratorioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Laboratorio> listarTodos() {
        String sql = """
            SELECT 
                l.codigo_sala, s.numero_sala, s.categoria, s.capacidade, s.codigo_predio,
                l.departamento_setor, l.quantidade_equipamentos_informatica,
                p.codigo as predio_codigo, p.nome as predio_nome, p.bloco as predio_bloco,
                c.codigo as campus_codigo, c.nome as campus_nome, c.cidade as campus_cidade
            FROM laboratorio l
            INNER JOIN sala s ON l.codigo_sala = s.codigo
            INNER JOIN predio p ON s.codigo_predio = p.codigo
            INNER JOIN campus c ON p.codigo_campus = c.codigo
            ORDER BY s.numero_sala
            """;

        return jdbcTemplate.query(sql, LABORATORIO_ROW_MAPPER);
    }

    public Laboratorio buscarPorId(Long id) {
        String sql = """
            SELECT 
                l.codigo_sala, s.numero_sala, s.categoria, s.capacidade, s.codigo_predio,
                l.departamento_setor, l.quantidade_equipamentos_informatica,
                p.codigo as predio_codigo, p.nome as predio_nome, p.bloco as predio_bloco,
                c.codigo as campus_codigo, c.nome as campus_nome, c.cidade as campus_cidade
            FROM laboratorio l
            INNER JOIN sala s ON l.codigo_sala = s.codigo
            INNER JOIN predio p ON s.codigo_predio = p.codigo
            INNER JOIN campus c ON p.codigo_campus = c.codigo
            WHERE l.codigo_sala = ?
            """;

        List<Laboratorio> labs = jdbcTemplate.query(sql, LABORATORIO_ROW_MAPPER, id);
        return labs.isEmpty() ? null : labs.get(0);
    }

    public void salvar(Laboratorio laboratorio) {
        // Primeiro inserimos na tabela sala
        jdbcTemplate.update(
            "INSERT INTO sala (numero_sala, categoria, capacidade, codigo_predio) VALUES (?, ?, ?, ?)",
            new Object[]{laboratorio.getNumeroSala(), laboratorio.getCategoria(), laboratorio.getCapacidade(), laboratorio.getCodigoPredio()}
        );

        // Recuperamos o codigo_sala recém inserido
        Long codigoSala = jdbcTemplate.queryForObject(
            "SELECT codigo FROM sala WHERE numero_sala = ? ORDER BY codigo DESC LIMIT 1",
            Long.class, laboratorio.getNumeroSala()
        );
        laboratorio.setCodigo(codigoSala);

        // Depois inserimos na tabela laboratorio
        jdbcTemplate.update(
            "INSERT INTO laboratorio (codigo_sala, departamento_setor, quantidade_equipamentos_informatica) VALUES (?, ?, ?)",
            codigoSala, laboratorio.getDepartamentoSetor(), laboratorio.getQuantidadeEquipamentosInformatica()
        );
    }

    public void atualizar(Laboratorio laboratorio) {
        // Atualiza na tabela sala
        jdbcTemplate.update(
            "UPDATE sala SET numero_sala = ?, categoria = ?, capacidade = ?, codigo_predio = ? WHERE codigo = ?",
            laboratorio.getNumeroSala(), laboratorio.getCategoria(), laboratorio.getCapacidade(), laboratorio.getCodigoPredio(), laboratorio.getCodigo()
        );

        // Atualiza na tabela laboratorio
        jdbcTemplate.update(
            "UPDATE laboratorio SET departamento_setor = ?, quantidade_equipamentos_informatica = ? WHERE codigo_sala = ?",
            laboratorio.getDepartamentoSetor(), laboratorio.getQuantidadeEquipamentosInformatica(), laboratorio.getCodigo()
        );
    }

    public void deletarPorId(Long id) {
        // Deleta da tabela laboratorio primeiro por causa da FK
        jdbcTemplate.update("DELETE FROM laboratorio WHERE codigo_sala = ?", id);
        // Depois deleta da tabela sala
        jdbcTemplate.update("DELETE FROM sala WHERE codigo = ?", id);
    }
}
