package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.controller;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.dao.AsignacionDAO;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.dao.PaqueteDAO;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.dao.VehiculoDAO;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ConexionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Asignacion;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.EstadoPaquete;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.EstadoVehiculo;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Paquete;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Vehiculo;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador de lógica de negocio para la gestión de asignaciones de entrega.
 *
 * <p>Coordina la interacción entre {@link AsignacionDAO}, {@link VehiculoDAO} y
 * {@link PaqueteDAO} para asignar paquetes a vehículos disponibles, actualizando
 * el estado de ambas entidades de forma atómica y registrando la asignación.</p>
 *
 * @author QuickDelivery S.A.
 */
public class AsignacionController {

    /** DAO para operaciones sobre asignaciones en la base de datos. */
    private final AsignacionDAO asignacionDAO = new AsignacionDAO();

    /** DAO para operaciones sobre vehículos en la base de datos. */
    private final VehiculoDAO vehiculoDAO = new VehiculoDAO();

    /** DAO para operaciones sobre paquetes en la base de datos. */
    private final PaqueteDAO paqueteDAO = new PaqueteDAO();


    /**
     * Retorna la lista de todas las asignaciones registradas en el sistema.
     *
     * @return lista de {@link Asignacion}; vacía si no hay registros.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public List<Asignacion> obtenerTodas() throws ConexionException, SQLException {
        return asignacionDAO.obtenerTodas();
    }

    /**
     * Asigna un paquete a un vehículo disponible de la lista proporcionada.
     *
     * <p>Busca el primer vehículo con estado disponible y capacidad suficiente
     * para el peso del paquete. Si se encuentra, actualiza el estado del paquete
     * a {@link EstadoPaquete#EN_TRANSITO}, el estado del vehículo a
     * {@link EstadoVehiculo#EN_RUTA} y persiste la asignación con la fecha actual.</p>
     *
     * @param vehiculos lista de vehículos candidatos para la asignación.
     * @param paquete   paquete a asignar; debe ser no nulo y no estar previamente asignado.
     * @return {@code true} si se encontró un vehículo apto y la asignación se realizó;
     *         {@code false} si el paquete es nulo, ya está asignado o no hay vehículo disponible.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error al persistir los cambios.
     */
    public boolean asignarPaquete(List<Vehiculo> vehiculos, Paquete paquete) throws ConexionException, SQLException {
        if (paquete == null || paquete.isAsignado()) {
            return false;
        }

        Vehiculo vehiculo = buscarVehiculoDisponible(vehiculos, paquete);

        if (vehiculo != null) {
            paquete.setEstado(EstadoPaquete.EN_TRANSITO);
            vehiculo.setEstado(EstadoVehiculo.EN_RUTA);

            paqueteDAO.actualizar(paquete);
            vehiculoDAO.actualizar(vehiculo);
            asignacionDAO.insertar(new Asignacion(0, vehiculo, paquete, LocalDateTime.now()));
            return true;
        }

        return false;
    }

    /**
     * Busca el primer vehículo disponible con capacidad suficiente para el paquete dado.
     *
     * <p>Itera la lista en orden y retorna el primer {@link Vehiculo} cuyo estado sea
     * disponible y cuya capacidad calculada sea mayor o igual al peso del paquete.</p>
     *
     * @param vehiculos lista de vehículos a evaluar.
     * @param paquete   paquete cuyo peso se usará como criterio mínimo de capacidad.
     * @return el primer {@link Vehiculo} apto, o {@code null} si ninguno cumple los criterios.
     */
    public Vehiculo buscarVehiculoDisponible(List<Vehiculo> vehiculos, Paquete paquete) {
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.estaDisponible() && vehiculo.calcularCapacidad() >= paquete.getPeso()) {
                return vehiculo;
            }
        }
        return null;
    }
}
