package br.ufscar.sistema_universidade.model;

public class Periodico extends MaterialAcervo {

    private String volume;
    private String issn;
    private String numero;

    public Periodico() {
    }

    public Periodico(Long codigo, String tipoMaterial, String titulo, Integer quantidadeCopias,
                     Integer anoPublicacao, String idioma, String categoria, String editora,
                     String volume, String issn, String numero) {
        super(codigo, tipoMaterial, titulo, quantidadeCopias, anoPublicacao, idioma, categoria, editora);
        this.volume = volume;
        this.issn = issn;
        this.numero = numero;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
