package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.client.view;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.client.util.ConexionServidor;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Usuario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Pantalla de monitoreo de entregas del sistema QuickDelivery S.A.
 *
 * @author Eryan / QuickDelivery S.A.
 */
public class MonitorDeEntregasFrame extends JFrame {

    private JPanel panelHeader;
    private JPanel panelSidebar;
    private JPanel panelContenido;

    private JLabel lblLogo;
    private JLabel lblUsuarioHeader;
    private JButton btnCerrarSesion;

    private JButton btnDashboard;
    private JButton btnPaquetes;
    private JButton btnAsignacion;
    private JButton btnMonitor;

    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;

    private JTable tablaVehiculosRuta;
    private JTable tablaEstadoPaquetes;
    private JTable tablaEstadoEntregas;

    private DefaultTableModel modeloVehiculosRuta;
    private DefaultTableModel modeloEstadoPaquetes;
    private DefaultTableModel modeloEstadoEntregas;

    private JButton btnVerMasVehiculos;
    private JButton btnVerMasPaquetes;

    private JButton btnPagina1;
    private JButton btnPagina2;
    private JButton btnPagina3;
    private JButton btnSiguiente;

    private static final int FILAS_POR_PAGINA = 10;

    private final Usuario usuarioActual;
    private List<String[]> listaAuditoria = new ArrayList<>();
    private int paginaActual = 0;

    public MonitorDeEntregasFrame(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("Monitor de entregas");
        initComponents();
        configurarVentana();
        setLocationRelativeTo(null);

        cargarMonitor();
        cargarEstadoEntregas();
    }

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 760);
        setMinimumSize(new Dimension(1100, 700));
    }

    private void initComponents() {
        Color azulHeader    = new Color(24, 40, 159);
        Color turquesaMenu  = new Color(55, 165, 168);
        Color turquesaOscuro = new Color(30, 98, 97);
        Color fondoGeneral  = new Color(230, 230, 230);
        Color fondoPanel    = Color.WHITE;
        Color colorBorde    = new Color(190, 190, 190);
        Color colorBusqueda = new Color(58, 167, 169);
        Color azulBoton     = new Color(24, 40, 159);
        Color rojoBoton     = new Color(255, 28, 17);
        Color colorLink     = new Color(58, 167, 169);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(fondoGeneral);

        construirHeader(azulHeader);
        construirSidebar(turquesaMenu, turquesaOscuro);
        construirContenido(fondoGeneral, fondoPanel, colorBorde, colorBusqueda,
                azulBoton, rojoBoton, colorLink);

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
        panelSidebar.setPreferredSize(new Dimension(205, 0));
        panelSidebar.setLayout(new BoxLayout(panelSidebar, BoxLayout.Y_AXIS));

        JPanel barraSuperior = new JPanel();
        barraSuperior.setBackground(new Color(86, 208, 207));
        barraSuperior.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        barraSuperior.setPreferredSize(new Dimension(205, 18));

        btnDashboard  = crearBotonMenu("Dashboard",   turquesaMenu,   Color.WHITE);
        btnPaquetes   = crearBotonMenu("Paquetes",    turquesaMenu,   Color.WHITE);
        btnAsignacion = crearBotonMenu("Asignación",  turquesaMenu,   Color.WHITE);
        btnMonitor    = crearBotonMenu("Monitor",     turquesaOscuro, Color.WHITE);

        btnDashboard.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new MenuDespachadorFrame(usuarioActual).setVisible(true));
        });
        btnPaquetes.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new GestionDePaquetesFrame(usuarioActual).setVisible(true));
        });
        btnAsignacion.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new AsignacionDePaquetesFrame(usuarioActual).setVisible(true));
        });
        btnMonitor.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Ya estás en Monitor."));

        panelSidebar.add(barraSuperior);
        panelSidebar.add(Box.createVerticalStrut(12));
        panelSidebar.add(btnDashboard);
        panelSidebar.add(btnPaquetes);
        panelSidebar.add(btnAsignacion);
        panelSidebar.add(btnMonitor);
        panelSidebar.add(Box.createVerticalGlue());
    }

    private void construirContenido(Color fondoGeneral, Color fondoPanel, Color colorBorde,
            Color colorBusqueda, Color azulBoton, Color rojoBoton, Color colorLink) {

        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(fondoGeneral);
        panelContenido.setBorder(new EmptyBorder(14, 24, 18, 24));

        JPanel panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Monitor de Entregas");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(70, 70, 70));
        lblTitulo.setBorder(new EmptyBorder(0, 10, 10, 0));

        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 0));
        panelBusqueda.setBackground(fondoPanel);
        panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 1),
                new EmptyBorder(10, 12, 10, 12)));
        panelBusqueda.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        txtBuscar = new JTextField("Buscar por ID, usuario, nombre");
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtBuscar.setForeground(Color.WHITE);
        txtBuscar.setBackground(colorBusqueda);
        txtBuscar.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        btnBuscar   = crearBotonIcono("⌕", azulBoton);
        btnAgregar  = crearBotonIcono("⊕", azulBoton);
        btnEditar   = crearBotonIcono("✎", azulBoton);
        btnEliminar = crearBotonIcono("🗑", rojoBoton);

        btnBuscar.addActionListener(e -> buscarMonitor());
        btnAgregar.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Use Asignación para registrar paquetes."));
        btnEditar.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Seleccione un registro para editar."));
        btnEliminar.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Los registros de auditoría no se pueden eliminar."));

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

        JPanel panelSuperior = new JPanel(new java.awt.GridLayout(1, 2, 18, 0));
        panelSuperior.setOpaque(false);
        panelSuperior.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        panelSuperior.add(crearPanelVehiculosRuta(fondoPanel, colorBorde, colorLink));
        panelSuperior.add(crearPanelEstadoPaquetes(fondoPanel, colorBorde, colorLink));

        JPanel panelInferior = crearPanelEstadoEntregas(fondoPanel, colorBorde);

        panelCentral.add(lblTitulo);
        panelCentral.add(panelBusqueda);
        panelCentral.add(Box.createVerticalStrut(14));
        panelCentral.add(panelSuperior);
        panelCentral.add(Box.createVerticalStrut(12));
        panelCentral.add(panelInferior);

        panelContenido.add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel crearPanelVehiculosRuta(Color fondoPanel, Color colorBorde, Color colorLink) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(fondoPanel);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 1),
                new EmptyBorder(12, 14, 10, 14)));

        JLabel lblTitulo = new JLabel("Vehículos en ruta");
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
        modeloVehiculosRuta = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaVehiculosRuta = new JTable(modeloVehiculosRuta);
        tablaVehiculosRuta.setRowHeight(34);
        tablaVehiculosRuta.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaVehiculosRuta.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaVehiculosRuta.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tablaVehiculosRuta);
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

    private JPanel crearPanelEstadoPaquetes(Color fondoPanel, Color colorBorde, Color colorLink) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(fondoPanel);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 1),
                new EmptyBorder(12, 14, 10, 14)));

        JLabel lblTitulo = new JLabel("Estado de Paquetes");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));

        JPanel linea = new JPanel();
        linea.setBackground(new Color(100, 210, 210));
        linea.setPreferredSize(new Dimension(0, 1));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);
        panelSuperior.add(linea, BorderLayout.SOUTH);
        panelSuperior.setBorder(new EmptyBorder(0, 0, 10, 0));

        String[] columnas = {"ID", "Código", "Descripción", "Estado"};
        modeloEstadoPaquetes = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaEstadoPaquetes = new JTable(modeloEstadoPaquetes);
        tablaEstadoPaquetes.setRowHeight(34);
        tablaEstadoPaquetes.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaEstadoPaquetes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaEstadoPaquetes.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tablaEstadoPaquetes);
        scroll.setBorder(BorderFactory.createLineBorder(colorBorde, 1));

        btnVerMasPaquetes = new JButton("Ver más");
        btnVerMasPaquetes.setBorderPainted(false);
        btnVerMasPaquetes.setContentAreaFilled(false);
        btnVerMasPaquetes.setForeground(colorLink);
        btnVerMasPaquetes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVerMasPaquetes.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new GestionDePaquetesFrame(usuarioActual).setVisible(true));
        });

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.setBorder(new EmptyBorder(10, 0, 0, 0));
        panelInferior.add(btnVerMasPaquetes, BorderLayout.EAST);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelInferior, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelEstadoEntregas(Color fondoPanel, Color colorBorde) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(fondoPanel);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 1),
                new EmptyBorder(10, 14, 10, 14)));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        JLabel lblTitulo = new JLabel("Estado de Entregas (Auditoría)");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 6, 0));

        String[] columnas = {"ID", "Usuario", "Acción", "Fecha"};
        modeloEstadoEntregas = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaEstadoEntregas = new JTable(modeloEstadoEntregas);
        tablaEstadoEntregas.setRowHeight(34);
        tablaEstadoEntregas.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaEstadoEntregas.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaEstadoEntregas.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tablaEstadoEntregas);
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

        btnPagina1.addActionListener(e -> irAPagina(0));
        btnPagina2.addActionListener(e -> irAPagina(1));
        btnPagina3.addActionListener(e -> irAPagina(2));
        btnSiguiente.addActionListener(e -> irAPagina(paginaActual + 1));

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

    /**
     * Envía GET_MONITOR y lee las dos líneas de respuesta:
     * línea 1 → vehículos EN_RUTA, línea 2 → paquetes EN_TRANSITO.
     */
    private void cargarMonitor() {
        modeloVehiculosRuta.setRowCount(0);
        modeloEstadoPaquetes.setRowCount(0);
        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();

            // Línea 1: vehículos en ruta
            // LIST|MONITOR_VEHICULOS|id|placa|tipo|estado~...
            String respV = cs.enviarYEsperar("GET_MONITOR");
            // Línea 2: paquetes en tránsito
            // LIST|MONITOR_PAQUETES|id|codigo|descripcion|peso|estado~...
            String respP = cs.leer();

            if (respV != null && respV.startsWith("LIST|MONITOR_VEHICULOS")) {
                String[] partes = respV.split("\\|", 3);
                if (partes.length == 3 && !partes[2].isEmpty()) {
                    for (String fila : partes[2].split("~")) {
                        String[] c = fila.split("\\|");
                        if (c.length < 4) continue;
                        String tipo;
                        switch (c[2]) {
                            case "Camion": tipo = "Camión"; break;
                            case "Moto":   tipo = "Moto";   break;
                            default:       tipo = "Furgón"; break;
                        }
                        modeloVehiculosRuta.addRow(new Object[]{c[1], tipo, "En Ruta", "—"});
                    }
                }
            }

            if (respP != null && respP.startsWith("LIST|MONITOR_PAQUETES")) {
                String[] partes = respP.split("\\|", 3);
                if (partes.length == 3 && !partes[2].isEmpty()) {
                    for (String fila : partes[2].split("~")) {
                        String[] c = fila.split("\\|");
                        if (c.length < 5) continue;
                        // c[0]=id, c[1]=codigo, c[2]=descripcion, c[3]=peso, c[4]=estado
                        modeloEstadoPaquetes.addRow(new Object[]{c[0], c[1], c[2], c[4]});
                    }
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar monitor: " + ex.getMessage());
        }
    }

    private void cargarEstadoEntregas() {
        listaAuditoria.clear();
        paginaActual = 0;
        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();
            // LIST|AUDITORIA|id|username|accion|fecha~...
            String resp = cs.enviarYEsperar("GET_AUDITORIA");
            if (resp != null && resp.startsWith("LIST|AUDITORIA")) {
                String[] partes = resp.split("\\|", 3);
                if (partes.length == 3 && !partes[2].isEmpty()) {
                    for (String fila : partes[2].split("~")) {
                        String[] c = fila.split("\\|");
                        if (c.length < 4) continue;
                        listaAuditoria.add(c);
                    }
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar auditoría: " + ex.getMessage());
        }
        mostrarPagina(0);
    }

    private void irAPagina(int pagina) {
        int totalPaginas = (int) Math.ceil((double) listaAuditoria.size() / FILAS_POR_PAGINA);
        if (pagina < 0 || pagina >= totalPaginas) return;
        mostrarPagina(pagina);
    }

    private void mostrarPagina(int pagina) {
        paginaActual = pagina;
        modeloEstadoEntregas.setRowCount(0);
        int desde = pagina * FILAS_POR_PAGINA;
        int hasta = Math.min(desde + FILAS_POR_PAGINA, listaAuditoria.size());
        for (int i = desde; i < hasta; i++) {
            String[] c = listaAuditoria.get(i);
            modeloEstadoEntregas.addRow(new Object[]{c[0], c[1], c[2], c[3]});
        }
        // Habilitar/deshabilitar botones según páginas disponibles
        int totalPaginas = (int) Math.ceil((double) listaAuditoria.size() / FILAS_POR_PAGINA);
        btnPagina1.setEnabled(totalPaginas >= 1 && pagina != 0);
        btnPagina2.setEnabled(totalPaginas >= 2 && pagina != 1);
        btnPagina3.setEnabled(totalPaginas >= 3 && pagina != 2);
        btnSiguiente.setEnabled(pagina < totalPaginas - 1);
    }

    private void buscarMonitor() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty() || texto.equalsIgnoreCase("Buscar por ID, usuario, nombre")) {
            mostrarPagina(0);
            return;
        }
        String lower = texto.toLowerCase();
        modeloEstadoEntregas.setRowCount(0);
        for (String[] c : listaAuditoria) {
            String id      = c[0];
            String usuario = c[1].toLowerCase();
            String accion  = c[2].toLowerCase();
            if (id.contains(lower) || usuario.contains(lower) || accion.contains(lower)) {
                modeloEstadoEntregas.addRow(new Object[]{c[0], c[1], c[2], c[3]});
            }
        }
        // Deshabilitar paginación mientras se muestra un resultado filtrado
        btnPagina1.setEnabled(false);
        btnPagina2.setEnabled(false);
        btnPagina3.setEnabled(false);
        btnSiguiente.setEnabled(false);
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
