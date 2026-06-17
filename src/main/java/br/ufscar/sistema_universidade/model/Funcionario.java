package br.ufscar.sistema_universidade.model;

public class Funcionario extends Usuario {

    private String idFunc;
    private String tipoVinculo;

    public Funcionario() {
    }

    public Funcionario(Long idPessoa, String nome, String cpf, String email, String telefone,
                       String idFunc, String tipoVinculo) {
        super(idPessoa, nome, cpf, email, telefone);
        this.idFunc = idFunc;
        this.tipoVinculo = tipoVinculo;
    }

    public String getIdFunc() {
        return idFunc;
    }

    public void setIdFunc(String idFunc) {
        this.idFunc = idFunc;
    }

    public String getTipoVinculo() {
        return tipoVinculo;
    }

    public void setTipoVinculo(String tipoVinculo) {
        this.tipoVinculo = tipoVinculo;
    }
}
