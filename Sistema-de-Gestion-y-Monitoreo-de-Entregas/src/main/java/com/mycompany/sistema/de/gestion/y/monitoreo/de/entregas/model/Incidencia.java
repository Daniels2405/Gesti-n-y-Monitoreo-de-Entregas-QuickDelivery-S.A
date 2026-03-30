package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

import java.time.LocalDateTime;

/**
 * Representa una incidencia ocurrida durante el proceso de entrega
 * de un paquete en el sistema de QuickDelivery S.A.
 *
 * <p>Una incidencia describe un problema o novedad (daño, extravío, retraso, etc.)
 * asociado a un {@link Paquete} específico y registra el momento en que ocurrió.</p>
 *
 * @author QuickDelivery S.A.
 */
public class Incidencia {

    /** Identificador único de la incidencia en la base de datos. */
    private int id;

    /** Descripción detallada del problema ocurrido durante la entrega. */
    private String descripcion;

    /** Fecha y hora en que se registró la incidencia. */
    private LocalDateTime fecha;

    /** Identificador del paquete afectado por la incidencia. */
    private int idPaquete;

    /**
     * Constructor sin argumentos requerido para frameworks de persistencia
     * y deserialización.
     */
    public Incidencia() {
    }

    /**
     * Construye una incidencia con todos sus atributos.
     *
     * @param id          identificador único de la incidencia.
     * @param descripcion descripción del problema ocurrido.
     * @param fecha       fecha y hora en que se registró la incidencia.
     * @param idPaquete   id del paquete afectado.
     */
    public Incidencia(int id, String descripcion, LocalDateTime fecha, int idPaquete) {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.idPaquete = idPaquete;
    }

    /**
     * Retorna el identificador único de la incidencia.
     *
     * @return id de la incidencia.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único de la incidencia.
     *
     * @param id nuevo id de la incidencia.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna la descripción del problema registrado.
     *
     * @return descripción de la incidencia.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del problema registrado.
     *
     * @param descripcion nueva descripción de la incidencia.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Retorna la fecha y hora en que se registró la incidencia.
     *
     * @return fecha y hora de la incidencia.
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha y hora en que se registró la incidencia.
     *
     * @param fecha nueva fecha y hora de la incidencia.
     */
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    /**
     * Retorna el identificador del paquete afectado por la incidencia.
     *
     * @return id del paquete afectado.
     */
    public int getIdPaquete() {
        return idPaquete;
    }

    /**
     * Establece el identificador del paquete afectado por la incidencia.
     *
     * @param idPaquete id del paquete afectado.
     */
    public void setIdPaquete(int idPaquete) {
        this.idPaquete = idPaquete;
    }

    /**
     * {@inheritDoc}
     */
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
