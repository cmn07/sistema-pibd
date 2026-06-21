package br.ufscar.sistema_universidade.dto;

public class RelatorioSalaDTO {
    private String nomeCampus;
    private String cidadeCampus;
    private String nomePredio;
    private String blocoPredio;
    private String numeroSala;
    private String categoriaSala;
    private Integer capacidade;

    public RelatorioSalaDTO(String nomeCampus, String cidadeCampus, String nomePredio,
                            String blocoPredio, String numeroSala, String categoriaSala, Integer capacidade) {
        this.nomeCampus = nomeCampus;
        this.cidadeCampus = cidadeCampus;
        this.nomePredio = nomePredio;
        this.blocoPredio = blocoPredio;
        this.numeroSala = numeroSala;
        this.categoriaSala = categoriaSala;
        this.capacidade = capacidade;
    }

    public String getNomeCampus() { return nomeCampus; }
    public String getCidadeCampus() { return cidadeCampus; }
    public String getNomePredio() { return nomePredio; }
    public String getBlocoPredio() { return blocoPredio; }
    public String getNumeroSala() { return numeroSala; }
    public String getCategoriaSala() { return categoriaSala; }
    public Integer getCapacidade() { return capacidade; }
}