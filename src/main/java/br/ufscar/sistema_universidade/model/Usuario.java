package br.ufscar.sistema_universidade.model;

public class Usuario extends Pessoa {

    public Usuario() {
    }

    public Usuario(Long idPessoa, String nome, String cpf, String email, String telefone) {
        super(idPessoa, nome, cpf, email, telefone);
    }
}
