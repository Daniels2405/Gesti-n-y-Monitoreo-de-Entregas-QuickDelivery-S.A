package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

/**
 * Representa un furgón de reparto en el sistema de QuickDelivery S.A.
 *
 * <p>El furgón no aplica ningún factor de ajuste, por lo que su capacidad
 * efectiva es igual a la {@link Vehiculo#capacidadMaxima capacidad base}.
 * Se posiciona entre la moto y el camión en términos de capacidad de carga.</p>
 *
 * @author QuickDelivery S.A.
 * @see Vehiculo
 */
public class Furgon extends Vehiculo {

    /**
     * Constructor sin argumentos requerido para frameworks de persistencia
     * y deserialización.
     */
    public Furgon() {
        super();
    }

    /**
     * Construye un furgón con todos sus atributos.
     *
     * @param id             identificador único del furgón.
     * @param placa          placa/matrícula del furgón.
     * @param capacidadMaxima capacidad máxima de carga base en kg.
     * @param estado         estado operativo inicial del furgón.
     */
    public Furgon(int id, String placa, double capacidadMaxima, EstadoVehiculo estado) {
        super(id, placa, capacidadMaxima, estado);
    }

    /**
     * Retorna la capacidad efectiva del furgón, que es igual a su
     * capacidad base sin ningún factor de ajuste.
     *
     * @return capacidad efectiva en kg ({@code capacidadMaxima}).
     */
    @Override
    public double calcularCapacidad() {
        return capacidadMaxima;
    }
}
