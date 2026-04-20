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
 * Dashboard principal del Despachador.
 *
 * @author Eryan / QuickDelivery S.A.
 */
public class MenuDespachadorFrame extends JFrame {

    private JPanel panelHeader;
    private JPanel panelSidebar;
    private JPanel panelContenido;

    private JLabel lblLogo;
    private JLabel lblUsuario;
    private JButton btnCerrarSesion;

    private JButton btnDashboard;
    private JButton btnPaquetes;
    private JButton btnAsignacion;
    private JButton btnMonitor;

    private JLabel lblPaquetesPendientes;
    private JLabel lblVehiculosDisponibles;
    private JLabel lblPaquetesTransito;
    private JLabel lblIncidenciasActivas;

    private JTable tablaPaquetes;
    private JTable tablaVehiculos;
    private DefaultTableModel modeloPaquetes;
    private DefaultTableModel modeloVehiculos;

    private JButton btnVerMasPaquetes;
    private JButton btnVerMasVehiculos;

    private final Usuario usuarioActual;

    public MenuDespachadorFrame(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("Menu – Despachador");

        Color azulHeader = new Color(24, 40, 159);
        Color turquesaMenu = new Color(55, 165, 168);
        Color turquesaOscuro = new Color(30, 98, 97);
        Color fondoGeneral = new Color(230, 230, 230);
        Color fondoPanel = Color.WHITE;
        Color colorTarjeta = new Color(58, 167, 169);
        Color colorTarjetaRoja = new Color(246, 10, 5);
        Color colorTexto = new Color(35, 35, 35);
        Color colorBorde = new Color(190, 190, 190);
        Color colorLink = new Color(58, 167, 169);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(fondoGeneral);

        construirHeader(azulHeader);
        construirSidebar(turquesaMenu, turquesaOscuro);
        construirContenido(fondoGeneral, fondoPanel, colorTarjeta, colorTarjetaRoja, colorTexto, colorBorde, colorLink);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelSidebar, BorderLayout.WEST);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 760);
        setMinimumSize(new Dimension(1100, 700));
        setContentPane(panelPrincipal);
        setLocationRelativeTo(null);

        cargarDatos();
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
        panelSidebar.setPreferredSize(new Dimension(205, 0));
        panelSidebar.setLayout(new BoxLayout(panelSidebar, BoxLayout.Y_AXIS));

        JPanel barraSuperior = new JPanel();
        barraSuperior.setBackground(new Color(86, 208, 207));
        barraSuperior.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        barraSuperior.setPreferredSize(new Dimension(205, 18));

        btnDashboard = crearBotonMenu("Dashboard", turquesaOscuro, Color.WHITE);
        btnPaquetes  = crearBotonMenu("Paquetes",  turquesaMenu,  Color.WHITE);
        btnAsignacion= crearBotonMenu("Asignación",turquesaMenu,  Color.WHITE);
        btnMonitor   = crearBotonMenu("Monitor",   turquesaMenu,  Color.WHITE);

        btnDashboard.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ya estás en Dashboard."));
        btnPaquetes.addActionListener(e -> { dispose(); SwingUtilities.invokeLater(() -> new GestionDePaquetesFrame(usuarioActual).setVisible(true)); });
        btnAsignacion.addActionListener(e -> { dispose(); SwingUtilities.invokeLater(() -> new AsignacionDePaquetesFrame(usuarioActual).setVisible(true)); });
        btnMonitor.addActionListener(e -> { dispose(); SwingUtilities.invokeLater(() -> new MonitorDeEntregasFrame(usuarioActual).setVisible(true)); });

        panelSidebar.add(barraSuperior);
        panelSidebar.add(Box.createVerticalStrut(12));
        panelSidebar.add(btnDashboard);
        panelSidebar.add(btnPaquetes);
        panelSidebar.add(btnAsignacion);
        panelSidebar.add(btnMonitor);
        panelSidebar.add(Box.createVerticalGlue());
    }

    private void construirContenido(Color fondoGeneral, Color fondoPanel, Color colorTarjeta,
            Color colorTarjetaRoja, Color colorTexto, Color colorBorde, Color colorLink) {
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(fondoGeneral);
        panelContenido.setBorder(new EmptyBorder(22, 28, 22, 30));

        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(fondoGeneral);
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        JPanel panelTarjetas = new JPanel(new GridLayout(1, 4, 18, 0));
        panelTarjetas.setOpaque(false);
        panelTarjetas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        panelTarjetas.setPreferredSize(new Dimension(900, 100));

        lblPaquetesPendientes = new JLabel("...");
        lblVehiculosDisponibles = new JLabel("...");
        lblPaquetesTransito = new JLabel("...");
        lblIncidenciasActivas = new JLabel("...");

        panelTarjetas.add(crearTarjeta("Paquetes\nPendientes", lblPaquetesPendientes, colorTarjeta));
        panelTarjetas.add(crearTarjeta("Vehículos\nDisponibles", lblVehiculosDisponibles, colorTarjeta));
        panelTarjetas.add(crearTarjeta("Paquetes en\ntránsito", lblPaquetesTransito, colorTarjeta));
        panelTarjetas.add(crearTarjeta("Incidencias\nActivas", lblIncidenciasActivas, colorTarjetaRoja));

        JPanel panelBloques = new JPanel(new GridLayout(1, 2, 16, 0));
        panelBloques.setOpaque(false);
        panelBloques.setMaximumSize(new Dimension(Integer.MAX_VALUE, 255));

        panelBloques.add(crearPanelPaquetes(fondoPanel, colorTexto, colorBorde, colorLink));
        panelBloques.add(crearPanelVehiculos(fondoPanel, colorTexto, colorBorde, colorLink));

        panelCentral.add(panelTarjetas);
        panelCentral.add(Box.createVerticalStrut(16));
        panelCentral.add(panelBloques);

        panelContenido.add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel crearPanelPaquetes(Color fondoPanel, Color colorTexto, Color colorBorde, Color colorLink) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(fondoPanel);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 1),
                new EmptyBorder(14, 14, 10, 14)));

        JLabel lblTitulo = new JLabel("Paquetes Pendientes");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setForeground(colorTexto);

        JPanel linea = new JPanel();
        linea.setBackground(new Color(100, 210, 210));
        linea.setPreferredSize(new Dimension(0, 1));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);
        panelSuperior.add(linea, BorderLayout.SOUTH);
        panelSuperior.setBorder(new EmptyBorder(0, 0, 10, 0));

        String[] columnas = {"ID", "Código", "Descripción", "Estado"};
        modeloPaquetes = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaPaquetes = new JTable(modeloPaquetes);
        tablaPaquetes.setRowHeight(32);
        tablaPaquetes.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaPaquetes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaPaquetes.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tablaPaquetes);
        scroll.setBorder(BorderFactory.createLineBorder(colorBorde, 1));

        btnVerMasPaquetes = new JButton("Ver más");
        btnVerMasPaquetes.setBorderPainted(false);
        btnVerMasPaquetes.setContentAreaFilled(false);
        btnVerMasPaquetes.setForeground(colorLink);
        btnVerMasPaquetes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVerMasPaquetes.addActionListener(e -> { dispose(); SwingUtilities.invokeLater(() -> new GestionDePaquetesFrame(usuarioActual).setVisible(true)); });

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.add(btnVerMasPaquetes, BorderLayout.EAST);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelInferior, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelVehiculos(Color fondoPanel, Color colorTexto, Color colorBorde, Color colorLink) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(fondoPanel);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 1),
                new EmptyBorder(14, 14, 10, 14)));

        JLabel lblTitulo = new JLabel("Vehículos Disponibles");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setForeground(colorTexto);

        JPanel linea = new JPanel();
        linea.setBackground(new Color(100, 210, 210));
        linea.setPreferredSize(new Dimension(0, 1));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);
        panelSuperior.add(linea, BorderLayout.SOUTH);
        panelSuperior.setBorder(new EmptyBorder(0, 0, 10, 0));

        String[] columnas = {"ID", "Placa", "Tipo", "Estado"};
        modeloVehiculos = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaVehiculos = new JTable(modeloVehiculos);
        tablaVehiculos.setRowHeight(32);
        tablaVehiculos.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaVehiculos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaVehiculos.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tablaVehiculos);
        scroll.setBorder(BorderFactory.createLineBorder(colorBorde, 1));

        btnVerMasVehiculos = new JButton("Ver más");
        btnVerMasVehiculos.setBorderPainted(false);
        btnVerMasVehiculos.setContentAreaFilled(false);
        btnVerMasVehiculos.setForeground(colorLink);
        btnVerMasVehiculos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVerMasVehiculos.addActionListener(e -> { dispose(); SwingUtilities.invokeLater(() -> new GestionDeVehiculosFrame(usuarioActual).setVisible(true)); });

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.add(btnVerMasVehiculos, BorderLayout.EAST);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelInferior, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearTarjeta(String titulo, JLabel lblValor, Color colorTarjeta) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(colorTarjeta);
        panel.setBorder(new EmptyBorder(8, 12, 8, 12));
        JLabel lblTitulo = new JLabel("<html><center>" + titulo.replace("\n", "<br>") + "</center></html>");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblValor.setForeground(Color.WHITE);
        lblValor.setFont(new Font("SansSerif", Font.BOLD, 42));
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblValor, BorderLayout.CENTER);
        return panel;
    }

    private JButton crearBotonMenu(String texto, Color fondo, Color letra) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(LEFT_ALIGNMENT);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        boton.setPreferredSize(new Dimension(205, 46));
        boton.setBackground(fondo);
        boton.setForeground(letra);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(new EmptyBorder(0, 22, 0, 0));
        return boton;
    }

    private void cargarDatos() {
        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();

            // Paquetes en espera
            String respPend = cs.enviarYEsperar("GET_PAQUETES_ESPERA");
            // Vehículos disponibles
            String respDisp = cs.enviarYEsperar("GET_VEHICULOS_DISPONIBLES");
            // Conteos para tarjetas
            String respTrans = cs.enviarYEsperar("GET_PAQUETES_ESTADO|EN_TRANSITO");
            String respInc   = cs.enviarYEsperar("GET_PAQUETES_ESTADO|INCIDENCIA");

            // Parsear paquetes pendientes
            String[][] filasPend = parsearFilas(respPend, "PAQUETES");
            // Parsear vehículos disponibles
            String[][] filasDisp = parsearFilas(respDisp, "VEHICULOS");

            int pendientes  = filasPend.length;
            int disponibles = filasDisp.length;
            int transito    = contarFilas(respTrans, "PAQUETES");
            int incidencias = contarFilas(respInc,   "PAQUETES");

            setDatosResumen(pendientes, disponibles, transito, incidencias);

            // Tabla paquetes: ID|Código|Descripción|Estado
            int limitePaq = Math.min(pendientes, 5);
            for (int i = 0; i < limitePaq; i++) {
                String[] c = filasPend[i]; // c[0]=id,c[1]=codigo,c[2]=desc,c[3]=peso,c[4]=estado
                if (c.length < 5) continue;
                modeloPaquetes.addRow(new Object[]{c[0], c[1], c[2], c[4]});
            }
            // Tabla vehículos: ID|Placa|Tipo|Estado
            int limiteVeh = Math.min(disponibles, 5);
            for (int i = 0; i < limiteVeh; i++) {
                String[] c = filasDisp[i]; // c[0]=id,c[1]=placa,c[2]=tipo,c[3]=cap,c[4]=estado
                if (c.length < 5) continue;
                modeloVehiculos.addRow(new Object[]{c[0], c[1], c[2], c[4]});
            }
        } catch (IOException ex) {
            // Si falla la conexión los paneles quedan vacíos
        }
    }

    private int contarFilas(String resp, String tipo) {
        if (resp == null || !resp.startsWith("LIST|" + tipo)) return 0;
        String[] p = resp.split("\\|", 3);
        if (p.length < 3 || p[2].isEmpty()) return 0;
        return p[2].split("~").length;
    }

    private String[][] parsearFilas(String resp, String tipo) {
        if (resp == null || !resp.startsWith("LIST|" + tipo)) return new String[0][];
        String[] p = resp.split("\\|", 3);
        if (p.length < 3 || p[2].isEmpty()) return new String[0][];
        String[] rows = p[2].split("~");
        String[][] result = new String[rows.length][];
        for (int i = 0; i < rows.length; i++) result[i] = rows[i].split("\\|");
        return result;
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

    public void setDatosResumen(int pendientes, int disponibles, int transito, int incidencias) {
        lblPaquetesPendientes.setText(String.valueOf(pendientes));
        lblVehiculosDisponibles.setText(String.valueOf(disponibles));
        lblPaquetesTransito.setText(String.valueOf(transito));
        lblIncidenciasActivas.setText(String.valueOf(incidencias));
    }
}
