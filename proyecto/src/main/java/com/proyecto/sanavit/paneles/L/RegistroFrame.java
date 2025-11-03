package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RegistroFrame extends JFrame {

    private JTextField txtNombreUsuario, txtNombre, txtApellido, txtTelefono, txtEmail;
    private JPasswordField txtContrasena;
    private JButton btnRegistrar, btnCancelar;

    public RegistroFrame() {
        setTitle("Registro de Usuario - Sanavit");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(new Color(35, 210, 43));
        setContentPane(fondo);

        JLabel titulo = new JLabel("Registro de Paciente", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        fondo.add(titulo, BorderLayout.NORTH);

        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        // Campos del formulario
        panelCampos.add(new JLabel("Nombre de usuario:"), gbc);
        gbc.gridx = 1;
        txtNombreUsuario = new JTextField(20);
        panelCampos.add(txtNombreUsuario, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panelCampos.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        txtContrasena = new JPasswordField(20);
        panelCampos.add(txtContrasena, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panelCampos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        panelCampos.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panelCampos.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1;
        txtApellido = new JTextField(20);
        panelCampos.add(txtApellido, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panelCampos.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(20);
        panelCampos.add(txtTelefono, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panelCampos.add(new JLabel("Correo electrónico:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        panelCampos.add(txtEmail, gbc);

        // Botones
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        btnRegistrar = new JButton("Registrar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);
        panelCampos.add(panelBotones, gbc);

        fondo.add(panelCampos, BorderLayout.CENTER);

        // Listeners
        btnRegistrar.addActionListener(e -> registrarUsuario());
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void registrarUsuario() {
        String nombreUsuario = txtNombreUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword()).trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();

        if (nombreUsuario.isEmpty() || contrasena.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete los campos obligatorios.");
            return;
        }

        try {
            UsuarioDao usuarioDao = new UsuarioDao();
            PacienteDao pacienteDao = new PacienteDao();

            // 🔹 Asignar rol de paciente directamente (ID según tu catálogo en MySQL)
            int idRolPaciente = 3; // Cambia este número si tu tabla rol tiene otro ID

            // Crear usuario
            Usuario usuario = new Usuario(idRolPaciente, idRolPaciente, nombreUsuario, contrasena, nombreRol);
            usuario.setNombreUsuario(n ombreUsuario);
            usuario.setContraseña(contrasena);
            usuario.setIdRol(idRolPaciente);

            if (!usuarioDao.insertarUsuario(usuario)) {
                JOptionPane.showMessageDialog(this, "Error al registrar el usuario.");
                return;
            }

            // Crear paciente vinculado al usuario recién creado
            Paciente paciente = new Paciente();
            paciente.setNombre(nombre);
            paciente.setTelefono(telefono);
            paciente.setCorreo(email);
            paciente.setIdUsuario(usuario.getIdUsuario());

            if (pacienteDao.insertarPaciente(paciente)) {
                JOptionPane.showMessageDialog(this, "Registro exitoso. Ya puedes iniciar sesión.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar paciente.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new RegistroFrame();
    }
}
