package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.ConfiguracionSistema;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server.util.Logger;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servidor concurrente de QuickDelivery.
 * Acepta conexiones de cualquier cliente (admin, despachador, conductor),
 * asigna un hilo por cliente y mantiene un conjunto de clientes activos.
 *
 * <p>Al arrancar carga (o crea) una {@link ConfiguracionSistema} serializada.
 * Al detenerse la persiste automáticamente via shutdown hook.
 *
 * <p>Basado en el mismo patrón de SaladeChat del curso.
 * Protocolo de mensajes definido en {@link ClientHandler}.
 *
 * @author daniel-2405
 */
public class ServidorQuickDelivery {

    /**
     * Conjunto de todos los clientes activos conectados al servidor.
     * ConcurrentHashMap.newKeySet() garantiza acceso seguro desde múltiples hilos.
     */
    private static final Set<ClientHandler> clientes =
            ConcurrentHashMap.newKeySet();

    /**
     * Punto de entrada del servidor. Carga la configuración serializada (o crea una
     * por defecto), registra un shutdown hook para persistirla al apagar y entra en
     * loop aceptando conexiones de clientes.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        // ── Cargar o crear configuración serializada ──────────────────────────
        ConfiguracionSistema config = ConfiguracionSistema.cargar();
        if (config == null) {
            int puerto = leerPuerto();
            config = new ConfiguracionSistema(puerto, 30, "1.0");
            System.out.println("Nueva configuración creada: " + config);
        }

        // Guardar configuración al apagar el servidor (Ctrl+C, kill, etc.)
        final ConfiguracionSistema configFinal = config;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            configFinal.guardar();
            Logger.registrar("SERVIDOR_FIN", "SISTEMA", "Configuración persistida al apagar");
        }));

        int puerto = config.getPuerto();
        int maxClientes = config.getMaxClientes();

        System.out.println("Servidor QuickDelivery iniciado en el puerto: " + puerto
                + " | versión: " + config.getVersion());
        Logger.registrar("SERVIDOR_INICIO", "SISTEMA", "Puerto " + puerto + " abierto");

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            while (true) {
                Socket socketCliente = serverSocket.accept();
                if (clientes.size() < maxClientes) {
                    ClientHandler handler = new ClientHandler(socketCliente);
                    clientes.add(handler);          // agregar antes de iniciar el hilo
                    new Thread(handler).start();
                } else {
                    System.out.println("Máximo de clientes alcanzado. Rechazando conexión.");
                    try (PrintWriter tempOut = new PrintWriter(socketCliente.getOutputStream(), true)) {
                        tempOut.println("ERROR|Servidor lleno, intente más tarde");
                    } catch (IOException e) {
                        System.out.println("Error al rechazar conexión: " + e.getMessage());
                    }
                    socketCliente.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
            Logger.registrar("ERROR_SERVIDOR", "SISTEMA", e.getMessage());
        }
    }

    /**
     * Elimina un cliente del conjunto al desconectarse.
     *
     * @param handler handler del cliente que se desconectó
     */
    public static void removerCliente(ClientHandler handler) {
        clientes.remove(handler);
        System.out.println("Cliente desconectado. Activos: " + clientes.size());
    }

    /**
     * Envía un mensaje a todos los clientes conectados.
     *
     * @param mensaje texto a enviar
     */
    public static void broadcast(String mensaje) {
        for (ClientHandler handler : clientes) {
            handler.enviar(mensaje);
        }
    }

    /**
     * Lee el puerto desde config.properties. Si falla, usa 5000 por defecto.
     */
    private static int leerPuerto() {
        try (InputStream input = ServidorQuickDelivery.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            Properties props = new Properties();
            props.load(input);
            return Integer.parseInt(props.getProperty("server.port", "5000"));
        } catch (Exception e) {
            System.out.println("No se pudo leer config.properties, usando puerto 5000");
            return 5000;
        }
    }
}
