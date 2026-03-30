package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

import java.time.LocalDateTime;

/**
 * Representa la asignación de un {@link Paquete} a un {@link Vehiculo}
 * en un momento determinado dentro del sistema de QuickDelivery S.A.
 *
 * <p>Cuando se crea una asignación, el paquete pasa al estado
 * {@link EstadoPaquete#EN_TRANSITO} y el vehículo al estado
 * {@link EstadoVehiculo#EN_RUTA}.</p>
 *
 * @author QuickDelivery S.A.
 */
public class Asignacion {

    /** Identificador único de la asignación en la base de datos. */
    private int id;

    /** Vehículo asignado para realizar la entrega. */
    private Vehiculo vehiculo;

    /** Paquete que será entregado. */
    private Paquete paquete;

    /** Fecha y hora en que se realizó la asignación. */
    private LocalDateTime fecha;

    /**
     * Constructor sin argumentos requerido para frameworks de persistencia
     * y deserialización.
     */
    public Asignacion() {
    }

    /**
     * Construye una asignación con todos sus atributos.
     *
     * @param id      identificador único de la asignación.
     * @param vehiculo vehículo asignado para la entrega.
     * @param paquete  paquete a entregar.
     * @param fecha   fecha y hora de la asignación.
     */
    public Asignacion(int id, Vehiculo vehiculo, Paquete paquete, LocalDateTime fecha) {
        this.id = id;
        this.vehiculo = vehiculo;
        this.paquete = paquete;
        this.fecha = fecha;
    }

    /**
     * Retorna el identificador único de la asignación.
     *
     * @return id de la asignación.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único de la asignación.
     *
     * @param id nuevo id de la asignación.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna el vehículo asignado para realizar la entrega.
     *
     * @return vehículo de la asignación.
     */
    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    /**
     * Establece el vehículo asignado para realizar la entrega.
     *
     * @param vehiculo vehículo de la asignación.
     */
    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    /**
     * Retorna el paquete asociado a la asignación.
     *
     * @return paquete de la asignación.
     */
    public Paquete getPaquete() {
        return paquete;
    }

    /**
     * Establece el paquete asociado a la asignación.
     *
     * @param paquete paquete de la asignación.
     */
    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
    }

    /**
     * Retorna la fecha y hora en que se realizó la asignación.
     *
     * @return fecha y hora de la asignación.
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha y hora de la asignación.
     *
     * @param fecha nueva fecha y hora de la asignación.
     */
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    /**
     * {@inheritDoc}
     */
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
