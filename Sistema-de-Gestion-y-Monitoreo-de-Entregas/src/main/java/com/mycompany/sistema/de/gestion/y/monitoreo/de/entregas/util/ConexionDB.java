/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.util;
import java.sql.*;
import java.util.Properties;
import java.io.*;
/**
 *
 * @author daniel-2405
 */
public class ConexionDB {
    private static Connection conexion;

    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()){
                Properties props = new Properties();
                InputStream input = ConexionDB.class.getClassLoader().getResourceAsStream("db.properties");
                props.load(input);
                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String password = props.getProperty("db.password");
                conexion = DriverManager.getConnection(url, user, password);
                System.out.println("Conexion establecida");
            }
        } catch (Exception e) {
            System.out.println("Error al crear la conexion a la base de datos, " + e.getMessage());
        }
        return conexion;
    }
}
