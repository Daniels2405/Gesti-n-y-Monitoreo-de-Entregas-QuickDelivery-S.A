package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.dao;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ConexionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Incidencia;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.util.ConexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) para la entidad {@link Incidencia}.
 *
 * <p>Proporciona operaciones de lectura, inserción y eliminación sobre
 * la tabla {@code INCIDENCIA} de la base de datos, haciendo uso de
 * {@link ConexionDB} para obtener conexiones.</p>
 *
 * @author QuickDelivery S.A.
 */
public class IncidenciaDAO {

    /**
     * Retorna la lista de todas las incidencias registradas en el sistema.
     *
     * @return lista de {@link Incidencia}; vacía si no hay registros.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
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

    /**
     * Retorna la lista de incidencias asociadas a un paquete específico.
     *
     * @param idPaquete identificador del paquete cuyas incidencias se desean obtener.
     * @return lista de {@link Incidencia} del paquete indicado; vacía si no tiene incidencias.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
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

    /**
     * Inserta una nueva incidencia en la base de datos.
     *
     * @param incidencia incidencia a insertar con descripción, fecha e id de paquete definidos.
     * @return {@code true} si se insertó al menos una fila; {@code false} en caso contrario.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la inserción SQL.
     */
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

    /**
     * Elimina una incidencia de la base de datos por su identificador único.
     *
     * @param id identificador de la incidencia a eliminar.
     * @return {@code true} si se eliminó al menos una fila; {@code false} si no existía el registro.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la eliminación SQL.
     */
    public boolean eliminar(int id) throws ConexionException, SQLException {
        String sql = "DELETE FROM INCIDENCIA WHERE id_incidencia = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Convierte la fila actual del {@link ResultSet} en una instancia de {@link Incidencia}.
     *
     * @param rs resultado de la consulta SQL posicionado en la fila a mapear.
     * @return instancia de {@link Incidencia} con los datos de la fila.
     * @throws SQLException si ocurre un error al leer el ResultSet.
     */
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
