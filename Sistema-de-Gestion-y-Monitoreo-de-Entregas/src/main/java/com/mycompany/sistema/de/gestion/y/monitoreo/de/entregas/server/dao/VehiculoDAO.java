package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.dao;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ConexionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Camion;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.EstadoVehiculo;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Furgon;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Moto;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Vehiculo;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de acceso a datos (DAO) para la entidad {@link Vehiculo}.
 *
 * <p>Proporciona operaciones CRUD sobre la tabla {@code vehiculo} de la base
 * de datos, haciendo uso de {@link ConexionDB} para obtener conexiones y
 * mapeando los resultados a las subclases concretas
 * ({@link Camion}, {@link Moto}, {@link Furgon}) según el tipo registrado.</p>
 *
 * @author QuickDelivery S.A.
 */
public class VehiculoDAO {

    /**
     * Retorna la lista de todos los vehículos registrados en el sistema.
     *
     * @return lista de {@link Vehiculo}; vacía si no hay registros.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public List<Vehiculo> obtenerTodos() throws ConexionException, SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT v.id_vehiculo, v.placa, v.capacidad, " +
                     "tv.nombre AS tipo, ev.nombre AS estado " +
                     "FROM vehiculo v " +
                     "JOIN tipo_vehiculo tv ON v.id_tipo   = tv.id_tipo " +
                     "JOIN estado_vehiculo ev ON v.id_estado = ev.id_estado";

        try (Connection conexion = ConexionDB.getConexion();
             Statement stm = conexion.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearVehiculo(rs));
            }
        }
        return lista;
    }

    /**
     * Retorna la lista de vehículos cuyo estado es {@link EstadoVehiculo#DISPONIBLE}.
     *
     * @return lista de vehículos disponibles; vacía si ninguno está disponible.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public List<Vehiculo> obtenerDisponibles() throws ConexionException, SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT v.id_vehiculo, v.placa, v.capacidad, " +
                     "tv.nombre AS tipo, ev.nombre AS estado " +
                     "FROM vehiculo v " +
                     "JOIN tipo_vehiculo tv ON v.id_tipo   = tv.id_tipo " +
                     "JOIN estado_vehiculo ev ON v.id_estado = ev.id_estado " +
                     "WHERE ev.nombre = 'disponible'";

        try (Connection conexion = ConexionDB.getConexion();
             Statement stm = conexion.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearVehiculo(rs));
            }
        }
        return lista;
    }

    /**
     * Retorna la lista de vehículos filtrados por un estado específico.
     *
     * @param estado estado por el que se filtra.
     * @return lista de vehículos con ese estado; vacía si no hay coincidencias.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la consulta SQL.
     */
    public List<Vehiculo> obtenerPorEstado(EstadoVehiculo estado) throws ConexionException, SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT v.id_vehiculo, v.placa, v.capacidad, " +
                     "tv.nombre AS tipo, ev.nombre AS estado " +
                     "FROM vehiculo v " +
                     "JOIN tipo_vehiculo tv ON v.id_tipo   = tv.id_tipo " +
                     "JOIN estado_vehiculo ev ON v.id_estado = ev.id_estado " +
                     "WHERE ev.nombre = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, estado.toDbString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearVehiculo(rs));
                }
            }
        }
        return lista;
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
        String sql = "SELECT v.id_vehiculo, v.placa, v.capacidad, " +
                     "tv.nombre AS tipo, ev.nombre AS estado " +
                     "FROM vehiculo v " +
                     "JOIN tipo_vehiculo tv ON v.id_tipo   = tv.id_tipo " +
                     "JOIN estado_vehiculo ev ON v.id_estado = ev.id_estado " +
                     "WHERE v.id_vehiculo = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearVehiculo(rs);
                }
            }
        }
        return null;
    }

    /**
     * Inserta un nuevo vehículo en la base de datos.
     *
     * @param vehiculo vehículo a insertar; su tipo concreto determina el {@code tipo_vehiculo}.
     * @return {@code true} si se insertó al menos una fila; {@code false} en caso contrario.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la inserción SQL.
     */
    public boolean insertar(Vehiculo vehiculo) throws ConexionException, SQLException {
        String sql = "INSERT INTO vehiculo (placa, capacidad, id_tipo, id_estado) " +
                     "VALUES (?, ?, " +
                     "(SELECT id_tipo  FROM tipo_vehiculo   WHERE nombre = ?), " +
                     "(SELECT id_estado FROM estado_vehiculo WHERE nombre = ?))";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, vehiculo.getPlaca());
            ps.setDouble(2, vehiculo.getCapacidadMaxima());
            ps.setString(3, obtenerNombreTipo(vehiculo));
            ps.setString(4, vehiculo.getEstado().toDbString());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Actualiza los datos de un vehículo existente en la base de datos.
     *
     * @param vehiculo vehículo con los datos actualizados; se identifica por su {@code id}.
     * @return {@code true} si se actualizó al menos una fila; {@code false} en caso contrario.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la actualización SQL.
     */
    public boolean actualizar(Vehiculo vehiculo) throws ConexionException, SQLException {
        String sql = "UPDATE vehiculo SET placa = ?, capacidad = ?, " +
                     "id_estado = (SELECT id_estado FROM estado_vehiculo WHERE nombre = ?) " +
                     "WHERE id_vehiculo = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, vehiculo.getPlaca());
            ps.setDouble(2, vehiculo.getCapacidadMaxima());
            ps.setString(3, vehiculo.getEstado().toDbString());
            ps.setInt(4, vehiculo.getId());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Elimina un vehículo de la base de datos por su identificador único.
     *
     * @param id identificador del vehículo a eliminar.
     * @return {@code true} si se eliminó al menos una fila; {@code false} si no existía el registro.
     * @throws ConexionException si no se puede obtener una conexión a la base de datos.
     * @throws SQLException      si ocurre un error durante la eliminación SQL.
     */
    public boolean eliminar(int id) throws ConexionException, SQLException {
        String sql = "DELETE FROM vehiculo WHERE id_vehiculo = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Convierte la fila actual del {@link ResultSet} en una instancia concreta de {@link Vehiculo}
     * ({@link Camion}, {@link Moto} o {@link Furgon}) según el campo {@code tipo}.
     *
     * @param rs resultado de la consulta SQL posicionado en la fila a mapear.
     * @return instancia concreta de {@link Vehiculo}.
     * @throws SQLException si el tipo de vehículo es desconocido o hay un error al leer el ResultSet.
     */
    private Vehiculo mapearVehiculo(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_vehiculo");
        String placa = rs.getString("placa");
        double capacidad = rs.getDouble("capacidad");
        EstadoVehiculo estado = EstadoVehiculo.fromString(rs.getString("estado"));
        String tipo = rs.getString("tipo");

        switch (tipo) {
            case "Camión": return new Camion(id, placa, capacidad, estado);
            case "Moto":   return new Moto(id, placa, capacidad, estado);
            case "Furgón": return new Furgon(id, placa, capacidad, estado);
            default: throw new SQLException("Tipo de vehiculo desconocido: " + tipo);
        }
    }

    /**
     * Retorna el nombre del tipo de vehículo tal como está registrado en la tabla
     * {@code tipo_vehiculo}, deducido a partir de la clase concreta del objeto.
     *
     * @param vehiculo vehículo cuyo tipo se desea determinar.
     * @return {@code "Camión"}, {@code "Moto"}, {@code "Furgón"} o {@code "DESCONOCIDO"}.
     */
    private String obtenerNombreTipo(Vehiculo vehiculo) {
        if (vehiculo instanceof Camion)  return "Camión";
        if (vehiculo instanceof Moto)    return "Moto";
        if (vehiculo instanceof Furgon)  return "Furgón";
        return "DESCONOCIDO";
    }
}
