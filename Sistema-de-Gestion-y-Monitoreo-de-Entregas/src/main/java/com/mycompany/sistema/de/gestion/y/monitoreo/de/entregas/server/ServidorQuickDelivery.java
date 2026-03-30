package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.util.Logger;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servidor concurrente de QuickDelivery.
 * Acepta conexiones de conductores, asigna un hilo por conductor
 * y mantiene un mapa de conductores activos.
 *
 * <p>Basado en el mismo patrón de SaladeChat del curso.
 * Protocolo de mensajes definido en {@link ClientHandler}.
 *
 * @author daniel-2405
 */
public class ServidorQuickDelivery {

    private static final int MAX_CONDUCTORES = 30;

    /**
     * Mapa de conductores activos: clave = ID del conductor, valor = su handler.
     * ConcurrentHashMap garantiza acceso seguro desde múltiples hilos.
     */
    private static final ConcurrentHashMap<Integer, ClientHandler> conductoresActivos
            = new ConcurrentHashMap<>();

    /**
     * Punto de entrada del servidor. Lee el puerto desde config.properties
     * y entra en loop aceptando conexiones de conductores.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        int puerto = leerPuerto();
        System.out.println("Servidor QuickDelivery iniciado en el puerto: " + puerto);
        Logger.registrar("SERVIDOR_INICIO", "SISTEMA", "Puerto " + puerto + " abierto");

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            while (true) {
                Socket conexionConductor = serverSocket.accept();
                if (conductoresActivos.size() < MAX_CONDUCTORES) {
                    ClientHandler handler = new ClientHandler(conexionConductor);
                    new Thread(handler).start();
                } else {
                    System.out.println("Máximo de conductores alcanzado. Rechazando conexión.");
                    try (PrintWriter tempOut = new PrintWriter(conexionConductor.getOutputStream(), true)) {
                        tempOut.println("ERROR|Servidor lleno, intente más tarde");
                    } catch (IOException e) {
                        System.out.println("Error al rechazar conexión: " + e.getMessage());
                    }
                    conexionConductor.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
            Logger.registrar("ERROR_SERVIDOR", "SISTEMA", e.getMessage());
        }
    }

    /**
     * Registra un conductor en el mapa de conductores activos.
     *
     * @param idConductor ID único del conductor
     * @param handler     hilo que maneja su conexión
     */
    public static void agregarConductor(int idConductor, ClientHandler handler) {
        conductoresActivos.put(idConductor, handler);
        System.out.println("Conductor #" + idConductor + " conectado. Activos: " + conductoresActivos.size());
        Logger.registrar("LOGIN", "Conductor#" + idConductor, "Conectado al servidor");
    }

    /**
     * Elimina un conductor del mapa al desconectarse.
     *
     * @param idConductor ID del conductor que se desconectó
     */
    public static void removerConductor(int idConductor) {
        conductoresActivos.remove(idConductor);
        System.out.println("Conductor #" + idConductor + " desconectado. Activos: " + conductoresActivos.size());
        Logger.registrar("DESCONEXION", "Conductor#" + idConductor, "Desconectado del servidor");
    }

    /**
     * Envía un mensaje a todos los conductores conectados.
     * Útil para notificaciones generales del sistema.
     *
     * @param mensaje texto a enviar
     */
    public static void broadcast(String mensaje) {
        for (ClientHandler handler : conductoresActivos.values()) {
            handler.sendMessage(mensaje);
        }
    }

    /**
     * Retorna el mapa de conductores activos (para el monitor del despachador).
     *
     * @return mapa de conductores activos, clave = ID conductor
     */
    public static Map<Integer, ClientHandler> getConductoresActivos() {
        return conductoresActivos;
    }

    /**
     * Lee el puerto desde config.properties. Si falla, usa 5000 por defecto.
     */
    private static int leerPuerto() {
        try (InputStream input = ServidorQuickDelivery.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties props = new Properties();
            props.load(input);
            return Integer.parseInt(props.getProperty("server.port", "5000"));
        } catch (Exception e) {
            System.out.println("No se pudo leer config.properties, usando puerto 5000");
            return 5000;
        }
    }
}
