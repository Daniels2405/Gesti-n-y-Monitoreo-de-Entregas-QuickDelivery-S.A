package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.controller;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.dao.AuditoriaDAO;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.dao.PaqueteDAO;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ConexionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.EstadoPaquete;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Paquete;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.TipoAccion;

import java.sql.SQLException;
import java.util.List;

/**
 * Controlador de lógica de negocio para la gestión de paquetes.
 *
 * <p>Actúa como intermediario entre la capa de presentación y {@link PaqueteDAO},
 * exponiendo operaciones CRUD y consultas filtradas sobre la entidad {@link Paquete}.</p>
 *
 * @author QuickDelivery S.A.
 */
public class PaqueteController {

    /** DAO para operaciones sobre paquetes en la base de datos. */
    private final PaqueteDAO paqueteDAO = new PaqueteDAO();

    /** DAO para registro de auditoría en la tabla {@code auditoria_log}. */
    private final AuditoriaDAO auditoriaDAO = new AuditoriaDAO();


    /**
     * Retorna la lista de todos los paquetes registrados en el sistema.
     *
     * @return lista de {@link Paquete}; vacía si no hay registros.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public List<Paquete> obtenerTodos() throws ConexionException, SQLException {
        return paqueteDAO.obtenerTodos();
    }

    /**
     * Retorna los paquetes que se encuentran en un estado específico.
     *
     * @param estado estado por el que se filtra; no debe ser {@code null}.
     * @return lista de {@link Paquete} con el estado indicado; vacía si no hay coincidencias.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public List<Paquete> obtenerPorEstado(EstadoPaquete estado) throws ConexionException, SQLException {
        return paqueteDAO.obtenerPorEstado(estado);
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
        return paqueteDAO.obtenerPorId(id);
    }

    /**
     * Retorna los paquetes EN_TRANSITO asignados al vehículo de un conductor.
     *
     * @param idConductor identificador del conductor.
     * @return lista de {@link Paquete} en tránsito del conductor; vacía si no hay ninguno.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public List<Paquete> obtenerPorConductor(int idConductor) throws ConexionException, SQLException {
        return paqueteDAO.obtenerPorConductor(idConductor);
    }

    /**
     * Registra un nuevo paquete en el sistema y guarda la acción en auditoría.
     *
     * @param paquete paquete a registrar con todos sus campos obligatorios definidos.
     * @param idActor identificador del usuario que realiza la acción.
     * @return {@code true} si se insertó al menos una fila; {@code false} en caso contrario.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la inserción SQL.
     */
    public boolean registrar(Paquete paquete, int idActor) throws ConexionException, SQLException {
        boolean ok = paqueteDAO.insertar(paquete);
        if (ok) {
            auditoriaDAO.registrar(idActor, TipoAccion.CREATE, "paquete",
                    paquete.getId(), "Código: " + paquete.getCodigo());
        }
        return ok;
    }

    // ----------------------------------------------------------------- UPDATE

    /**
     * Actualiza los datos de un paquete existente en el sistema y guarda la acción en auditoría.
     *
     * @param paquete paquete con los datos modificados; debe tener un {@code id} válido.
     * @param idActor identificador del usuario que realiza la acción.
     * @return {@code true} si se actualizó al menos una fila; {@code false} si no existía el registro.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la actualización SQL.
     */
    public boolean actualizar(Paquete paquete, int idActor) throws ConexionException, SQLException {
        boolean ok = paqueteDAO.actualizar(paquete);
        if (ok) {
            auditoriaDAO.registrar(idActor, TipoAccion.UPDATE, "paquete",
                    paquete.getId(), "Estado: " + paquete.getEstado() + " | Código: " + paquete.getCodigo());
        }
        return ok;
    }

    /**
     * Elimina un paquete del sistema por su identificador único y guarda la acción en auditoría.
     *
     * @param id      identificador del paquete a eliminar.
     * @param idActor identificador del usuario que realiza la acción.
     * @return {@code true} si se eliminó al menos una fila; {@code false} si no existía el registro.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la eliminación SQL.
     */
    public boolean eliminar(int id, int idActor) throws ConexionException, SQLException {
        boolean ok = paqueteDAO.eliminar(id);
        if (ok) {
            auditoriaDAO.registrar(idActor, TipoAccion.DELETE, "paquete", id, "Paquete eliminado");
        }
        return ok;
    }
}
