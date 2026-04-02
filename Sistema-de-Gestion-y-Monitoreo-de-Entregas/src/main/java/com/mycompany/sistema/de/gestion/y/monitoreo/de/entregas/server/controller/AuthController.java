package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.controller;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.dao.AuditoriaDAO;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.dao.UsuarioDAO;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ConexionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ValidacionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.TipoAccion;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Usuario;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.util.ConexionDB;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.util.Logger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

/**
 * Controlador de autenticación y gestión de sesiones del sistema QuickDelivery S.A.
 *
 * <p>Responsabilidades:</p>
 * <ul>
 *   <li>Validar credenciales contra la base de datos (HU-02).</li>
 *   <li>Registrar inicios y cierres de sesión en {@code sesion_login}.</li>
 *   <li>Escribir entradas de auditoría en {@code auditoria_log} y en archivo {@code .log}.</li>
 *   <li>Proveer hash SHA-256 para la verificación y creación de contraseñas.</li>
 * </ul>
 *
 * @author QuickDelivery S.A.
 */
public class AuthController {
    /** DAO para consultar y verificar usuarios en la base de datos. */
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /** DAO para registrar acciones en la tabla de auditoría. */
    private final AuditoriaDAO auditoriaDAO = new AuditoriaDAO();

    // ----------------------------------------------------------------- LOGIN

    /**
     * Autentica a un usuario con sus credenciales y abre una sesión en el sistema.
     *
     * <p>Flujo:
     * <ol>
     *   <li>Busca el usuario por {@code username} en la base de datos.</li>
     *   <li>Verifica que la cuenta esté activa.</li>
     *   <li>Compara el hash SHA-256 de la contraseña recibida con el almacenado.</li>
     *   <li>Registra el inicio de sesión en {@code sesion_login} y en auditoría.</li>
     * </ol>
     *
     * @param username nombre de usuario.
     * @param password contraseña en texto plano (se hashea internamente).
     * @return el {@link Usuario} autenticado con su rol cargado.
     * @throws ValidacionException si las credenciales son incorrectas o la cuenta no está activa.
     * @throws ConexionException   si no se puede obtener una conexión a la base de datos.
     * @throws SQLException        si ocurre un error durante las operaciones SQL.
     */
    public Usuario login(String username, String password)
            throws ValidacionException, ConexionException, SQLException {

        Usuario usuario = usuarioDAO.obtenerPorUsername(username);

        if (usuario == null) {
            throw new ValidacionException("Credenciales incorrectas.");
        }
        if (!usuario.isActivo()) {
            throw new ValidacionException("La cuenta está inactiva o suspendida.");
        }
        if (!hashSHA256(password).equals(usuario.getPassHash())) {
            throw new ValidacionException("Credenciales incorrectas.");
        }

        registrarInicioSesion(usuario.getId());
        auditoriaDAO.registrar(usuario.getId(), TipoAccion.LOGIN, null, null,
                "Inicio de sesion exitoso");
        Logger.registrar("LOGIN", usuario.getUsername(), "Sesion iniciada correctamente");

        return usuario;
    }

    // --------------------------------------------------------------- LOGOUT

    /**
     * Cierra la sesión activa de un usuario registrando la hora de fin.
     *
     * @param idSesion  identificador de la sesión en {@code sesion_login} a cerrar.
     * @param idUsuario identificador del usuario que cierra sesión.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante las operaciones SQL.
     */
    public void cerrarSesion(int idSesion, int idUsuario)
            throws ConexionException, SQLException {

        String sql = "UPDATE sesion_login SET fin = NOW() WHERE id_sesion = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSesion);
            ps.executeUpdate();
        }

        auditoriaDAO.registrar(idUsuario, TipoAccion.LOGOUT, null, null,
                "Sesion cerrada");
        Logger.registrar("LOGOUT", "id=" + idUsuario, "Sesion cerrada");
    }

    // ----------------------------------------------------------- HASH UTIL

    /**
     * Genera el hash SHA-256 en hexadecimal de la contraseña proporcionada.
     *
     * <p>Se utiliza tanto para verificar credenciales en el login como para
     * hashear nuevas contraseñas antes de almacenarlas.</p>
     *
     * @param password contraseña en texto plano.
     * @return representación hexadecimal del hash SHA-256.
     * @throws RuntimeException si el algoritmo SHA-256 no está disponible en la JVM.
     */
    public String hashSHA256(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible en esta JVM", e);
        }
    }

    // --------------------------------------------------------------- privados

    /**
     * Inserta un registro de inicio de sesión en la tabla {@code sesion_login}
     * y retorna el identificador de sesión generado.
     *
     * @param idUsuario identificador del usuario que inicia sesión.
     * @return id de sesión generado por la base de datos.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la inserción SQL.
     */
    private int registrarInicioSesion(int idUsuario) throws ConexionException, SQLException {
        String sql = "INSERT INTO sesion_login (id_usuario) VALUES (?)";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idUsuario);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }
    
}
