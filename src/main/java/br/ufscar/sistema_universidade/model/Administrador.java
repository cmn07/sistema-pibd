package br.ufscar.sistema_universidade.model;

public class Administrador extends Pessoa {

    private String idAdmin;
    private String setor;

    public Administrador() {
    }

    public Administrador(Long idPessoa, String nome, String cpf, String email, String telefone,
                         String idAdmin, String setor) {
        super(idPessoa, nome, cpf, email, telefone);
        this.idAdmin = idAdmin;
        this.setor = setor;
    }

    public String getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }
}
