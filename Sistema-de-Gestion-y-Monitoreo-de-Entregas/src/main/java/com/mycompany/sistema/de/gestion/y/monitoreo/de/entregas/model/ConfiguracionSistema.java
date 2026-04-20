package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Configuración del servidor QuickDelivery.
 *
 * <p>Implementa {@link Serializable} para poder persistirse en disco
 * mediante {@link ObjectOutputStream}/{@link ObjectInputStream}.
 * Al arrancar el servidor se carga (o se crea) esta configuración;
 * al detenerse se serializa con la fecha del último inicio actualizada.</p>
 *
 * @author daniel-2405
 */
public class ConfiguracionSistema implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Ruta del archivo de configuración serializado. */
    public static final String RUTA_ARCHIVO = "quickdelivery_config.ser";

    private int    puerto;
    private int    maxClientes;
    private String version;
    private String fechaUltimoInicio;   // String: LocalDateTime no es Serializable

    // ----------------------------------------------------------------- constructor

    /**
     * Crea una nueva configuración con los valores dados.
     * La fecha de último inicio se registra automáticamente como ahora.
     *
     * @param puerto      puerto TCP del servidor.
     * @param maxClientes máximo de conexiones simultáneas.
     * @param version     versión del sistema (ej. {@code "1.0"}).
     */
    public ConfiguracionSistema(int puerto, int maxClientes, String version) {
        this.puerto      = puerto;
        this.maxClientes = maxClientes;
        this.version     = version;
        actualizarFechaInicio();
    }

    // ----------------------------------------------------------------- serialización

    /**
     * Deserializa la configuración desde {@value #RUTA_ARCHIVO}.
     * Si el archivo no existe o está corrupto retorna {@code null}.
     *
     * @return configuración cargada, o {@code null} si no existe.
     */
    public static ConfiguracionSistema cargar() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            ConfiguracionSistema cfg = (ConfiguracionSistema) ois.readObject();
            System.out.println("Configuración cargada desde " + RUTA_ARCHIVO + ": " + cfg);
            return cfg;
        } catch (Exception e) {
            System.err.println("No se pudo deserializar configuración: " + e.getMessage());
            return null;
        }
    }

    /**
     * Serializa esta instancia en {@value #RUTA_ARCHIVO}.
     * Actualiza la fecha de último inicio antes de guardar.
     */
    public void guardar() {
        actualizarFechaInicio();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RUTA_ARCHIVO))) {
            oos.writeObject(this);
            System.out.println("Configuración serializada en " + RUTA_ARCHIVO);
        } catch (IOException e) {
            System.err.println("Error al serializar configuración: " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------- utilidades

    /** Actualiza {@code fechaUltimoInicio} con la hora actual. */
    private void actualizarFechaInicio() {
        this.fechaUltimoInicio = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // ----------------------------------------------------------------- getters/setters

    public int getPuerto()            { return puerto; }
    public void setPuerto(int p)      { this.puerto = p; }

    public int getMaxClientes()       { return maxClientes; }
    public void setMaxClientes(int m) { this.maxClientes = m; }

    public String getVersion()        { return version; }
    public void setVersion(String v)  { this.version = v; }

    public String getFechaUltimoInicio() { return fechaUltimoInicio; }

    @Override
    public String toString() {
        return "ConfiguracionSistema{puerto=" + puerto
                + ", maxClientes=" + maxClientes
                + ", version='" + version + '\''
                + ", ultimoInicio='" + fechaUltimoInicio + "'}";
    }
}
