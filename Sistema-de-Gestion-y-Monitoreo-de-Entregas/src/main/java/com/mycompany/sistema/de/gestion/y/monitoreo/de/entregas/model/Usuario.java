package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model;

/**
 * Representa un usuario del sistema QuickDelivery S.A.
 *
 * <p>Un usuario tiene credenciales de acceso, un {@link Rol} que determina
 * sus permisos y un estado que controla si puede iniciar sesión.</p>
 *
 * @author QuickDelivery S.A.
 */
public class Usuario {

    /** Identificador único del usuario en la base de datos. */
    private int id;

    /** Nombre completo del usuario. */
    private String nombre;

    /** Nombre de usuario único para inicio de sesión. */
    private String username;

    /** Hash SHA-256 de la contraseña del usuario. */
    private String passHash;

    /**
     * Estado de la cuenta.
     * Valores posibles: {@code "activo"}, {@code "inactivo"}, {@code "suspendido"}.
     */
    private String estado;

    /** Rol asignado al usuario; determina su nivel de acceso. */
    private Rol rol;

    /**
     * Constructor sin argumentos requerido para frameworks de persistencia
     * y deserialización.
     */
    public Usuario() {
    }

    /**
     * Construye un usuario con todos sus atributos.
     *
     * @param id       identificador único del usuario.
     * @param nombre   nombre completo del usuario.
     * @param username nombre de usuario para inicio de sesión.
     * @param passHash hash SHA-256 de la contraseña.
     * @param estado   estado de la cuenta: {@code "activo"}, {@code "inactivo"} o {@code "suspendido"}.
     * @param rol      rol asignado al usuario.
     */
    public Usuario(int id, String nombre, String username,
                   String passHash, String estado, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.username = username;
        this.passHash = passHash;
        this.estado = estado;
        this.rol = rol;
    }

    // ──────────────── Getters y Setters ────────────────

    /** @return identificador único del usuario. */
    public int getId() { return id; }

    /** @param id nuevo identificador del usuario. */
    public void setId(int id) { this.id = id; }

    /** @return nombre completo del usuario. */
    public String getNombre() { return nombre; }

    /** @param nombre nuevo nombre completo del usuario. */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /** @return nombre de usuario para inicio de sesión. */
    public String getUsername() { return username; }

    /** @param username nuevo nombre de usuario. */
    public void setUsername(String username) { this.username = username; }

    /** @return hash SHA-256 de la contraseña. */
    public String getPassHash() { return passHash; }

    /** @param passHash nuevo hash SHA-256 de la contraseña. */
    public void setPassHash(String passHash) { this.passHash = passHash; }

    /** @return estado de la cuenta: {@code "activo"}, {@code "inactivo"} o {@code "suspendido"}. */
    public String getEstado() { return estado; }

    /** @param estado nuevo estado de la cuenta. */
    public void setEstado(String estado) { this.estado = estado; }

    /** @return rol asignado al usuario. */
    public Rol getRol() { return rol; }

    /** @param rol nuevo rol asignado al usuario. */
    public void setRol(Rol rol) { this.rol = rol; }

    /**
     * Indica si el usuario puede iniciar sesión en el sistema.
     *
     * @return {@code true} si el estado de la cuenta es {@code "activo"}.
     */
    public boolean isActivo() {
        return "activo".equalsIgnoreCase(estado);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Usuario{id=" + id +
                ", username='" + username + '\'' +
                ", nombre='" + nombre + '\'' +
                ", rol=" + rol +
                ", estado='" + estado + '\'' + '}';
    }
}
