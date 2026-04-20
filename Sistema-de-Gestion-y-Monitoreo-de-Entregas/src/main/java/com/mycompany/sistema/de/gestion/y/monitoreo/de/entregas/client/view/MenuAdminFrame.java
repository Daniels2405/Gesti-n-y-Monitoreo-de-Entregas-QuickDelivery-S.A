package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.client.view;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.client.util.ConexionServidor;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Usuario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
 * Dashboard principal del Administrador.
 *
 * @author Eryan / QuickDelivery S.A.
 */
public class MenuAdminFrame extends JFrame {

    private JPanel panelHeader;
    private JPanel panelSidebar;
    private JPanel panelContenido;

    private JLabel lblLogo;
    private JLabel lblUsuario;
    private JButton btnCerrarSesion;

    private JButton btnDashboard;
    private JButton btnUsuarios;
    private JButton btnVehiculos;

    private JLabel lblTotalUsuarios;
    private JLabel lblVehiculosActivos;
    private JLabel lblPaquetesTrafico;
    private JLabel lblIncidencias;

    private JButton btnRegistrarUsuarios;
    private JButton btnConsultarUsuarios;
    private JButton btnRegistrarVehiculos;
    private JButton btnConsultarVehiculos;

    private JTable tablaActividades;
    private DefaultTableModel modeloTabla;

    private final Usuario usuarioActual;

    public MenuAdminFrame(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("Menu Para Administradores");

        Color azulHeader = new Color(24, 40, 159);
        Color turquesaMenu = new Color(55, 165, 168);
        Color turquesaOscuro = new Color(30, 98, 97);
        Color fondoGeneral = new Color(231, 231, 231);
        Color fondoPanel = Color.WHITE;
        Color colorTarjeta = new Color(58, 167, 169);
        Color colorBoton = new Color(24, 40, 159);
        Color colorTexto = new Color(35, 35, 35);
        Color colorBorde = new Color(190, 190, 190);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(fondoGeneral);

        construirHeader(azulHeader);
        construirSidebar(turquesaMenu, turquesaOscuro);
        construirContenido(fondoGeneral, fondoPanel, colorTarjeta, colorBoton, colorTexto, colorBorde);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelSidebar, BorderLayout.WEST);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 760);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);

        cargarResumen();
        cargarDatosTabla();
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

        lblUsuario = new JLabel(usuarioActual.getNombre());
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("SansSerif", Font.PLAIN, 20));

        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setBackground(azulHeader);
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setContentAreaFilled(false);
        btnCerrarSesion.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        panelDerechoHeader.add(lblUsuario);
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

        btnDashboard = crearBotonMenu("Dashboard", turquesaOscuro, Color.WHITE);
        btnUsuarios = crearBotonMenu("Usuarios", turquesaMenu, Color.WHITE);
        btnVehiculos = crearBotonMenu("Vehículos", turquesaMenu, Color.WHITE);

        btnDashboard.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ya estás en Dashboard."));
        btnUsuarios.addActionListener(e -> { dispose(); SwingUtilities.invokeLater(() -> new GestionDeUsuariosFrame(usuarioActual).setVisible(true)); });
        btnVehiculos.addActionListener(e -> { dispose(); SwingUtilities.invokeLater(() -> new GestionDeVehiculosFrame(usuarioActual).setVisible(true)); });

        panelSidebar.add(barraSuperior);
        panelSidebar.add(Box.createVerticalStrut(12));
        panelSidebar.add(btnDashboard);
        panelSidebar.add(btnUsuarios);
        panelSidebar.add(btnVehiculos);
        panelSidebar.add(Box.createVerticalGlue());
    }

    private void construirContenido(Color fondoGeneral, Color fondoPanel, Color colorTarjeta,
                                    Color colorBoton, Color colorTexto, Color colorBorde) {
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(fondoGeneral);
        panelContenido.setBorder(new EmptyBorder(24, 30, 24, 30));

        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(fondoGeneral);
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        JPanel panelTarjetas = new JPanel(new GridLayout(1, 4, 18, 0));
        panelTarjetas.setOpaque(false);
        panelTarjetas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        panelTarjetas.setPreferredSize(new Dimension(900, 110));

        lblTotalUsuarios = new JLabel("...");
        lblVehiculosActivos = new JLabel("...");
        lblPaquetesTrafico = new JLabel("...");
        lblIncidencias = new JLabel("...");

        panelTarjetas.add(crearTarjeta("Total de usuarios", lblTotalUsuarios, colorTarjeta));
        panelTarjetas.add(crearTarjeta("Vehículos Activos", lblVehiculosActivos, colorTarjeta));
        panelTarjetas.add(crearTarjeta("Paquetes en\ntráfico", lblPaquetesTrafico, colorTarjeta));
        panelTarjetas.add(crearTarjeta("Incidencias\nReportadas", lblIncidencias, colorTarjeta));

        JPanel panelGestion = new JPanel(new GridLayout(1, 2, 18, 0));
        panelGestion.setOpaque(false);
        panelGestion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 145));
        panelGestion.setPreferredSize(new Dimension(900, 145));

        btnRegistrarUsuarios = crearBotonAccion("Registrar\nUsuarios", colorBoton);
        btnConsultarUsuarios = crearBotonAccion("Consultar\nUsuarios", colorBoton);
        btnRegistrarVehiculos = crearBotonAccion("Registrar\nVehículos", colorBoton);
        btnConsultarVehiculos = crearBotonAccion("Consultar\nVehículos", colorBoton);

        btnRegistrarUsuarios.addActionListener(e -> { dispose(); SwingUtilities.invokeLater(() -> new EdicionDeUsuariosFrame(null, usuarioActual).setVisible(true)); });
        btnConsultarUsuarios.addActionListener(e -> { dispose(); SwingUtilities.invokeLater(() -> new GestionDeUsuariosFrame(usuarioActual).setVisible(true)); });
        btnRegistrarVehiculos.addActionListener(e -> { dispose(); SwingUtilities.invokeLater(() -> new EdicionDeVehiculosFrame(null, usuarioActual).setVisible(true)); });
        btnConsultarVehiculos.addActionListener(e -> { dispose(); SwingUtilities.invokeLater(() -> new GestionDeVehiculosFrame(usuarioActual).setVisible(true)); });

        panelGestion.add(crearPanelGestion("Gestión de Usuarios", btnRegistrarUsuarios, btnConsultarUsuarios, fondoPanel, colorBorde));
        panelGestion.add(crearPanelGestion("Gestión de Vehículos", btnRegistrarVehiculos, btnConsultarVehiculos, fondoPanel, colorBorde));

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(fondoPanel);
        panelTabla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 1),
                new EmptyBorder(14, 18, 18, 18)));
        panelTabla.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JLabel lblTituloTabla = new JLabel("Últimas Actividades");
        lblTituloTabla.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTituloTabla.setForeground(colorTexto);
        lblTituloTabla.setBorder(new EmptyBorder(0, 0, 14, 0));

        String[] columnas = {"Fecha", "Usuario", "Acción", "Detalle"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaActividades = new JTable(modeloTabla);
        tablaActividades.setRowHeight(36);
        tablaActividades.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaActividades.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tablaActividades.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tablaActividades);
        scroll.setBorder(BorderFactory.createLineBorder(colorBorde, 1));

        panelTabla.add(lblTituloTabla, BorderLayout.NORTH);
        panelTabla.add(scroll, BorderLayout.CENTER);

        panelCentral.add(panelTarjetas);
        panelCentral.add(Box.createVerticalStrut(18));
        panelCentral.add(panelGestion);
        panelCentral.add(Box.createVerticalStrut(20));
        panelCentral.add(panelTabla);

        panelContenido.add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel crearTarjeta(String titulo, JLabel lblValor, Color colorTarjeta) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(colorTarjeta);
        panel.setBorder(new EmptyBorder(10, 12, 10, 12));
        JLabel lblTitulo = new JLabel("<html>" + titulo.replace("\n", "<br>") + "</html>");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblValor.setForeground(Color.WHITE);
        lblValor.setFont(new Font("SansSerif", Font.BOLD, 50));
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblValor, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelGestion(String titulo, JButton boton1, JButton boton2, Color fondoPanel, Color colorBorde) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(fondoPanel);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 1),
                new EmptyBorder(16, 16, 16, 16)));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 12, 0));
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 8, 0));
        panelBotones.setOpaque(false);
        panelBotones.add(boton1);
        panelBotones.add(boton2);
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(panelBotones, BorderLayout.CENTER);
        return panel;
    }

    private JButton crearBotonAccion(String texto, Color colorBoton) {
        JButton boton = new JButton("<html><center>" + texto.replace("\n", "<br>") + "</center></html>");
        boton.setBackground(colorBoton);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFont(new Font("SansSerif", Font.BOLD, 15));
        boton.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
        return boton;
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

    private void cargarResumen() {
        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();

            String respU = cs.enviarYEsperar("GET_USUARIOS");
            String respV = cs.enviarYEsperar("GET_VEHICULOS");
            String respT = cs.enviarYEsperar("GET_PAQUETES_ESTADO|EN_TRANSITO");
            String respI = cs.enviarYEsperar("GET_PAQUETES_ESTADO|INCIDENCIA");

            int totalUsuarios = contarFilas(respU, "USUARIOS");
            int enRuta        = contarVehiculosConEstado(respV, "EN_RUTA");
            int enTransito    = contarFilas(respT, "PAQUETES");
            int incidencias   = contarFilas(respI, "PAQUETES");

            setDatosResumen(totalUsuarios, enRuta, enTransito, incidencias);
        } catch (IOException ex) {
            // Si falla la conexión, mantenemos "..."
        }
    }

    private void cargarDatosTabla() {
        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();
            // LIST|AUDITORIA|id|username|accion|fecha~...
            String resp = cs.enviarYEsperar("GET_AUDITORIA");
            if (resp == null || !resp.startsWith("LIST|AUDITORIA")) return;
            String[] partes = resp.split("\\|", 3);
            if (partes.length < 3 || partes[2].isEmpty()) return;
            String[] filas = partes[2].split("~");
            int limite = Math.min(filas.length, 20);
            for (int i = 0; i < limite; i++) {
                String[] c = filas[i].split("\\|");
                if (c.length < 4) continue;
                String fecha = c[3].length() >= 19 ? c[3].substring(0, 19) : c[3];
                modeloTabla.addRow(new Object[]{fecha, c[1], c[2], ""});
            }
        } catch (IOException ex) {
            // Si falla, la tabla queda vacía
        }
    }

    private int contarFilas(String resp, String tipo) {
        if (resp == null || !resp.startsWith("LIST|" + tipo)) return 0;
        String[] p = resp.split("\\|", 3);
        if (p.length < 3 || p[2].isEmpty()) return 0;
        return p[2].split("~").length;
    }

    private int contarVehiculosConEstado(String resp, String estado) {
        if (resp == null || !resp.startsWith("LIST|VEHICULOS")) return 0;
        String[] p = resp.split("\\|", 3);
        if (p.length < 3 || p[2].isEmpty()) return 0;
        int count = 0;
        for (String fila : p[2].split("~")) {
            String[] c = fila.split("\\|");
            if (c.length >= 5 && estado.equals(c[4])) count++;
        }
        return count;
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?", "QuickDelivery", JOptionPane.YES_NO_OPTION);
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

    public void setDatosResumen(int totalUsuarios, int vehiculosActivos, int paquetesTrafico, int incidencias) {
        lblTotalUsuarios.setText(String.valueOf(totalUsuarios));
        lblVehiculosActivos.setText(String.valueOf(vehiculosActivos));
        lblPaquetesTrafico.setText(String.valueOf(paquetesTrafico));
        lblIncidencias.setText(String.valueOf(incidencias));
    }
}
