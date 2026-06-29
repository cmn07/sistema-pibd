package br.ufscar.sistema_universidade.dto;

import java.time.LocalDate;

public class RelatorioEmprestimoUsuarioDTO {

    private final Long idPessoa;
    private final String nomeUsuario;
    private final Integer quantidadeEmprestimos;
    private final LocalDate primeiroEmprestimo;
    private final LocalDate ultimoEmprestimo;

    public RelatorioEmprestimoUsuarioDTO(Long idPessoa, String nomeUsuario,
                                         Integer quantidadeEmprestimos, LocalDate primeiroEmprestimo, LocalDate ultimoEmprestimo) {
        this.idPessoa = idPessoa;
        this.nomeUsuario = nomeUsuario;
        this.quantidadeEmprestimos = quantidadeEmprestimos;
        this.primeiroEmprestimo = primeiroEmprestimo;
        this.ultimoEmprestimo = ultimoEmprestimo;
    }

    public Long getIdPessoa() {
        return idPessoa;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public Integer getQuantidadeEmprestimos() {
        return quantidadeEmprestimos;
    }

    public LocalDate getPrimeiroEmprestimo() {
        return primeiroEmprestimo;
    }

    public LocalDate getUltimoEmprestimo() {
        return ultimoEmprestimo;
    }
}
