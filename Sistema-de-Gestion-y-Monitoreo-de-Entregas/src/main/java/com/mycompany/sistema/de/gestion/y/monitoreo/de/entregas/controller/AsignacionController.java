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

public class AsignacionController {

    private final AsignacionDAO asignacionDAO = new AsignacionDAO();
    private final VehiculoDAO vehiculoDAO = new VehiculoDAO();
    private final PaqueteDAO paqueteDAO = new PaqueteDAO();

    public List<Asignacion> obtenerTodas() throws ConexionException, SQLException {
        return asignacionDAO.obtenerTodas();
    }

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

    public Vehiculo buscarVehiculoDisponible(List<Vehiculo> vehiculos, Paquete paquete) {
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.estaDisponible() && vehiculo.calcularCapacidad() >= paquete.getPeso()) {
                return vehiculo;
            }
        }
        return null;
    }
}
