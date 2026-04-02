package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.client.view;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.client.util.ConexionServidor;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Rol;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Usuario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * Pantalla de creación/edición de usuario del sistema QuickDelivery S.A.
 *
 * @author Eryan / QuickDelivery S.A.
 */
public class EdicionDeUsuariosFrame extends JFrame {

    private JPanel panelHeader;
    private JPanel panelSidebar;
    private JPanel panelContenido;

    private JLabel lblLogo;
    private JLabel lblUsuarioHeader;
    private JButton btnCerrarSesion;

    private JButton btnDashboard;
    private JButton btnUsuarios;
    private JButton btnVehiculos;

    private JLabel lblIdValor;
    private JTextField txtUsuario;
    private JTextField txtNombreCompleto;
    private JComboBox<String> cmbRol;
    private JComboBox<String> cmbEstado;
    private JTextField txtContrasena;

    private JButton btnCambiarClave;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton btnBorrar;

    private final Usuario usuarioEditar;
    private final Usuario usuarioActual;

    /** Crea la pantalla en modo creación (usuarioEditar == null) o edición. */
    public EdicionDeUsuariosFrame(Usuario usuarioEditar, Usuario usuarioActual) {
        this.usuarioEditar = usuarioEditar;
        this.usuarioActual = usuarioActual;
        setTitle(usuarioEditar == null ? "Registrar Usuario" : "Edición de Usuarios");
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 760);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        if (usuarioEditar != null) {
            cargarDatosUsuario();
        } else {
            lblIdValor.setText("(nuevo)");
            txtContrasena.setEditable(true);
            btnBorrar.setEnabled(false);
        }
    }

    private void initComponents() {
        Color azulHeader = new Color(24, 40, 159);
        Color turquesaMenu = new Color(55, 165, 168);
        Color turquesaOscuro = new Color(30, 98, 97);
        Color fondoGeneral = new Color(230, 230, 230);
        Color fondoPanel = new Color(245, 245, 245);
        Color grisCampo = new Color(213, 213, 213);
        Color verdeRol = new Color(92, 233, 71);
        Color turquesaEstado = new Color(98, 204, 208);
        Color azulBoton = new Color(24, 40, 159);
        Color rojoBoton = new Color(255, 16, 5);
        Color turquesaGuardar = new Color(55, 165, 168);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(fondoGeneral);

        construirHeader(azulHeader);
        construirSidebar(turquesaMenu, turquesaOscuro);
        construirContenido(fondoGeneral, fondoPanel, grisCampo, verdeRol, turquesaEstado,
                azulBoton, rojoBoton, turquesaGuardar);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelSidebar, BorderLayout.WEST);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
    }

    private void construirHeader(Color azulHeader) {
        panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(azulHeader);
        panelHeader.setPreferredSize(new Dimension(0, 95));
        panelHeader.setBorder(new EmptyBorder(10, 20, 10, 25));

        lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.LEFT);
        cargarLogoHeader();

        JPanel panelDerechoHeader = new JPanel();
        panelDerechoHeader.setOpaque(false);
        panelDerechoHeader.setLayout(new BoxLayout(panelDerechoHeader, BoxLayout.X_AXIS));

        lblUsuarioHeader = new JLabel(" " + usuarioActual.getNombre());
        lblUsuarioHeader.setForeground(Color.WHITE);
        lblUsuarioHeader.setFont(new Font("SansSerif", Font.PLAIN, 20));

        btnCerrarSesion = new JButton(" Cerrar Sesión");
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setBackground(azulHeader);
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setContentAreaFilled(false);
        btnCerrarSesion.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        panelDerechoHeader.add(lblUsuarioHeader);
        panelDerechoHeader.add(Box.createHorizontalStrut(35));
        panelDerechoHeader.add(btnCerrarSesion);

        panelHeader.add(lblLogo, BorderLayout.WEST);
        panelHeader.add(panelDerechoHeader, BorderLayout.EAST);
    }

    private void construirSidebar(Color turquesaMenu, Color turquesaOscuro) {
        panelSidebar = new JPanel();
        panelSidebar.setBackground(turquesaMenu);
        panelSidebar.setPreferredSize(new Dimension(230, 0));
        panelSidebar.setLayout(new BoxLayout(panelSidebar, BoxLayout.Y_AXIS));

        JPanel barraSuperior = new JPanel();
        barraSuperior.setBackground(new Color(86, 208, 207));
        barraSuperior.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        barraSuperior.setPreferredSize(new Dimension(230, 18));

        btnDashboard = crearBotonMenu("Dashboard", turquesaMenu, Color.WHITE);
        btnUsuarios  = crearBotonMenu("Usuarios",  turquesaOscuro, Color.WHITE);
        btnVehiculos = crearBotonMenu("Vehículos", turquesaMenu, Color.WHITE);

        btnDashboard.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new MenuAdminFrame(usuarioActual).setVisible(true));
        });
        btnUsuarios.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new GestionDeUsuariosFrame(usuarioActual).setVisible(true));
        });
        btnVehiculos.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new GestionDeVehiculosFrame(usuarioActual).setVisible(true));
        });

        panelSidebar.add(barraSuperior);
        panelSidebar.add(Box.createVerticalStrut(12));
        panelSidebar.add(btnDashboard);
        panelSidebar.add(btnUsuarios);
        panelSidebar.add(btnVehiculos);
        panelSidebar.add(Box.createVerticalGlue());
    }

    private void construirContenido(Color fondoGeneral, Color fondoPanel, Color grisCampo,
                                    Color verdeRol, Color turquesaEstado, Color azulBoton,
                                    Color rojoBoton, Color turquesaGuardar) {
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(fondoGeneral);
        panelContenido.setBorder(new EmptyBorder(14, 26, 18, 30));

        JPanel panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        String tituloTexto = usuarioEditar == null ? "Registrar Usuario" : "Edición de Usuarios";
        JLabel lblTitulo = new JLabel(tituloTexto);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(70, 70, 70));
        lblTitulo.setBorder(new EmptyBorder(0, 10, 14, 0));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(fondoPanel);
        panelFormulario.setBorder(new EmptyBorder(26, 30, 24, 30));
        panelFormulario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 430));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(9, 0, 9, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblId = new JLabel("ID:");
        lblId.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblIdValor = new JLabel("");
        lblIdValor.setFont(new Font("SansSerif", Font.BOLD, 22));

        JPanel panelId = new JPanel();
        panelId.setOpaque(false);
        panelId.setLayout(new BoxLayout(panelId, BoxLayout.X_AXIS));
        panelId.add(lblId);
        panelId.add(Box.createHorizontalStrut(8));
        panelId.add(lblIdValor);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        panelFormulario.add(panelId, gbc);

        JPanel linea = new JPanel();
        linea.setBackground(new Color(180, 180, 180));
        linea.setPreferredSize(new Dimension(100, 1));
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 18, 0);
        panelFormulario.add(linea, gbc);

        gbc.gridwidth = 1; gbc.insets = new Insets(10, 0, 10, 0);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.30;
        panelFormulario.add(lblUsuario, gbc);

        txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(320, 34));
        txtUsuario.setBackground(grisCampo);
        txtUsuario.setFont(new Font("SansSerif", Font.PLAIN, 15));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(170, 170, 170), 1),
                new EmptyBorder(6, 10, 6, 10)));
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.50;
        panelFormulario.add(txtUsuario, gbc);

        JLabel lblNombre = new JLabel("Nombre Completo:");
        lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.30;
        panelFormulario.add(lblNombre, gbc);

        txtNombreCompleto = new JTextField();
        txtNombreCompleto.setPreferredSize(new Dimension(320, 34));
        txtNombreCompleto.setBackground(new Color(240, 240, 240));
        txtNombreCompleto.setFont(new Font("SansSerif", Font.PLAIN, 15));
        txtNombreCompleto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(170, 170, 170), 1),
                new EmptyBorder(6, 10, 6, 10)));
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.50;
        panelFormulario.add(txtNombreCompleto, gbc);

        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.30;
        panelFormulario.add(lblRol, gbc);

        cmbRol = new JComboBox<>(new String[]{"Administrador", "Despachador", "Conductor"});
        cmbRol.setBackground(verdeRol);
        cmbRol.setFont(new Font("SansSerif", Font.PLAIN, 15));
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 0.50;
        panelFormulario.add(cmbRol, gbc);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.30;
        panelFormulario.add(lblEstado, gbc);

        cmbEstado = new JComboBox<>(new String[]{"activo", "inactivo"});
        cmbEstado.setBackground(turquesaEstado);
        cmbEstado.setFont(new Font("SansSerif", Font.PLAIN, 15));
        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 0.50;
        panelFormulario.add(cmbEstado, gbc);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0.30;
        panelFormulario.add(lblContrasena, gbc);

        txtContrasena = new JTextField();
        txtContrasena.setPreferredSize(new Dimension(320, 34));
        txtContrasena.setBackground(grisCampo);
        txtContrasena.setFont(new Font("SansSerif", Font.PLAIN, 15));
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(170, 170, 170), 1),
                new EmptyBorder(6, 10, 6, 10)));
        txtContrasena.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 6; gbc.weightx = 0.50;
        panelFormulario.add(txtContrasena, gbc);

        btnCambiarClave = new JButton("Cambiar clave");
        btnCambiarClave.setBackground(azulBoton);
        btnCambiarClave.setForeground(Color.WHITE);
        btnCambiarClave.setFocusPainted(false);
        btnCambiarClave.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnCambiarClave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCambiarClave.addActionListener(e -> cambiarClave());
        gbc.gridx = 2; gbc.gridy = 6; gbc.weightx = 0.20;
        gbc.insets = new Insets(10, 18, 10, 0);
        panelFormulario.add(btnCambiarClave, gbc);

        gbc.insets = new Insets(28, 0, 0, 0);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(turquesaGuardar);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> guardarCambios());
        gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0;
        panelFormulario.add(btnGuardar, gbc);

        JPanel panelDerechoBotones = new JPanel();
        panelDerechoBotones.setOpaque(false);
        panelDerechoBotones.setLayout(new BoxLayout(panelDerechoBotones, BoxLayout.X_AXIS));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(azulBoton);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> cancelarEdicion());

        btnBorrar = new JButton("Borrar");
        btnBorrar.setBackground(rojoBoton);
        btnBorrar.setForeground(Color.WHITE);
        btnBorrar.setFocusPainted(false);
        btnBorrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnBorrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBorrar.addActionListener(e -> borrarUsuario());

        panelDerechoBotones.add(btnCancelar);
        panelDerechoBotones.add(Box.createHorizontalStrut(10));
        panelDerechoBotones.add(btnBorrar);

        gbc.gridx = 1; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panelFormulario.add(panelDerechoBotones, gbc);

        panelCentral.add(lblTitulo);
        panelCentral.add(panelFormulario);

        panelContenido.add(panelCentral, BorderLayout.CENTER);
    }

    private JButton crearBotonMenu(String texto, Color fondo, Color letra) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(LEFT_ALIGNMENT);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        boton.setPreferredSize(new Dimension(230, 46));
        boton.setBackground(fondo);
        boton.setForeground(letra);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(new EmptyBorder(0, 28, 0, 0));
        return boton;
    }

    private void cargarDatosUsuario() {
        lblIdValor.setText(String.valueOf(usuarioEditar.getId()));
        txtUsuario.setText(usuarioEditar.getUsername());
        txtNombreCompleto.setText(usuarioEditar.getNombre());
        cmbRol.setSelectedItem(capitalizar(usuarioEditar.getRol().name()));
        cmbEstado.setSelectedItem(usuarioEditar.getEstado());
        txtContrasena.setText("●●●●●●●●");
        txtContrasena.setEditable(false);
    }

    private String capitalizar(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.charAt(0) + s.substring(1).toLowerCase();
    }

    private void guardarCambios() {
        String username = txtUsuario.getText().trim();
        String nombre = txtNombreCompleto.getText().trim();

        if (username.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y nombre completo son obligatorios.");
            return;
        }

        String rolStr = cmbRol.getSelectedItem().toString();
        String estado = cmbEstado.getSelectedItem().toString();

        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            String resp;
            if (usuarioEditar == null) {
                // Modo creación: el servidor hace el hash SHA-256
                String clave = txtContrasena.getText().trim();
                if (clave.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debe ingresar una contraseña para el nuevo usuario.");
                    return;
                }
                // CREATE_USUARIO|nombre|username|password|rol|estado
                resp = cs.enviarYEsperar("CREATE_USUARIO|" + nombre + "|" + username + "|" + clave + "|" + rolStr + "|" + estado);
            } else {
                // Modo edición: password vacío = no cambiar la clave existente
                String clave = txtContrasena.isEditable() ? txtContrasena.getText().trim() : "";
                // UPDATE_USUARIO|id|nombre|username|password|rol|estado
                resp = cs.enviarYEsperar("UPDATE_USUARIO|" + usuarioEditar.getId() + "|" + nombre + "|" + username + "|" + clave + "|" + rolStr + "|" + estado);
            }
            if (resp != null && resp.startsWith("OK")) {
                String msg = usuarioEditar == null ? "Usuario registrado correctamente." : "Cambios guardados correctamente.";
                JOptionPane.showMessageDialog(this, msg);
                dispose();
                SwingUtilities.invokeLater(() -> new GestionDeUsuariosFrame(usuarioActual).setVisible(true));
            } else {
                String msg = (resp != null && resp.contains("|")) ? resp.split("\\|", 2)[1] : "No se pudo guardar";
                JOptionPane.showMessageDialog(this, "Error: " + msg);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    private void cambiarClave() {
        String nuevaClave = JOptionPane.showInputDialog(this, "Ingrese la nueva clave:");
        if (nuevaClave == null) return;
        nuevaClave = nuevaClave.trim();
        if (nuevaClave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La clave no puede ir vacía.");
            return;
        }
        txtContrasena.setText(nuevaClave);
        txtContrasena.setEditable(true);
        JOptionPane.showMessageDialog(this, "Clave lista. Presione Guardar para confirmar.");
    }

    private void cancelarEdicion() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea cancelar?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            if (usuarioEditar != null) {
                cargarDatosUsuario();
            } else {
                txtUsuario.setText("");
                txtNombreCompleto.setText("");
                txtContrasena.setText("");
                cmbRol.setSelectedIndex(0);
                cmbEstado.setSelectedIndex(0);
            }
        }
    }

    private void borrarUsuario() {
        if (usuarioEditar == null) return;
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea borrar al usuario " + usuarioEditar.getUsername() + "?",
                "Confirmar borrado", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            try {
                String resp = ConexionServidor.getInstancia().enviarYEsperar("DELETE_USUARIO|" + usuarioEditar.getId());
                if (resp != null && resp.startsWith("OK")) {
                    JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
                    dispose();
                    SwingUtilities.invokeLater(() -> new GestionDeUsuariosFrame(usuarioActual).setVisible(true));
                } else {
                    String msg = (resp != null && resp.contains("|")) ? resp.split("\\|", 2)[1] : "Error al eliminar";
                    JOptionPane.showMessageDialog(this, "Error: " + msg);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
            }
        }
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea cerrar sesión?", "QuickDelivery", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }

    private void cargarLogoHeader() {
        URL rutaImagen = getClass().getResource("/images/logo_quickdelivery_blanco.png");
        if (rutaImagen != null) {
            ImageIcon iconoOriginal = new ImageIcon(rutaImagen);
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(145, 70, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(imagenEscalada));
            lblLogo.setText("");
        } else {
            lblLogo.setText("QUICKDELIVERY S.A.");
            lblLogo.setForeground(Color.WHITE);
            lblLogo.setFont(new Font("SansSerif", Font.BOLD, 24));
        }
    }
}
