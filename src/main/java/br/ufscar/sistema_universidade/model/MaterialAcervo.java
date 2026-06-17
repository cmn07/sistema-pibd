package br.ufscar.sistema_universidade.model;

public class MaterialAcervo {

    private Long codigo;
    private String tipoMaterial;
    private String titulo;
    private Integer quantidadeCopias;
    private Integer anoPublicacao;
    private String idioma;
    private String categoria;
    private String editora;

    public MaterialAcervo() {
    }

    public MaterialAcervo(Long codigo, String tipoMaterial, String titulo, Integer quantidadeCopias,
                          Integer anoPublicacao, String idioma, String categoria, String editora) {
        this.codigo = codigo;
        this.tipoMaterial = tipoMaterial;
        this.titulo = titulo;
        this.quantidadeCopias = quantidadeCopias;
        this.anoPublicacao = anoPublicacao;
        this.idioma = idioma;
        this.categoria = categoria;
        this.editora = editora;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public void setTipoMaterial(String tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getQuantidadeCopias() {
        return quantidadeCopias;
    }

    public void setQuantidadeCopias(Integer quantidadeCopias) {
        this.quantidadeCopias = quantidadeCopias;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }
}
