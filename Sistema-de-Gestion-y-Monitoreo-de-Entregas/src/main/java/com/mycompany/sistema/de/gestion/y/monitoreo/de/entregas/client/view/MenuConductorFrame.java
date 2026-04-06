package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.client.view;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.client.util.ConexionServidor;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Usuario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Dashboard principal del Conductor.
 *
 * @author Eryan / QuickDelivery S.A.
 */
public class MenuConductorFrame extends JFrame {

    private JPanel panelHeader;
    private JPanel panelSidebar;
    private JPanel panelContenido;

    private JLabel lblLogo;
    private JLabel lblUsuarioHeader;
    private JButton btnCerrarSesion;

    private JButton btnDashboard;
    private JButton btnMisPaquetes;
    private JButton btnIncidencias;

    private JLabel lblPlaca;
    private JLabel lblEstadoVehiculo;
    private JLabel lblVehiculoAsignado;
    private JLabel lblConductorAsignado;
    private JButton btnEnviarUbicacion;

    private JLabel lblIncidenciasActivas;
    private JLabel lblPaquetesEntregados;

    private JTable tablaPaquetes;
    private DefaultTableModel modeloTablaPaquetes;

    private JComboBox<String> cmbEstadoPaquete;
    private JButton btnGuardarEstado;

    private final Usuario usuarioActual;
    private List<String[]> listaPaquetes = new ArrayList<>();

    public MenuConductorFrame(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("Menu – Conductor");
        initComponents();
        configurarVentana();
        setLocationRelativeTo(null);
        registrarConductor();
        cargarDatos();
    }

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 760);
        setMinimumSize(new Dimension(1100, 700));
    }

    private void initComponents() {
        Color azulHeader  = new Color(20, 37, 160);
        Color turquesaMenu = new Color(16, 166, 171);
        Color turquesaOscuro = new Color(0, 101, 103);
        Color fondoGeneral = new Color(231, 231, 231);
        Color fondoPanel   = Color.WHITE;
        Color bordePanel   = new Color(190, 190, 190);
        Color azulTarjeta  = new Color(39, 73, 221);
        Color rojoTarjeta  = new Color(255, 16, 5);
        Color turquesaBoton = new Color(16, 166, 171);
        Color grisCombo    = new Color(212, 212, 212);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(fondoGeneral);

        construirHeader(azulHeader);
        construirSidebar(turquesaMenu, turquesaOscuro);
        construirContenido(fondoGeneral, fondoPanel, bordePanel, azulTarjeta, rojoTarjeta, turquesaBoton, grisCombo);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelSidebar, BorderLayout.WEST);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        setContentPane(panelPrincipal);
    }

    private void construirHeader(Color azulHeader) {
        panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(azulHeader);
        panelHeader.setPreferredSize(new Dimension(0, 95));
        panelHeader.setBorder(new EmptyBorder(10, 18, 10, 25));

        lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.LEFT);
        cargarLogoHeader();

        JPanel panelDerecho = new JPanel();
        panelDerecho.setOpaque(false);
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.X_AXIS));

        lblUsuarioHeader = new JLabel(usuarioActual.getNombre());
        lblUsuarioHeader.setForeground(Color.WHITE);
        lblUsuarioHeader.setFont(new Font("SansSerif", Font.PLAIN, 20));

        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setBackground(azulHeader);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setContentAreaFilled(false);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        panelDerecho.add(lblUsuarioHeader);
        panelDerecho.add(Box.createHorizontalStrut(35));
        panelDerecho.add(btnCerrarSesion);

        panelHeader.add(lblLogo, BorderLayout.WEST);
        panelHeader.add(panelDerecho, BorderLayout.EAST);
    }

    private void construirSidebar(Color turquesaMenu, Color turquesaOscuro) {
        panelSidebar = new JPanel();
        panelSidebar.setBackground(turquesaMenu);
        panelSidebar.setPreferredSize(new Dimension(210, 0));
        panelSidebar.setLayout(new BoxLayout(panelSidebar, BoxLayout.Y_AXIS));

        JPanel franja = new JPanel();
        franja.setBackground(new Color(86, 208, 207));
        franja.setMaximumSize(new Dimension(Integer.MAX_VALUE, 14));
        franja.setPreferredSize(new Dimension(210, 14));

        btnDashboard  = crearBotonMenu("Dashboard",   turquesaOscuro, Color.WHITE);
        btnMisPaquetes= crearBotonMenu("Mis Paquetes", turquesaMenu, Color.WHITE);
        btnIncidencias= crearBotonMenu("Incidencias",  turquesaMenu, Color.WHITE);

        btnDashboard.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ya estás en Dashboard."));
        btnMisPaquetes.addActionListener(e -> { dispose(); SwingUtilities.invokeLater(() -> new MisPaquetesFrame(usuarioActual).setVisible(true)); });
        btnIncidencias.addActionListener(e -> { dispose(); SwingUtilities.invokeLater(() -> new IncidenciasFrame(usuarioActual).setVisible(true)); });

        panelSidebar.add(franja);
        panelSidebar.add(Box.createVerticalStrut(10));
        panelSidebar.add(btnDashboard);
        panelSidebar.add(btnMisPaquetes);
        panelSidebar.add(btnIncidencias);
        panelSidebar.add(Box.createVerticalGlue());
    }

    private void construirContenido(Color fondoGeneral, Color fondoPanel, Color bordePanel,
                                    Color azulTarjeta, Color rojoTarjeta, Color turquesaBoton, Color grisCombo) {
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(fondoGeneral);
        panelContenido.setBorder(new EmptyBorder(18, 26, 18, 26));

        JPanel panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        JPanel panelSuperior = new JPanel(new BorderLayout(14, 0));
        panelSuperior.setOpaque(false);
        panelSuperior.setMaximumSize(new Dimension(Integer.MAX_VALUE, 195));

        panelSuperior.add(crearPanelVehiculo(fondoPanel, bordePanel, azulTarjeta, turquesaBoton), BorderLayout.CENTER);
        panelSuperior.add(crearPanelResumen(rojoTarjeta, azulTarjeta), BorderLayout.EAST);

        JPanel panelTabla  = crearPanelTablaPaquetes(fondoPanel, bordePanel);
        JPanel panelEstado = crearPanelActualizarEstado(fondoPanel, bordePanel, grisCombo, turquesaBoton);

        panelCentral.add(panelSuperior);
        panelCentral.add(Box.createVerticalStrut(18));
        panelCentral.add(panelTabla);
        panelCentral.add(Box.createVerticalStrut(14));
        panelCentral.add(panelEstado);

        panelContenido.add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel crearPanelVehiculo(Color fondoPanel, Color bordePanel, Color azulTarjeta, Color turquesaBoton) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(fondoPanel);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordePanel, 1),
                new EmptyBorder(0, 0, 16, 0)));

        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(azulTarjeta);
        barra.setPreferredSize(new Dimension(0, 38));
        barra.setBorder(new EmptyBorder(0, 22, 0, 22));

        lblPlaca = new JLabel("—");
        lblPlaca.setForeground(Color.WHITE);
        lblPlaca.setFont(new Font("SansSerif", Font.BOLD, 18));

        lblEstadoVehiculo = new JLabel("");
        lblEstadoVehiculo.setForeground(Color.WHITE);
        lblEstadoVehiculo.setFont(new Font("SansSerif", Font.BOLD, 18));

        barra.add(lblPlaca, BorderLayout.WEST);
        barra.add(lblEstadoVehiculo, BorderLayout.CENTER);

        JPanel contenido = new JPanel();
        contenido.setOpaque(false);
        contenido.setBorder(new EmptyBorder(16, 22, 0, 22));
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));

        lblVehiculoAsignado = new JLabel("Vehículo asignado: —");
        lblVehiculoAsignado.setFont(new Font("SansSerif", Font.PLAIN, 16));

        lblConductorAsignado = new JLabel("Conductor: " + usuarioActual.getNombre());
        lblConductorAsignado.setFont(new Font("SansSerif", Font.PLAIN, 16));

        btnEnviarUbicacion = new JButton("Enviar ubicación");
        btnEnviarUbicacion.setBackground(turquesaBoton);
        btnEnviarUbicacion.setForeground(Color.WHITE);
        btnEnviarUbicacion.setFocusPainted(false);
        btnEnviarUbicacion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEnviarUbicacion.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnEnviarUbicacion.addActionListener(e -> enviarUbicacion());

        contenido.add(lblVehiculoAsignado);
        contenido.add(Box.createVerticalStrut(24));
        contenido.add(lblConductorAsignado);
        contenido.add(Box.createVerticalStrut(42));
        contenido.add(btnEnviarUbicacion);

        panel.add(barra, BorderLayout.NORTH);
        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelResumen(Color rojoTarjeta, Color azulTarjeta) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 8));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(120, 0));

        lblIncidenciasActivas = new JLabel("...");
        lblPaquetesEntregados = new JLabel("...");

        panel.add(crearTarjetaResumen("Incidencias\nActivas", lblIncidenciasActivas, rojoTarjeta));
        panel.add(crearTarjetaResumen("Paquetes\nEntregados", lblPaquetesEntregados, azulTarjeta));
        return panel;
    }

    private JPanel crearTarjetaResumen(String titulo, JLabel valor, Color fondo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(fondo);
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));
        JLabel lblTitulo = new JLabel("<html><center>" + titulo.replace("\n", "<br>") + "</center></html>");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        valor.setForeground(Color.WHITE);
        valor.setFont(new Font("SansSerif", Font.BOLD, 42));
        valor.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(valor, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelTablaPaquetes(Color fondoPanel, Color bordePanel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(fondoPanel);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordePanel, 1),
                new EmptyBorder(14, 16, 18, 16)));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        JLabel titulo = new JLabel("Mis paquetes asignados");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setBorder(new EmptyBorder(0, 0, 12, 0));

        String[] columnas = {"ID", "Código", "Estado", "Descripción"};
        modeloTablaPaquetes = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaPaquetes = new JTable(modeloTablaPaquetes);
        tablaPaquetes.setRowHeight(32);
        tablaPaquetes.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaPaquetes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tablaPaquetes.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tablaPaquetes);
        scroll.setBorder(BorderFactory.createLineBorder(bordePanel, 1));

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelActualizarEstado(Color fondoPanel, Color bordePanel, Color grisCombo, Color turquesaBoton) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(fondoPanel);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordePanel, 1),
                new EmptyBorder(10, 16, 10, 16)));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        JLabel titulo = new JLabel("Actualizar estado");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setBorder(new EmptyBorder(0, 0, 8, 0));

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 0;

        cmbEstadoPaquete = new JComboBox<>(new String[]{
            "Seleccione el estado actualizado", "En tránsito", "Entregado", "Incidencia", "En Espera"});
        cmbEstadoPaquete.setBackground(grisCombo);
        cmbEstadoPaquete.setFont(new Font("SansSerif", Font.PLAIN, 16));
        centro.add(cmbEstadoPaquete, gbc);

        gbc.gridx = 1; gbc.weightx = 0;
        btnGuardarEstado = new JButton("Guardar");
        btnGuardarEstado.setBackground(turquesaBoton);
        btnGuardarEstado.setForeground(Color.WHITE);
        btnGuardarEstado.setFocusPainted(false);
        btnGuardarEstado.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardarEstado.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnGuardarEstado.addActionListener(e -> actualizarEstado());
        centro.add(btnGuardarEstado, gbc);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(centro, BorderLayout.CENTER);
        return panel;
    }

    private JButton crearBotonMenu(String texto, Color fondo, Color letra) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(LEFT_ALIGNMENT);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        boton.setPreferredSize(new Dimension(210, 48));
        boton.setBackground(fondo);
        boton.setForeground(letra);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(new EmptyBorder(0, 24, 0, 0));
        return boton;
    }

    private void registrarConductor() {
        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();
            cs.enviarYEsperar("LOGIN_CONDUCTOR|" + usuarioActual.getId()
                    + "|" + usuarioActual.getNombre());
        } catch (IOException ex) {
            // No bloquea el inicio si falla el registro
        }
    }

    private void enviarUbicacion() {
        String descripcion = JOptionPane.showInputDialog(this,
                "Ingrese su ubicación actual:", "Enviar ubicación",
                JOptionPane.PLAIN_MESSAGE);
        if (descripcion == null || descripcion.trim().isEmpty()) return;
        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();
            String resp = cs.enviarYEsperar("UBICACION|" + usuarioActual.getId()
                    + "|" + descripcion.trim());
            if (resp != null && resp.startsWith("OK")) {
                JOptionPane.showMessageDialog(this, "Ubicación enviada correctamente.",
                        "Conductor", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String msg = (resp != null && resp.contains("|")) ? resp.split("\\|", 2)[1] : "Error";
                JOptionPane.showMessageDialog(this, "Error: " + msg,
                        "Conductor", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatos() {
        listaPaquetes.clear();
        modeloTablaPaquetes.setRowCount(0);
        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();

            // Vehículo asignado al conductor
            String respV = cs.enviarYEsperar("GET_MI_VEHICULO");
            if (respV != null && respV.startsWith("DATA|")) {
                String[] partes = respV.split("\\|");
                if (partes.length >= 6 && !"NINGUNO".equals(partes[1])) {
                    // DATA|id|placa|tipo|capacidad|estado
                    String placa = partes[2];
                    String tipoRaw = partes[3]; // "Camion", "Moto", "Furgon"
                    String tipoDisplay = tipoRaw.equals("Camion") ? "Camión"
                            : tipoRaw.equals("Furgon") ? "Furgón" : tipoRaw;
                    String estadoDisplay = partes[5].replace("_", " ");
                    lblPlaca.setText(placa);
                    lblEstadoVehiculo.setText("  |  " + estadoDisplay);
                    lblVehiculoAsignado.setText("Vehículo asignado: " + placa + "  ·  " + tipoDisplay);
                } else {
                    lblPlaca.setText("Sin vehículo");
                    lblEstadoVehiculo.setText("");
                    lblVehiculoAsignado.setText("Vehículo asignado: —");
                }
            }

            // Paquetes en tránsito del conductor
            String respT = cs.enviarYEsperar("GET_MIS_PAQUETES|" + usuarioActual.getId());
            String respE = cs.enviarYEsperar("GET_PAQUETES_ESTADO|ENTREGADO");
            String respI = cs.enviarYEsperar("GET_PAQUETES_ESTADO|INCIDENCIA");

            lblPaquetesEntregados.setText(String.valueOf(contarFilas(respE, "PAQUETES")));
            lblIncidenciasActivas.setText(String.valueOf(contarFilas(respI, "PAQUETES")));

            // LIST|PAQUETES|id|codigo|descripcion|peso|estado~...
            if (respT != null && respT.startsWith("LIST|PAQUETES")) {
                String[] partes = respT.split("\\|", 3);
                if (partes.length == 3 && !partes[2].isEmpty()) {
                    for (String fila : partes[2].split("~")) {
                        if (fila.startsWith("|")) fila = fila.substring(1);
                        String[] c = fila.split("\\|");
                        if (c.length < 5) continue;
                        listaPaquetes.add(c);
                        modeloTablaPaquetes.addRow(new Object[]{c[0], c[1], "En tránsito", c[2]});
                    }
                }
            }
        } catch (IOException ex) {
            // Conexión no disponible: tablas vacías
        }
    }

    private int contarFilas(String resp, String tipo) {
        if (resp == null || !resp.startsWith("LIST|" + tipo)) return 0;
        String[] p = resp.split("\\|", 3);
        if (p.length < 3 || p[2].isEmpty()) return 0;
        return p[2].split("~").length;
    }

    private void actualizarEstado() {
        int fila = tablaPaquetes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paquete en la tabla.");
            return;
        }
        String estadoStr = cmbEstadoPaquete.getSelectedItem().toString();
        if (estadoStr.equals("Seleccione el estado actualizado")) {
            JOptionPane.showMessageDialog(this, "Seleccione un estado válido.");
            return;
        }

        String idStr = modeloTablaPaquetes.getValueAt(fila, 0).toString();
        String nuevoEstado;
        switch (estadoStr) {
            case "En tránsito": nuevoEstado = "EN_TRANSITO"; break;
            case "Entregado":   nuevoEstado = "ENTREGADO";   break;
            case "Incidencia":  nuevoEstado = "INCIDENCIA";  break;
            case "En Espera":   nuevoEstado = "EN_ESPERA";   break;
            default:            nuevoEstado = "EN_TRANSITO"; break;
        }

        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();
            String resp = cs.enviarYEsperar("ESTADO|" + idStr + "|" + nuevoEstado);
            if (resp != null && resp.startsWith("OK")) {
                JOptionPane.showMessageDialog(this, "Estado actualizado a: " + estadoStr,
                        "Conductor", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            } else {
                String msg = (resp != null && resp.contains("|")) ? resp.split("\\|", 2)[1] : "Sin respuesta";
                JOptionPane.showMessageDialog(this, "Error: " + msg, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?", "QuickDelivery", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }

    private void cargarLogoHeader() {
        URL rutaImagen = getClass().getResource("/images/logo_quickdelivery_blanco.png");
        if (rutaImagen != null) {
            ImageIcon iconoOriginal = new ImageIcon(rutaImagen);
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(140, 72, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(imagenEscalada));
            lblLogo.setText("");
        } else {
            lblLogo.setText("QUICKDELIVERY S.A.");
            lblLogo.setForeground(Color.WHITE);
            lblLogo.setFont(new Font("SansSerif", Font.BOLD, 24));
        }
    }
}
