package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

/**
 * Clase base abstracta que representa un vehículo de reparto
 * en el sistema de QuickDelivery S.A.
 *
 * <p>Cada tipo concreto de vehículo ({@link Camion}, {@link Moto}, {@link Furgon})
 * extiende esta clase e implementa {@link #calcularCapacidad()} con su propio
 * factor de ajuste de capacidad.</p>
 *
 * @author QuickDelivery S.A.
 */
public abstract class Vehiculo {

    /** Identificador único del vehículo en la base de datos. */
    protected int id;

    /** Placa/matrícula del vehículo. */
    protected String placa;

    /** Capacidad máxima de carga base del vehículo (en kg). */
    protected double capacidadMaxima;

    /** Estado operativo actual del vehículo. */
    protected EstadoVehiculo estado;

    /**
     * Constructor sin argumentos requerido para frameworks de persistencia
     * y deserialización.
     */
    public Vehiculo() {
    }

    /**
     * Construye un vehículo con todos sus atributos.
     *
     * @param id             identificador único del vehículo.
     * @param placa          placa/matrícula del vehículo.
     * @param capacidadMaxima capacidad máxima de carga base en kg.
     * @param estado         estado operativo inicial del vehículo.
     */
    public Vehiculo(int id, String placa, double capacidadMaxima, EstadoVehiculo estado) {
        this.id = id;
        this.placa = placa;
        this.capacidadMaxima = capacidadMaxima;
        this.estado = estado;
    }

    /**
     * Calcula la capacidad efectiva de carga del vehículo aplicando
     * el factor de ajuste propio de cada tipo.
     *
     * @return capacidad efectiva de carga en kg.
     */
    public abstract double calcularCapacidad();

    /**
     * Retorna el identificador único del vehículo.
     *
     * @return id del vehículo.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del vehículo.
     *
     * @param id nuevo id del vehículo.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna la placa/matrícula del vehículo.
     *
     * @return placa del vehículo.
     */
    public String getPlaca() {
        return placa;
    }

    /**
     * Establece la placa/matrícula del vehículo.
     *
     * @param placa nueva placa del vehículo.
     */
    public void setPlaca(String placa) {
        this.placa = placa;
    }

    /**
     * Retorna la capacidad máxima de carga base del vehículo en kg.
     *
     * @return capacidad máxima base en kg.
     */
    public double getCapacidadMaxima() {
        return capacidadMaxima;
    }

    /**
     * Establece la capacidad máxima de carga base del vehículo en kg.
     *
     * @param capacidadMaxima nueva capacidad máxima base en kg.
     */
    public void setCapacidadMaxima(double capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    /**
     * Retorna el estado operativo actual del vehículo.
     *
     * @return estado del vehículo.
     */
    public EstadoVehiculo getEstado() {
        return estado;
    }

    /**
     * Establece el estado operativo del vehículo.
     *
     * @param estado nuevo estado del vehículo.
     */
    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }

    /**
     * Indica si el vehículo está disponible para recibir una asignación.
     *
     * @return {@code true} si el estado es {@link EstadoVehiculo#DISPONIBLE}, {@code false} en caso contrario.
     */
    public boolean estaDisponible() {
        return estado == EstadoVehiculo.DISPONIBLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Vehiculo{" +
                "id=" + id +
                ", placa='" + placa + '\'' +
                ", capacidadMaxima=" + capacidadMaxima +
                ", estado=" + estado +
                '}';
    }
}
