package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.dao;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ConexionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Asignacion;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Paquete;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Vehiculo;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.util.ConexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AsignacionDAO {

    private final VehiculoDAO vehiculoDAO = new VehiculoDAO();
    private final PaqueteDAO paqueteDAO = new PaqueteDAO();

    // ------------------------------------------------------------------ READ

    public List<Asignacion> obtenerTodas() throws ConexionException, SQLException {
        List<Asignacion> lista = new ArrayList<>();
        String sql = "SELECT id_asignacion, id_vehiculo, id_paquete, fecha FROM ASIGNACION";

        try (Connection conexion = ConexionDB.getConexion();
             Statement stm = conexion.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearAsignacion(rs));
            }
        }
        return lista;
    }

    public Asignacion obtenerPorId(int id) throws ConexionException, SQLException {
        String sql = "SELECT id_asignacion, id_vehiculo, id_paquete, fecha FROM ASIGNACION WHERE id_asignacion = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearAsignacion(rs);
                }
            }
        }
        return null;
    }

    // ----------------------------------------------------------------- CREATE

    public boolean insertar(Asignacion asignacion) throws ConexionException, SQLException {
        String sql = "INSERT INTO ASIGNACION (id_vehiculo, id_paquete, fecha) VALUES (?, ?, ?)";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, asignacion.getVehiculo().getId());
            ps.setInt(2, asignacion.getPaquete().getId());
            ps.setTimestamp(3, Timestamp.valueOf(asignacion.getFecha()));
            return ps.executeUpdate() > 0;
        }
    }

    // ----------------------------------------------------------------- DELETE

    public boolean eliminar(int id) throws ConexionException, SQLException {
        String sql = "DELETE FROM ASIGNACION WHERE id_asignacion = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // --------------------------------------------------------------- privados

    private Asignacion mapearAsignacion(ResultSet rs) throws ConexionException, SQLException {
        int id = rs.getInt("id_asignacion");
        LocalDateTime fecha = rs.getTimestamp("fecha").toLocalDateTime();
        Vehiculo vehiculo = vehiculoDAO.obtenerPorId(rs.getInt("id_vehiculo"));
        Paquete paquete = paqueteDAO.obtenerPorId(rs.getInt("id_paquete"));
        return new Asignacion(id, vehiculo, paquete, fecha);
    }
}
