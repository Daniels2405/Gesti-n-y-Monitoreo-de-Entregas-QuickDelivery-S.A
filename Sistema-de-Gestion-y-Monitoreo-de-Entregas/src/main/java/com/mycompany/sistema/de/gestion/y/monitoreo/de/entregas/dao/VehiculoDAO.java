package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.dao;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ConexionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Camion;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.EstadoVehiculo;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Furgon;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Moto;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Vehiculo;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    // ------------------------------------------------------------------ READ

    public List<Vehiculo> obtenerTodos() throws ConexionException, SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT v.id_vehiculo, v.placa, v.capacidad_maxima, " +
                     "tv.nombre AS tipo, ev.nombre AS estado " +
                     "FROM VEHICULO v " +
                     "JOIN TIPO_VEHICULO tv ON v.id_tipo_vehiculo = tv.id_tipo_vehiculo " +
                     "JOIN ESTADO_VEHICULO ev ON v.id_estado_vehiculo = ev.id_estado_vehiculo";

        try (Connection conexion = ConexionDB.getConexion();
             Statement stm = conexion.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearVehiculo(rs));
            }
        }
        return lista;
    }

    public List<Vehiculo> obtenerDisponibles() throws ConexionException, SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT v.id_vehiculo, v.placa, v.capacidad_maxima, " +
                     "tv.nombre AS tipo, ev.nombre AS estado " +
                     "FROM VEHICULO v " +
                     "JOIN TIPO_VEHICULO tv ON v.id_tipo_vehiculo = tv.id_tipo_vehiculo " +
                     "JOIN ESTADO_VEHICULO ev ON v.id_estado_vehiculo = ev.id_estado_vehiculo " +
                     "WHERE ev.nombre = 'DISPONIBLE'";

        try (Connection conexion = ConexionDB.getConexion();
             Statement stm = conexion.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearVehiculo(rs));
            }
        }
        return lista;
    }

    public Vehiculo obtenerPorId(int id) throws ConexionException, SQLException {
        String sql = "SELECT v.id_vehiculo, v.placa, v.capacidad_maxima, " +
                     "tv.nombre AS tipo, ev.nombre AS estado " +
                     "FROM VEHICULO v " +
                     "JOIN TIPO_VEHICULO tv ON v.id_tipo_vehiculo = tv.id_tipo_vehiculo " +
                     "JOIN ESTADO_VEHICULO ev ON v.id_estado_vehiculo = ev.id_estado_vehiculo " +
                     "WHERE v.id_vehiculo = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearVehiculo(rs);
                }
            }
        }
        return null;
    }

    // ----------------------------------------------------------------- CREATE

    public boolean insertar(Vehiculo vehiculo) throws ConexionException, SQLException {
        String sql = "INSERT INTO VEHICULO (placa, capacidad_maxima, id_tipo_vehiculo, id_estado_vehiculo) " +
                     "VALUES (?, ?, " +
                     "(SELECT id_tipo_vehiculo FROM TIPO_VEHICULO WHERE nombre = ?), " +
                     "(SELECT id_estado_vehiculo FROM ESTADO_VEHICULO WHERE nombre = ?))";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, vehiculo.getPlaca());
            ps.setDouble(2, vehiculo.getCapacidadMaxima());
            ps.setString(3, obtenerNombreTipo(vehiculo));
            ps.setString(4, vehiculo.getEstado().name());
            return ps.executeUpdate() > 0;
        }
    }

    // ----------------------------------------------------------------- UPDATE

    public boolean actualizar(Vehiculo vehiculo) throws ConexionException, SQLException {
        String sql = "UPDATE VEHICULO SET placa = ?, capacidad_maxima = ?, " +
                     "id_estado_vehiculo = (SELECT id_estado_vehiculo FROM ESTADO_VEHICULO WHERE nombre = ?) " +
                     "WHERE id_vehiculo = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, vehiculo.getPlaca());
            ps.setDouble(2, vehiculo.getCapacidadMaxima());
            ps.setString(3, vehiculo.getEstado().name());
            ps.setInt(4, vehiculo.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // ----------------------------------------------------------------- DELETE

    public boolean eliminar(int id) throws ConexionException, SQLException {
        String sql = "DELETE FROM VEHICULO WHERE id_vehiculo = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // --------------------------------------------------------------- privados

    private Vehiculo mapearVehiculo(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_vehiculo");
        String placa = rs.getString("placa");
        double capacidad = rs.getDouble("capacidad_maxima");
        EstadoVehiculo estado = EstadoVehiculo.valueOf(rs.getString("estado"));
        String tipo = rs.getString("tipo");

        switch (tipo) {
            case "CAMION":  return new Camion(id, placa, capacidad, estado);
            case "MOTO":    return new Moto(id, placa, capacidad, estado);
            case "FURGON":  return new Furgon(id, placa, capacidad, estado);
            default: throw new SQLException("Tipo de vehiculo desconocido: " + tipo);
        }
    }

    private String obtenerNombreTipo(Vehiculo vehiculo) {
        if (vehiculo instanceof Camion)  return "CAMION";
        if (vehiculo instanceof Moto)    return "MOTO";
        if (vehiculo instanceof Furgon)  return "FURGON";
        return "DESCONOCIDO";
    }
}
