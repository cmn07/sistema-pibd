package br.ufscar.sistema_universidade.model;

import java.time.LocalDate;

public class Emprestimo {

    private Long idEmprestimo;
    private LocalDate dataEmprestimo;
    private String statusEmprestimo;
    private LocalDate dataDevolucaoReal;
    private LocalDate dataDevolucaoPrevista;
    private Long codigoMaterialAcervo;
    private Long codigoUsuario;
    private Long codigoAdministrador;

    public Emprestimo() {
    }

    public Emprestimo(Long idEmprestimo, LocalDate dataEmprestimo, String statusEmprestimo,
                      LocalDate dataDevolucaoReal, LocalDate dataDevolucaoPrevista,
                      Long codigoMaterialAcervo, Long codigoUsuario, Long codigoAdministrador) {
        this.idEmprestimo = idEmprestimo;
        this.dataEmprestimo = dataEmprestimo;
        this.statusEmprestimo = statusEmprestimo;
        this.dataDevolucaoReal = dataDevolucaoReal;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.codigoMaterialAcervo = codigoMaterialAcervo;
        this.codigoUsuario = codigoUsuario;
        this.codigoAdministrador = codigoAdministrador;
    }

    public Long getIdEmprestimo() {
        return idEmprestimo;
    }

    public void setIdEmprestimo(Long idEmprestimo) {
        this.idEmprestimo = idEmprestimo;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public String getStatusEmprestimo() {
        return statusEmprestimo;
    }

    public void setStatusEmprestimo(String statusEmprestimo) {
        this.statusEmprestimo = statusEmprestimo;
    }

    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public Long getCodigoMaterialAcervo() {
        return codigoMaterialAcervo;
    }

    public void setCodigoMaterialAcervo(Long codigoMaterialAcervo) {
        this.codigoMaterialAcervo = codigoMaterialAcervo;
    }

    public Long getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(Long codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public Long getCodigoAdministrador() {
        return codigoAdministrador;
    }

    public void setCodigoAdministrador(Long codigoAdministrador) {
        this.codigoAdministrador = codigoAdministrador;
    }
}
