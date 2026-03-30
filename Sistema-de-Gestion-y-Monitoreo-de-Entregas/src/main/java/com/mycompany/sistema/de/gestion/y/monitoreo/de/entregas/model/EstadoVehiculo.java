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
    INACTIVO;

    // ----------------------------------------------------------------- mapeo BD

    /**
     * Convierte el nombre almacenado en la tabla {@code estado_vehiculo} al enum correspondiente.
     *
     * @param nombre valor de la columna {@code nombre} en la base de datos.
     * @return constante del enum equivalente.
     * @throws IllegalArgumentException si el valor no tiene equivalente conocido.
     */
    public static EstadoVehiculo fromString(String nombre) {
        switch (nombre.toLowerCase()) {
            case "disponible":    return DISPONIBLE;
            case "en_ruta":       return EN_RUTA;
            case "mantenimiento": return INACTIVO;
            default: throw new IllegalArgumentException("EstadoVehiculo desconocido: " + nombre);
        }
    }

    /**
     * Retorna el nombre tal como está almacenado en la tabla {@code estado_vehiculo}.
     *
     * @return cadena compatible con la columna {@code nombre} de la base de datos.
     */
    public String toDbString() {
        switch (this) {
            case DISPONIBLE: return "disponible";
            case EN_RUTA:    return "en_ruta";
            case INACTIVO:   return "mantenimiento";
            default:         return name().toLowerCase();
        }
    }
}
