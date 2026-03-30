package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

public abstract class Vehiculo {
    protected int id;
    protected String placa;
    protected double capacidadMaxima;
    protected EstadoVehiculo estado;

    public Vehiculo() {
    }

    public Vehiculo(int id, String placa, double capacidadMaxima, EstadoVehiculo estado) {
        this.id = id;
        this.placa = placa;
        this.capacidadMaxima = capacidadMaxima;
        this.estado = estado;
    }

    public abstract double calcularCapacidad();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public double getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(double capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }

    public boolean estaDisponible() {
        return estado == EstadoVehiculo.DISPONIBLE;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "id=" + id +
                ", placa='" + placa + '\'' +
                ", capacidadMaxima=" + capacidadMaxima +
                ", estado=" + estado +
                '}';
    }
}
