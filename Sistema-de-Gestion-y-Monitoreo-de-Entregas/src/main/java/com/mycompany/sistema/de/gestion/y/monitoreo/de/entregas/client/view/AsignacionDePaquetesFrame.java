package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.client.view;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.client.util.ConexionServidor;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.EstadoPaquete;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Paquete;
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
import java.util.ArrayList;
import java.util.List;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Pantalla de asignación de paquetes a vehículos del sistema QuickDelivery S.A.
 *
 * @author Eryan / QuickDelivery S.A.
 */
public class AsignacionDePaquetesFrame extends JFrame {

    private JPanel panelHeader;
    private JPanel panelSidebar;
    private JPanel panelContenido;

    private JLabel lblLogo;
    private JLabel lblUsuarioHeader;
    private JButton btnCerrarSesion;

    private JButton btnPaquetes;
    private JButton btnAsignacion;
    private JButton btnMonitor;

    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;

    private JTable tablaPaquetesDisponibles;
    private JTable tablaVehiculos;
    private JTable tablaAsignacionesRecientes;

    private DefaultTableModel modeloPaquetesDisponibles;
    private DefaultTableModel modeloVehiculos;
    private DefaultTableModel modeloAsignacionesRecientes;

    private JButton btnAsignar;
    private JButton btnVerMasVehiculos;

    private JButton btnPagina1;
    private JButton btnPagina2;
    private JButton btnPagina3;
    private JButton btnSiguiente;

    private final Usuario usuarioActual;
    private List<Paquete>   listaPaquetes       = new ArrayList<>();
    private List<String[]>  listaVehiculos      = new ArrayList<>();
    private List<String[]>  listaAsignaciones   = new ArrayList<>();
    private int             paginaAsignaciones  = 0;
    private static final int FILAS_ASIGNACIONES = 10;

    public AsignacionDePaquetesFrame(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("Asignación de paquetes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 760);
        setMinimumSize(new Dimension(1100, 700));

        Color azulHeader   = new Color(24, 40, 159);
        Color turquesaMenu = new Color(55, 165, 168);
        Color turquesaOscuro = new Color(30, 98, 97);
        Color fondoGeneral = new Color(230, 230, 230);
        Color fondoPanel   = Color.WHITE;
        Color colorBorde   = new Color(190, 190, 190);
        Color colorBusqueda = new Color(58, 167, 169);
        Color azulBoton    = new Color(24, 40, 159);
        Color rojoBoton    = new Color(255, 28, 17);
        Color turquesaBoton = new Color(55, 165, 168);
        Color colorLink    = new Color(58, 167, 169);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(fondoGeneral);

        construirHeader(azulHeader);
        construirSidebar(turquesaMenu, turquesaOscuro);
        construirContenido(fondoGeneral, fondoPanel, colorBorde, colorBusqueda,
                azulBoton, rojoBoton, turquesaBoton, colorLink);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelSidebar, BorderLayout.WEST);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
        setLocationRelativeTo(null);

        cargarPaquetesDisponibles();
        cargarVehiculos();
        cargarAsignacionesRecientes();
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
        panelSidebar.setPreferredSize(new Dimension(205, 0));
        panelSidebar.setLayout(new BoxLayout(panelSidebar, BoxLayout.Y_AXIS));

        JPanel barraSuperior = new JPanel();
        barraSuperior.setBackground(new Color(86, 208, 207));
        barraSuperior.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        barraSuperior.setPreferredSize(new Dimension(205, 18));

        JButton btnDashboard = crearBotonMenu("Dashboard",  turquesaMenu,   Color.WHITE);
        btnPaquetes          = crearBotonMenu("Paquetes",   turquesaMenu,   Color.WHITE);
        btnAsignacion        = crearBotonMenu("Asignación", turquesaOscuro, Color.WHITE);
        btnMonitor           = crearBotonMenu("Monitor",    turquesaMenu,   Color.WHITE);

        btnDashboard.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new MenuDespachadorFrame(usuarioActual).setVisible(true));
        });
        btnPaquetes.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new GestionDePaquetesFrame(usuarioActual).setVisible(true));
        });
        btnAsignacion.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Ya estás en Asignación."));
        btnMonitor.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new MonitorDeEntregasFrame(usuarioActual).setVisible(true));
        });

        panelSidebar.add(barraSuperior);
        panelSidebar.add(Box.createVerticalStrut(12));
        panelSidebar.add(btnDashboard);
        panelSidebar.add(btnPaquetes);
        panelSidebar.add(btnAsignacion);
        panelSidebar.add(btnMonitor);
        panelSidebar.add(Box.createVerticalGlue());
    }

    private void construirContenido(Color fondoGeneral, Color fondoPanel, Color colorBorde,
            Color colorBusqueda, Color azulBoton, Color rojoBoton,
            Color turquesaBoton, Color colorLink) {

        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(fondoGeneral);
        panelContenido.setBorder(new EmptyBorder(14, 24, 18, 24));

        JPanel panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Asignación de Paquetes");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(70, 70, 70));
        lblTitulo.setBorder(new EmptyBorder(0, 10, 10, 0));

        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 0));
        panelBusqueda.setBackground(fondoPanel);
        panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 1),
                new EmptyBorder(10, 12, 10, 12)));
        panelBusqueda.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        txtBuscar = new JTextField("Buscar por ID, código, descripción");
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtBuscar.setForeground(Color.WHITE);
        txtBuscar.setBackground(colorBusqueda);
        txtBuscar.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        btnBuscar  = crearBotonIcono("⌕", azulBoton);
        btnAgregar = crearBotonIcono("⊕", azulBoton);
        btnEditar  = crearBotonIcono("✎", azulBoton);
        btnEliminar = crearBotonIcono("🗑", rojoBoton);

        btnBuscar.addActionListener(e -> buscarPaquete());
        btnAgregar.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new GestionDePaquetesFrame(usuarioActual).setVisible(true));
        });
        btnEditar.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Seleccione un paquete y use el botón Asignar."));
        btnEliminar.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Para eliminar paquetes vaya a Gestión de Paquetes."));

        JPanel panelBotonesBusqueda = new JPanel();
        panelBotonesBusqueda.setOpaque(false);
        panelBotonesBusqueda.setLayout(new BoxLayout(panelBotonesBusqueda, BoxLayout.X_AXIS));
        panelBotonesBusqueda.add(btnBuscar);
        panelBotonesBusqueda.add(Box.createHorizontalStrut(6));
        panelBotonesBusqueda.add(btnAgregar);
        panelBotonesBusqueda.add(Box.createHorizontalStrut(6));
        panelBotonesBusqueda.add(btnEditar);
        panelBotonesBusqueda.add(Box.createHorizontalStrut(6));
        panelBotonesBusqueda.add(btnEliminar);

        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
        panelBusqueda.add(panelBotonesBusqueda, BorderLayout.EAST);

        JPanel panelSuperiorBloques = new JPanel(new GridLayout(1, 2, 18, 0));
        panelSuperiorBloques.setOpaque(false);
        panelSuperiorBloques.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        panelSuperiorBloques.add(crearPanelPaquetesDisponibles(fondoPanel, colorBorde, turquesaBoton));
        panelSuperiorBloques.add(crearPanelVehiculos(fondoPanel, colorBorde, colorLink));

        JPanel panelInferior = crearPanelAsignacionesRecientes(fondoPanel, colorBorde);

        panelCentral.add(lblTitulo);
        panelCentral.add(panelBusqueda);
        panelCentral.add(Box.createVerticalStrut(14));
        panelCentral.add(panelSuperiorBloques);
        panelCentral.add(Box.createVerticalStrut(12));
        panelCentral.add(panelInferior);

        panelContenido.add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel crearPanelPaquetesDisponibles(Color fondoPanel, Color colorBorde, Color turquesaBoton) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(fondoPanel);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 1),
                new EmptyBorder(12, 14, 10, 14)));

        JLabel lblTitulo = new JLabel("Paquetes Disponibles (En Espera)");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));

        JPanel linea = new JPanel();
        linea.setBackground(new Color(100, 210, 210));
        linea.setPreferredSize(new Dimension(0, 1));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);
        panelSuperior.add(linea, BorderLayout.SOUTH);
        panelSuperior.setBorder(new EmptyBorder(0, 0, 10, 0));

        String[] columnas = {"ID", "Código", "Descripción", "Peso (kg)"};
        modeloPaquetesDisponibles = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaPaquetesDisponibles = new JTable(modeloPaquetesDisponibles);
        tablaPaquetesDisponibles.setRowHeight(34);
        tablaPaquetesDisponibles.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaPaquetesDisponibles.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaPaquetesDisponibles.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tablaPaquetesDisponibles);
        scroll.setBorder(BorderFactory.createLineBorder(colorBorde, 1));

        btnAsignar = new JButton("Asignar");
        btnAsignar.setBackground(turquesaBoton);
        btnAsignar.setForeground(Color.WHITE);
        btnAsignar.setFocusPainted(false);
        btnAsignar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAsignar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAsignar.addActionListener(e -> asignarPaquete());

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.setBorder(new EmptyBorder(10, 0, 0, 0));
        panelInferior.add(btnAsignar, BorderLayout.EAST);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelInferior, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelVehiculos(Color fondoPanel, Color colorBorde, Color colorLink) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(fondoPanel);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 1),
                new EmptyBorder(12, 14, 10, 14)));

        JLabel lblTitulo = new JLabel("Vehículos");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));

        JPanel linea = new JPanel();
        linea.setBackground(new Color(100, 210, 210));
        linea.setPreferredSize(new Dimension(0, 1));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);
        panelSuperior.add(linea, BorderLayout.SOUTH);
        panelSuperior.setBorder(new EmptyBorder(0, 0, 10, 0));

        String[] columnas = {"Placa", "Tipo", "Estado", "Capacidad (kg)"};
        modeloVehiculos = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaVehiculos = new JTable(modeloVehiculos);
        tablaVehiculos.setRowHeight(34);
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
        btnVerMasVehiculos.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new GestionDeVehiculosFrame(usuarioActual).setVisible(true));
        });

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.setBorder(new EmptyBorder(10, 0, 0, 0));
        panelInferior.add(btnVerMasVehiculos, BorderLayout.EAST);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelInferior, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelAsignacionesRecientes(Color fondoPanel, Color colorBorde) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(fondoPanel);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 1),
                new EmptyBorder(10, 14, 10, 14)));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        JLabel lblTitulo = new JLabel("Asignaciones recientes");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 6, 0));

        String[] columnas = {"ID", "Paquete", "Vehículo", "Fecha"};
        modeloAsignacionesRecientes = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaAsignacionesRecientes = new JTable(modeloAsignacionesRecientes);
        tablaAsignacionesRecientes.setRowHeight(34);
        tablaAsignacionesRecientes.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaAsignacionesRecientes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaAsignacionesRecientes.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tablaAsignacionesRecientes);
        scroll.setBorder(BorderFactory.createLineBorder(colorBorde, 1));

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel panelPaginacion = new JPanel();
        panelPaginacion.setOpaque(false);
        panelPaginacion.setLayout(new BoxLayout(panelPaginacion, BoxLayout.X_AXIS));

        btnPagina1   = crearBotonPagina("1");
        btnPagina2   = crearBotonPagina("2");
        btnPagina3   = crearBotonPagina("3");
        btnSiguiente = crearBotonPagina("Siguiente >");

        btnPagina1.addActionListener(e -> mostrarPaginaAsignaciones(0));
        btnPagina2.addActionListener(e -> mostrarPaginaAsignaciones(1));
        btnPagina3.addActionListener(e -> mostrarPaginaAsignaciones(2));
        btnSiguiente.addActionListener(e -> mostrarPaginaAsignaciones(paginaAsignaciones + 1));

        panelPaginacion.add(btnPagina1);
        panelPaginacion.add(Box.createHorizontalStrut(4));
        panelPaginacion.add(btnPagina2);
        panelPaginacion.add(Box.createHorizontalStrut(4));
        panelPaginacion.add(btnPagina3);
        panelPaginacion.add(Box.createHorizontalStrut(4));
        panelPaginacion.add(btnSiguiente);

        panelInferior.add(panelPaginacion, BorderLayout.EAST);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelInferior, BorderLayout.SOUTH);

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

    private JButton crearBotonIcono(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(48, 40));
        boton.setMaximumSize(new Dimension(48, 40));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("SansSerif", Font.BOLD, 22));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder());
        return boton;
    }

    private JButton crearBotonPagina(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(24, 40, 159));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        return boton;
    }

    private void cargarPaquetesDisponibles() {
        modeloPaquetesDisponibles.setRowCount(0);
        listaPaquetes.clear();
        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();
            // GET_PAQUETES_ESPERA devuelve solo los paquetes EN_ESPERA
            String respuesta = cs.enviarYEsperar("GET_PAQUETES_ESPERA");
            if (respuesta == null || !respuesta.startsWith("LIST")) return;
            // LIST|PAQUETES|id|codigo|descripcion|peso|estado~...
            String[] partes = respuesta.split("\\|", 3);
            if (partes.length < 3 || partes[2].isEmpty()) return;
            for (String fila : partes[2].split("~")) {
                if (fila.startsWith("|")) fila = fila.substring(1);
                String[] c = fila.split("\\|");
                if (c.length < 5) continue;
                Paquete p = new Paquete(Integer.parseInt(c[0]), c[1], c[2], Double.parseDouble(c[3]), EstadoPaquete.valueOf(c[4]));
                listaPaquetes.add(p);
                modeloPaquetesDisponibles.addRow(new Object[]{p.getId(), p.getCodigo(), p.getDescripcion(), p.getPeso()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar paquetes: " + ex.getMessage());
        }
    }

    private void cargarVehiculos() {
        modeloVehiculos.setRowCount(0);
        listaVehiculos.clear();
        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();
            String respuesta = cs.enviarYEsperar("GET_VEHICULOS");
            if (respuesta == null || !respuesta.startsWith("LIST")) return;
            // LIST|VEHICULOS|id|placa|tipo|capacidad|estado~...
            String[] partes = respuesta.split("\\|", 3);
            if (partes.length < 3 || partes[2].isEmpty()) return;
            for (String fila : partes[2].split("~")) {
                if (fila.startsWith("|")) fila = fila.substring(1);
                String[] c = fila.split("\\|");
                if (c.length < 5) continue;
                // c[0]=id, c[1]=placa, c[2]=tipo, c[3]=capacidad, c[4]=estado
                listaVehiculos.add(c);
                String tipoDisplay = c[2].equals("Camion") ? "Camión" : c[2].equals("Furgon") ? "Furgón" : c[2];
                modeloVehiculos.addRow(new Object[]{c[1], tipoDisplay, c[4], c[3]});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar vehículos: " + ex.getMessage());
        }
    }

    private void cargarAsignacionesRecientes() {
        listaAsignaciones.clear();
        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();
            String respuesta = cs.enviarYEsperar("GET_ASIGNACIONES");
            if (respuesta == null || !respuesta.startsWith("LIST")) return;
            // LIST|ASIGNACIONES|id|codigoPaquete|placaVehiculo|fecha~...
            String[] partes = respuesta.split("\\|", 3);
            if (partes.length < 3 || partes[2].isEmpty()) return;
            for (String fila : partes[2].split("~")) {
                if (fila.startsWith("|")) fila = fila.substring(1);
                String[] c = fila.split("\\|");
                if (c.length < 4) continue;
                listaAsignaciones.add(c);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar asignaciones: " + ex.getMessage());
        }
        mostrarPaginaAsignaciones(0);
    }

    private void mostrarPaginaAsignaciones(int pagina) {
        int total = listaAsignaciones.size();
        int totalPaginas = Math.max(1, (int) Math.ceil(total / (double) FILAS_ASIGNACIONES));
        if (pagina < 0) pagina = 0;
        if (pagina >= totalPaginas) pagina = totalPaginas - 1;
        paginaAsignaciones = pagina;

        modeloAsignacionesRecientes.setRowCount(0);
        int desde = pagina * FILAS_ASIGNACIONES;
        int hasta = Math.min(desde + FILAS_ASIGNACIONES, total);
        for (int i = desde; i < hasta; i++) {
            String[] c = listaAsignaciones.get(i);
            modeloAsignacionesRecientes.addRow(new Object[]{c[0], c[1], c[2], c[3]});
        }

        btnPagina1.setEnabled(pagina != 0);
        btnPagina2.setEnabled(totalPaginas > 1 && pagina != 1);
        btnPagina3.setEnabled(totalPaginas > 2 && pagina != 2);
        btnSiguiente.setEnabled(pagina < totalPaginas - 1);
    }

    private void buscarPaquete() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty() || texto.equalsIgnoreCase("Buscar por ID, código, descripción")) {
            cargarPaquetesDisponibles();
            return;
        }
        String lower = texto.toLowerCase();
        modeloPaquetesDisponibles.setRowCount(0);
        for (Paquete p : listaPaquetes) {
            if (String.valueOf(p.getId()).contains(lower)
                    || p.getCodigo().toLowerCase().contains(lower)
                    || p.getDescripcion().toLowerCase().contains(lower)) {
                modeloPaquetesDisponibles.addRow(new Object[]{
                    p.getId(), p.getCodigo(), p.getDescripcion(), p.getPeso()
                });
            }
        }
    }

    private void asignarPaquete() {
        int filaPaquete = tablaPaquetesDisponibles.getSelectedRow();
        if (filaPaquete == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paquete disponible.");
            return;
        }
        int idPaquete = (int) modeloPaquetesDisponibles.getValueAt(filaPaquete, 0);
        Paquete paquete = listaPaquetes.stream()
                .filter(p -> p.getId() == idPaquete).findFirst().orElse(null);
        if (paquete == null) return;

        // Si el usuario seleccionó un vehículo, usarlo; si no, auto-elegir el primero disponible
        int filaVehiculo = tablaVehiculos.getSelectedRow();
        String idVehiculo = null;
        if (filaVehiculo != -1 && filaVehiculo < listaVehiculos.size()) {
            idVehiculo = listaVehiculos.get(filaVehiculo)[0];
        } else {
            for (String[] v : listaVehiculos) {
                if ("DISPONIBLE".equals(v[4]) && Double.parseDouble(v[3]) >= paquete.getPeso()) {
                    idVehiculo = v[0];
                    break;
                }
            }
        }

        if (idVehiculo == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay vehículo disponible con capacidad suficiente para este paquete.");
            return;
        }

        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();
            String resp = cs.enviarYEsperar("ASIGNAR|" + idPaquete + "|" + idVehiculo);
            if (resp != null && resp.startsWith("OK")) {
                JOptionPane.showMessageDialog(this, "Paquete asignado correctamente.");
                cargarPaquetesDisponibles();
                cargarVehiculos();
                cargarAsignacionesRecientes();
            } else {
                String msg = (resp != null && resp.contains("|")) ? resp.split("\\|", 2)[1] : "No se pudo asignar";
                JOptionPane.showMessageDialog(this, "Error: " + msg);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
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
