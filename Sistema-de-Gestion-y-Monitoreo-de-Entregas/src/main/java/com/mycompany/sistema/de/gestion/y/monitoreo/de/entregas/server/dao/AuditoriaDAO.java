package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.dao;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ConexionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.TipoAccion;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) para la tabla {@code auditoria_log}.
 *
 * <p>Registra en base de datos las acciones relevantes del sistema
 * (login, logout, CRUD de entidades) con el usuario responsable, el
 * tipo de acción y un detalle libre. Complementa el registro en archivo
 * que realiza {@link com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.util.Logger}.</p>
 *
 * @author QuickDelivery S.A.
 */
public class AuditoriaDAO {

    // ----------------------------------------------------------------- CREATE

    /**
     * Registra una acción en la tabla {@code auditoria_log}.
     *
     * <p>Compone la columna {@code accion} en el formato:
     * {@code [TIPO] tabla=X | id=Y | detalle}</p>
     *
     * @param idUsuario     identificador del usuario que realizó la acción;
     *                      {@code null} si la acción la generó el sistema.
     * @param tipo          tipo de acción realizada.
     * @param tablaAfectada nombre de la tabla o entidad afectada, ej: {@code "usuario"};
     *                      puede ser {@code null} para acciones de sesión.
     * @param idEntidad     id del registro afectado; {@code null} para
     *                      acciones de sesión (LOGIN / LOGOUT).
     * @param detalle       descripción libre adicional; puede ser {@code null}.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la inserción SQL.
     */
    public void registrar(Integer idUsuario, TipoAccion tipo,
                          String tablaAfectada, Integer idEntidad,
                          String detalle) throws ConexionException, SQLException {

        String accion = construirAccion(tipo, tablaAfectada, idEntidad, detalle);
        String sql = "INSERT INTO auditoria_log (id_usuario, accion) VALUES (?, ?)";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            if (idUsuario != null) {
                ps.setInt(1, idUsuario);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, accion);
            ps.executeUpdate();
        }
    }

    // ------------------------------------------------------------------ READ

    /**
     * Retorna todos los registros de auditoría ordenados del más reciente al más antiguo.
     *
     * @return lista de arreglos {@code Object[]} con los campos
     *         {@code [id_log, id_usuario, accion, fecha]}; vacía si no hay registros.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public List<Object[]> obtenerTodos() throws ConexionException, SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT al.id_log, u.username, al.accion, al.fecha " +
                     "FROM auditoria_log al " +
                     "LEFT JOIN usuario u ON al.id_usuario = u.id_usuario " +
                     "ORDER BY al.fecha DESC";

        try (Connection conexion = ConexionDB.getConexion();
             Statement stm = conexion.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_log"),
                    rs.getString("username"),
                    rs.getString("accion"),
                    rs.getTimestamp("fecha")
                });
            }
        }
        return lista;
    }

    // --------------------------------------------------------------- privados

    /**
     * Construye el texto de la columna {@code accion} a partir de los parámetros estructurados.
     *
     * @param tipo          tipo de acción.
     * @param tablaAfectada tabla o entidad afectada; puede ser {@code null}.
     * @param idEntidad     id del registro afectado; puede ser {@code null}.
     * @param detalle       descripción libre; puede ser {@code null}.
     * @return cadena formateada, ej: {@code "[CREATE] tabla=usuario | id=3 | Nuevo usuario: juan"}.
     */
    private String construirAccion(TipoAccion tipo, String tablaAfectada,
                                   Integer idEntidad, String detalle) {
        StringBuilder sb = new StringBuilder("[").append(tipo.name()).append("]");
        if (tablaAfectada != null) sb.append(" tabla=").append(tablaAfectada);
        if (idEntidad    != null) sb.append(" | id=").append(idEntidad);
        if (detalle      != null) sb.append(" | ").append(detalle);
        return sb.toString();
    }
}
