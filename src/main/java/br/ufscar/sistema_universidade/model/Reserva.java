package br.ufscar.sistema_universidade.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reserva {

    private Long idReserva;
    private LocalDate data;
    private String tipoReserva;
    private String statusReserva;
    private String objetivo;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;
    private Long codigoUsuario;
    private Long codigoSala;
    private Long codigoAdministrador;

    public Reserva() {
    }

    public Reserva(Long idReserva, LocalDate data, String tipoReserva, String statusReserva, String objetivo,
                   LocalTime horarioInicio, LocalTime horarioFim, Long codigoUsuario, Long codigoSala,
                   Long codigoAdministrador) {
        this.idReserva = idReserva;
        this.data = data;
        this.tipoReserva = tipoReserva;
        this.statusReserva = statusReserva;
        this.objetivo = objetivo;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.codigoUsuario = codigoUsuario;
        this.codigoSala = codigoSala;
        this.codigoAdministrador = codigoAdministrador;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getTipoReserva() {
        return tipoReserva;
    }

    public void setTipoReserva(String tipoReserva) {
        this.tipoReserva = tipoReserva;
    }

    public String getStatusReserva() {
        return statusReserva;
    }

    public void setStatusReserva(String statusReserva) {
        this.statusReserva = statusReserva;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalTime horarioFim) {
        this.horarioFim = horarioFim;
    }

    public Long getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(Long codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public Long getCodigoSala() {
        return codigoSala;
    }

    public void setCodigoSala(Long codigoSala) {
        this.codigoSala = codigoSala;
    }

    public Long getCodigoAdministrador() {
        return codigoAdministrador;
    }

    public void setCodigoAdministrador(Long codigoAdministrador) {
        this.codigoAdministrador = codigoAdministrador;
    }
}
