package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

import java.time.LocalDateTime;

public class Asignacion {
    private int id;
    private Vehiculo vehiculo;
    private Paquete paquete;
    private LocalDateTime fecha;

    public Asignacion() {
    }

    public Asignacion(int id, Vehiculo vehiculo, Paquete paquete, LocalDateTime fecha) {
        this.id = id;
        this.vehiculo = vehiculo;
        this.paquete = paquete;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Paquete getPaquete() {
        return paquete;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Asignacion{" +
                "id=" + id +
                ", vehiculo=" + vehiculo +
                ", paquete=" + paquete +
                ", fecha=" + fecha +
                '}';
    }
}
