package br.ufscar.sistema_universidade.model;

public class Livro extends MaterialAcervo {

    private String edicao;
    private String isbn;
    private String tradutor;

    public Livro() {
    }

    public Livro(Long codigo, String tipoMaterial, String titulo, Integer quantidadeCopias,
                 Integer anoPublicacao, String idioma, String categoria, String editora,
                 String edicao, String isbn, String tradutor) {
        super(codigo, tipoMaterial, titulo, quantidadeCopias, anoPublicacao, idioma, categoria, editora);
        this.edicao = edicao;
        this.isbn = isbn;
        this.tradutor = tradutor;
    }

    public String getEdicao() {
        return edicao;
    }

    public void setEdicao(String edicao) {
        this.edicao = edicao;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTradutor() {
        return tradutor;
    }

    public void setTradutor(String tradutor) {
        this.tradutor = tradutor;
    }
}
