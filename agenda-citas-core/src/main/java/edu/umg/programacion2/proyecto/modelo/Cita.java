package edu.umg.programacion2.proyecto.modelo;

import java.time.LocalDateTime;

public class Cita {

    private int id;
    private String cliente;
    private LocalDateTime fechaHora;
    private String servicio;
    private int duracionMinutos;
    private EstadoCita estado;
    private boolean requiereConfirmacionLlamada;
    private boolean esPrimeraVisita;

    public Cita() {
    }

    // Constructor sin id: para crear una cita nueva (el id lo asigna la BD)
    public Cita(String cliente, LocalDateTime fechaHora, String servicio,
            int duracionMinutos, EstadoCita estado,
            boolean requiereConfirmacionLlamada, boolean esPrimeraVisita) {
        this.cliente = cliente;
        this.fechaHora = fechaHora;
        this.servicio = servicio;
        this.duracionMinutos = duracionMinutos;
        this.estado = estado;
        this.requiereConfirmacionLlamada = requiereConfirmacionLlamada;
        this.esPrimeraVisita = esPrimeraVisita;
    }

    // Constructor completo: para reconstruir una cita leida desde la BD
    public Cita(int id, String cliente, LocalDateTime fechaHora, String servicio,
            int duracionMinutos, EstadoCita estado,
            boolean requiereConfirmacionLlamada, boolean esPrimeraVisita) {
        this.id = id;
        this.cliente = cliente;
        this.fechaHora = fechaHora;
        this.servicio = servicio;
        this.duracionMinutos = duracionMinutos;
        this.estado = estado;
        this.requiereConfirmacionLlamada = requiereConfirmacionLlamada;
        this.esPrimeraVisita = esPrimeraVisita;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public boolean isRequiereConfirmacionLlamada() { return requiereConfirmacionLlamada; }
    public void setRequiereConfirmacionLlamada(boolean requiereConfirmacionLlamada) {
        this.requiereConfirmacionLlamada = requiereConfirmacionLlamada;
    }

    public boolean isEsPrimeraVisita() { return esPrimeraVisita; }
    public void setEsPrimeraVisita(boolean esPrimeraVisita) {
        this.esPrimeraVisita = esPrimeraVisita;
    }
    
    @Override
    public String toString() {
        return "Cita{id=" + id + ", cliente='" + cliente + "', fechaHora=" + fechaHora
                + ", servicio='" + servicio + "', duracionMinutos=" + duracionMinutos
                + ", estado=" + estado + '}';
    }
}