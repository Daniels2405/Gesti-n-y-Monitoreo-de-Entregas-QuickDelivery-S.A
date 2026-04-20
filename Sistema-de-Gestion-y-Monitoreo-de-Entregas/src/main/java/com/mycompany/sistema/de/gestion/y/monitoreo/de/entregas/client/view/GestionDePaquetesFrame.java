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
 * Pantalla de gestión de paquetes del sistema QuickDelivery S.A.
 *
 * @author Eryan / QuickDelivery S.A.
 */
public class GestionDePaquetesFrame extends JFrame {

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

    private JTable tablaPaquetes;
    private DefaultTableModel modeloTabla;

    private final Usuario usuarioActual;
    private List<Paquete> listaPaquetes = new ArrayList<>();

    public GestionDePaquetesFrame(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("Gestión de Paquetes");
        initComponents();
        configurarVentana();
        setLocationRelativeTo(null);
        cargarPaquetes();
    }

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 760);
        setMinimumSize(new Dimension(1100, 700));
    }

    private void initComponents() {
        Color azulHeader = new Color(24, 40, 159);
        Color turquesaMenu = new Color(55, 165, 168);
        Color turquesaOscuro = new Color(30, 98, 97);
        Color fondoGeneral = new Color(230, 230, 230);
        Color fondoPanel = Color.WHITE;
        Color colorBorde = new Color(190, 190, 190);
        Color colorBusqueda = new Color(58, 167, 169);
        Color azulBoton = new Color(24, 40, 159);
        Color rojoBoton = new Color(255, 28, 17);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(fondoGeneral);

        construirHeader(azulHeader);
        construirSidebar(turquesaMenu, turquesaOscuro);
        construirContenido(fondoGeneral, fondoPanel, colorBorde, colorBusqueda, azulBoton, rojoBoton);

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

        btnCerrarSesion = new JButton("Cerrar Sesión");
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

        btnDashboard  = crearBotonMenu("Dashboard",  turquesaMenu,   Color.WHITE);
        btnPaquetes   = crearBotonMenu("Paquetes",   turquesaOscuro, Color.WHITE);
        btnAsignacion = crearBotonMenu("Asignación", turquesaMenu,   Color.WHITE);
        btnMonitor    = crearBotonMenu("Monitor",    turquesaMenu,   Color.WHITE);

        btnDashboard.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new MenuDespachadorFrame(usuarioActual).setVisible(true));
        });
        btnPaquetes.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Ya estás en Gestión de Paquetes."));
        btnAsignacion.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new AsignacionDePaquetesFrame(usuarioActual).setVisible(true));
        });
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
                                    Color colorBusqueda, Color azulBoton, Color rojoBoton) {

        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(fondoGeneral);
        panelContenido.setBorder(new EmptyBorder(14, 26, 18, 26));

        JPanel panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Gestión de Paquetes");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(70, 70, 70));
        lblTitulo.setBorder(new EmptyBorder(0, 10, 14, 0));

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

        btnBuscar   = crearBotonIcono("⌕", azulBoton);
        btnAgregar  = crearBotonIcono("⊕", azulBoton);
        btnEditar   = crearBotonIcono("✎", azulBoton);
        btnEliminar = crearBotonIcono("🗑", rojoBoton);

        btnBuscar.addActionListener(e -> buscarPaquete());
        btnAgregar.addActionListener(e -> agregarPaquete());
        btnEditar.addActionListener(e -> editarPaquete());
        btnEliminar.addActionListener(e -> eliminarPaquete());

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

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(fondoPanel);
        panelTabla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 1),
                new EmptyBorder(12, 16, 10, 16)));

        JLabel lblSubtitulo = new JLabel("Lista de paquetes registrados");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSubtitulo.setBorder(new EmptyBorder(0, 0, 10, 0));

        String[] columnas = {"ID", "Código", "Descripción", "Peso (kg)", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaPaquetes = new JTable(modeloTabla);
        tablaPaquetes.setRowHeight(32);
        tablaPaquetes.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaPaquetes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tablaPaquetes.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tablaPaquetes);
        scroll.setBorder(BorderFactory.createLineBorder(colorBorde, 1));

        panelTabla.add(lblSubtitulo, BorderLayout.NORTH);
        panelTabla.add(scroll, BorderLayout.CENTER);

        panelCentral.add(lblTitulo);
        panelCentral.add(panelBusqueda);
        panelCentral.add(Box.createVerticalStrut(18));
        panelCentral.add(panelTabla);

        panelContenido.add(panelCentral, BorderLayout.CENTER);
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

    private void cargarPaquetes() {
        modeloTabla.setRowCount(0);
        listaPaquetes.clear();
        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();
            String respuesta = cs.enviarYEsperar("GET_PAQUETES");
            if (respuesta == null || !respuesta.startsWith("LIST")) {
                JOptionPane.showMessageDialog(this, "Error al cargar paquetes: " +
                        (respuesta != null ? respuesta : "Sin respuesta"));
                return;
            }
            // LIST|PAQUETES|id|codigo|descripcion|peso|estado~|id|codigo|...
            String[] partes = respuesta.split("\\|", 3);
            if (partes.length < 3 || partes[2].isEmpty()) return;
            for (String fila : partes[2].split("~")) {
                if (fila.startsWith("|")) fila = fila.substring(1);
                String[] c = fila.split("\\|");
                if (c.length < 5) continue;
                // c[0]=id, c[1]=codigo, c[2]=descripcion, c[3]=peso, c[4]=estado(enum name)
                Paquete p = new Paquete(Integer.parseInt(c[0]), c[1], c[2], Double.parseDouble(c[3]), EstadoPaquete.valueOf(c[4]));
                listaPaquetes.add(p);
                modeloTabla.addRow(new Object[]{p.getId(), p.getCodigo(), p.getDescripcion(), p.getPeso(), p.getEstado().toDbString()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    private void buscarPaquete() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty() || texto.equalsIgnoreCase("Buscar por ID, código, descripción")) {
            cargarPaquetes();
            return;
        }
        String lower = texto.toLowerCase();
        modeloTabla.setRowCount(0);
        for (Paquete p : listaPaquetes) {
            if (String.valueOf(p.getId()).contains(lower)
                    || p.getCodigo().toLowerCase().contains(lower)
                    || p.getDescripcion().toLowerCase().contains(lower)) {
                modeloTabla.addRow(new Object[]{
                    p.getId(), p.getCodigo(), p.getDescripcion(),
                    p.getPeso(), p.getEstado().toDbString()
                });
            }
        }
    }

    private void eliminarPaquete() {
        int fila = tablaPaquetes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paquete de la tabla para eliminar.");
            return;
        }
        String codigo = modeloTabla.getValueAt(fila, 1).toString();
        int id = (int) modeloTabla.getValueAt(fila, 0);

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar el paquete " + codigo + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                String resp = ConexionServidor.getInstancia().enviarYEsperar("DELETE_PAQUETE|" + id);
                if (resp != null && resp.startsWith("OK")) {
                    cargarPaquetes();
                    JOptionPane.showMessageDialog(this, "Paquete eliminado correctamente.");
                } else {
                    String msg = (resp != null && resp.contains("|")) ? resp.split("\\|", 2)[1] : "Error al eliminar";
                    JOptionPane.showMessageDialog(this, "Error: " + msg);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
            }
        }
    }

    private void agregarPaquete() {
        javax.swing.JTextField txtCodigo = new javax.swing.JTextField(20);
        javax.swing.JTextField txtDescripcion = new javax.swing.JTextField(20);
        javax.swing.JTextField txtPeso = new javax.swing.JTextField(10);

        JPanel form = new JPanel();
        form.setLayout(new java.awt.GridLayout(3, 2, 8, 8));
        form.add(new JLabel("Código:"));       form.add(txtCodigo);
        form.add(new JLabel("Descripción:"));  form.add(txtDescripcion);
        form.add(new JLabel("Peso (kg):"));    form.add(txtPeso);

        int resultado = JOptionPane.showConfirmDialog(this, form,
                "Nuevo Paquete", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (resultado != JOptionPane.OK_OPTION) return;

        String codigo = txtCodigo.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String pesoStr = txtPeso.getText().trim();

        if (codigo.isEmpty() || descripcion.isEmpty() || pesoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }
        double peso;
        try {
            peso = Double.parseDouble(pesoStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El peso debe ser un número válido.");
            return;
        }

        try {
            String resp = ConexionServidor.getInstancia().enviarYEsperar(
                    "CREATE_PAQUETE|" + codigo + "|" + descripcion + "|" + peso);
            if (resp != null && resp.startsWith("OK")) {
                cargarPaquetes();
                JOptionPane.showMessageDialog(this, "Paquete registrado correctamente.");
            } else {
                String msg = (resp != null && resp.contains("|")) ? resp.split("\\|", 2)[1] : "Error al registrar";
                JOptionPane.showMessageDialog(this, "Error: " + msg);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    private void editarPaquete() {
        int fila = tablaPaquetes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paquete de la tabla para editar.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        Paquete paquete = null;
        for (Paquete p : listaPaquetes) {
            if (p.getId() == id) { paquete = p; break; }
        }
        if (paquete == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el paquete seleccionado.");
            return;
        }

        javax.swing.JTextField txtCodigo = new javax.swing.JTextField(paquete.getCodigo(), 20);
        javax.swing.JTextField txtDescripcion = new javax.swing.JTextField(paquete.getDescripcion(), 20);
        javax.swing.JTextField txtPeso = new javax.swing.JTextField(String.valueOf(paquete.getPeso()), 10);

        JPanel form = new JPanel();
        form.setLayout(new java.awt.GridLayout(3, 2, 8, 8));
        form.add(new JLabel("Código:"));       form.add(txtCodigo);
        form.add(new JLabel("Descripción:"));  form.add(txtDescripcion);
        form.add(new JLabel("Peso (kg):"));    form.add(txtPeso);

        int resultado = JOptionPane.showConfirmDialog(this, form,
                "Editar Paquete #" + id, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (resultado != JOptionPane.OK_OPTION) return;

        String codigo = txtCodigo.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String pesoStr = txtPeso.getText().trim();

        if (codigo.isEmpty() || descripcion.isEmpty() || pesoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }
        double peso;
        try {
            peso = Double.parseDouble(pesoStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El peso debe ser un número válido.");
            return;
        }

        try {
            String resp = ConexionServidor.getInstancia().enviarYEsperar(
                    "UPDATE_PAQUETE|" + id + "|" + codigo + "|" + descripcion + "|" + peso);
            if (resp != null && resp.startsWith("OK")) {
                cargarPaquetes();
                JOptionPane.showMessageDialog(this, "Paquete actualizado correctamente.");
            } else {
                String msg = (resp != null && resp.contains("|")) ? resp.split("\\|", 2)[1] : "Error al actualizar";
                JOptionPane.showMessageDialog(this, "Error: " + msg);
            }
        } catch (IOException ex) {
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
