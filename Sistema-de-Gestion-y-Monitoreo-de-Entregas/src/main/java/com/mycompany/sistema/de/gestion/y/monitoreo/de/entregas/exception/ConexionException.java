package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception;

/**
 * Excepción para fallos de red o base de datos.
 * Se lanza cuando no se puede establecer o mantener la conexión JDBC
 * o cuando ocurre un error en la comunicación por sockets.
 *
 * @author daniel-2405
 */
public class ConexionException extends QuickDeliveryException {

    /**
     * Crea una nueva excepción de conexión con el mensaje indicado.
     *
     * @param message descripción del fallo de conexión
     */
    public ConexionException(String message) {
        super(message);
    }
}
