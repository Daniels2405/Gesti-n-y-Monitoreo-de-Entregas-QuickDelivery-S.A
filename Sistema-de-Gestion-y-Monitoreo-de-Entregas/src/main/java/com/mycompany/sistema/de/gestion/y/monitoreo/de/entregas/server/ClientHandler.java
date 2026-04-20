package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ConexionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception.ValidacionException;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.*;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.controller.*;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.dao.AsignacionDAO;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.dao.AuditoriaDAO;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.dao.IncidenciaDAO;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.dao.PaqueteDAO;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.dao.VehiculoDAO;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.util.Logger;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Maneja la conexión de un cliente en un hilo independiente.
 * Cada usuario conectado al servidor (admin, despachador o conductor)
 * tiene su propio {@code ClientHandler}.
 *
 * <p>Protocolo de mensajes (texto plano separado por '|'):
 * <pre>
 *   Cliente → Servidor:
 *     LOGIN_APP|username|password          — autenticación (cualquier rol)
 *     GET_USUARIOS                         — listar usuarios (ADMINISTRADOR)
 *     CREATE_USUARIO|nombre|user|pass|rol|estado
 *     UPDATE_USUARIO|id|nombre|user|pass|rol|estado
 *     DELETE_USUARIO|id
 *     GET_VEHICULOS                        — listar todos los vehículos
 *     GET_VEHICULOS_DISPONIBLES            — solo disponibles
 *     CREATE_VEHICULO|placa|tipo|capacidad|estado
 *     UPDATE_VEHICULO|id|placa|tipo|capacidad|estado
 *     DELETE_VEHICULO|id
 *     GET_PAQUETES                         — listar todos los paquetes
 *     GET_PAQUETES_ESTADO|estado           — filtrar por estado
 *     GET_PAQUETES_ESPERA                  — alias EN_ESPERA
 *     CREATE_PAQUETE|codigo|descripcion|peso
 *     UPDATE_PAQUETE|id|codigo|descripcion|peso
 *     DELETE_PAQUETE|id
 *     ASIGNAR|idPaquete|idVehiculo         — asignar paquete a vehículo
 *     GET_MONITOR                          — vehículos EN_RUTA + paquetes EN_TRANSITO
 *     GET_AUDITORIA                        — registros del log de auditoría
 *     LOGIN_CONDUCTOR|idConductor|nombre   — registrar conductor activo
 *     GET_MIS_PAQUETES|idConductor         — paquetes asignados al conductor
 *     UBICACION|idConductor|descripcion    — reportar ubicación
 *     ESTADO|idPaquete|nuevoEstado         — actualizar estado de paquete
 *     GET_INCIDENCIAS                      — listar incidencias
 *     CREATE_INCIDENCIA|descripcion|idPaquete|idConductor
 *
 *   Servidor → Cliente:
 *     OK|mensaje                           — operación exitosa
 *     DATA|campo1|campo2|...               — registro único
 *     LIST|TIPO|fila1campo1|...|fila1campoN~fila2campo1|...|fila2campoN
 *     ERROR|descripción                    — error de negocio o SQL
 *     FORBIDDEN|motivo                     — sin permisos para la operación
 * </pre>
 *
 * @author daniel-2405
 */
public class ClientHandler implements Runnable {

    private final Socket connection;
    private PrintWriter out;
    private BufferedReader in;

    /** Usuario autenticado; null hasta que se procese LOGIN_APP con éxito. */
    private Usuario usuarioActual;

    // ── Controladores (solo servidor accede a la BD) ───────────────────────
    private final AuthController     authCtrl     = new AuthController();
    private final UsuarioController  usuarioCtrl  = new UsuarioController();
    private final PaqueteController  paqueteCtrl  = new PaqueteController();
    private final VehiculoController vehiculoCtrl = new VehiculoController();
    private final AsignacionController asignCtrl  = new AsignacionController();
    private final IncidenciaDAO      incidenciaDAO = new IncidenciaDAO();
    private final AsignacionDAO      asignacionDAO = new AsignacionDAO();
    private final AuditoriaDAO       auditoriaDAO  = new AuditoriaDAO();
    private final PaqueteDAO         paqueteDAO    = new PaqueteDAO();
    private final VehiculoDAO        vehiculoDAO   = new VehiculoDAO();

    public ClientHandler(Socket connection) {
        this.connection = connection;
    }

    /**
     * Envía un mensaje al cliente conectado.
     *
     * @param mensaje texto a enviar
     */
    public void enviar(String mensaje) {
        if (out != null) out.println(mensaje);
    }

    /**
     * Lógica principal del hilo. Igual que SaladeChat:
     * configura streams, envía bienvenida, y entra en loop leyendo mensajes.
     */
    @Override
    public void run() {
        try {
            in  = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out = new PrintWriter(connection.getOutputStream(), true); // auto-flush

            out.println("OK|Conectado al servidor QuickDelivery");

            String linea;
            while ((linea = in.readLine()) != null) {
                procesarMensaje(linea);
            }

        } catch (IOException e) {
            System.out.println("Cliente desconectado inesperadamente: " + e.getMessage());
        } finally {
            ServidorQuickDelivery.removerCliente(this);
            try { connection.close(); } catch (IOException ignored) {}
        }
    }

    // =========================================================================
    // ENRUTADOR PRINCIPAL
    // =========================================================================

    private void procesarMensaje(String mensaje) {
        String[] p = mensaje.split("\\|");
        String cmd = p[0];

        // LOGIN no requiere autenticación previa
        if ("LOGIN_APP".equals(cmd)) {
            manejarLoginApp(p);
            return;
        }

        // Todos los demás comandos requieren sesión activa
        if (usuarioActual == null) {
            enviar("ERROR|Debe autenticarse primero con LOGIN_APP");
            return;
        }

        switch (cmd) {
            // ── USUARIOS ──────────────────────────────────────────────────────
            case "GET_USUARIOS":         manejarGetUsuarios();         break;
            case "CREATE_USUARIO":       manejarCreateUsuario(p);      break;
            case "UPDATE_USUARIO":       manejarUpdateUsuario(p);      break;
            case "DELETE_USUARIO":       manejarDeleteUsuario(p);      break;
            // ── VEHÍCULOS ─────────────────────────────────────────────────────
            case "GET_VEHICULOS":            manejarGetVehiculos();             break;
            case "GET_VEHICULOS_DISPONIBLES": manejarGetVehiculosDisponibles(); break;
            case "GET_CONDUCTORES":          manejarGetConductores();           break;
            case "CREATE_VEHICULO":          manejarCreateVehiculo(p);          break;
            case "UPDATE_VEHICULO":          manejarUpdateVehiculo(p);          break;
            case "DELETE_VEHICULO":          manejarDeleteVehiculo(p);          break;
            // ── PAQUETES ──────────────────────────────────────────────────────
            case "GET_PAQUETES":         manejarGetPaquetes();          break;
            case "GET_PAQUETES_ESTADO":  manejarGetPaquetesEstado(p);  break;
            case "GET_PAQUETES_ESPERA":  manejarGetPaquetesEspera();   break;
            case "CREATE_PAQUETE":       manejarCreatePaquete(p);      break;
            case "UPDATE_PAQUETE":       manejarUpdatePaquete(p);      break;
            case "DELETE_PAQUETE":       manejarDeletePaquete(p);      break;
            // ── ASIGNACIÓN ────────────────────────────────────────────────────
            case "ASIGNAR":              manejarAsignar(p);            break;
            case "GET_ASIGNACIONES":     manejarGetAsignaciones();     break;
            // ── MONITOR ───────────────────────────────────────────────────────
            case "GET_MONITOR":          manejarGetMonitor();          break;
            case "GET_AUDITORIA":        manejarGetAuditoria();        break;
            // ── CONDUCTOR ─────────────────────────────────────────────────────
            case "LOGIN_CONDUCTOR":      manejarLoginConductor(p);     break;
            case "GET_MI_VEHICULO":      manejarGetMiVehiculo();       break;
            case "GET_MIS_PAQUETES":     manejarGetMisPaquetes();      break;
            case "UBICACION":            manejarUbicacion(p);          break;
            case "ESTADO":               manejarEstado(p);             break;
            // ── INCIDENCIAS ───────────────────────────────────────────────────
            case "GET_INCIDENCIAS":      manejarGetIncidencias();      break;
            case "CREATE_INCIDENCIA":    manejarCreateIncidencia(p);   break;
            case "DELETE_INCIDENCIA":    manejarDeleteIncidencia(p);   break;

            default:
                enviar("ERROR|Comando desconocido: " + cmd);
        }
    }

    // =========================================================================
    // AUTENTICACIÓN
    // =========================================================================

    /**
     * LOGIN_APP|username|password
     * Autentica al usuario; si es correcto, almacena el usuario en sesión
     * y responde DATA con sus datos para que el cliente sepa el rol.
     */
    private void manejarLoginApp(String[] p) {
        if (p.length < 3) { enviar("ERROR|Formato: LOGIN_APP|username|password"); return; }
        try {
            Usuario u = authCtrl.login(p[1], p[2]);
            usuarioActual = u;
            // DATA|id|nombre|username|rol|estado
            enviar("DATA|" + u.getId() + "|" + u.getNombre() + "|"
                    + u.getUsername() + "|" + u.getRol() + "|" + u.getEstado());
        } catch (ValidacionException e) {
            enviar("ERROR|" + e.getMessage());
        } catch (Exception e) {
            Logger.registrar("ERROR_LOGIN", p[1], e.getMessage());
            enviar("ERROR|Error interno al autenticar");
        }
    }

    // =========================================================================
    // USUARIOS
    // =========================================================================

    /** GET_USUARIOS — solo ADMINISTRADOR */
    private void manejarGetUsuarios() {
        if (usuarioActual.getRol() != Rol.ADMINISTRADOR) { enviar("FORBIDDEN|Solo el administrador puede listar usuarios"); return; }
        try {
            List<Usuario> lista = usuarioCtrl.obtenerTodos();
            StringBuilder sb = new StringBuilder("LIST|USUARIOS");
            for (Usuario u : lista) {
                sb.append("|").append(u.getId())
                  .append("|").append(u.getNombre())
                  .append("|").append(u.getUsername())
                  .append("|").append(u.getRol())
                  .append("|").append(u.getEstado())
                  .append("~");
            }
            enviar(quitarUltimaTilde(sb.toString()));
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /** CREATE_USUARIO|nombre|username|password|rol|estado */
    private void manejarCreateUsuario(String[] p) {
        if (usuarioActual.getRol() != Rol.ADMINISTRADOR) { enviar("FORBIDDEN|Sin permisos"); return; }
        if (p.length < 6) { enviar("ERROR|Formato: CREATE_USUARIO|nombre|username|password|rol|estado"); return; }
        try {
            Usuario nuevo = new Usuario();
            nuevo.setNombre(p[1]);
            nuevo.setUsername(p[2]);
            nuevo.setPassHash(authCtrl.hashSHA256(p[3]));
            nuevo.setRol(Rol.fromString(p[4]));
            nuevo.setEstado(p[5]);
            usuarioCtrl.registrar(nuevo, usuarioActual.getId());
            enviar("OK|Usuario creado correctamente");
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /**
     * UPDATE_USUARIO|id|nombre|username|password|rol|estado
     * Si password viene vacío, no se cambia el hash existente.
     */
    private void manejarUpdateUsuario(String[] p) {
        if (usuarioActual.getRol() != Rol.ADMINISTRADOR) { enviar("FORBIDDEN|Sin permisos"); return; }
        if (p.length < 7) { enviar("ERROR|Formato: UPDATE_USUARIO|id|nombre|username|password|rol|estado"); return; }
        try {
            int id = Integer.parseInt(p[1]);
            Usuario u = usuarioCtrl.obtenerPorId(id);
            if (u == null) { enviar("ERROR|Usuario no encontrado"); return; }
            u.setNombre(p[2]);
            u.setUsername(p[3]);
            if (!p[4].isEmpty()) u.setPassHash(authCtrl.hashSHA256(p[4]));
            u.setRol(Rol.fromString(p[5]));
            u.setEstado(p[6]);
            usuarioCtrl.actualizar(u, usuarioActual.getId());
            enviar("OK|Usuario actualizado");
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /** DELETE_USUARIO|id */
    private void manejarDeleteUsuario(String[] p) {
        if (usuarioActual.getRol() != Rol.ADMINISTRADOR) { enviar("FORBIDDEN|Sin permisos"); return; }
        if (p.length < 2) { enviar("ERROR|Formato: DELETE_USUARIO|id"); return; }
        try {
            usuarioCtrl.eliminar(Integer.parseInt(p[1]), usuarioActual.getId());
            enviar("OK|Usuario eliminado");
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    // =========================================================================
    // VEHÍCULOS
    // =========================================================================

    /** GET_VEHICULOS */
    private void manejarGetVehiculos() {
        try {
            enviar(construirListaVehiculos(vehiculoCtrl.obtenerTodos()));
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /**
     * GET_CONDUCTORES — lista todos los conductores activos.
     * Formato: LIST|CONDUCTORES|id|nombre~...
     */
    private void manejarGetConductores() {
        try {
            List<Usuario> conductores = usuarioCtrl.obtenerConductores();
            StringBuilder sb = new StringBuilder("LIST|CONDUCTORES");
            for (Usuario u : conductores) {
                sb.append("|").append(u.getId())
                  .append("|").append(u.getNombre())
                  .append("~");
            }
            enviar(quitarUltimaTilde(sb.toString()));
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /** GET_VEHICULOS_DISPONIBLES */
    private void manejarGetVehiculosDisponibles() {
        try {
            enviar(construirListaVehiculos(vehiculoCtrl.obtenerDisponibles()));
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /** CREATE_VEHICULO|placa|tipo|capacidadMaxima|estado|idConductor */
    private void manejarCreateVehiculo(String[] p) {
        if (usuarioActual.getRol() == Rol.CONDUCTOR) { enviar("FORBIDDEN|Sin permisos"); return; }
        if (p.length < 6) { enviar("ERROR|Formato: CREATE_VEHICULO|placa|tipo|capacidad|estado|idConductor"); return; }
        try {
            Vehiculo v = crearInstanciaVehiculo(p[2]);
            v.setPlaca(p[1]);
            v.setCapacidadMaxima(Double.parseDouble(p[3]));
            v.setEstado(EstadoVehiculo.fromString(p[4]));
            v.setIdConductor(Integer.parseInt(p[5]));
            vehiculoCtrl.registrar(v, usuarioActual.getId());
            enviar("OK|Vehículo registrado");
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /** UPDATE_VEHICULO|id|placa|tipo|capacidadMaxima|estado|idConductor */
    private void manejarUpdateVehiculo(String[] p) {
        if (usuarioActual.getRol() == Rol.CONDUCTOR) { enviar("FORBIDDEN|Sin permisos"); return; }
        if (p.length < 7) { enviar("ERROR|Formato: UPDATE_VEHICULO|id|placa|tipo|capacidad|estado|idConductor"); return; }
        try {
            Vehiculo v = vehiculoCtrl.obtenerPorId(Integer.parseInt(p[1]));
            if (v == null) { enviar("ERROR|Vehículo no encontrado"); return; }
            v.setPlaca(p[2]);
            v.setCapacidadMaxima(Double.parseDouble(p[4]));
            v.setEstado(EstadoVehiculo.fromString(p[5]));
            v.setIdConductor(Integer.parseInt(p[6]));
            vehiculoCtrl.actualizar(v, usuarioActual.getId());
            enviar("OK|Vehículo actualizado");
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /** DELETE_VEHICULO|id */
    private void manejarDeleteVehiculo(String[] p) {
        if (usuarioActual.getRol() == Rol.CONDUCTOR) { enviar("FORBIDDEN|Sin permisos"); return; }
        if (p.length < 2) { enviar("ERROR|Formato: DELETE_VEHICULO|id"); return; }
        try {
            vehiculoCtrl.eliminar(Integer.parseInt(p[1]), usuarioActual.getId());
            enviar("OK|Vehículo eliminado");
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    // =========================================================================
    // PAQUETES
    // =========================================================================

    /** GET_PAQUETES */
    private void manejarGetPaquetes() {
        try {
            enviar(construirListaPaquetes(paqueteCtrl.obtenerTodos()));
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /** GET_PAQUETES_ESTADO|estado (EN_ESPERA | EN_TRANSITO | ENTREGADO | INCIDENCIA) */
    private void manejarGetPaquetesEstado(String[] p) {
        if (p.length < 2) { enviar("ERROR|Formato: GET_PAQUETES_ESTADO|estado"); return; }
        try {
            EstadoPaquete estado = EstadoPaquete.valueOf(p[1]);
            enviar(construirListaPaquetes(paqueteCtrl.obtenerPorEstado(estado)));
        } catch (IllegalArgumentException e) {
            enviar("ERROR|Estado inválido: " + p[1]);
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /** GET_PAQUETES_ESPERA — alias de GET_PAQUETES_ESTADO|EN_ESPERA */
    private void manejarGetPaquetesEspera() {
        try {
            enviar(construirListaPaquetes(paqueteCtrl.obtenerPorEstado(EstadoPaquete.EN_ESPERA)));
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /** CREATE_PAQUETE|codigo|descripcion|peso */
    private void manejarCreatePaquete(String[] p) {
        if (p.length < 4) { enviar("ERROR|Formato: CREATE_PAQUETE|codigo|descripcion|peso"); return; }
        try {
            Paquete nuevo = new Paquete();
            nuevo.setCodigo(p[1]);
            nuevo.setDescripcion(p[2]);
            nuevo.setPeso(Double.parseDouble(p[3]));
            nuevo.setEstado(EstadoPaquete.EN_ESPERA);
            paqueteCtrl.registrar(nuevo, usuarioActual.getId());
            enviar("OK|Paquete registrado");
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /** UPDATE_PAQUETE|id|codigo|descripcion|peso */
    private void manejarUpdatePaquete(String[] p) {
        if (p.length < 5) { enviar("ERROR|Formato: UPDATE_PAQUETE|id|codigo|descripcion|peso"); return; }
        try {
            Paquete paquete = paqueteCtrl.obtenerPorId(Integer.parseInt(p[1]));
            if (paquete == null) { enviar("ERROR|Paquete no encontrado"); return; }
            paquete.setCodigo(p[2]);
            paquete.setDescripcion(p[3]);
            paquete.setPeso(Double.parseDouble(p[4]));
            paqueteCtrl.actualizar(paquete, usuarioActual.getId());
            enviar("OK|Paquete actualizado");
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /** DELETE_PAQUETE|id */
    private void manejarDeletePaquete(String[] p) {
        if (p.length < 2) { enviar("ERROR|Formato: DELETE_PAQUETE|id"); return; }
        try {
            paqueteCtrl.eliminar(Integer.parseInt(p[1]), usuarioActual.getId());
            enviar("OK|Paquete eliminado");
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    // =========================================================================
    // ASIGNACIÓN
    // =========================================================================

    /**
     * ASIGNAR|idPaquete|idVehiculo
     * Obtiene el paquete y el vehículo por ID y delega en AsignacionController.
     */
    private void manejarAsignar(String[] p) {
        if (usuarioActual.getRol() == Rol.CONDUCTOR) { enviar("FORBIDDEN|Sin permisos"); return; }
        if (p.length < 3) { enviar("ERROR|Formato: ASIGNAR|idPaquete|idVehiculo"); return; }
        try {
            Paquete paquete = paqueteCtrl.obtenerPorId(Integer.parseInt(p[1]));
            Vehiculo vehiculo = vehiculoCtrl.obtenerPorId(Integer.parseInt(p[2]));

            if (paquete == null)  { enviar("ERROR|Paquete no encontrado");  return; }
            if (vehiculo == null) { enviar("ERROR|Vehículo no encontrado"); return; }

            boolean ok = asignCtrl.asignarPaquete(Arrays.asList(vehiculo), paquete, usuarioActual.getId());
            if (ok) enviar("OK|Paquete asignado correctamente");
            else    enviar("ERROR|No se pudo asignar: verifique estado y capacidad del vehículo");
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /**
     * GET_ASIGNACIONES — lista todas las asignaciones registradas.
     * Formato: LIST|ASIGNACIONES|id|codigoPaquete|placaVehiculo|fecha~...
     */
    private void manejarGetAsignaciones() {
        try {
            List<Asignacion> lista = asignacionDAO.obtenerTodas();
            StringBuilder sb = new StringBuilder("LIST|ASIGNACIONES");
            for (Asignacion a : lista) {
                sb.append("|").append(a.getId())
                  .append("|").append(a.getPaquete().getCodigo())
                  .append("|").append(a.getVehiculo().getPlaca())
                  .append("|").append(a.getFecha())
                  .append("~");
            }
            enviar(quitarUltimaTilde(sb.toString()));
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    // =========================================================================
    // MONITOR
    // =========================================================================

    /**
     * GET_MONITOR
     * Envía DOS líneas: vehículos EN_RUTA y luego paquetes EN_TRANSITO.
     * El cliente lee dos respuestas consecutivas.
     */
    private void manejarGetMonitor() {
        try {
            // Línea 1: vehículos en ruta
            List<Vehiculo> enRuta = vehiculoDAO.obtenerPorEstado(EstadoVehiculo.EN_RUTA);
            StringBuilder sbV = new StringBuilder("LIST|MONITOR_VEHICULOS");
            for (Vehiculo v : enRuta) {
                sbV.append("|").append(v.getId())
                   .append("|").append(v.getPlaca())
                   .append("|").append(v.getClass().getSimpleName())
                   .append("|").append(v.getEstado())
                   .append("~");
            }
            enviar(quitarUltimaTilde(sbV.toString()));

            // Línea 2: paquetes en tránsito
            List<Paquete> enTransito = paqueteCtrl.obtenerPorEstado(EstadoPaquete.EN_TRANSITO);
            enviar(construirListaPaquetes(enTransito)
                    .replace("LIST|PAQUETES", "LIST|MONITOR_PAQUETES"));
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /**
     * GET_AUDITORIA
     * Retorna todos los registros del log de auditoría.
     * Formato: LIST|AUDITORIA|id|username|accion|fecha~...
     * Los '|' internos del campo accion se reemplazan por ' - ' para no romper el protocolo.
     */
    private void manejarGetAuditoria() {
        try {
            List<Object[]> logs = auditoriaDAO.obtenerTodos();
            StringBuilder sb = new StringBuilder("LIST|AUDITORIA");
            for (Object[] fila : logs) {
                String accion = fila[2] != null ? fila[2].toString().replace("|", " - ") : "";
                String usuario = fila[1] != null ? fila[1].toString() : "";
                sb.append("|").append(fila[0])
                  .append("|").append(usuario)
                  .append("|").append(accion)
                  .append("|").append(fila[3])
                  .append("~");
            }
            enviar(quitarUltimaTilde(sb.toString()));
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    // =========================================================================
    // CONDUCTOR
    // =========================================================================

    /**
     * LOGIN_CONDUCTOR|idConductor|nombre
     * Registra en el log que el conductor está activo en el sistema de entregas.
     */
    private void manejarLoginConductor(String[] p) {
        if (p.length < 3) { enviar("ERROR|Formato: LOGIN_CONDUCTOR|idConductor|nombre"); return; }
        Logger.registrar("LOGIN_CONDUCTOR", p[2], "Conductor id=" + p[1] + " conectado al servidor de entregas");
        enviar("OK|Conductor registrado en sistema de entregas");
    }

    /**
     * GET_MI_VEHICULO
     * Retorna el vehículo asignado al conductor autenticado.
     * Formato exitoso: DATA|id|placa|tipo|capacidad|estado
     * Sin vehículo:    DATA|NINGUNO
     */
    private void manejarGetMiVehiculo() {
        try {
            Vehiculo v = vehiculoCtrl.obtenerPorConductor(usuarioActual.getId());
            if (v == null) {
                enviar("DATA|NINGUNO");
            } else {
                enviar("DATA|" + v.getId()
                        + "|" + v.getPlaca()
                        + "|" + v.getClass().getSimpleName()
                        + "|" + v.calcularCapacidad()
                        + "|" + v.getEstado());
            }
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /**
     * GET_MIS_PAQUETES|idConductor
     * Retorna solo los paquetes EN_TRANSITO asignados al vehículo del conductor autenticado.
     */
    private void manejarGetMisPaquetes() {
        try {
            List<Paquete> lista = paqueteCtrl.obtenerPorConductor(usuarioActual.getId());
            enviar(construirListaPaquetes(lista));
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /**
     * UBICACION|idConductor|descripcion
     * Registra la ubicación textual del conductor en el log.
     */
    private void manejarUbicacion(String[] p) {
        if (p.length < 3) { enviar("ERROR|Formato: UBICACION|idConductor|descripcion"); return; }
        Logger.registrar("UBICACION", "Conductor#" + p[1], p[2]);
        enviar("OK|Ubicación registrada");
    }

    /**
     * ESTADO|idPaquete|nuevoEstado
     * Actualiza el estado del paquete. Si es ENTREGADO o INCIDENCIA,
     * también libera el vehículo asignado (lo pone DISPONIBLE).
     */
    private void manejarEstado(String[] p) {
        if (p.length < 3) { enviar("ERROR|Formato: ESTADO|idPaquete|nuevoEstado"); return; }
        try {
            int idPaquete = Integer.parseInt(p[1]);
            EstadoPaquete nuevoEstado = EstadoPaquete.valueOf(p[2]);

            Paquete paquete = paqueteCtrl.obtenerPorId(idPaquete);
            if (paquete == null) { enviar("ERROR|Paquete no encontrado"); return; }

            paquete.setEstado(nuevoEstado);
            paqueteCtrl.actualizar(paquete, usuarioActual.getId());

            // Cerrar el ciclo: liberar el vehículo cuando el paquete termina
            if (nuevoEstado == EstadoPaquete.ENTREGADO || nuevoEstado == EstadoPaquete.INCIDENCIA) {
                liberarVehiculo(idPaquete);
            }

            Logger.registrar("ESTADO_PAQUETE", "Conductor#" + usuarioActual.getId(),
                    "Paquete#" + idPaquete + " -> " + nuevoEstado);
            enviar("OK|Estado actualizado a " + nuevoEstado);
        } catch (IllegalArgumentException e) {
            enviar("ERROR|Estado inválido: " + p[2]);
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /**
     * Busca la asignación activa del paquete y pone el vehículo en DISPONIBLE.
     */
    private void liberarVehiculo(int idPaquete) {
        try {
            List<Asignacion> todas = asignacionDAO.obtenerTodas();
            for (Asignacion a : todas) {
                if (a.getPaquete().getId() == idPaquete) {
                    Vehiculo v = a.getVehiculo();
                    v.setEstado(EstadoVehiculo.DISPONIBLE);
                    vehiculoCtrl.actualizar(v, 0);  // 0 = acción automática del sistema
                    Logger.registrar("VEHICULO_LIBERADO", "SISTEMA",
                            "Vehículo#" + v.getId() + " disponible tras paquete#" + idPaquete);
                    break;
                }
            }
        } catch (Exception e) {
            Logger.registrar("ERROR_LIBERAR", "SISTEMA", e.getMessage());
        }
    }

    // =========================================================================
    // INCIDENCIAS
    // =========================================================================

    /** GET_INCIDENCIAS */
    private void manejarGetIncidencias() {
        try {
            List<Incidencia> lista = incidenciaDAO.obtenerTodas();
            StringBuilder sb = new StringBuilder("LIST|INCIDENCIAS");
            for (Incidencia i : lista) {
                sb.append("|").append(i.getId())
                  .append("|").append(i.getDescripcion())
                  .append("|").append(i.getIdPaquete())
                  .append("|").append(i.getIdConductor())
                  .append("|").append(i.getFecha())
                  .append("~");
            }
            enviar(quitarUltimaTilde(sb.toString()));
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /**
     * CREATE_INCIDENCIA|descripcion|idPaquete|idConductor
     * Registra la incidencia y actualiza el estado del paquete a INCIDENCIA.
     */
    private void manejarCreateIncidencia(String[] p) {
        if (p.length < 4) { enviar("ERROR|Formato: CREATE_INCIDENCIA|descripcion|idPaquete|idConductor"); return; }
        try {
            int idPaquete   = Integer.parseInt(p[2]);
            int idConductor = Integer.parseInt(p[3]);

            Incidencia inc = new Incidencia(0, p[1], LocalDateTime.now(), idPaquete, idConductor);
            incidenciaDAO.insertar(inc);

            // Actualizar estado del paquete
            Paquete paquete = paqueteCtrl.obtenerPorId(idPaquete);
            if (paquete != null) {
                paquete.setEstado(EstadoPaquete.INCIDENCIA);
                paqueteCtrl.actualizar(paquete, usuarioActual.getId());
                liberarVehiculo(idPaquete);
            }

            enviar("OK|Incidencia registrada");
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    /** DELETE_INCIDENCIA|id */
    private void manejarDeleteIncidencia(String[] p) {
        if (p.length < 2) { enviar("ERROR|Formato: DELETE_INCIDENCIA|id"); return; }
        try {
            boolean ok = incidenciaDAO.eliminar(Integer.parseInt(p[1]));
            if (ok) enviar("OK|Incidencia eliminada");
            else    enviar("ERROR|No se encontró la incidencia");
        } catch (Exception e) {
            enviar("ERROR|" + e.getMessage());
        }
    }

    // =========================================================================
    // UTILIDADES
    // =========================================================================

    /**
     * Construye la respuesta LIST para una lista de paquetes.
     * Formato: LIST|PAQUETES|id|codigo|descripcion|peso|estado~...
     */
    private String construirListaPaquetes(List<Paquete> lista) {
        StringBuilder sb = new StringBuilder("LIST|PAQUETES");
        for (Paquete p : lista) {
            sb.append("|").append(p.getId())
              .append("|").append(p.getCodigo())
              .append("|").append(p.getDescripcion())
              .append("|").append(p.getPeso())
              .append("|").append(p.getEstado())
              .append("~");
        }
        return quitarUltimaTilde(sb.toString());
    }

    /**
     * Construye la respuesta LIST para una lista de vehículos.
     * Formato: LIST|VEHICULOS|id|placa|tipo|capacidad|estado|idConductor~...
     */
    private String construirListaVehiculos(List<Vehiculo> lista) {
        StringBuilder sb = new StringBuilder("LIST|VEHICULOS");
        for (Vehiculo v : lista) {
            sb.append("|").append(v.getId())
              .append("|").append(v.getPlaca())
              .append("|").append(v.getClass().getSimpleName())
              .append("|").append(v.calcularCapacidad())
              .append("|").append(v.getEstado())
              .append("|").append(v.getIdConductor())
              .append("~");
        }
        return quitarUltimaTilde(sb.toString());
    }

    /**
     * Instancia el subtipo de Vehiculo correcto según el nombre del tipo.
     */
    private Vehiculo crearInstanciaVehiculo(String tipo) {
        switch (tipo) {
            case "Camion": return new Camion();
            case "Furgon": return new Furgon();
            default:       return new Moto();
        }
    }

    /** Elimina el '~' final de un StringBuilder si existe. */
    private String quitarUltimaTilde(String s) {
        return s.endsWith("~") ? s.substring(0, s.length() - 1) : s;
    }
}
