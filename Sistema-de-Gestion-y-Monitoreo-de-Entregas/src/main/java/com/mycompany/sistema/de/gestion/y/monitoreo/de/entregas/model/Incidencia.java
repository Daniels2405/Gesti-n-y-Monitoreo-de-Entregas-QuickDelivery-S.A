package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

import java.time.LocalDateTime;

public class Incidencia {
    private int id;
    private String descripcion;
    private LocalDateTime fecha;
    private int idPaquete;

    public Incidencia() {
    }

    public Incidencia(int id, String descripcion, LocalDateTime fecha, int idPaquete) {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.idPaquete = idPaquete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(int idPaquete) {
        this.idPaquete = idPaquete;
    }

    @Override
    public String toString() {
        return "Incidencia{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                ", idPaquete=" + idPaquete +
                '}';
    }
}
