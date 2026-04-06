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
 * Pantalla de gestión de paquetes asignados al conductor del sistema QuickDelivery S.A.
 *
 * @author Eryan / QuickDelivery S.A.
 */
public class MisPaquetesFrame extends JFrame {

    private JPanel panelHeader;
    private JPanel panelSidebar;
    private JPanel panelContenido;

    private JLabel lblLogo;
    private JLabel lblUsuarioHeader;
    private JButton btnCerrarSesion;

    private JButton btnDashboard;
    private JButton btnMisPaquetes;
    private JButton btnIncidencias;

    private JTable tablaPaquetes;
    private DefaultTableModel modeloTabla;

    private JComboBox<String> cmbPaquete;
    private JComboBox<String> cmbEstadoActualizado;
    private JTextField txtNotas;

    private JButton btnGuardar;
    private JButton btnCancelar;

    private final Usuario usuarioActual;
    private List<String[]> listaPaquetes = new ArrayList<>();

    public MisPaquetesFrame(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("Mis Paquetes");
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

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(fondoGeneral);

        construirHeader(azulHeader);
        construirSidebar(turquesaMenu, turquesaOscuro);
        construirContenido(fondoGeneral, fondoPanel, bordePanel, grisCampo, turquesaBoton, azulBoton);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelSidebar, BorderLayout.WEST);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
        setLocationRelativeTo(null);

        cargarPaquetes();
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

        btnDashboard  = crearBotonMenu("Dashboard",   turquesaMenu,   Color.WHITE);
        btnMisPaquetes = crearBotonMenu("Mis Paquetes", turquesaOscuro, Color.WHITE);
        btnIncidencias = crearBotonMenu("Incidencias", turquesaMenu,   Color.WHITE);

        btnDashboard.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new MenuConductorFrame(usuarioActual).setVisible(true));
        });
        btnMisPaquetes.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Ya estás en Mis Paquetes."));
        btnIncidencias.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new IncidenciasFrame(usuarioActual).setVisible(true));
        });

        panelSidebar.add(franja);
        panelSidebar.add(Box.createVerticalStrut(10));
        panelSidebar.add(btnDashboard);
        panelSidebar.add(btnMisPaquetes);
        panelSidebar.add(btnIncidencias);
        panelSidebar.add(Box.createVerticalGlue());
    }

    private void construirContenido(Color fondoGeneral, Color fondoPanel, Color bordePanel,
            Color grisCampo, Color turquesaBoton, Color azulBoton) {

        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(fondoGeneral);
        panelContenido.setBorder(new EmptyBorder(18, 26, 18, 26));

        JPanel panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        JPanel panelTabla     = crearPanelTabla(fondoPanel, bordePanel);
        JPanel panelFormulario = crearPanelFormulario(fondoPanel, bordePanel, grisCampo, turquesaBoton, azulBoton);

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
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        JLabel titulo = new JLabel("Mis paquetes asignados (En Tránsito)");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setBorder(new EmptyBorder(0, 0, 12, 10));

        String[] columnas = {"ID", "Código", "Descripción", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaPaquetes = new JTable(modeloTabla);
        tablaPaquetes.setRowHeight(32);
        tablaPaquetes.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaPaquetes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tablaPaquetes.getTableHeader().setReorderingAllowed(false);

        tablaPaquetes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarDatosSeleccionados();
        });

        JScrollPane scroll = new JScrollPane(tablaPaquetes);
        scroll.setBorder(BorderFactory.createLineBorder(bordePanel, 1));

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelFormulario(Color fondoPanel, Color bordePanel, Color grisCampo,
            Color turquesaBoton, Color azulBoton) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(fondoPanel);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordePanel, 1),
                new EmptyBorder(14, 22, 14, 22)));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 235));

        JLabel titulo = new JLabel("Actualizar paquete");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setBorder(new EmptyBorder(0, 0, 18, 0));

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 16);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblPaquete = new JLabel("Seleccionar Paquete:");
        lblPaquete.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.20;
        centro.add(lblPaquete, gbc);

        cmbPaquete = new JComboBox<>(new String[]{"Seleccione el paquete"});
        cmbPaquete.setBackground(grisCampo);
        cmbPaquete.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.40;
        centro.add(cmbPaquete, gbc);

        JLabel lblEstado = new JLabel("Estado actualizado:");
        lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.20;
        centro.add(lblEstado, gbc);

        cmbEstadoActualizado = new JComboBox<>(new String[]{
            "Seleccione el estado", "En Tránsito", "Entregado", "Incidencia"
        });
        cmbEstadoActualizado.setBackground(grisCampo);
        cmbEstadoActualizado.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.40;
        centro.add(cmbEstadoActualizado, gbc);

        JLabel lblNotas = new JLabel("Notas:");
        lblNotas.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.20;
        centro.add(lblNotas, gbc);

        txtNotas = new JTextField();
        txtNotas.setBackground(grisCampo);
        txtNotas.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtNotas.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(170, 170, 170), 1),
                new EmptyBorder(6, 10, 6, 10)));
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.40;
        centro.add(txtNotas, gbc);

        JPanel panelBotones = new JPanel(new BorderLayout());
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(14, 0, 0, 0));

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(turquesaBoton);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnGuardar.addActionListener(e -> guardarCambios());

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(azulBoton);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnCancelar.addActionListener(e -> limpiarFormulario());

        panelBotones.add(btnGuardar, BorderLayout.WEST);
        panelBotones.add(btnCancelar, BorderLayout.EAST);

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
        modeloTabla.setRowCount(0);
        listaPaquetes.clear();
        cmbPaquete.removeAllItems();
        cmbPaquete.addItem("Seleccione el paquete");
        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();
            // LIST|PAQUETES|id|codigo|descripcion|peso|estado~...  (EN_TRANSITO solamente)
            String resp = cs.enviarYEsperar("GET_MIS_PAQUETES|" + usuarioActual.getId());
            if (resp != null && resp.startsWith("LIST|PAQUETES")) {
                String[] partes = resp.split("\\|", 3);
                if (partes.length == 3 && !partes[2].isEmpty()) {
                    for (String fila : partes[2].split("~")) {
                        if (fila.startsWith("|")) fila = fila.substring(1);
                        String[] c = fila.split("\\|");
                        if (c.length < 5) continue;
                        listaPaquetes.add(c);
                        modeloTabla.addRow(new Object[]{c[0], c[1], c[2], c[4]});
                        cmbPaquete.addItem("Paquete #" + c[0] + " - " + c[1]);
                    }
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar paquetes: " + ex.getMessage());
        }
    }

    private void cargarDatosSeleccionados() {
        int fila = tablaPaquetes.getSelectedRow();
        if (fila == -1) return;

        String idStr = modeloTabla.getValueAt(fila, 0).toString();
        String[] c = null;
        for (String[] p : listaPaquetes) {
            if (p[0].equals(idStr)) { c = p; break; }
        }
        if (c == null) return;

        for (int i = 1; i < cmbPaquete.getItemCount(); i++) {
            if (cmbPaquete.getItemAt(i).contains("#" + c[0] + " ")) {
                cmbPaquete.setSelectedIndex(i);
                break;
            }
        }
        // c[4] = estado enum name
        switch (c[4]) {
            case "EN_TRANSITO": cmbEstadoActualizado.setSelectedItem("En Tránsito"); break;
            case "ENTREGADO":   cmbEstadoActualizado.setSelectedItem("Entregado");   break;
            case "INCIDENCIA":  cmbEstadoActualizado.setSelectedItem("Incidencia");  break;
            default:            cmbEstadoActualizado.setSelectedIndex(0);            break;
        }
        txtNotas.setText(c[2]); // descripcion
    }

    private void guardarCambios() {
        int fila = tablaPaquetes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paquete en la tabla.");
            return;
        }
        if (cmbEstadoActualizado.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un estado válido.");
            return;
        }

        String idStr = modeloTabla.getValueAt(fila, 0).toString();
        String nuevoEstado;
        switch (cmbEstadoActualizado.getSelectedItem().toString()) {
            case "Entregado":  nuevoEstado = "ENTREGADO";   break;
            case "Incidencia": nuevoEstado = "INCIDENCIA";  break;
            default:           nuevoEstado = "EN_TRANSITO"; break;
        }

        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) cs.conectar();
            String resp = cs.enviarYEsperar("ESTADO|" + idStr + "|" + nuevoEstado);
            if (resp != null && resp.startsWith("OK")) {
                JOptionPane.showMessageDialog(this, "Paquete actualizado correctamente.");
                cargarPaquetes();
                limpiarFormulario();
            } else {
                String msg = (resp != null && resp.contains("|")) ? resp.split("\\|", 2)[1] : "Error";
                JOptionPane.showMessageDialog(this, "Error: " + msg);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
        }
    }

    private void limpiarFormulario() {
        tablaPaquetes.clearSelection();
        cmbPaquete.setSelectedIndex(0);
        cmbEstadoActualizado.setSelectedIndex(0);
        txtNotas.setText("");
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
