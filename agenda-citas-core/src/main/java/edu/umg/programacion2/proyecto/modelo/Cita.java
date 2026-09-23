package edu.umg.programacion2.proyecto.modelo;

import java.time.LocalDateTime;

public class Cita {

    private int id;
    private String cliente;
    private LocalDateTime fechaHora;
    private String servicio;
    private int duracionMinutos;
    private EstadoCita estado;

    public Cita() {
    }

    // Constructor sin id: para crear una cita nueva (el id lo asigna la BD)
    public Cita(String cliente, LocalDateTime fechaHora, String servicio,
                int duracionMinutos, EstadoCita estado) {
        this.cliente = cliente;
        this.fechaHora = fechaHora;
        this.servicio = servicio;
        this.duracionMinutos = duracionMinutos;
        this.estado = estado;
    }

    // Constructor completo: para reconstruir una cita leida desde la BD
    public Cita(int id, String cliente, LocalDateTime fechaHora, String servicio,
                int duracionMinutos, EstadoCita estado) {
        this.id = id;
        this.cliente = cliente;
        this.fechaHora = fechaHora;
        this.servicio = servicio;
        this.duracionMinutos = duracionMinutos;
        this.estado = estado;
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

    @Override
    public String toString() {
        return "Cita{id=" + id + ", cliente='" + cliente + "', fechaHora=" + fechaHora
                + ", servicio='" + servicio + "', duracionMinutos=" + duracionMinutos
                + ", estado=" + estado + '}';
    }
}