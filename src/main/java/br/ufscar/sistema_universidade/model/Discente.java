package br.ufscar.sistema_universidade.model;

public class Discente extends Usuario {

    private String ra;
    private String curso;

    public Discente() {
    }

    public Discente(Long idPessoa, String nome, String cpf, String email, String telefone,
                    String ra, String curso) {
        super(idPessoa, nome, cpf, email, telefone);
        this.ra = ra;
        this.curso = curso;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}
