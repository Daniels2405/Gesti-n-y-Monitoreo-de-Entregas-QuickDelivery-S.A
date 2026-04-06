package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.controller;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.dao.VehiculoDAO;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ConexionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Vehiculo;

import java.sql.SQLException;
import java.util.List;

/**
 * Controlador de lógica de negocio para la gestión de vehículos de reparto.
 *
 * <p>Actúa como intermediario entre la capa de presentación y {@link VehiculoDAO},
 * exponiendo operaciones CRUD y consultas filtradas sobre la entidad {@link Vehiculo}.</p>
 *
 * @author QuickDelivery S.A.
 */
public class VehiculoController {

    /** DAO para operaciones sobre vehículos en la base de datos. */
    private final VehiculoDAO vehiculoDAO = new VehiculoDAO();


    /**
     * Retorna la lista de todos los vehículos registrados en el sistema.
     *
     * @return lista de {@link Vehiculo}; vacía si no hay registros.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public List<Vehiculo> obtenerTodos() throws ConexionException, SQLException {
        return vehiculoDAO.obtenerTodos();
    }

    /**
     * Retorna únicamente los vehículos que se encuentran disponibles para una nueva asignación.
     *
     * @return lista de {@link Vehiculo} con estado disponible; vacía si ninguno está libre.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public List<Vehiculo> obtenerDisponibles() throws ConexionException, SQLException {
        return vehiculoDAO.obtenerDisponibles();
    }

    /**
     * Busca y retorna un vehículo por su identificador único.
     *
     * @param id identificador del vehículo a buscar.
     * @return el {@link Vehiculo} encontrado, o {@code null} si no existe.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public Vehiculo obtenerPorId(int id) throws ConexionException, SQLException {
        return vehiculoDAO.obtenerPorId(id);
    }

    /**
     * Retorna el vehículo asignado a un conductor específico.
     *
     * @param idConductor identificador del conductor.
     * @return el {@link Vehiculo} asignado, o {@code null} si no tiene vehículo.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public Vehiculo obtenerPorConductor(int idConductor) throws ConexionException, SQLException {
        return vehiculoDAO.obtenerPorConductor(idConductor);
    }

    /**
     * Registra un nuevo vehículo en el sistema.
     *
     * @param vehiculo vehículo a registrar con todos sus campos obligatorios definidos.
     * @return {@code true} si se insertó al menos una fila; {@code false} en caso contrario.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la inserción SQL.
     */
    public boolean registrar(Vehiculo vehiculo) throws ConexionException, SQLException {
        return vehiculoDAO.insertar(vehiculo);
    }

    /**
     * Actualiza los datos de un vehículo existente en el sistema.
     *
     * @param vehiculo vehículo con los datos modificados; debe tener un {@code id} válido.
     * @return {@code true} si se actualizó al menos una fila; {@code false} si no existía el registro.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la actualización SQL.
     */
    public boolean actualizar(Vehiculo vehiculo) throws ConexionException, SQLException {
        return vehiculoDAO.actualizar(vehiculo);
    }

    /**
     * Elimina un vehículo del sistema por su identificador único.
     *
     * @param id identificador del vehículo a eliminar.
     * @return {@code true} si se eliminó al menos una fila; {@code false} si no existía el registro.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la eliminación SQL.
     */
    public boolean eliminar(int id) throws ConexionException, SQLException {
        return vehiculoDAO.eliminar(id);
    }
}
