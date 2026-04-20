package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

/**
 * Tipos de acción registrables en la tabla {@code AUDITORIA_LOG}.
 *
 * <p>Se usa tanto en {@code AuditoriaDAO} (registro en BD) como en
 * {@code Logger} (registro en archivo {@code .log}) para garantizar
 * valores consistentes en toda la aplicación.</p>
 *
 * @author QuickDelivery S.A.
 */
public enum TipoAccion {

    /** Inicio de sesión exitoso de un usuario. */
    LOGIN,

    /** Cierre de sesión de un usuario. */
    LOGOUT,

    /** Creación de un nuevo registro en el sistema. */
    CREATE,

    /** Modificación de un registro existente. */
    UPDATE,

    /** Eliminación de un registro del sistema. */
    DELETE,

    /** Asignación de un paquete a un vehículo. */
    ASIGNACION
}
