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

        // Fondo principal
        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(new Color(35, 210, 43));
        setContentPane(fondo);

        // Logo superior
        ImageIcon logo = new ImageIcon("C:\\Users\\samir\\OneDrive\\Escritorio\\OneDrive\\Documentos\\prototipo\\logo.png");
        JLabel labellogo = new JLabel(logo);
        labellogo.setHorizontalAlignment(SwingConstants.CENTER);
        labellogo.setIcon(new ImageIcon(logo.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH)));
        fondo.add(labellogo, BorderLayout.NORTH);

        // Panel de login central
        JPanel panelLogin = new JPanel(new GridBagLayout());
        panelLogin.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.CENTER;

        // Campos de texto
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelLogin.add(new JLabel("Nombre de usuario:"), gbc);

        gbc.gridy++;
        txtUsuario = new JTextField(20);
        panelLogin.add(txtUsuario, gbc);

        gbc.gridy++;
        panelLogin.add(new JLabel("Contraseña:"), gbc);

        gbc.gridy++;
        txtContraseña = new JPasswordField(20);
        panelLogin.add(txtContraseña, gbc);

        // Botones
        gbc.gridy++;
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        btnIniciar = new JButton("Iniciar Sesión");
        btnRegistrar = new JButton("Registrarse");
        btnRecuperar = new JButton("¿Olvidaste tu contraseña?");
        panelBotones.add(btnIniciar);
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnRecuperar);
        panelLogin.add(panelBotones, gbc);

        fondo.add(panelLogin, BorderLayout.CENTER);

        // Listeners
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
                // case "paciente" -> abrirVentana(() -> new VentanaPaciente(u));
                // case "gestor" -> abrirVentana(() -> new VentanaGestorCitas(u));
                // case "administrador" -> abrirVentana(() -> new VentanaAdministrador(u));
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

    // 🔐 Nuevo: Recuperación / cambio de contraseña
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
