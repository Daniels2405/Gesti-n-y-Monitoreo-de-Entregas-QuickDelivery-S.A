package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.util;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Registro de eventos del sistema QuickDelivery.
 * Escribe cada evento en el archivo quickdelivery.log 
 * y en la tabla AUDITORIA_LOG de la base de datos.
 *
 * Uso: Logger.registrar("LOGIN", "Conductor#7", "Nombre=Daniel");
 *
 * @author daniel-2405
 */
public class Logger {

    private static final String ARCHIVO_LOG = "quickdelivery.log";
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** Constructor privado — no se instancia, solo métodos estáticos. */
    private Logger() {}

    /**
     * Registra un evento en archivo .log y en la base de datos.
     *
     * @param accion  Qué ocurrió, ej: "LOGIN", "UBICACION", "ERROR_RED"
     * @param usuario Quién lo provocó, ej: "Conductor#7", "SISTEMA"
     * @param detalle Información adicional, ej: "lat=9.934, lon=-84.087"
     */
    public static void registrar(String accion, String usuario, String detalle) {
        LocalDateTime ahora = LocalDateTime.now();
        String linea = String.format("[%s] [%-15s] [%-20s] %s",
                ahora.format(FORMATO), usuario, accion, detalle);

        // 1) Escribir en archivo
        try (PrintWriter printwriter = new PrintWriter(new FileWriter(ARCHIVO_LOG, true))) {
            printwriter.println(linea);
        } catch (IOException e) {
            System.err.println("ERROR escribiendo log en archivo: " + e.getMessage());
        }

        // 2) Escribir en BD
        String sql = "INSERT INTO AUDITORIA_LOG (usuario, accion, detalle, fecha) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConexionDB.getConexion();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, accion);
            ps.setString(3, detalle);
            ps.setTimestamp(4, Timestamp.valueOf(ahora));
            ps.executeUpdate();

        } catch (SQLException e) {
            // Si la BD no está lista aún, el archivo ya tiene el registro
            System.err.println("ERROR escribiendo log en BD: " + e.getMessage());
        }
    }
}
