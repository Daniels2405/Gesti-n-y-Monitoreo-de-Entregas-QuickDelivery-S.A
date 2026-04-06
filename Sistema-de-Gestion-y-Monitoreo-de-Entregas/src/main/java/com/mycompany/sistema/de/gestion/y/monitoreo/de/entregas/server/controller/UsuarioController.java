package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.controller;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.dao.AuditoriaDAO;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.dao.UsuarioDAO;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ConexionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.TipoAccion;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Usuario;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.util.Logger;

import java.sql.SQLException;
import java.util.List;

/**
 * Controlador de lógica de negocio para la gestión de usuarios del sistema (HU-01).
 *
 * <p>Actúa como intermediario entre la capa de presentación y {@link UsuarioDAO}.
 * Cada operación de escritura (crear, actualizar, eliminar) registra
 * automáticamente una entrada en {@code auditoria_log} y en el archivo
 * {@code .log}, cumpliendo con los requisitos de trazabilidad de HU-12.</p>
 *
 * @author QuickDelivery S.A.
 */
public class UsuarioController {

    /** DAO para operaciones CRUD sobre la tabla {@code usuario}. */
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /** DAO para registro de acciones en la tabla {@code auditoria_log}. */
    private final AuditoriaDAO auditoriaDAO = new AuditoriaDAO();

    // ------------------------------------------------------------------ READ

    /**
     * Retorna la lista de todos los usuarios registrados en el sistema.
     *
     * @return lista de {@link Usuario}; vacía si no hay registros.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public List<Usuario> obtenerTodos() throws ConexionException, SQLException {
        return usuarioDAO.obtenerTodos();
    }

    /**
     * Retorna la lista de todos los usuarios con rol conductor activos.
     *
     * @return lista de {@link Usuario} conductores activos; vacía si no hay registros.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public List<Usuario> obtenerConductores() throws ConexionException, SQLException {
        return usuarioDAO.obtenerConductores();
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
        return usuarioDAO.obtenerPorId(id);
    }

    // ----------------------------------------------------------------- CREATE

    /**
     * Registra un nuevo usuario en el sistema y audita la acción.
     *
     * <p>El campo {@code passHash} del usuario debe contener ya el hash
     * SHA-256 generado por {@link AuthController#hashSHA256(String)}.</p>
     *
     * @param usuario     usuario a registrar con todos sus campos obligatorios definidos.
     * @param idAdminActor identificador del administrador que realiza el registro.
     * @return {@code true} si se insertó correctamente; {@code false} en caso contrario.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la inserción SQL.
     */
    public boolean registrar(Usuario usuario, int idAdminActor)
            throws ConexionException, SQLException {

        boolean resultado = usuarioDAO.insertar(usuario);

        if (resultado) {
            Usuario creado = usuarioDAO.obtenerPorUsername(usuario.getUsername());
            int idNuevo = creado != null ? creado.getId() : -1;
            auditoriaDAO.registrar(idAdminActor, TipoAccion.CREATE, "usuario", idNuevo,
                    "Nuevo usuario: " + usuario.getUsername() +
                    " | rol: " + usuario.getRol().name().toLowerCase());
            Logger.registrar("CREATE_USUARIO", "admin#" + idAdminActor,
                    "usuario=" + usuario.getUsername());
        }
        return resultado;
    }

    // ----------------------------------------------------------------- UPDATE

    /**
     * Actualiza los datos de un usuario existente y audita la acción.
     *
     * @param usuario     usuario con los datos modificados; debe tener un {@code id} válido.
     * @param idAdminActor identificador del administrador que realiza la modificación.
     * @return {@code true} si se actualizó correctamente; {@code false} si no existía el registro.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la actualización SQL.
     */
    public boolean actualizar(Usuario usuario, int idAdminActor)
            throws ConexionException, SQLException {

        boolean resultado = usuarioDAO.actualizar(usuario);

        if (resultado) {
            auditoriaDAO.registrar(idAdminActor, TipoAccion.UPDATE, "usuario", usuario.getId(),
                    "Actualizado: " + usuario.getUsername());
            Logger.registrar("UPDATE_USUARIO", "admin#" + idAdminActor,
                    "id=" + usuario.getId() + " | username=" + usuario.getUsername());
        }
        return resultado;
    }

    // ----------------------------------------------------------------- DELETE

    /**
     * Elimina un usuario del sistema por su identificador único y audita la acción.
     *
     * @param id          identificador del usuario a eliminar.
     * @param idAdminActor identificador del administrador que realiza la eliminación.
     * @return {@code true} si se eliminó correctamente; {@code false} si no existía el registro.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la eliminación SQL.
     */
    public boolean eliminar(int id, int idAdminActor)
            throws ConexionException, SQLException {

        boolean resultado = usuarioDAO.eliminar(id);

        if (resultado) {
            auditoriaDAO.registrar(idAdminActor, TipoAccion.DELETE, "usuario", id,
                    "Usuario eliminado");
            Logger.registrar("DELETE_USUARIO", "admin#" + idAdminActor,
                    "id=" + id);
        }
        return resultado;
    }
}
