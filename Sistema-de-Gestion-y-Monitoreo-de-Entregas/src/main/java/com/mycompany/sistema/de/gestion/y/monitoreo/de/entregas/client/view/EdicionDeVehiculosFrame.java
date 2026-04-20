package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.client.view;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.client.util.ConexionServidor;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.EstadoVehiculo;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Usuario;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Vehiculo;

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
import java.util.ArrayList;
import java.util.List;
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
 * Pantalla de edición y creación de vehículos del sistema QuickDelivery S.A.
 *
 * @author Eryan / QuickDelivery S.A.
 */
public class EdicionDeVehiculosFrame extends JFrame {

    private JPanel panelHeader;
    private JPanel panelSidebar;
    private JPanel panelContenido;

    private JLabel lblLogo;
    private JLabel lblUsuarioHeader;
    private JButton btnCerrarSesion;

    private JButton btnDashboard;
    private JButton btnUsuarios;
    private JButton btnVehiculos;

    private JLabel lblPlacaTitulo;
    private JComboBox<String> cmbTipoVehiculo;
    private JComboBox<String> cmbConductor;
    private JTextField txtCapacidad;
    private JComboBox<String> cmbEstado;
    private JTextField txtPlaca;

    /** IDs de conductores paralelos a los ítems de cmbConductor; índice 0 = sin conductor (id=0). */
    private final List<Integer> conductorIds = new ArrayList<>();

    private JButton btnCambiarPlaca;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton btnBorrar;

    private final Vehiculo vehiculoEditar;
    private final Usuario usuarioActual;

    public EdicionDeVehiculosFrame(Vehiculo vehiculoEditar, Usuario usuarioActual) {
        this.vehiculoEditar = vehiculoEditar;
        this.usuarioActual = usuarioActual;
        setTitle(vehiculoEditar == null ? "Registrar Vehículo" : "Edición de Vehículos");

        Color azulHeader = new Color(24, 40, 159);
        Color turquesaMenu = new Color(55, 165, 168);
        Color turquesaOscuro = new Color(30, 98, 97);
        Color fondoGeneral = new Color(230, 230, 230);
        Color fondoPanel = new Color(245, 245, 245);
        Color grisCampo = new Color(213, 213, 213);
        Color turquesaEstado = new Color(98, 204, 208);
        Color azulBoton = new Color(24, 40, 159);
        Color rojoBoton = new Color(255, 16, 5);
        Color turquesaGuardar = new Color(55, 165, 168);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(fondoGeneral);

        construirHeader(azulHeader);
        construirSidebar(turquesaMenu, turquesaOscuro);
        construirContenido(fondoGeneral, fondoPanel, grisCampo, turquesaEstado,
                azulBoton, rojoBoton, turquesaGuardar);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelSidebar, BorderLayout.WEST);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 760);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);

        cargarConductores();

        if (vehiculoEditar != null) {
            cargarDatosVehiculo();
            cmbTipoVehiculo.setEnabled(false);
        } else {
            lblPlacaTitulo.setText("Vehículo nuevo");
            btnBorrar.setEnabled(false);
        }
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
        btnUsuarios  = crearBotonMenu("Usuarios",  turquesaMenu, Color.WHITE);
        btnVehiculos = crearBotonMenu("Vehículos", turquesaOscuro, Color.WHITE);

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
            Color turquesaEstado, Color azulBoton, Color rojoBoton, Color turquesaGuardar) {

        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(fondoGeneral);
        panelContenido.setBorder(new EmptyBorder(14, 26, 18, 30));

        JPanel panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Edición / Creación de Vehículos");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(70, 70, 70));
        lblTitulo.setBorder(new EmptyBorder(0, 10, 14, 0));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(fondoPanel);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 190, 190), 1),
                new EmptyBorder(26, 30, 24, 30)));
        panelFormulario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 430));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(9, 0, 9, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblPlacaTitulo = new JLabel("Placa: ---");
        lblPlacaTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        panelFormulario.add(lblPlacaTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(12, 0, 12, 0);

        JLabel lblTipoVehiculo = new JLabel("Tipo de Vehículo:");
        lblTipoVehiculo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.30;
        panelFormulario.add(lblTipoVehiculo, gbc);

        cmbTipoVehiculo = new JComboBox<>(new String[]{"Camión", "Moto", "Furgón"});
        cmbTipoVehiculo.setBackground(grisCampo);
        cmbTipoVehiculo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.50;
        panelFormulario.add(cmbTipoVehiculo, gbc);

        JLabel lblConductor = new JLabel("Conductor:");
        lblConductor.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.30;
        panelFormulario.add(lblConductor, gbc);

        cmbConductor = new JComboBox<>();
        cmbConductor.setBackground(grisCampo);
        cmbConductor.setFont(new Font("SansSerif", Font.PLAIN, 15));
        cmbConductor.setPreferredSize(new Dimension(320, 34));
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.50;
        panelFormulario.add(cmbConductor, gbc);

        JLabel lblCapacidad = new JLabel("<html>Capacidad<br>(kg):</html>");
        lblCapacidad.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.30;
        panelFormulario.add(lblCapacidad, gbc);

        txtCapacidad = new JTextField();
        txtCapacidad.setPreferredSize(new Dimension(320, 34));
        txtCapacidad.setBackground(grisCampo);
        txtCapacidad.setFont(new Font("SansSerif", Font.PLAIN, 15));
        txtCapacidad.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(170, 170, 170), 1),
                new EmptyBorder(6, 10, 6, 10)));
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.50;
        panelFormulario.add(txtCapacidad, gbc);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.30;
        panelFormulario.add(lblEstado, gbc);

        cmbEstado = new JComboBox<>(new String[]{"Disponible", "En Ruta", "Mantenimiento"});
        cmbEstado.setBackground(turquesaEstado);
        cmbEstado.setFont(new Font("SansSerif", Font.PLAIN, 15));
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 0.50;
        panelFormulario.add(cmbEstado, gbc);

        JLabel lblPlaca = new JLabel("Placa:");
        lblPlaca.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.30;
        panelFormulario.add(lblPlaca, gbc);

        txtPlaca = new JTextField();
        txtPlaca.setPreferredSize(new Dimension(320, 34));
        txtPlaca.setBackground(grisCampo);
        txtPlaca.setFont(new Font("SansSerif", Font.PLAIN, 15));
        txtPlaca.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(170, 170, 170), 1),
                new EmptyBorder(6, 10, 6, 10)));
        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 0.50;
        panelFormulario.add(txtPlaca, gbc);

        btnCambiarPlaca = new JButton("Cambiar placa");
        btnCambiarPlaca.setBackground(azulBoton);
        btnCambiarPlaca.setForeground(Color.WHITE);
        btnCambiarPlaca.setFocusPainted(false);
        btnCambiarPlaca.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnCambiarPlaca.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCambiarPlaca.addActionListener(e -> cambiarPlaca());
        gbc.gridx = 2; gbc.gridy = 5; gbc.weightx = 0.20;
        gbc.insets = new Insets(12, 18, 12, 0);
        panelFormulario.add(btnCambiarPlaca, gbc);

        gbc.insets = new Insets(28, 0, 0, 0);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(turquesaGuardar);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> guardarCambios());
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0;
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
        btnBorrar.addActionListener(e -> borrarVehiculo());

        panelDerechoBotones.add(btnCancelar);
        panelDerechoBotones.add(Box.createHorizontalStrut(10));
        panelDerechoBotones.add(btnBorrar);

        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 2;
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

    private void cargarConductores() {
        conductorIds.clear();
        cmbConductor.removeAllItems();
        // Opción vacía: sin conductor asignado
        conductorIds.add(0);
        cmbConductor.addItem("(Sin conductor)");
        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();
            String respuesta = cs.enviarYEsperar("GET_CONDUCTORES");
            if (respuesta == null || !respuesta.startsWith("LIST")) return;
            // LIST|CONDUCTORES|id|nombre~...
            String[] partes = respuesta.split("\\|", 3);
            if (partes.length < 3 || partes[2].isEmpty()) return;
            for (String fila : partes[2].split("~")) {
                if (fila.startsWith("|")) fila = fila.substring(1);
                String[] c = fila.split("\\|");
                if (c.length < 2) continue;
                conductorIds.add(Integer.parseInt(c[0]));
                cmbConductor.addItem(c[1]);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar conductores: " + ex.getMessage());
        }
    }

    private void cargarDatosVehiculo() {
        String tipo;
        switch (vehiculoEditar.getClass().getSimpleName()) {
            case "Camion": tipo = "Camión"; break;
            case "Moto":   tipo = "Moto";   break;
            default:       tipo = "Furgón"; break;
        }
        lblPlacaTitulo.setText("Placa: " + vehiculoEditar.getPlaca());
        cmbTipoVehiculo.setSelectedItem(tipo);
        txtCapacidad.setText(String.valueOf(vehiculoEditar.getCapacidadMaxima()));
        cmbEstado.setSelectedItem(estadoADisplay(vehiculoEditar.getEstado()));
        txtPlaca.setText(vehiculoEditar.getPlaca());

        // Seleccionar el conductor ya asignado
        int idConductorActual = vehiculoEditar.getIdConductor();
        int indexSeleccionado = conductorIds.indexOf(idConductorActual);
        cmbConductor.setSelectedIndex(indexSeleccionado >= 0 ? indexSeleccionado : 0);
    }

    private String estadoADisplay(EstadoVehiculo estado) {
        switch (estado) {
            case EN_RUTA:  return "En Ruta";
            case INACTIVO: return "Mantenimiento";
            default:       return "Disponible";
        }
    }

    private EstadoVehiculo displayAEstado(String display) {
        switch (display) {
            case "En Ruta":      return EstadoVehiculo.EN_RUTA;
            case "Mantenimiento": return EstadoVehiculo.INACTIVO;
            default:             return EstadoVehiculo.DISPONIBLE;
        }
    }

    private void guardarCambios() {
        String placa    = txtPlaca.getText().trim();
        String capStr   = txtCapacidad.getText().trim();
        String estadoStr = cmbEstado.getSelectedItem().toString();

        if (placa.isEmpty() || capStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Placa y capacidad son obligatorias.");
            return;
        }

        double capacidad;
        try {
            capacidad = Double.parseDouble(capStr);
            if (capacidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La capacidad debe ser un número positivo.");
            return;
        }

        String estadoDb = displayAEstado(estadoStr).toDbString();
        int idConductorSeleccionado = conductorIds.get(
                Math.max(0, cmbConductor.getSelectedIndex()));

        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            String resp;
            if (vehiculoEditar == null) {
                // CREATE_VEHICULO|placa|tipo|capacidad|estado|idConductor
                String tipoDisplay = cmbTipoVehiculo.getSelectedItem().toString();
                String tipoServidor = tipoDisplay.equals("Camión") ? "Camion"
                        : tipoDisplay.equals("Furgón") ? "Furgon" : "Moto";
                resp = cs.enviarYEsperar("CREATE_VEHICULO|" + placa + "|" + tipoServidor + "|"
                        + capacidad + "|" + estadoDb + "|" + idConductorSeleccionado);
            } else {
                // UPDATE_VEHICULO|id|placa|tipo|capacidad|estado|idConductor
                String tipoServidor = vehiculoEditar.getClass().getSimpleName();
                resp = cs.enviarYEsperar("UPDATE_VEHICULO|" + vehiculoEditar.getId() + "|"
                        + placa + "|" + tipoServidor + "|" + capacidad + "|" + estadoDb
                        + "|" + idConductorSeleccionado);
            }
            if (resp != null && resp.startsWith("OK")) {
                String msg = vehiculoEditar == null ? "Vehículo registrado correctamente." : "Vehículo actualizado correctamente.";
                JOptionPane.showMessageDialog(this, msg);
                dispose();
                SwingUtilities.invokeLater(() -> new GestionDeVehiculosFrame(usuarioActual).setVisible(true));
            } else {
                String msg = (resp != null && resp.contains("|")) ? resp.split("\\|", 2)[1] : "No se pudo guardar";
                JOptionPane.showMessageDialog(this, "Error: " + msg);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    private void cambiarPlaca() {
        String nuevaPlaca = JOptionPane.showInputDialog(this, "Ingrese la nueva placa:");
        if (nuevaPlaca == null) return;
        nuevaPlaca = nuevaPlaca.trim();
        if (nuevaPlaca.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La placa no puede ir vacía.");
            return;
        }
        txtPlaca.setText(nuevaPlaca);
        lblPlacaTitulo.setText("Placa: " + nuevaPlaca);
    }

    private void cancelarEdicion() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea cancelar?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> new GestionDeVehiculosFrame(usuarioActual).setVisible(true));
        }
    }

    private void borrarVehiculo() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea borrar este vehículo?", "Confirmar borrado", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            try {
                String resp = ConexionServidor.getInstancia().enviarYEsperar("DELETE_VEHICULO|" + vehiculoEditar.getId());
                if (resp != null && resp.startsWith("OK")) {
                    JOptionPane.showMessageDialog(this, "Vehículo eliminado correctamente.");
                    dispose();
                    SwingUtilities.invokeLater(() -> new GestionDeVehiculosFrame(usuarioActual).setVisible(true));
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
        URL rutaImagen = getClass().getResource("/imagenes/logo_quickdelivery.png");
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
