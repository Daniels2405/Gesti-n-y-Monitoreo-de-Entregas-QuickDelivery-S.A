package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.dao;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ConexionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.EstadoPaquete;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Paquete;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaqueteDAO {

    // ------------------------------------------------------------------ READ

    public List<Paquete> obtenerTodos() throws ConexionException, SQLException {
        List<Paquete> lista = new ArrayList<>();
        String sql = "SELECT p.id_paquete, p.codigo, p.descripcion, p.peso, ep.nombre AS estado " +
                     "FROM PAQUETE p " +
                     "JOIN ESTADO_PAQUETE ep ON p.id_estado_paquete = ep.id_estado_paquete";

        try (Connection conexion = ConexionDB.getConexion();
             Statement stm = conexion.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearPaquete(rs));
            }
        }
        return lista;
    }

    public Paquete obtenerPorId(int id) throws ConexionException, SQLException {
        String sql = "SELECT p.id_paquete, p.codigo, p.descripcion, p.peso, ep.nombre AS estado " +
                     "FROM PAQUETE p " +
                     "JOIN ESTADO_PAQUETE ep ON p.id_estado_paquete = ep.id_estado_paquete " +
                     "WHERE p.id_paquete = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPaquete(rs);
                }
            }
        }
        return null;
    }

    public List<Paquete> obtenerPorEstado(EstadoPaquete estado) throws ConexionException, SQLException {
        List<Paquete> lista = new ArrayList<>();
        String sql = "SELECT p.id_paquete, p.codigo, p.descripcion, p.peso, ep.nombre AS estado " +
                     "FROM PAQUETE p " +
                     "JOIN ESTADO_PAQUETE ep ON p.id_estado_paquete = ep.id_estado_paquete " +
                     "WHERE ep.nombre = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, estado.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPaquete(rs));
                }
            }
        }
        return lista;
    }

    // ----------------------------------------------------------------- CREATE

    public boolean insertar(Paquete paquete) throws ConexionException, SQLException {
        String sql = "INSERT INTO PAQUETE (codigo, descripcion, peso, id_estado_paquete) " +
                     "VALUES (?, ?, ?, " +
                     "(SELECT id_estado_paquete FROM ESTADO_PAQUETE WHERE nombre = ?))";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, paquete.getCodigo());
            ps.setString(2, paquete.getDescripcion());
            ps.setDouble(3, paquete.getPeso());
            ps.setString(4, paquete.getEstado().name());
            return ps.executeUpdate() > 0;
        }
    }

    // ----------------------------------------------------------------- UPDATE

    public boolean actualizar(Paquete paquete) throws ConexionException, SQLException {
        String sql = "UPDATE PAQUETE SET codigo = ?, descripcion = ?, peso = ?, " +
                     "id_estado_paquete = (SELECT id_estado_paquete FROM ESTADO_PAQUETE WHERE nombre = ?) " +
                     "WHERE id_paquete = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, paquete.getCodigo());
            ps.setString(2, paquete.getDescripcion());
            ps.setDouble(3, paquete.getPeso());
            ps.setString(4, paquete.getEstado().name());
            ps.setInt(5, paquete.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // ----------------------------------------------------------------- DELETE

    public boolean eliminar(int id) throws ConexionException, SQLException {
        String sql = "DELETE FROM PAQUETE WHERE id_paquete = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // --------------------------------------------------------------- privados

    private Paquete mapearPaquete(ResultSet rs) throws SQLException {
        return new Paquete(
                rs.getInt("id_paquete"),
                rs.getString("codigo"),
                rs.getString("descripcion"),
                rs.getDouble("peso"),
                EstadoPaquete.valueOf(rs.getString("estado"))
        );
    }
}
