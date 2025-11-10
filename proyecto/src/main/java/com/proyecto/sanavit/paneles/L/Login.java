package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContraseña;
    private JButton btnIniciar, btnRegistrar, btnRecuperar;

    public Login() {
        setTitle("Inicio de Sesión - Sanavit");
        setSize(980, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === PANEL PRINCIPAL DIVIDIDO EN DOS ===
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2, 0, 0));

        /// === PANEL IZQUIERDO: IMAGEN ===
        JPanel panelIzquierdo = new JPanel() {
            private final Image imagen = new ImageIcon(
                // ✅ Carga la imagen desde resources, sin ruta absoluta
                getClass().getResource("/images/login.png")
            ).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Escalar la imagen al tamaño del panel
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelIzquierdo.setBackground(Color.WHITE);
        // === PANEL DERECHO: FORMULARIO ===
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBackground(Color.WHITE);
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(60, 80, 60, 80));

        // === ENCABEZADO ===
        JLabel lblTitulo = new JLabel("Sanavit IPS", SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(26, 94, 59));

        JLabel lblSubtitulo1 = new JLabel("Salud, naturaleza y vitalidad", SwingConstants.LEFT);
        lblSubtitulo1.setFont(new Font("Segoe UI", Font.BOLD,16));
        lblSubtitulo1.setForeground(new Color(26, 94, 59));


        JLabel lblSubtitulo2 = new JLabel("Bienvenido(a), por favor inicia sesión");
        lblSubtitulo2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitulo2.setForeground(new Color(70, 70, 70));

        JPanel panelEncabezado = new JPanel(new GridLayout(3, 1, 0, 5));
        panelEncabezado.setBackground(Color.WHITE);
        panelEncabezado.add(lblTitulo);
        panelEncabezado.add(lblSubtitulo1);
        panelEncabezado.add(lblSubtitulo2);

        panelDerecho.add(panelEncabezado, BorderLayout.NORTH);

        // === FORMULARIO DE LOGIN ===
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // --- Usuario ---
        gbc.gridy = 0;
        JLabel lblUsuario = new JLabel("Nombre de Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelFormulario.add(lblUsuario, gbc);

        gbc.gridy++;
        txtUsuario = new JTextField(20);
        txtUsuario.setPreferredSize(new Dimension(250, 35));
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        panelFormulario.add(txtUsuario, gbc);

        // --- Contraseña ---
        gbc.gridy++;
        JLabel lblContraseña = new JLabel("Contraseña");
        lblContraseña.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelFormulario.add(lblContraseña, gbc);

        gbc.gridy++;
        txtContraseña = new JPasswordField(20);
        txtContraseña.setPreferredSize(new Dimension(250, 35));
        txtContraseña.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtContraseña.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        panelFormulario.add(txtContraseña, gbc);

        // --- Checkbox Recordar sesión ---
        // gbc.gridy++;
        // JCheckBox chkRecordar = new JCheckBox("Recordar sesión");
        // chkRecordar.setBackground(Color.WHITE);
        // chkRecordar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        // chkRecordar.setForeground(new Color(80, 80, 80));
        // panelFormulario.add(chkRecordar, gbc);

        // --- Botón Ingresar ---
        gbc.gridy++;
        btnIniciar = new JButton("INGRESAR");
        btnIniciar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setBackground(new Color(26, 94, 59));
        btnIniciar.setFocusPainted(false);
        btnIniciar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelFormulario.add(btnIniciar, gbc);

        // --- Link de registro ---
        gbc.gridy++;
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelInferior.setBackground(Color.WHITE);
        JLabel lblCuenta = new JLabel("¿No tienes cuenta?");
        lblCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblCuenta.setForeground(new Color(90, 90, 90));

        btnRegistrar = new JButton("Regístrate");
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRegistrar.setForeground(new Color(26, 94, 59));
        btnRegistrar.setBackground(Color.WHITE);
        btnRegistrar.setBorder(null);
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelInferior.add(lblCuenta);
        panelInferior.add(btnRegistrar);
        panelFormulario.add(panelInferior, gbc);

        panelDerecho.add(panelFormulario, BorderLayout.CENTER);

        // === PIE: Recuperar contraseña ===
        JPanel panelRecuperar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelRecuperar.setBackground(Color.WHITE);
        btnRecuperar = new JButton("¿Olvidaste tu contraseña?");
        btnRecuperar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnRecuperar.setForeground(new Color(120, 120, 120));
        btnRecuperar.setBorder(null);
        btnRecuperar.setBackground(Color.WHITE);
        btnRecuperar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelRecuperar.add(btnRecuperar);
        panelDerecho.add(panelRecuperar, BorderLayout.SOUTH);

        // === AGREGAR PANELES AL PRINCIPAL ===
        panelPrincipal.add(panelIzquierdo);
        panelPrincipal.add(panelDerecho);
        add(panelPrincipal);

        // === FUNCIONALIDAD ORIGINAL ===
        btnIniciar.addActionListener(e -> iniciarSesion());
        btnRegistrar.addActionListener(e -> abrirRegistro());
        btnRecuperar.addActionListener(e -> mostrarDialogoRecuperacion());

        setVisible(true);
    }

    // === MÉTODOS LÓGICOS IGUALES A TU CÓDIGO ORIGINAL ===

    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContraseña.getPassword()).trim();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese usuario y contraseña.");
            return;
        }

        UsuarioDao usuarioDAO = new UsuarioDao();
        Usuario u = usuarioDAO.autenticarUsuario(usuario, contrasena);

        if (u != null) {
            String rol = u.getNombreRol() != null ? u.getNombreRol().trim().toLowerCase() : "";
            JOptionPane.showMessageDialog(this, "Bienvenido, " + u.getNombreUsuario() + " (" + rol + ")");

            switch (rol) {
                case "medico" -> abrirVentana(() -> new VentanaMedico(u));
                case "paciente" -> abrirVentana(() -> new VentanaPaciente(u));
                case "administrador" -> abrirVentana(() -> new VentanaAdministrador());
                case "gestor de citas" -> abrirVentana(() -> new VentanaGestorCitas(u));
                default -> JOptionPane.showMessageDialog(this, "Rol no reconocido: '" + rol + "'");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
        }
    }

    private void abrirVentana(Runnable ventanaRunnable) {
        dispose();
        SwingUtilities.invokeLater(ventanaRunnable);
    }

    private void abrirRegistro() {
        new RegistroFrame().setVisible(true);
    }

    private void mostrarDialogoRecuperacion() {
        JTextField txtUsuarioRec = new JTextField();
        JPasswordField txtNueva = new JPasswordField();

        Object[] campos = {
                "Nombre de usuario:", txtUsuarioRec,
                "Nueva contraseña:", txtNueva
        };

        int opcion = JOptionPane.showConfirmDialog(this, campos, "Recuperar Contraseña", JOptionPane.OK_CANCEL_OPTION);

        if (opcion == JOptionPane.OK_OPTION) {
            String user = txtUsuarioRec.getText().trim();
            String nueva = new String(txtNueva.getPassword()).trim();

            if (user.isEmpty() || nueva.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete ambos campos.");
                return;
            }

            UsuarioDao dao = new UsuarioDao();
            Usuario u = dao.buscarUsuarioPorNombre(user);

            if (u != null && dao.cambiarContraseña(u.getIdUsuario(), nueva)) {
                JOptionPane.showMessageDialog(this, "Contraseña actualizada correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado o error al actualizar.");
            }
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
