package br.ufscar.sistema_universidade.dto;

import java.time.LocalDate;

public class PendenciaResumoDTO {

    private final Long codigo;
    private final String motivo;
    private final String statusPendencia;
    private final LocalDate dataInicioPendencia;
    private final LocalDate dataFimPendencia;
    private final Long codigoEmprestimo;
    private final Long codigoUsuario;
    private final String nomeUsuario;
    private final String tituloMaterial;
    private final String tipoMaterial;
    private final String nomeAdministrador;

    public PendenciaResumoDTO(
            Long codigo,
            String motivo,
            String statusPendencia,
            LocalDate dataInicioPendencia,
            LocalDate dataFimPendencia,
            Long codigoEmprestimo,
            Long codigoUsuario,
            String nomeUsuario,
            String tituloMaterial,
            String tipoMaterial,
            String nomeAdministrador
    ) {
        this.codigo = codigo;
        this.motivo = motivo;
        this.statusPendencia = statusPendencia;
        this.dataInicioPendencia = dataInicioPendencia;
        this.dataFimPendencia = dataFimPendencia;
        this.codigoEmprestimo = codigoEmprestimo;
        this.codigoUsuario = codigoUsuario;
        this.nomeUsuario = nomeUsuario;
        this.tituloMaterial = tituloMaterial;
        this.tipoMaterial = tipoMaterial;
        this.nomeAdministrador = nomeAdministrador;
    }

    public Long getCodigo() {
        return codigo;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getStatusPendencia() {
        return statusPendencia;
    }

    public LocalDate getDataInicioPendencia() {
        return dataInicioPendencia;
    }

    public LocalDate getDataFimPendencia() {
        return dataFimPendencia;
    }

    public Long getCodigoEmprestimo() {
        return codigoEmprestimo;
    }

    public Long getCodigoUsuario() {
        return codigoUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getTituloMaterial() {
        return tituloMaterial;
    }

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public String getNomeAdministrador() {
        return nomeAdministrador;
    }
}
