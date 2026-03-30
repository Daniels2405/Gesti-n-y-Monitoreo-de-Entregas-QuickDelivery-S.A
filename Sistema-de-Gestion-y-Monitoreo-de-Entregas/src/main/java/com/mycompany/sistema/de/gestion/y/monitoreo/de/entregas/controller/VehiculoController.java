package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.controller;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.dao.VehiculoDAO;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ConexionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Vehiculo;

import java.sql.SQLException;
import java.util.List;

public class VehiculoController {

    private final VehiculoDAO vehiculoDAO = new VehiculoDAO();

    public List<Vehiculo> obtenerTodos() throws ConexionException, SQLException {
        return vehiculoDAO.obtenerTodos();
    }

    public List<Vehiculo> obtenerDisponibles() throws ConexionException, SQLException {
        return vehiculoDAO.obtenerDisponibles();
    }

    public Vehiculo obtenerPorId(int id) throws ConexionException, SQLException {
        return vehiculoDAO.obtenerPorId(id);
    }

    public boolean registrar(Vehiculo vehiculo) throws ConexionException, SQLException {
        return vehiculoDAO.insertar(vehiculo);
    }

    public boolean actualizar(Vehiculo vehiculo) throws ConexionException, SQLException {
        return vehiculoDAO.actualizar(vehiculo);
    }

    public boolean eliminar(int id) throws ConexionException, SQLException {
        return vehiculoDAO.eliminar(id);
    }
}
