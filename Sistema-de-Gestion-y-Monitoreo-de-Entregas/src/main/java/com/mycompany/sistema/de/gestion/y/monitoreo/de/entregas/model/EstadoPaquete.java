package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

/**
 * Representa los posibles estados del ciclo de vida de un paquete
 * dentro del sistema de gestión de entregas de QuickDelivery.
 *
 * <ul>
 *   <li>{@link #EN_ESPERA}  – el paquete ha sido registrado pero aún no tiene vehículo asignado.</li>
 *   <li>{@link #EN_TRANSITO} – el paquete fue asignado a un vehículo y está en camino al destino.</li>
 *   <li>{@link #ENTREGADO}  – el paquete llegó exitosamente a su destino.</li>
 *   <li>{@link #INCIDENCIA} – se registró un problema durante el proceso de entrega.</li>
 * </ul>
 *
 * @author QuickDelivery S.A.
 */
public enum EstadoPaquete {

    /** El paquete está registrado en el sistema y esperando ser asignado a un vehículo. */
    EN_ESPERA,

    /** El paquete fue asignado y está siendo transportado hacia su destino. */
    EN_TRANSITO,

    /** El paquete fue entregado exitosamente al destinatario. */
    ENTREGADO,

    /** Se produjo una incidencia durante el proceso de entrega del paquete. */
    INCIDENCIA;

    // ----------------------------------------------------------------- mapeo BD

    /**
     * Convierte el nombre almacenado en la tabla {@code estado_paquete} al enum correspondiente.
     *
     * <p>La base de datos maneja estados más granulares ({@code registrado}, {@code en_bodega},
     * {@code asignado}, {@code devuelto}) que se mapean a las constantes más amplias de este enum.</p>
     *
     * @param nombre valor de la columna {@code nombre} en la base de datos.
     * @return constante del enum equivalente.
     * @throws IllegalArgumentException si el valor no tiene equivalente conocido.
     */
    public static EstadoPaquete fromString(String nombre) {
        switch (nombre.toLowerCase()) {
            case "registrado":
            case "en_bodega":
            case "asignado":    return EN_ESPERA;
            case "en_transito": return EN_TRANSITO;
            case "entregado":
            case "devuelto":    return ENTREGADO;
            case "incidencia":  return INCIDENCIA;
            default: throw new IllegalArgumentException("EstadoPaquete desconocido: " + nombre);
        }
    }

    /**
     * Retorna el nombre tal como está almacenado en la tabla {@code estado_paquete}.
     *
     * @return cadena compatible con la columna {@code nombre} de la base de datos.
     */
    public String toDbString() {
        switch (this) {
            case EN_ESPERA:   return "registrado";
            case EN_TRANSITO: return "en_transito";
            case ENTREGADO:   return "entregado";
            case INCIDENCIA:  return "incidencia";
            default:          return name().toLowerCase();
        }
    }
}
