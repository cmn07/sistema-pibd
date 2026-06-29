package br.ufscar.sistema_universidade.model;

public class Laboratorio extends Sala {

    private String departamentoSetor;
    private Integer quantidadeEquipamentosInformatica;
    private Predio predio;
    private Campus campus;

    public Laboratorio() {
    }

    public Laboratorio(Long codigo, String numeroSala, String categoria, Integer capacidade, Long codigoPredio,
                       String departamentoSetor, Integer quantidadeEquipamentosInformatica) {
        super(codigo, numeroSala, categoria, capacidade, codigoPredio);
        this.departamentoSetor = departamentoSetor;
        this.quantidadeEquipamentosInformatica = quantidadeEquipamentosInformatica;
    }

    public String getDepartamentoSetor() {
        return departamentoSetor;
    }

    public void setDepartamentoSetor(String departamentoSetor) {
        this.departamentoSetor = departamentoSetor;
    }

    public Integer getQuantidadeEquipamentosInformatica() {
        return quantidadeEquipamentosInformatica;
    }

    public void setQuantidadeEquipamentosInformatica(Integer quantidadeEquipamentosInformatica) {
        this.quantidadeEquipamentosInformatica = quantidadeEquipamentosInformatica;
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
