package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.dao;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ConexionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.EstadoPaquete;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Paquete;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) para la entidad {@link Paquete}.
 *
 * <p>Proporciona operaciones CRUD sobre la tabla {@code paquete} de la base
 * de datos, haciendo uso de {@link ConexionDB} para obtener conexiones.</p>
 *
 * @author QuickDelivery S.A.
 */
public class PaqueteDAO {


    /**
     * Retorna la lista de todos los paquetes registrados en el sistema.
     *
     * @return lista de {@link Paquete}; vacía si no hay registros.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public List<Paquete> obtenerTodos() throws ConexionException, SQLException {
        List<Paquete> lista = new ArrayList<>();
        String sql = "SELECT p.id_paquete, p.codigo, p.descripcion, p.peso, ep.nombre AS estado " +
                     "FROM paquete p " +
                     "JOIN estado_paquete ep ON p.id_estado = ep.id_estado";

        try (Connection conexion = ConexionDB.getConexion();
             Statement stm = conexion.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearPaquete(rs));
            }
        }
        return lista;
    }

    /**
     * Busca y retorna un paquete por su identificador único.
     *
     * @param id identificador del paquete a buscar.
     * @return el {@link Paquete} encontrado, o {@code null} si no existe.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public Paquete obtenerPorId(int id) throws ConexionException, SQLException {
        String sql = "SELECT p.id_paquete, p.codigo, p.descripcion, p.peso, ep.nombre AS estado " +
                     "FROM paquete p " +
                     "JOIN estado_paquete ep ON p.id_estado = ep.id_estado " +
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

    /**
     * Retorna los paquetes EN_TRANSITO asignados al vehículo de un conductor específico.
     *
     * @param idConductor identificador del conductor.
     * @return lista de {@link Paquete} en tránsito del conductor; vacía si no hay ninguno.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public List<Paquete> obtenerPorConductor(int idConductor) throws ConexionException, SQLException {
        List<Paquete> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT p.id_paquete, p.codigo, p.descripcion, p.peso, ep.nombre AS estado " +
                     "FROM paquete p " +
                     "JOIN estado_paquete ep ON p.id_estado = ep.id_estado " +
                     "JOIN asignacion a ON a.id_paquete = p.id_paquete " +
                     "JOIN vehiculo v ON v.id_vehiculo = a.id_vehiculo " +
                     "WHERE v.id_conductor = ? AND ep.nombre = 'en_transito'";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idConductor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPaquete(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Retorna la lista de paquetes que se encuentran en el estado indicado.
     *
     * @param estado estado por el que se filtrarán los paquetes.
     * @return lista de {@link Paquete} con el estado especificado; vacía si no hay coincidencias.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public List<Paquete> obtenerPorEstado(EstadoPaquete estado) throws ConexionException, SQLException {
        List<Paquete> lista = new ArrayList<>();
        String sql = "SELECT p.id_paquete, p.codigo, p.descripcion, p.peso, ep.nombre AS estado " +
                     "FROM paquete p " +
                     "JOIN estado_paquete ep ON p.id_estado = ep.id_estado " +
                     "WHERE ep.nombre = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, estado.toDbString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPaquete(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Inserta un nuevo paquete en la base de datos.
     *
     * @param paquete paquete a insertar.
     * @return {@code true} si se insertó al menos una fila; {@code false} en caso contrario.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la inserción SQL.
     */
    public boolean insertar(Paquete paquete) throws ConexionException, SQLException {
        String sql = "INSERT INTO paquete (codigo, descripcion, peso, id_estado) " +
                     "VALUES (?, ?, ?, " +
                     "(SELECT id_estado FROM estado_paquete WHERE nombre = ?))";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, paquete.getCodigo());
            ps.setString(2, paquete.getDescripcion());
            ps.setDouble(3, paquete.getPeso());
            ps.setString(4, paquete.getEstado().toDbString());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Actualiza los datos de un paquete existente en la base de datos.
     *
     * @param paquete paquete con los datos actualizados; se identifica por su {@code id}.
     * @return {@code true} si se actualizó al menos una fila; {@code false} en caso contrario.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la actualización SQL.
     */
    public boolean actualizar(Paquete paquete) throws ConexionException, SQLException {
        String sql = "UPDATE paquete SET codigo = ?, descripcion = ?, peso = ?, " +
                     "id_estado = (SELECT id_estado FROM estado_paquete WHERE nombre = ?) " +
                     "WHERE id_paquete = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, paquete.getCodigo());
            ps.setString(2, paquete.getDescripcion());
            ps.setDouble(3, paquete.getPeso());
            ps.setString(4, paquete.getEstado().toDbString());
            ps.setInt(5, paquete.getId());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Elimina un paquete de la base de datos por su identificador único.
     *
     * @param id identificador del paquete a eliminar.
     * @return {@code true} si se eliminó al menos una fila; {@code false} si no existía el registro.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la eliminación SQL.
     */
    public boolean eliminar(int id) throws ConexionException, SQLException {
        String sql = "DELETE FROM paquete WHERE id_paquete = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }


    /**
     * Convierte la fila actual del {@link ResultSet} en una instancia de {@link Paquete}.
     *
     * @param rs resultado de la consulta SQL posicionado en la fila a mapear.
     * @return instancia de {@link Paquete} con los datos de la fila.
     * @throws SQLException si ocurre un error al leer el ResultSet.
     */
    private Paquete mapearPaquete(ResultSet rs) throws SQLException {
        return new Paquete(
                rs.getInt("id_paquete"),
                rs.getString("codigo"),
                rs.getString("descripcion"),
                rs.getDouble("peso"),
                EstadoPaquete.fromString(rs.getString("estado"))
        );
    }
}
