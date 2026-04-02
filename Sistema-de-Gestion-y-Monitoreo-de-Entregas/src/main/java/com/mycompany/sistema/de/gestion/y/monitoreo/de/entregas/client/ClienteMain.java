package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.client;

import javax.swing.SwingUtilities;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.client.view.LoginFrame;

/**
 * Punto de entrada principal del sistema de gestión y monitoreo de entregas
 * de QuickDelivery S.A.
 *
 * <p>Abre la ventana de login. El servidor debe ejecutarse por separado
 * mediante {@code ServidorQuickDelivery} antes de iniciar este cliente.</p>
 *
 * @author daniel-2405
 */
public class ClienteMain {

    /**
     * Método principal de la aplicación.
     *
     * @param args argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> new LoginFrame().setVisible(true));
    }
}
