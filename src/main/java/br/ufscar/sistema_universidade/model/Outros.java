package br.ufscar.sistema_universidade.model;

public class Outros extends MaterialAcervo {

    private String descricao;

    public Outros() {
    }

    public Outros(Long codigo, String tipoMaterial, String titulo, Integer quantidadeCopias,
                  Integer anoPublicacao, String idioma, String categoria, String editora, String descricao) {
        super(codigo, tipoMaterial, titulo, quantidadeCopias, anoPublicacao, idioma, categoria, editora);
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
