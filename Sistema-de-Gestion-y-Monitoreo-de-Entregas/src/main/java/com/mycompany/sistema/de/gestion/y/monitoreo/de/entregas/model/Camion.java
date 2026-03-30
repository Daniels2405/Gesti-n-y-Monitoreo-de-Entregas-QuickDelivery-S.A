package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

public class Camion extends Vehiculo {

    public Camion() {
        super();
    }

    public Camion(int id, String placa, double capacidadMaxima, EstadoVehiculo estado) {
        super(id, placa, capacidadMaxima, estado);
    }

    @Override
    public double calcularCapacidad() {
        return capacidadMaxima * 1.5;
    }
}
