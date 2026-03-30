package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

/**
 * Representa un paquete dentro del sistema de gestión de entregas
 * de QuickDelivery S.A.
 *
 * <p>Un paquete tiene un código único de seguimiento, una descripción,
 * un peso en kg y un {@link EstadoPaquete estado} que evoluciona a lo
 * largo de su ciclo de vida (EN_ESPERA → EN_TRANSITO → ENTREGADO o INCIDENCIA).</p>
 *
 * @author QuickDelivery S.A.
 */
public class Paquete {

    /** Identificador único del paquete en la base de datos. */
    private int id;

    /** Código alfanumérico de seguimiento del paquete. */
    private String codigo;

    /** Descripción del contenido o destino del paquete. */
    private String descripcion;

    /** Peso del paquete en kilogramos. */
    private double peso;

    /** Estado actual del paquete en su ciclo de vida. */
    private EstadoPaquete estado;

    /**
     * Constructor sin argumentos requerido para frameworks de persistencia
     * y deserialización.
     */
    public Paquete() {
    }

    /**
     * Construye un paquete con todos sus atributos.
     *
     * @param id          identificador único del paquete.
     * @param codigo      código de seguimiento del paquete.
     * @param descripcion descripción del contenido o destino.
     * @param peso        peso del paquete en kg.
     * @param estado      estado inicial del paquete.
     */
    public Paquete(int id, String codigo, String descripcion, double peso, EstadoPaquete estado) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.peso = peso;
        this.estado = estado;
    }

    /**
     * Retorna el identificador único del paquete.
     *
     * @return id del paquete.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del paquete.
     *
     * @param id nuevo id del paquete.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna el código de seguimiento del paquete.
     *
     * @return código del paquete.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Establece el código de seguimiento del paquete.
     *
     * @param codigo nuevo código del paquete.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Retorna la descripción del contenido o destino del paquete.
     *
     * @return descripción del paquete.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del contenido o destino del paquete.
     *
     * @param descripcion nueva descripción del paquete.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Retorna el peso del paquete en kilogramos.
     *
     * @return peso en kg.
     */
    public double getPeso() {
        return peso;
    }

    /**
     * Establece el peso del paquete en kilogramos.
     *
     * @param peso nuevo peso en kg.
     */
    public void setPeso(double peso) {
        this.peso = peso;
    }

    /**
     * Retorna el estado actual del paquete en su ciclo de vida.
     *
     * @return estado del paquete.
     */
    public EstadoPaquete getEstado() {
        return estado;
    }

    /**
     * Establece el estado del paquete en su ciclo de vida.
     *
     * @param estado nuevo estado del paquete.
     */
    public void setEstado(EstadoPaquete estado) {
        this.estado = estado;
    }

    /**
     * Indica si el paquete ya fue asignado a un vehículo, es decir,
     * su estado es distinto de {@link EstadoPaquete#EN_ESPERA} y no es nulo.
     *
     * @return {@code true} si el paquete está asignado; {@code false} si aún está en espera o sin estado.
     */
    public boolean isAsignado() {
        return estado != null && estado != EstadoPaquete.EN_ESPERA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Paquete{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", peso=" + peso +
                ", estado=" + estado +
                '}';
    }
}
