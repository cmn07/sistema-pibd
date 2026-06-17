package br.ufscar.sistema_universidade.model;

import java.time.LocalDate;

public class Pendencia {

    private Long codigo;
    private String motivo;
    private String statusPendencia;
    private LocalDate dataInicioPendencia;
    private LocalDate dataFimPendencia;
    private Long codigoEmprestimo;
    private Long codigoAdministrador;

    public Pendencia() {
    }

    public Pendencia(Long codigo, String motivo, String statusPendencia, LocalDate dataInicioPendencia,
                     LocalDate dataFimPendencia, Long codigoEmprestimo, Long codigoAdministrador) {
        this.codigo = codigo;
        this.motivo = motivo;
        this.statusPendencia = statusPendencia;
        this.dataInicioPendencia = dataInicioPendencia;
        this.dataFimPendencia = dataFimPendencia;
        this.codigoEmprestimo = codigoEmprestimo;
        this.codigoAdministrador = codigoAdministrador;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getStatusPendencia() {
        return statusPendencia;
    }

    public void setStatusPendencia(String statusPendencia) {
        this.statusPendencia = statusPendencia;
    }

    public LocalDate getDataInicioPendencia() {
        return dataInicioPendencia;
    }

    public void setDataInicioPendencia(LocalDate dataInicioPendencia) {
        this.dataInicioPendencia = dataInicioPendencia;
    }

    public LocalDate getDataFimPendencia() {
        return dataFimPendencia;
    }

    public void setDataFimPendencia(LocalDate dataFimPendencia) {
        this.dataFimPendencia = dataFimPendencia;
    }

    public Long getCodigoEmprestimo() {
        return codigoEmprestimo;
    }

    public void setCodigoEmprestimo(Long codigoEmprestimo) {
        this.codigoEmprestimo = codigoEmprestimo;
    }

    public Long getCodigoAdministrador() {
        return codigoAdministrador;
    }

    public void setCodigoAdministrador(Long codigoAdministrador) {
        this.codigoAdministrador = codigoAdministrador;
    }
}
