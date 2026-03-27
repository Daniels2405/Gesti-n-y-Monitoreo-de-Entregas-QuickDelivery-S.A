package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception;

/**
 * Excepción base del sistema QuickDelivery.
 * Todas las excepciones personalizadas del sistema extienden de esta clase.
 *
 * <p>Jerarquía:
 * <pre>
 * Exception
 * └── QuickDeliveryException
 *     ├── ConexionException
 *     └── ValidacionException
 * </pre>
 *
 * @author daniel-2405
 */
public class QuickDeliveryException extends Exception {

    /**
     * Crea una nueva excepción con el mensaje indicado.
     *
     * @param message descripción del error ocurrido
     */
    public QuickDeliveryException(String message) {
        super(message);
    }
}
