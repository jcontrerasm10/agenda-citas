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
