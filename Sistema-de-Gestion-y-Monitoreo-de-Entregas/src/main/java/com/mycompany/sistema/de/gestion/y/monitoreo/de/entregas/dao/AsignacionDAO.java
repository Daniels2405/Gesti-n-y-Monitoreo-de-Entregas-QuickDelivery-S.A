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

/**
 * Objeto de acceso a datos (DAO) para la entidad {@link Asignacion}.
 *
 * <p>Proporciona operaciones CRUD sobre la tabla {@code ASIGNACION} de la base
 * de datos. Al mapear un registro, delega en {@link VehiculoDAO} y
 * {@link PaqueteDAO} para cargar los objetos relacionados.</p>
 *
 * @author QuickDelivery S.A.
 */
public class AsignacionDAO {

    private final VehiculoDAO vehiculoDAO = new VehiculoDAO();
    private final PaqueteDAO paqueteDAO = new PaqueteDAO();

    /**
     * Retorna la lista de todas las asignaciones registradas en el sistema.
     *
     * @return lista de {@link Asignacion}; vacía si no hay registros.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
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

    /**
     * Busca y retorna una asignación por su identificador único.
     *
     * @param id identificador de la asignación a buscar.
     * @return la {@link Asignacion} encontrada, o {@code null} si no existe.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
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

    /**
     * Inserta una nueva asignación en la base de datos.
     *
     * @param asignacion asignación a insertar con vehículo, paquete y fecha definidos.
     * @return {@code true} si se insertó al menos una fila; {@code false} en caso contrario.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la inserción SQL.
     */
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

    /**
     * Elimina una asignación de la base de datos por su identificador único.
     *
     * @param id identificador de la asignación a eliminar.
     * @return {@code true} si se eliminó al menos una fila; {@code false} si no existía el registro.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la eliminación SQL.
     */
    public boolean eliminar(int id) throws ConexionException, SQLException {
        String sql = "DELETE FROM ASIGNACION WHERE id_asignacion = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Convierte la fila actual del {@link ResultSet} en una instancia de {@link Asignacion},
     * cargando el {@link Vehiculo} y el {@link Paquete} asociados desde sus respectivos DAOs.
     *
     * @param rs resultado de la consulta SQL posicionado en la fila a mapear.
     * @return instancia de {@link Asignacion} con todos los objetos relacionados cargados.
     * @throws ConexionException si no se puede obtener una conexión al cargar los objetos relacionados.
     * @throws SQLException      si ocurre un error al leer el ResultSet.
     */
    private Asignacion mapearAsignacion(ResultSet rs) throws ConexionException, SQLException {
        int id = rs.getInt("id_asignacion");
        LocalDateTime fecha = rs.getTimestamp("fecha").toLocalDateTime();
        Vehiculo vehiculo = vehiculoDAO.obtenerPorId(rs.getInt("id_vehiculo"));
        Paquete paquete = paqueteDAO.obtenerPorId(rs.getInt("id_paquete"));
        return new Asignacion(id, vehiculo, paquete, fecha);
    }
}
