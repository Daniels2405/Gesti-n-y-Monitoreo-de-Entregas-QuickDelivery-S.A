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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Pantalla de registro y consulta de incidencias del sistema QuickDelivery S.A.
 *
 * @author Eryan / QuickDelivery S.A.
 */
public class IncidenciasFrame extends JFrame {

    private JPanel panelHeader;
    private JPanel panelSidebar;
    private JPanel panelContenido;

    private JLabel lblLogo;
    private JLabel lblUsuarioHeader;
    private JButton btnCerrarSesion;

    private JButton btnDashboard;
    private JButton btnMisPaquetes;
    private JButton btnIncidencias;

    private JTable tablaIncidencias;
    private DefaultTableModel modeloTabla;

    private JComboBox<String> cmbPaquete;
    private JComboBox<String> cmbTipoIncidencia;
    private JTextField txtDescripcion;

    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton btnBorrar;

    private final Usuario usuarioActual;
    private List<String[]> listaPaquetes = new ArrayList<>();
    private List<String[]> listaIncidencias = new ArrayList<>();

    public IncidenciasFrame(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("Incidencias");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 760);
        setMinimumSize(new Dimension(1100, 700));

        Color azulHeader    = new Color(20, 37, 160);
        Color turquesaMenu  = new Color(16, 166, 171);
        Color turquesaOscuro = new Color(0, 101, 103);
        Color fondoGeneral  = new Color(231, 231, 231);
        Color fondoPanel    = Color.WHITE;
        Color bordePanel    = new Color(190, 190, 190);
        Color grisCampo     = new Color(212, 212, 212);
        Color turquesaBoton = new Color(16, 166, 171);
        Color azulBoton     = new Color(20, 37, 160);
        Color rojoBoton     = new Color(255, 16, 5);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(fondoGeneral);

        construirHeader(azulHeader);
        construirSidebar(turquesaMenu, turquesaOscuro);
        construirContenido(fondoGeneral, fondoPanel, bordePanel, grisCampo, turquesaBoton, azulBoton, rojoBoton);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelSidebar, BorderLayout.WEST);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
        setLocationRelativeTo(null);

        cargarPaquetes();
        cargarIncidencias();
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

        lblUsuarioHeader = new JLabel(" " + usuarioActual.getNombre());
        lblUsuarioHeader.setForeground(Color.WHITE);
        lblUsuarioHeader.setFont(new Font("SansSerif", Font.PLAIN, 20));

        btnCerrarSesion = new JButton(" Cerrar Sesión");
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

        btnDashboard  = crearBotonMenu("Dashboard",    turquesaMenu,   Color.WHITE);
        btnMisPaquetes = crearBotonMenu("Mis Paquetes", turquesaMenu,   Color.WHITE);
        btnIncidencias = crearBotonMenu("Incidencias",  turquesaOscuro, Color.WHITE);

        btnDashboard.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new MenuConductorFrame(usuarioActual).setVisible(true));
        });
        btnMisPaquetes.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new MisPaquetesFrame(usuarioActual).setVisible(true));
        });
        btnIncidencias.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Ya estás en Incidencias."));

        panelSidebar.add(franja);
        panelSidebar.add(Box.createVerticalStrut(10));
        panelSidebar.add(btnDashboard);
        panelSidebar.add(btnMisPaquetes);
        panelSidebar.add(btnIncidencias);
        panelSidebar.add(Box.createVerticalGlue());
    }

    private void construirContenido(Color fondoGeneral, Color fondoPanel, Color bordePanel,
                                    Color grisCampo, Color turquesaBoton, Color azulBoton, Color rojoBoton) {

        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(fondoGeneral);
        panelContenido.setBorder(new EmptyBorder(18, 26, 18, 26));

        JPanel panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        JPanel panelTabla     = crearPanelTabla(fondoPanel, bordePanel);
        JPanel panelFormulario = crearPanelFormulario(fondoPanel, bordePanel, grisCampo, turquesaBoton, azulBoton, rojoBoton);

        panelCentral.add(panelTabla);
        panelCentral.add(Box.createVerticalStrut(24));
        panelCentral.add(panelFormulario);

        panelContenido.add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel crearPanelTabla(Color fondoPanel, Color bordePanel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(fondoPanel);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordePanel, 1),
                new EmptyBorder(16, 16, 18, 16)));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        JLabel titulo = new JLabel("Incidencias Registradas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setBorder(new EmptyBorder(0, 0, 14, 0));

        String[] columnas = {"ID", "ID Paquete", "Descripción", "Fecha"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaIncidencias = new JTable(modeloTabla);
        tablaIncidencias.setRowHeight(32);
        tablaIncidencias.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaIncidencias.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tablaIncidencias.getTableHeader().setReorderingAllowed(false);

        tablaIncidencias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarDatosSeleccionados();
        });

        JScrollPane scroll = new JScrollPane(tablaIncidencias);
        scroll.setBorder(BorderFactory.createLineBorder(bordePanel, 1));

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelFormulario(Color fondoPanel, Color bordePanel, Color grisCampo,
                                        Color turquesaBoton, Color azulBoton, Color rojoBoton) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(fondoPanel);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordePanel, 1),
                new EmptyBorder(14, 22, 14, 22)));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 245));

        JLabel titulo = new JLabel("Registrar Incidencia");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setBorder(new EmptyBorder(0, 0, 18, 0));

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 0, 8, 16);
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        JLabel lblPaquete = new JLabel("Seleccionar Paquete:");
        lblPaquete.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.20;
        centro.add(lblPaquete, gbc);

        cmbPaquete = new JComboBox<>(new String[]{"Seleccione el paquete"});
        cmbPaquete.setBackground(grisCampo);
        cmbPaquete.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.40;
        centro.add(cmbPaquete, gbc);

        JLabel lblTipo = new JLabel("Tipo de Incidencia:");
        lblTipo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.20;
        centro.add(lblTipo, gbc);

        cmbTipoIncidencia = new JComboBox<>(new String[]{
                "Seleccione la incidencia",
                "Choque",
                "Retraso por tráfico",
                "Cancelamiento",
                "Avería",
                "Dirección incorrecta"
        });
        cmbTipoIncidencia.setBackground(grisCampo);
        cmbTipoIncidencia.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.40;
        centro.add(cmbTipoIncidencia, gbc);

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.20;
        centro.add(lblDescripcion, gbc);

        txtDescripcion = new JTextField();
        txtDescripcion.setBackground(grisCampo);
        txtDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(170, 170, 170), 1),
                new EmptyBorder(6, 10, 6, 10)));
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.40;
        centro.add(txtDescripcion, gbc);

        JPanel panelBotones = new JPanel(new BorderLayout());
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(14, 0, 0, 0));

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(turquesaBoton);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnGuardar.addActionListener(e -> guardarIncidencia());

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(azulBoton);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnCancelar.addActionListener(e -> limpiarFormulario());

        btnBorrar = new JButton("Borrar");
        btnBorrar.setBackground(rojoBoton);
        btnBorrar.setForeground(Color.WHITE);
        btnBorrar.setFocusPainted(false);
        btnBorrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBorrar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnBorrar.addActionListener(e -> borrarIncidencia());

        JPanel derecha = new JPanel();
        derecha.setOpaque(false);
        derecha.setLayout(new BoxLayout(derecha, BoxLayout.X_AXIS));
        derecha.add(btnCancelar);
        derecha.add(Box.createHorizontalStrut(10));
        derecha.add(btnBorrar);

        panelBotones.add(btnGuardar, BorderLayout.WEST);
        panelBotones.add(derecha, BorderLayout.EAST);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(centro, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

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

    private void cargarPaquetes() {
        cmbPaquete.removeAllItems();
        cmbPaquete.addItem("Seleccione el paquete");
        listaPaquetes.clear();
        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();
            // LIST|PAQUETES|id|codigo|descripcion|peso|estado~...
            String resp = cs.enviarYEsperar("GET_PAQUETES_ESTADO|EN_TRANSITO");
            if (resp != null && resp.startsWith("LIST|PAQUETES")) {
                String[] partes = resp.split("\\|", 3);
                if (partes.length == 3 && !partes[2].isEmpty()) {
                    for (String fila : partes[2].split("~")) {
                        String[] c = fila.split("\\|");
                        if (c.length < 2) continue;
                        listaPaquetes.add(c);
                        cmbPaquete.addItem("Paquete #" + c[0] + " - " + c[1]);
                    }
                }
            }
        } catch (IOException ex) {
            // Si falla, el combo queda solo con el placeholder
        }
    }

    private void cargarIncidencias() {
        modeloTabla.setRowCount(0);
        listaIncidencias.clear();
        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();
            // LIST|INCIDENCIAS|id|descripcion|idPaquete|idConductor|fecha~...
            String resp = cs.enviarYEsperar("GET_INCIDENCIAS");
            if (resp != null && resp.startsWith("LIST|INCIDENCIAS")) {
                String[] partes = resp.split("\\|", 3);
                if (partes.length == 3 && !partes[2].isEmpty()) {
                    for (String fila : partes[2].split("~")) {
                        String[] c = fila.split("\\|");
                        if (c.length < 5) continue;
                        listaIncidencias.add(c);
                        String fecha = c[4].length() >= 16 ? c[4].substring(0, 16).replace("T", " ") : c[4];
                        modeloTabla.addRow(new Object[]{c[0], c[2], c[1], fecha});
                    }
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar incidencias: " + ex.getMessage());
        }
    }

    private void cargarDatosSeleccionados() {
        int fila = tablaIncidencias.getSelectedRow();
        if (fila == -1) return;

        String idPaquete = modeloTabla.getValueAt(fila, 1).toString();
        String descripcion = modeloTabla.getValueAt(fila, 2).toString();

        for (int i = 1; i < cmbPaquete.getItemCount(); i++) {
            if (cmbPaquete.getItemAt(i).contains("#" + idPaquete + " ")) {
                cmbPaquete.setSelectedIndex(i);
                break;
            }
        }
        txtDescripcion.setText(descripcion);
    }

    private void guardarIncidencia() {
        if (cmbPaquete.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un paquete válido.");
            return;
        }
        if (cmbTipoIncidencia.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un tipo de incidencia.");
            return;
        }
        String descripcionTexto = txtDescripcion.getText().trim();
        if (descripcionTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la descripción.");
            return;
        }

        int idxPaquete = cmbPaquete.getSelectedIndex() - 1;
        if (idxPaquete < 0 || idxPaquete >= listaPaquetes.size()) {
            JOptionPane.showMessageDialog(this, "Paquete no válido.");
            return;
        }
        String idPaquete = listaPaquetes.get(idxPaquete)[0];
        String tipo = cmbTipoIncidencia.getSelectedItem().toString();
        // Sanitizar '|' para no romper el protocolo TCP
        String descripcionCompleta = (tipo + ": " + descripcionTexto).replace("|", " - ");

        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();
            String resp = cs.enviarYEsperar("CREATE_INCIDENCIA|" + descripcionCompleta
                    + "|" + idPaquete + "|" + usuarioActual.getId());
            if (resp != null && resp.startsWith("OK")) {
                JOptionPane.showMessageDialog(this, "Incidencia registrada correctamente.",
                        "Incidencias", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarIncidencias();
            } else {
                String msg = (resp != null && resp.contains("|")) ? resp.split("\\|", 2)[1] : "Error";
                JOptionPane.showMessageDialog(this, "Error: " + msg);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar incidencia: " + ex.getMessage());
        }
    }

    private void borrarIncidencia() {
        int fila = tablaIncidencias.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una incidencia en la tabla para borrar.");
            return;
        }
        String idStr = modeloTabla.getValueAt(fila, 0).toString();
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea borrar la incidencia seleccionada?",
                "Confirmar borrado", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            try {
                ConexionServidor cs = ConexionServidor.getInstancia();
                if (!cs.isConectado()) cs.conectar();
                String resp = cs.enviarYEsperar("DELETE_INCIDENCIA|" + idStr);
                if (resp != null && resp.startsWith("OK")) {
                    JOptionPane.showMessageDialog(this, "Incidencia eliminada correctamente.",
                            "Incidencias", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                    cargarIncidencias();
                } else {
                    String msg = (resp != null && resp.contains("|")) ? resp.split("\\|", 2)[1] : "Error";
                    JOptionPane.showMessageDialog(this, "Error: " + msg);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
            }
        }
    }

    private void limpiarFormulario() {
        cmbPaquete.setSelectedIndex(0);
        cmbTipoIncidencia.setSelectedIndex(0);
        txtDescripcion.setText("");
        tablaIncidencias.clearSelection();
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
