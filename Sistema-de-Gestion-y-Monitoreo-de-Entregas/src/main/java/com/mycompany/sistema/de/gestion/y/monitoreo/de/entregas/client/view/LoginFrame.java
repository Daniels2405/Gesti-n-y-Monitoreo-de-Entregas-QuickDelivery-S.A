package com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.client.view;

import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.client.util.ConexionServidor;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Rol;
import com.mycompany.sistema.de.gestion.y.monitoreo.de.entregas.model.Usuario;

import java.io.IOException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * Pantalla de inicio de sesión del sistema QuickDelivery S.A.
 *
 * @author Eryan / QuickDelivery S.A.
 */
public class LoginFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIniciarSesion;
    private JButton btnMostrarOcultar;
    private JLabel lblMensaje;
    private JLabel lblLogo;

    private boolean contrasenaVisible = false;
    private char echoOriginal;


    public LoginFrame() {
        setTitle("Login – QuickDelivery S.A.");
        initComponents();
        configurarVentana();
        setLocationRelativeTo(null);
    }

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1120, 780);
        setMinimumSize(new Dimension(980, 680));
    }

    private void initComponents() {
        Color colorFondoVentana = new Color(93, 93, 93);
        Color colorFondoContenido = new Color(239, 239, 239);
        Color colorCampo = new Color(214, 214, 214);
        Color colorBoton = new Color(10, 24, 117);
        Color colorTexto = new Color(30, 30, 30);
        Color colorError = new Color(204, 51, 51);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(colorFondoVentana);
        panelPrincipal.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setBackground(colorFondoContenido);
        panelContenido.setBorder(BorderFactory.createLineBorder(colorFondoContenido, 8));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(6, 0, 6, 0);

        lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        cargarLogo();
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 28, 0);
        panelContenido.add(lblLogo, gbc);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblUsuario.setForeground(colorTexto);
        lblUsuario.setHorizontalAlignment(SwingConstants.LEFT);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 6, 0);
        panelContenido.add(wrapWithWidth(lblUsuario, 380), gbc);

        txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(380, 34));
        txtUsuario.setFont(new Font("SansSerif", Font.PLAIN, 15));
        txtUsuario.setBackground(colorCampo);
        txtUsuario.setForeground(colorTexto);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorCampo, 1),
                new EmptyBorder(6, 10, 6, 10)));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 14, 0);
        panelContenido.add(wrapWithWidth(txtUsuario, 380), gbc);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblContrasena.setForeground(colorTexto);
        lblContrasena.setHorizontalAlignment(SwingConstants.LEFT);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 6, 0);
        panelContenido.add(wrapWithWidth(lblContrasena, 380), gbc);

        JPanel panelPassword = new JPanel(new BorderLayout());
        panelPassword.setOpaque(false);
        panelPassword.setPreferredSize(new Dimension(380, 34));

        txtContrasena = new JPasswordField();
        txtContrasena.setFont(new Font("SansSerif", Font.PLAIN, 15));
        txtContrasena.setBackground(colorCampo);
        txtContrasena.setForeground(colorTexto);
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorCampo, 1),
                new EmptyBorder(6, 10, 6, 10)));
        echoOriginal = txtContrasena.getEchoChar();

        btnMostrarOcultar = new JButton("👁");
        btnMostrarOcultar.setFocusable(false);
        btnMostrarOcultar.setBackground(colorCampo);
        btnMostrarOcultar.setForeground(Color.WHITE);
        btnMostrarOcultar.setBorder(BorderFactory.createLineBorder(colorCampo, 1));
        btnMostrarOcultar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMostrarOcultar.setPreferredSize(new Dimension(44, 34));
        btnMostrarOcultar.addActionListener(e -> alternarContrasena());

        panelPassword.add(txtContrasena, BorderLayout.CENTER);
        panelPassword.add(btnMostrarOcultar, BorderLayout.EAST);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 14, 0);
        panelContenido.add(wrapWithWidth(panelPassword, 380), gbc);

        lblMensaje = new JLabel(" ");
        lblMensaje.setForeground(colorError);
        lblMensaje.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 10, 0);
        panelContenido.add(lblMensaje, gbc);

        btnIniciarSesion = new JButton("Iniciar Sesión");
        btnIniciarSesion.setPreferredSize(new Dimension(204, 36));
        btnIniciarSesion.setBackground(colorBoton);
        btnIniciarSesion.setForeground(Color.WHITE);
        btnIniciarSesion.setFocusPainted(false);
        btnIniciarSesion.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnIniciarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIniciarSesion.addActionListener(e -> iniciarSesion());
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 30, 0);
        panelContenido.add(btnIniciarSesion, gbc);

        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        setContentPane(panelPrincipal);
    }

    private JPanel wrapWithWidth(JComponent component, int width) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(width, component.getPreferredSize().height));
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private void cargarLogo() {
        URL rutaImagen = getClass().getResource("/images/logo_quickdelivery.png");
        if (rutaImagen != null) {
            ImageIcon iconoOriginal = new ImageIcon(rutaImagen);
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(420, 190, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(imagenEscalada));
            lblLogo.setText("");
        } else {
            lblLogo.setText("QUICKDELIVERY S.A.");
            lblLogo.setFont(new Font("SansSerif", Font.BOLD, 32));
            lblLogo.setForeground(Color.BLACK);
        }
    }

    private void alternarContrasena() {
        contrasenaVisible = !contrasenaVisible;
        txtContrasena.setEchoChar(contrasenaVisible ? (char) 0 : echoOriginal);
    }

    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword()).trim();

        lblMensaje.setText(" ");

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            lblMensaje.setText("Debe ingresar usuario y contraseña.");
            return;
        }

        try {
            ConexionServidor cs = ConexionServidor.getInstancia();
            if (!cs.isConectado()) {
                cs.conectar();
            }
            String respuesta = cs.enviarYEsperar("LOGIN_APP|" + usuario + "|" + contrasena);
            if (respuesta == null || !respuesta.startsWith("DATA")) {
                String msg = (respuesta != null && respuesta.contains("|"))
                        ? respuesta.split("\\|", 2)[1]
                        : "Sin respuesta del servidor";
                lblMensaje.setText(msg);
                return;
            }
            // DATA|id|nombre|username|rol|estado
            String[] p = respuesta.split("\\|");
            int id = Integer.parseInt(p[1]);
            String nombre = p[2];
            String username = p[3];
            Rol rol = Rol.fromString(p[4]);
            String estado = p[5];
            Usuario u = new Usuario(id, nombre, username, null, estado, rol);
            abrirPanelSegunRol(u);
        } catch (IOException ex) {
            lblMensaje.setText("Error de conexión con el servidor.");
        }
    }

    private void abrirPanelSegunRol(Usuario u) {
        switch (u.getRol()) {
            case ADMINISTRADOR:
                SwingUtilities.invokeLater(() -> new MenuAdminFrame(u).setVisible(true));
                break;
            case DESPACHADOR:
                SwingUtilities.invokeLater(() -> new MenuDespachadorFrame(u).setVisible(true));
                break;
            case CONDUCTOR:
                SwingUtilities.invokeLater(() -> new MenuConductorFrame(u).setVisible(true));
                break;
            default:
                JOptionPane.showMessageDialog(this, "Rol no reconocido.");
                return;
        }
        dispose();
    }
}
