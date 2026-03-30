package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

/**
 * Roles disponibles en el sistema QuickDelivery S.A.
 *
 * <p>Cada usuario tiene exactamente un rol asignado, que determina qué
 * interfaces y operaciones puede ejecutar dentro del sistema.</p>
 *
 * <ul>
 *   <li>{@link #ADMINISTRADOR} – gestiona usuarios, consulta auditoría y
 *       tiene acceso total al sistema.</li>
 *   <li>{@link #DESPACHADOR}   – registra y asigna paquetes a vehículos.</li>
 *   <li>{@link #CONDUCTOR}     – actualiza estados de paquetes y envía
 *       ubicación desde el cliente conductor.</li>
 * </ul>
 *
 * @author QuickDelivery S.A.
 */
public enum Rol {

    /** Acceso completo: gestión de usuarios, auditoría y supervisión general. */
    ADMINISTRADOR,

    /** Gestión de paquetes y asignación a vehículos disponibles. */
    DESPACHADOR,

    /** Actualización de estado de paquetes y envío de ubicación en tiempo real. */
    CONDUCTOR;

    /**
     * Convierte el nombre almacenado en base de datos (minúsculas) al enum correspondiente.
     *
     * @param nombre valor de la columna {@code rol.nombre}, ej: {@code "administrador"}.
     * @return el {@link Rol} correspondiente.
     * @throws IllegalArgumentException si el nombre no coincide con ningún rol conocido.
     */
    public static Rol fromString(String nombre) {
        return Rol.valueOf(nombre.toUpperCase());
    }
}
