package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.controller;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.dao.PaqueteDAO;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ConexionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.EstadoPaquete;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Paquete;

import java.sql.SQLException;
import java.util.List;

public class PaqueteController {

    private final PaqueteDAO paqueteDAO = new PaqueteDAO();

    public List<Paquete> obtenerTodos() throws ConexionException, SQLException {
        return paqueteDAO.obtenerTodos();
    }

    public List<Paquete> obtenerPorEstado(EstadoPaquete estado) throws ConexionException, SQLException {
        return paqueteDAO.obtenerPorEstado(estado);
    }

    public Paquete obtenerPorId(int id) throws ConexionException, SQLException {
        return paqueteDAO.obtenerPorId(id);
    }

    public boolean registrar(Paquete paquete) throws ConexionException, SQLException {
        return paqueteDAO.insertar(paquete);
    }

    public boolean actualizar(Paquete paquete) throws ConexionException, SQLException {
        return paqueteDAO.actualizar(paquete);
    }

    public boolean eliminar(int id) throws ConexionException, SQLException {
        return paqueteDAO.eliminar(id);
    }
}
