package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

public class Paquete {
    private int id;
    private String codigo;
    private String descripcion;
    private double peso;
    private EstadoPaquete estado;

    public Paquete() {
    }

    public Paquete(int id, String codigo, String descripcion, double peso, EstadoPaquete estado) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.peso = peso;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public EstadoPaquete getEstado() {
        return estado;
    }

    public void setEstado(EstadoPaquete estado) {
        this.estado = estado;
    }

    public boolean isAsignado() {
        return estado != null && estado != EstadoPaquete.EN_ESPERA;
    }

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
