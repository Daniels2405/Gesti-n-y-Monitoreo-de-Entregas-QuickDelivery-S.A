package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.exception;

/**
 * Excepción para datos inválidos ingresados en el sistema.
 * Se lanza cuando un campo no cumple las reglas de validación,
 * por ejemplo: campos vacíos, formatos incorrectos o valores fuera de rango.
 *
 * @author daniel-2405
 */
public class ValidacionException extends QuickDeliveryException {

    /**
     * Crea una nueva excepción de validación con el mensaje indicado.
     *
     * @param message descripción del dato inválido
     */
    public ValidacionException(String message) {
        super(message);
    }
}
