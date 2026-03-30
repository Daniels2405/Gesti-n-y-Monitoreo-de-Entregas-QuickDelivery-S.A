package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.server;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.util.Logger;
import java.io.*;
import java.net.*;

/**
 * Maneja la conexión de un conductor en un hilo independiente.
 * Cada conductor conectado al servidor tiene su propio {@code ClientHandler}.
 *
 * <p>Protocolo de mensajes (texto plano separado por '|'):
 * <pre>
 *   Cliente → Servidor:
 *     LOGIN|idConductor|nombre          — primer mensaje al conectarse
 *     UBICACION|idConductor|descripcion — ubicación textual del conductor
 *     ESTADO|idPaquete|nuevoEstado      — actualización de estado de paquete
 *
 *   Servidor → Cliente:
 *     OK|mensaje                        — confirmación
 *     ERROR|descripción                 — error
 * </pre>
 *
 * @author daniel-2405
 */
public class ClientHandler implements Runnable {

    private final Socket connection;
    private PrintWriter out;
    private BufferedReader in;
    private int idConductor;
    private String nombre;

    /**
     * Crea un handler para la conexión del conductor.
     *
     * @param connection socket aceptado por el servidor
     */
    public ClientHandler(Socket connection) {
        this.connection = connection;
    }

    /**
     * Retorna el nombre del conductor asociado a este handler.
     *
     * @return nombre del conductor
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Envía un mensaje al conductor conectado.
     *
     * @param mensaje texto a enviar
     */
    public void sendMessage(String mensaje) {
        out.println(mensaje);
    }

    /**
     * Lógica principal del hilo. Lee mensajes del conductor,
     * los procesa según el protocolo y actualiza el servidor.
     */
    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out = new PrintWriter(connection.getOutputStream(), true);

            // Primer mensaje debe ser LOGIN|idConductor|nombre
            String primerMensaje = in.readLine();
            if (primerMensaje == null || !primerMensaje.startsWith("LOGIN")) {
                out.println("ERROR|Se esperaba LOGIN como primer mensaje");
                return;
            }

            // split("\\|") corta el mensaje en sus campos usando '|' como separador
            // Ejemplo: "LOGIN|7|Daniel" → camposLogin[0]="LOGIN", [1]="7", [2]="Daniel"
            String[] camposLogin = primerMensaje.split("\\|");
            idConductor = Integer.parseInt(camposLogin[1]);
            nombre = camposLogin[2];

            ServidorQuickDelivery.agregarConductor(idConductor, this);
            out.println("OK|Bienvenido " + nombre);

            // Loop principal: escuchar mensajes del conductor
            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                procesarMensaje(mensaje);
            }

        } catch (IOException e) {
            System.out.println("Error con conductor #" + idConductor + ": " + e.getMessage());
            Logger.registrar("ERROR_RED", "Conductor#" + idConductor, e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
            ServidorQuickDelivery.removerConductor(idConductor);
        }
    }

    /**
     * Procesa un mensaje recibido del conductor según el protocolo.
     *
     * @param mensaje línea de texto recibida
     */
    private void procesarMensaje(String mensaje) {
        // split("\\|") separa el mensaje en campos usando '|' como delimitador
        // Ejemplo: "UBICACION|7|Avenida Central 50" → camposMensaje[0]="UBICACION", [1]="7", [2]="Avenida Central 50"
        String[] camposMensaje = mensaje.split("\\|");
        switch (camposMensaje[0]) {
            case "UBICACION":
                // UBICACION|idConductor|descripcion
                Logger.registrar("UBICACION", "Conductor#" + idConductor,
                        camposMensaje[2]);
                out.println("OK|Ubicación registrada");
                break;
            case "ESTADO":
                // ESTADO|idPaquete|nuevoEstado
                Logger.registrar("ESTADO_PAQUETE", "Conductor#" + idConductor,
                        "Paquete#" + camposMensaje[1] + " -> " + camposMensaje[2]);
                out.println("OK|Estado actualizado");
                break;
            default:
                out.println("ERROR|Mensaje desconocido: " + camposMensaje[0]);
                break;
        }
    }
}
