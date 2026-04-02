package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

/**
 * Representa una moto de reparto en el sistema de QuickDelivery S.A.
 *
 * <p>La moto aplica un factor de capacidad de {@code 0.5} sobre la
 * {@link Vehiculo#capacidadMaxima capacidad base}, lo que la convierte
 * en el vehículo con menor capacidad efectiva de carga del sistema,
 * adecuada para entregas de paquetes pequeños y ligeros.</p>
 *
 * @author QuickDelivery S.A.
 * @see Vehiculo
 */
public class Moto extends Vehiculo {

    /**
     * Constructor sin argumentos requerido para frameworks de persistencia
     * y deserialización.
     */
    public Moto() {
        super();
    }

    /**
     * Construye una moto con todos sus atributos.
     *
     * @param id             identificador único de la moto.
     * @param placa          placa/matrícula de la moto.
     * @param capacidadMaxima capacidad máxima de carga base en kg.
     * @param estado         estado operativo inicial de la moto.
     */
    public Moto(int id, String placa, double capacidadMaxima, EstadoVehiculo estado) {
        super(id, placa, capacidadMaxima, estado);
    }

    /**
     * Calcula la capacidad efectiva de la moto aplicando un factor de {@code 0.5}
     * sobre la capacidad base, reflejando su limitado espacio de carga.
     *
     * @return capacidad efectiva en kg ({@code capacidadMaxima * 0.5}).
     */
    @Override
    public double calcularCapacidad() {
        return capacidadMaxima * 0.5;
    }
}
