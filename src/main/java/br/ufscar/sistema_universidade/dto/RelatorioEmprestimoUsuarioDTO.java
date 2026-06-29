package br.ufscar.sistema_universidade.dto;

import java.time.LocalDate;

public class RelatorioEmprestimoUsuarioDTO {

    private Long idPessoa;
    private String nomeUsuario;
    private Integer quantidadeEmprestimos;
    private LocalDate primeiroEmprestimo;
    private LocalDate ultimoEmprestimo;

    public RelatorioEmprestimoUsuarioDTO(Long idPessoa, String nomeUsuario, Integer quantidadeEmprestimos, LocalDate primeiroEmprestimo, LocalDate ultimoEmprestimo) {
        this.idPessoa = idPessoa;
        this.nomeUsuario = nomeUsuario;
        this.quantidadeEmprestimos = quantidadeEmprestimos;
        this.primeiroEmprestimo = primeiroEmprestimo;
        this.ultimoEmprestimo = ultimoEmprestimo;
    }

    public Long getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Long idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public Integer getQuantidadeEmprestimos() {
        return quantidadeEmprestimos;
    }

    public void setQuantidadeEmprestimos(Integer quantidadeEmprestimos) {
        this.quantidadeEmprestimos = quantidadeEmprestimos;
    }

    public LocalDate getPrimeiroEmprestimo() {
        return primeiroEmprestimo;
    }

    public void setPrimeiroEmprestimo(LocalDate primeiroEmprestimo) {
        this.primeiroEmprestimo = primeiroEmprestimo;
    }

    public LocalDate getUltimoEmprestimo() {
        return ultimoEmprestimo;
    }

    public void setUltimoEmprestimo(LocalDate ultimoEmprestimo) {
        this.ultimoEmprestimo = ultimoEmprestimo;
    }
}
