package br.ufscar.sistema_universidade.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class RelatorioReservaDTO {

    private final Long idReserva;
    private final LocalDate data;
    private final LocalTime horarioInicio;
    private final LocalTime horarioFim;
    private final String tipoReserva;
    private final String statusReserva;
    private final String nomeSolicitante;
    private final String numeroSala;
    private final String categoriaSala;
    private final String nomePredio;
    private final String blocoPredio;
    private final String nomeCampus;

    public RelatorioReservaDTO(
            Long idReserva,
            LocalDate data,
            LocalTime horarioInicio,
            LocalTime horarioFim,
            String tipoReserva,
            String statusReserva,
            String nomeSolicitante,
            String numeroSala,
            String categoriaSala,
            String nomePredio,
            String blocoPredio,
            String nomeCampus
    ) {
        this.idReserva = idReserva;
        this.data = data;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.tipoReserva = tipoReserva;
        this.statusReserva = statusReserva;
        this.nomeSolicitante = nomeSolicitante;
        this.numeroSala = numeroSala;
        this.categoriaSala = categoriaSala;
        this.nomePredio = nomePredio;
        this.blocoPredio = blocoPredio;
        this.nomeCampus = nomeCampus;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public String getTipoReserva() {
        return tipoReserva;
    }

    public String getStatusReserva() {
        return statusReserva;
    }

    public String getNomeSolicitante() {
        return nomeSolicitante;
    }

    public String getNumeroSala() {
        return numeroSala;
    }

    public String getCategoriaSala() {
        return categoriaSala;
    }

    public String getNomePredio() {
        return nomePredio;
    }

    public String getBlocoPredio() {
        return blocoPredio;
    }

    public String getNomeCampus() {
        return nomeCampus;
    }
}
