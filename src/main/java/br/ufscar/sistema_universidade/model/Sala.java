package br.ufscar.sistema_universidade.model;

public class Sala {

    private Long codigo;
    private String numeroSala;
    private String categoria;
    private Integer capacidade;
    private Long codigoPredio;
    private Predio predio;
    private Campus campus;

    public Sala() {
    }

    public Sala(Long codigo, String numeroSala, String categoria, Integer capacidade, Long codigoPredio) {
        this.codigo = codigo;
        this.numeroSala = numeroSala;
        this.categoria = categoria;
        this.capacidade = capacidade;
        this.codigoPredio = codigoPredio;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNumeroSala() {
        return numeroSala;
    }

    public void setNumeroSala(String numeroSala) {
        this.numeroSala = numeroSala;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public Long getCodigoPredio() {
        return codigoPredio;
    }

    public void setCodigoPredio(Long codigoPredio) {
        this.codigoPredio = codigoPredio;
    }

    public Predio getPredio() {
        return predio;
    }

    public void setPredio(Predio predio) {
        this.predio = predio;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }
}
