package br.ufscar.sistema_universidade.model;

public class CredencialAcesso {

    private Long idPessoa;
    private String nomePessoa;
    private String login;
    private String senhaHash;
    private Boolean ativo;

    public CredencialAcesso() {
    }

    public CredencialAcesso(Long idPessoa, String login, String senhaHash, Boolean ativo) {
        this(idPessoa, null, login, senhaHash, ativo);
    }

    public CredencialAcesso(Long idPessoa, String nomePessoa, String login, String senhaHash, Boolean ativo) {
        this.idPessoa = idPessoa;
        this.nomePessoa = nomePessoa;
        this.login = login;
        this.senhaHash = senhaHash;
        this.ativo = ativo;
    }

    public Long getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Long idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
