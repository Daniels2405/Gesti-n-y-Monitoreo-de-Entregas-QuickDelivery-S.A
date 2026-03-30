package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

public class Furgon extends Vehiculo {

    public Furgon() {
        super();
    }

    public Furgon(int id, String placa, double capacidadMaxima, EstadoVehiculo estado) {
        super(id, placa, capacidadMaxima, estado);
    }

    @Override
    public double calcularCapacidad() {
        return capacidadMaxima;
    }
}
