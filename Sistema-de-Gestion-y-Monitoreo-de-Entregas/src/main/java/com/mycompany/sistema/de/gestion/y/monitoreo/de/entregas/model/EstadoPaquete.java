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
    INCIDENCIA
}
