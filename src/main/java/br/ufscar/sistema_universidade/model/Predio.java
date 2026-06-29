package br.ufscar.sistema_universidade.model;

public class Predio {

    private Long codigo;
    private String nome;
    private String bloco;
    private Long codigoCampus;
    private Campus campus;

    public Predio() {
    }

    public Predio(Long codigo, String nome, String bloco, Long codigoCampus) {
        this.codigo = codigo;
        this.nome = nome;
        this.bloco = bloco;
        this.codigoCampus = codigoCampus;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }

    public Long getCodigoCampus() {
        return codigoCampus;
    }

    public void setCodigoCampus(Long codigoCampus) {
        this.codigoCampus = codigoCampus;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }
}
