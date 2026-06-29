package br.ufscar.sistema_universidade.dto;

public class RelatorioInfraestruturaDTO {
    private String campus;
    private String predio;
    private Long totalSalas;
    private Long totalLaboratorios;

    public RelatorioInfraestruturaDTO() {
    }

    public RelatorioInfraestruturaDTO(String campus, String predio, Long totalSalas, Long totalLaboratorios) {
        this.campus = campus;
        this.predio = predio;
        this.totalSalas = totalSalas;
        this.totalLaboratorios = totalLaboratorios;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getPredio() {
        return predio;
    }

    public void setPredio(String predio) {
        this.predio = predio;
    }

    public Long getTotalSalas() {
        return totalSalas;
    }

    public void setTotalSalas(Long totalSalas) {
        this.totalSalas = totalSalas;
    }

    public Long getTotalLaboratorios() {
        return totalLaboratorios;
    }

    public void setTotalLaboratorios(Long totalLaboratorios) {
        this.totalLaboratorios = totalLaboratorios;
    }
}
