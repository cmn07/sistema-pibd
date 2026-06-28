package br.ufscar.sistema_universidade.dto;

import java.time.LocalDate;

public class EmprestimoResumoDTO {

    private final Long idEmprestimo;
    private final LocalDate dataEmprestimo;
    private final String statusEmprestimo;
    private final LocalDate dataDevolucaoReal;
    private final LocalDate dataDevolucaoPrevista;
    private final Long codigoMaterialAcervo;
    private final String tituloMaterial;
    private final String tipoMaterial;
    private final Long codigoUsuario;
    private final String nomeUsuario;

    public EmprestimoResumoDTO(
            Long idEmprestimo,
            LocalDate dataEmprestimo,
            String statusEmprestimo,
            LocalDate dataDevolucaoReal,
            LocalDate dataDevolucaoPrevista,
            Long codigoMaterialAcervo,
            String tituloMaterial,
            String tipoMaterial,
            Long codigoUsuario,
            String nomeUsuario
    ) {
        this.idEmprestimo = idEmprestimo;
        this.dataEmprestimo = dataEmprestimo;
        this.statusEmprestimo = statusEmprestimo;
        this.dataDevolucaoReal = dataDevolucaoReal;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.codigoMaterialAcervo = codigoMaterialAcervo;
        this.tituloMaterial = tituloMaterial;
        this.tipoMaterial = tipoMaterial;
        this.codigoUsuario = codigoUsuario;
        this.nomeUsuario = nomeUsuario;
    }

    public Long getIdEmprestimo() {
        return idEmprestimo;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public String getStatusEmprestimo() {
        return statusEmprestimo;
    }

    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public Long getCodigoMaterialAcervo() {
        return codigoMaterialAcervo;
    }

    public String getTituloMaterial() {
        return tituloMaterial;
    }

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public Long getCodigoUsuario() {
        return codigoUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }
}
