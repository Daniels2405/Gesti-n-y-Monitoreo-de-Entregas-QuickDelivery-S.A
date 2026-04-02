package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.util;

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

    /**
     * Crea y retorna una nueva conexión JDBC a la base de datos.
     * Cada llamada abre una conexión independiente; el llamador es
     * responsable de cerrarla (idealmente con try-with-resources).
     *
     * @return nueva {@link Connection} con la base de datos
     * @throws ConexionException si no se puede leer la configuración o conectar
     */
    public static Connection getConexion() throws ConexionException {
        try {
            Properties props = new Properties();
            InputStream input = ConexionDB.class.getClassLoader().getResourceAsStream("config.properties");
            props.load(input);
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new ConexionException("Error al crear la conexion a la base de datos: " + e.getMessage());
        }
    }
}
