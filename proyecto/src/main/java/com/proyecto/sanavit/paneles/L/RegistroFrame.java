package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;

public class RegistroFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContraseña;
    private JComboBox<String> comboRol;
    private JButton btnRegistrar, btnAtras;

    public RegistroFrame() {
        setTitle("Registro de Usuario - Sanavit");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 🎨 Fondo y estilo general
        getContentPane().setBackground(new Color(78, 207, 78));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblTitulo = new JLabel("Registro de Usuario");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(10, 10, 10));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitulo, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.gridy++;

        // 🧍 Usuario
        gbc.gridx = 0;
        add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        txtUsuario = new JTextField(20);
        txtUsuario.setPreferredSize(new Dimension(200, 30));
        add(txtUsuario, gbc);

        // 🔒 Contraseña (alineada correctamente)
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        txtContraseña = new JPasswordField(20);
        txtContraseña.setPreferredSize(new Dimension(200, 30)); // altura correcta
        txtContraseña.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(txtContraseña, gbc);

        // 🎭 Rol
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1;
        comboRol = new JComboBox<>(new String[]{"Administrador", "Medico", "Gestor de Citas", "Paciente"});
        comboRol.setPreferredSize(new Dimension(200, 30));
        comboRol.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(comboRol, gbc);

        // 🔘 Botones
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel panelBtns = new JPanel();
        panelBtns.setBackground(new Color(78, 207, 78));
        btnRegistrar = new JButton("Registrar");
        btnAtras = new JButton("Atrás");
        btnRegistrar.setFocusPainted(false);
        btnAtras.setFocusPainted(false);
        panelBtns.add(btnRegistrar);
        panelBtns.add(btnAtras);
        add(panelBtns, gbc);

        // Eventos
        btnRegistrar.addActionListener(e -> registrarUsuario());
        btnAtras.addActionListener(e -> {
            dispose();
            new Login();
        });

        setVisible(true);
    }

    private void registrarUsuario() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContraseña.getPassword()).trim();
        String rolSeleccionado = comboRol.getSelectedItem().toString().toLowerCase();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y contraseña son obligatorios.");
            return;
        }

        try {
            UsuarioDao usuarioDAO = new UsuarioDao();
            Usuario nuevoUsuario = new Usuario(0, 0, usuario, contrasena, rolSeleccionado);

            int idRol = switch (rolSeleccionado) {
                case "administrador" -> 1;
                case "medico" -> 2;
                case "gestor de citas" -> 3;
                case "paciente" -> 4;
                default -> -1;
            };
            nuevoUsuario.setIdRol(idRol);

            int idUsuario = usuarioDAO.insertarUsuario(nuevoUsuario);
            if (idUsuario == -1) {
                JOptionPane.showMessageDialog(this, "Error al registrar usuario en la base de datos.");
                return;
            }
            nuevoUsuario.setIdUsuario(idUsuario);

            switch (idRol) {
                case 2 -> new ModalMedico(this, nuevoUsuario);
                case 4 -> new ModalPaciente(this, nuevoUsuario);
                default -> JOptionPane.showMessageDialog(this, "Registro completado solo con credenciales.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al registrar usuario: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new RegistroFrame();
    }
}