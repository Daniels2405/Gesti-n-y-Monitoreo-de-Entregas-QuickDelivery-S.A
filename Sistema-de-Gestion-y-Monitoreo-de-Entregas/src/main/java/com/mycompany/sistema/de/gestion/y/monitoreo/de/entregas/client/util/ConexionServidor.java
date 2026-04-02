package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Singleton que gestiona la conexión TCP del cliente con el servidor QuickDelivery.
 *
 * <p>Todas las vistas del cliente obtienen esta instancia única para enviar
 * comandos y recibir respuestas del servidor mediante el protocolo de texto
 * separado por '|' definido en {@code ClientHandler}.</p>
 *
 * @author daniel-2405
 */
public class ConexionServidor {

    // Cambiar HOST a la IP de la máquina donde corre el servidor si es remoto
    private static final String HOST = "127.0.0.1";
    private static final int    PUERTO = 5000;

    private static ConexionServidor instancia;

    private Socket        socket;
    private PrintWriter   out;
    private BufferedReader in;
    private boolean       conectado = false;

    private ConexionServidor() {}

    /**
     * Retorna la única instancia del singleton.
     */
    public static ConexionServidor getInstancia() {
        if (instancia == null) {
            instancia = new ConexionServidor();
        }
        return instancia;
    }

    /**
     * Abre la conexión con el servidor y consume el mensaje de bienvenida.
     *
     * @throws IOException si no se puede conectar al servidor.
     */
    public void conectar() throws IOException {
        socket = new Socket(HOST, PUERTO);
        out    = new PrintWriter(socket.getOutputStream(), true); // auto-flush
        in     = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        in.readLine(); // consumir: "OK|Conectado al servidor QuickDelivery"
        conectado = true;
    }

    /**
     * Envía un mensaje al servidor y espera una línea de respuesta.
     *
     * @param mensaje mensaje a enviar (sin salto de línea).
     * @return respuesta del servidor, o {@code null} si la conexión se cerró.
     * @throws IOException si ocurre un error de E/S.
     */
    public String enviarYEsperar(String mensaje) throws IOException {
        out.println(mensaje);
        return in.readLine();
    }

    /**
     * Envía un mensaje sin esperar respuesta (útil para notificaciones).
     *
     * @param mensaje mensaje a enviar.
     */
    public void enviar(String mensaje) {
        out.println(mensaje);
    }

    /**
     * Lee una línea del servidor sin enviar previamente un mensaje.
     * Útil cuando el servidor envía múltiples respuestas consecutivas (p. ej. GET_MONITOR).
     *
     * @return línea leída, o {@code null} si la conexión se cerró.
     * @throws IOException si ocurre un error de E/S.
     */
    public String leer() throws IOException {
        return in.readLine();
    }

    /**
     * Indica si la conexión está activa.
     */
    public boolean isConectado() {
        return conectado && socket != null && !socket.isClosed();
    }

    /**
     * Cierra la conexión y destruye la instancia singleton para permitir reconexión.
     */
    public void cerrar() {
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
        conectado = false;
        instancia = null;
    }
}
