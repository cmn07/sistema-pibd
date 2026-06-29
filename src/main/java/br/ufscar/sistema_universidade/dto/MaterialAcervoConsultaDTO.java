package br.ufscar.sistema_universidade.dto;

import java.time.LocalDate;

public class MaterialAcervoConsultaDTO {

    private final Long codigoMaterial;
    private final String titulo;
    private final String tipoMaterial;
    private final String categoria;
    private final String editora;
    private final String idioma;
    private final Integer anoPublicacao;
    private final Integer quantidadeCopias;
    private final Long idEmprestimo;
    private final String statusEmprestimo;
    private final LocalDate dataDevolucaoPrevista;
    private final Long codigoUsuario;
    private final String nomeUsuario;

    public MaterialAcervoConsultaDTO(
            Long codigoMaterial,
            String titulo,
            String tipoMaterial,
            String categoria,
            String editora,
            String idioma,
            Integer anoPublicacao,
            Integer quantidadeCopias,
            Long idEmprestimo,
            String statusEmprestimo,
            LocalDate dataDevolucaoPrevista,
            Long codigoUsuario,
            String nomeUsuario
    ) {
        this.codigoMaterial = codigoMaterial;
        this.titulo = titulo;
        this.tipoMaterial = tipoMaterial;
        this.categoria = categoria;
        this.editora = editora;
        this.idioma = idioma;
        this.anoPublicacao = anoPublicacao;
        this.quantidadeCopias = quantidadeCopias;
        this.idEmprestimo = idEmprestimo;
        this.statusEmprestimo = statusEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.codigoUsuario = codigoUsuario;
        this.nomeUsuario = nomeUsuario;
    }

    public Long getCodigoMaterial() {
        return codigoMaterial;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getEditora() {
        return editora;
    }

    public String getIdioma() {
        return idioma;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public Integer getQuantidadeCopias() {
        return quantidadeCopias;
    }

    public Long getIdEmprestimo() {
        return idEmprestimo;
    }

    public String getStatusEmprestimo() {
        return statusEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public Long getCodigoUsuario() {
        return codigoUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public boolean isEmprestado() {
        return idEmprestimo != null;
    }
}
