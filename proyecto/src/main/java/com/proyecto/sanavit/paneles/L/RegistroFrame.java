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
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Usuario
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        txtUsuario = new JTextField(20);
        add(txtUsuario, gbc);

        // Contraseña
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        txtContraseña = new JPasswordField(20);
        add(txtContraseña, gbc);

        // Rol
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1;
        comboRol = new JComboBox<>(new String[]{"Administrador", "Medico", "Gestor de Citas", "Paciente"});
        add(comboRol, gbc);

        // Botones
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel panelBtns = new JPanel();
        btnRegistrar = new JButton("Registrar");
        btnAtras = new JButton("Atrás");
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

            // Asignar ID del rol
            int idRol = switch (rolSeleccionado) {
                case "administrador" -> 1;
                case "medico" -> 2;
                case "gestor de citas" -> 3;
                case "paciente" -> 4;
                default -> -1;
            };
            nuevoUsuario.setIdRol(idRol);

            // Insertar usuario
            int idUsuario = usuarioDAO.insertarUsuario(nuevoUsuario);
            if (idUsuario == -1) {
                JOptionPane.showMessageDialog(this, "Error al registrar usuario en la base de datos.");
                return;
            }
            nuevoUsuario.setIdUsuario(idUsuario);

            // Abrir el modal correspondiente
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
