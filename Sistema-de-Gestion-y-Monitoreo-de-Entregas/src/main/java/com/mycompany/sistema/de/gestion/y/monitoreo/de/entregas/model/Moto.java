package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

public class Moto extends Vehiculo {

    public Moto() {
        super();
    }

    public Moto(int id, String placa, double capacidadMaxima, EstadoVehiculo estado) {
        super(id, placa, capacidadMaxima, estado);
    }

    @Override
    public double calcularCapacidad() {
        return capacidadMaxima * 0.5;
    }
}
