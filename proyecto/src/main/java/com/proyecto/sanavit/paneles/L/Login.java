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

        // 🌈 Fondo degradado
        JPanel fondo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();

                // Degradado de azul claro a verde suave
                Color color1 = new Color(78, 207, 78); 
                Color color2 = new Color(165, 242, 203); // Verde agua
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        setContentPane(fondo);

        // 🏥 Logo superior
        ImageIcon logo = new ImageIcon("C:\\Users\\samir\\OneDrive\\Escritorio\\OneDrive\\Documentos\\prototipo\\logo.png");
        JLabel labellogo = new JLabel(logo);
        labellogo.setHorizontalAlignment(SwingConstants.CENTER);
        labellogo.setIcon(new ImageIcon(logo.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH)));
        fondo.add(labellogo, BorderLayout.NORTH);

        // 🧩 Panel de login
        JPanel panelLogin = new JPanel(new GridBagLayout());
        panelLogin.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.CENTER;

        // 🧍 Usuario
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblUsuario = new JLabel("Nombre de usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelLogin.add(lblUsuario, gbc);

        gbc.gridy++;
        txtUsuario = new JTextField(20);
        txtUsuario.setPreferredSize(new Dimension(220, 35));
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelLogin.add(txtUsuario, gbc);

        // 🔒 Contraseña
        gbc.gridy++;
        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelLogin.add(lblContraseña, gbc);

        gbc.gridy++;
        txtContraseña = new JPasswordField(20);
        txtContraseña.setPreferredSize(new Dimension(220, 35));
        txtContraseña.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelLogin.add(txtContraseña, gbc);

        // 🔘 Botones superiores
        gbc.gridy++;
        JPanel panelBotonesSuperior = new JPanel();
        panelBotonesSuperior.setOpaque(false);
        btnIniciar = new JButton("Iniciar Sesión");
        btnRegistrar = new JButton("Registrarse");
        panelBotonesSuperior.add(btnIniciar);
        panelBotonesSuperior.add(btnRegistrar);
        panelLogin.add(panelBotonesSuperior, gbc);

        // 🔁 Recuperar contraseña
        gbc.gridy++;
        btnRecuperar = new JButton("¿Olvidaste tu contraseña?");
        btnRecuperar.setFocusPainted(false);
        panelLogin.add(btnRecuperar, gbc);

        fondo.add(panelLogin, BorderLayout.CENTER);

        // 🎯 Listeners
        btnIniciar.addActionListener(e -> iniciarSesion());
        btnRegistrar.addActionListener(e -> abrirRegistro());
        btnRecuperar.addActionListener(e -> mostrarDialogoRecuperacion());

        setVisible(true);
    }

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

    // 🔐 Recuperación / cambio de contraseña
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
