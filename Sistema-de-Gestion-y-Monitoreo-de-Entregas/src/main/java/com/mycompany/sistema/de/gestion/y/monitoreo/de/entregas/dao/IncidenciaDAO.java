package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.dao;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ConexionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Incidencia;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.util.ConexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IncidenciaDAO {

    // ------------------------------------------------------------------ READ

    public List<Incidencia> obtenerTodas() throws ConexionException, SQLException {
        List<Incidencia> lista = new ArrayList<>();
        String sql = "SELECT id_incidencia, descripcion, fecha, id_paquete FROM INCIDENCIA";

        try (Connection conexion = ConexionDB.getConexion();
             Statement stm = conexion.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearIncidencia(rs));
            }
        }
        return lista;
    }

    public List<Incidencia> obtenerPorPaquete(int idPaquete) throws ConexionException, SQLException {
        List<Incidencia> lista = new ArrayList<>();
        String sql = "SELECT id_incidencia, descripcion, fecha, id_paquete FROM INCIDENCIA WHERE id_paquete = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idPaquete);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearIncidencia(rs));
                }
            }
        }
        return lista;
    }

    // ----------------------------------------------------------------- CREATE

    public boolean insertar(Incidencia incidencia) throws ConexionException, SQLException {
        String sql = "INSERT INTO INCIDENCIA (descripcion, fecha, id_paquete) VALUES (?, ?, ?)";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, incidencia.getDescripcion());
            ps.setTimestamp(2, Timestamp.valueOf(incidencia.getFecha()));
            ps.setInt(3, incidencia.getIdPaquete());
            return ps.executeUpdate() > 0;
        }
    }

    // ----------------------------------------------------------------- DELETE

    public boolean eliminar(int id) throws ConexionException, SQLException {
        String sql = "DELETE FROM INCIDENCIA WHERE id_incidencia = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // --------------------------------------------------------------- privados

    private Incidencia mapearIncidencia(ResultSet rs) throws SQLException {
        LocalDateTime fecha = rs.getTimestamp("fecha").toLocalDateTime();
        return new Incidencia(
                rs.getInt("id_incidencia"),
                rs.getString("descripcion"),
                fecha,
                rs.getInt("id_paquete")
        );
    }
}
