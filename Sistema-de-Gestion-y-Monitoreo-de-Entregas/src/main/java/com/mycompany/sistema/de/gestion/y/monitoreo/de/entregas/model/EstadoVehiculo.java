package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

/**
 * Representa los posibles estados operativos de un vehículo
 * dentro del sistema de gestión de entregas de QuickDelivery.
 *
 * <ul>
 *   <li>{@link #DISPONIBLE} – el vehículo está libre y puede recibir una asignación.</li>
 *   <li>{@link #EN_RUTA}    – el vehículo está en camino realizando una entrega.</li>
 *   <li>{@link #INACTIVO}   – el vehículo no está operativo (mantenimiento, fuera de servicio, etc.).</li>
 * </ul>
 *
 * @author QuickDelivery S.A.
 */
public enum EstadoVehiculo {

    /** El vehículo se encuentra disponible para ser asignado a una entrega. */
    DISPONIBLE,

    /** El vehículo está actualmente en ruta realizando una entrega. */
    EN_RUTA,

    /** El vehículo está inactivo y no puede recibir asignaciones. */
    INACTIVO
}
