package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.util;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ConexionException;
import java.sql.*;
import java.util.Properties;
import java.io.*;

/**
 * Singleton que provee una única conexión JDBC a la base de datos MySQL.
 * Lee las credenciales desde {@code config.properties} en el classpath.
 *
 * <p>Uso:
 * <pre>
 *   Connection con = ConexionDB.getConexion();
 * </pre>
 *
 * @author daniel-2405
 */
public class ConexionDB {

    private static Connection conexion;

    /**
     * Retorna la conexión activa a la base de datos.
     * Si no existe o fue cerrada, crea una nueva leyendo {@code config.properties}.
     *
     * @return {@link Connection} activa con la base de datos
     * @throws ConexionException si no se puede leer la configuración o conectar
     */
    public static Connection getConexion() throws ConexionException {
        try {
            if (conexion == null || conexion.isClosed()){
                Properties props = new Properties();
                InputStream input = ConexionDB.class.getClassLoader().getResourceAsStream("config.properties");
                props.load(input);
                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String password = props.getProperty("db.password");
                conexion = DriverManager.getConnection(url, user, password);
                System.out.println("Conexion establecida");
            }
        } catch (Exception e) {
            throw new ConexionException("Error al crear la conexion a la base de datos: " + e.getMessage());
        }
        return conexion;
    }

    /**
     * Cierra la conexión activa y libera los recursos JDBC.
     * Llamar al cerrar la aplicación.
     *
     * @throws ConexionException si ocurre un error al cerrar
     */
    public static void cerrarConexion() throws ConexionException {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                System.out.println("Conexion cerrada");
            } catch (SQLException e) {
                throw new ConexionException("Error al cerrar la conexion: " + e.getMessage());
            }
        }
    }
}
