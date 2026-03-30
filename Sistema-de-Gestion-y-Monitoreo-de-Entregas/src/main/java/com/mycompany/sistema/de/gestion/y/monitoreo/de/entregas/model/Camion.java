package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

/**
 * Representa un camión de reparto en el sistema de QuickDelivery S.A.
 *
 * <p>El camión aplica un factor de capacidad de {@code 1.5} sobre la
 * {@link Vehiculo#capacidadMaxima capacidad base}, lo que lo convierte
 * en el vehículo con mayor capacidad efectiva de carga del sistema.</p>
 *
 * @author QuickDelivery S.A.
 * @see Vehiculo
 */
public class Camion extends Vehiculo {

    /**
     * Constructor sin argumentos requerido para frameworks de persistencia
     * y deserialización.
     */
    public Camion() {
        super();
    }

    /**
     * Construye un camión con todos sus atributos.
     *
     * @param id             identificador único del camión.
     * @param placa          placa/matrícula del camión.
     * @param capacidadMaxima capacidad máxima de carga base en kg.
     * @param estado         estado operativo inicial del camión.
     */
    public Camion(int id, String placa, double capacidadMaxima, EstadoVehiculo estado) {
        super(id, placa, capacidadMaxima, estado);
    }

    /**
     * Calcula la capacidad efectiva del camión aplicando un factor de {@code 1.5}
     * sobre la capacidad base, reflejando su mayor volumen de carga.
     *
     * @return capacidad efectiva en kg ({@code capacidadMaxima * 1.5}).
     */
    @Override
    public double calcularCapacidad() {
        return capacidadMaxima * 1.5;
    }
}
