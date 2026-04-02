package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.dao;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ConexionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Rol;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Usuario;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) para la entidad {@link Usuario}.
 *
 * <p>Proporciona operaciones CRUD sobre la tabla {@code usuario} de la base
 * de datos. El campo {@code id_rol} se resuelve mediante JOIN con la tabla
 * {@code rol} para retornar instancias completamente pobladas.</p>
 *
 * @author QuickDelivery S.A.
 */
public class UsuarioDAO {

    // ------------------------------------------------------------------ READ

    /**
     * Retorna la lista de todos los usuarios registrados en el sistema.
     *
     * @return lista de {@link Usuario}; vacía si no hay registros.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public List<Usuario> obtenerTodos() throws ConexionException, SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.nombre, u.username, u.pass_hash, " +
                     "u.estado, r.nombre AS rol " +
                     "FROM usuario u " +
                     "JOIN rol r ON u.id_rol = r.id_rol " +
                     "ORDER BY u.nombre";

        try (Connection conexion = ConexionDB.getConexion();
             Statement stm = conexion.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        }
        return lista;
    }

    /**
     * Busca y retorna un usuario por su identificador único.
     *
     * @param id identificador del usuario a buscar.
     * @return el {@link Usuario} encontrado, o {@code null} si no existe.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public Usuario obtenerPorId(int id) throws ConexionException, SQLException {
        String sql = "SELECT u.id_usuario, u.nombre, u.username, u.pass_hash, " +
                     "u.estado, r.nombre AS rol " +
                     "FROM usuario u " +
                     "JOIN rol r ON u.id_rol = r.id_rol " +
                     "WHERE u.id_usuario = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearUsuario(rs);
            }
        }
        return null;
    }

    /**
     * Busca y retorna un usuario por su nombre de usuario (username).
     *
     * @param username nombre de usuario único a buscar.
     * @return el {@link Usuario} encontrado, o {@code null} si no existe.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public Usuario obtenerPorUsername(String username) throws ConexionException, SQLException {
        String sql = "SELECT u.id_usuario, u.nombre, u.username, u.pass_hash, " +
                     "u.estado, r.nombre AS rol " +
                     "FROM usuario u " +
                     "JOIN rol r ON u.id_rol = r.id_rol " +
                     "WHERE u.username = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearUsuario(rs);
            }
        }
        return null;
    }

    // ----------------------------------------------------------------- CREATE

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * <p>El {@code id_rol} se resuelve internamente con una subconsulta
     * sobre la tabla {@code rol}.</p>
     *
     * @param usuario usuario a insertar; su campo {@code passHash} debe
     *                contener ya el hash SHA-256 de la contraseña.
     * @return {@code true} si se insertó al menos una fila; {@code false} en caso contrario.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la inserción SQL.
     */
    public boolean insertar(Usuario usuario) throws ConexionException, SQLException {
        String sql = "INSERT INTO usuario (nombre, username, pass_hash, estado, id_rol) " +
                     "VALUES (?, ?, ?, ?, (SELECT id_rol FROM rol WHERE nombre = ?))";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getUsername());
            ps.setString(3, usuario.getPassHash());
            ps.setString(4, usuario.getEstado());
            ps.setString(5, usuario.getRol().name().toLowerCase());
            return ps.executeUpdate() > 0;
        }
    }

    // ----------------------------------------------------------------- UPDATE

    /**
     * Actualiza los datos de un usuario existente en la base de datos.
     *
     * @param usuario usuario con los datos modificados; debe tener un {@code id} válido.
     * @return {@code true} si se actualizó al menos una fila; {@code false} si no existía el registro.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la actualización SQL.
     */
    public boolean actualizar(Usuario usuario) throws ConexionException, SQLException {
        String sql = "UPDATE usuario SET nombre = ?, username = ?, pass_hash = ?, " +
                     "estado = ?, id_rol = (SELECT id_rol FROM rol WHERE nombre = ?) " +
                     "WHERE id_usuario = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getUsername());
            ps.setString(3, usuario.getPassHash());
            ps.setString(4, usuario.getEstado());
            ps.setString(5, usuario.getRol().name().toLowerCase());
            ps.setInt(6, usuario.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // ----------------------------------------------------------------- DELETE

    /**
     * Elimina un usuario de la base de datos por su identificador único.
     *
     * @param id identificador del usuario a eliminar.
     * @return {@code true} si se eliminó al menos una fila; {@code false} si no existía el registro.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la eliminación SQL.
     */
    public boolean eliminar(int id) throws ConexionException, SQLException {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // --------------------------------------------------------------- privados

    /**
     * Convierte la fila actual del {@link ResultSet} en una instancia de {@link Usuario}.
     *
     * @param rs resultado de la consulta SQL posicionado en la fila a mapear.
     * @return instancia de {@link Usuario} con todos sus campos poblados.
     * @throws SQLException si ocurre un error al leer el ResultSet.
     */
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id_usuario"),
                rs.getString("nombre"),
                rs.getString("username"),
                rs.getString("pass_hash"),
                rs.getString("estado"),
                Rol.fromString(rs.getString("rol"))
        );
    }
}
