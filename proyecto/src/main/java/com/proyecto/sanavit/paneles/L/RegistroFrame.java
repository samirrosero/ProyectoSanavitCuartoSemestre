package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegistroFrame extends JFrame {

    private JTextField txtUsuario, txtNombreMedico, txtNombrePaciente, txtCorreo, txtEdad, txtTelefono, txtDireccion,
            txtIdentificacion;
    private JPasswordField txtContraseña;
    private JComboBox<String> comboRol, comboSexo, comboEspecialidad;
    private JButton btnRegistrar, btnAtras;

    public RegistroFrame() {
        setTitle("Registro de Usuario");
        setSize(980, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(new Color(35, 210, 43));
        setContentPane(fondo);

        // LOGO
        ImageIcon logo = new ImageIcon(
                "C:\\Users\\samir\\OneDrive\\Escritorio\\OneDrive\\Documentos\\prototipo\\logo.png");
        JLabel labellogo = new JLabel();
        labellogo.setHorizontalAlignment(SwingConstants.CENTER);
        labellogo.setVerticalAlignment(SwingConstants.CENTER);
        labellogo.setIcon(new ImageIcon(logo.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
        fondo.add(labellogo, BorderLayout.NORTH);

        // ========================
        // PANEL PRINCIPAL DE REGISTRO
        // ========================
        JPanel registro = new JPanel(new GridBagLayout());
        registro.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        registro.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        txtUsuario = new JTextField(20);
        registro.add(txtUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        registro.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        txtContraseña = new JPasswordField(20);
        registro.add(txtContraseña, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        registro.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1;
        comboRol = new JComboBox<>(new String[] { "Administrador", "Medico", "Gestor de Citas", "Paciente" });
        registro.add(comboRol, gbc);

        // ========================
        // PANEL MÉDICO
        // ========================
        JPanel panelMedico = new JPanel(new GridBagLayout());
        panelMedico.setOpaque(false);
        GridBagConstraints gbcM = new GridBagConstraints();
        gbcM.insets = new Insets(10, 10, 10, 20);
        gbcM.fill = GridBagConstraints.HORIZONTAL;
        gbcM.gridx = 0;
        gbcM.gridy = 0;

        panelMedico.add(new JLabel("Nombre:"), gbcM);
        gbcM.gridx = 1;
        txtNombreMedico = new JTextField(20);
        panelMedico.add(txtNombreMedico, gbcM);

        gbcM.gridx = 0;
        gbcM.gridy++;
        panelMedico.add(new JLabel("Especialidad:"), gbcM);
        gbcM.gridx = 1;
        comboEspecialidad = new JComboBox<>(new String[] {
                "Medicina General", "Pediatría", "Ginecología", "Cardiología", "Odontología", "Oftalmología"
        });
        panelMedico.add(comboEspecialidad, gbcM);

        // ========================
        // PANEL PACIENTE
        // ========================
        JPanel panelPaciente = new JPanel(new GridBagLayout());
        panelPaciente.setOpaque(false);
        GridBagConstraints gbcP = new GridBagConstraints();
        gbcP.insets = new Insets(5, 5, 5, 5);
        gbcP.fill = GridBagConstraints.HORIZONTAL;
        gbcP.gridx = 0;
        gbcP.gridy = 0;

        panelPaciente.add(new JLabel("Nombre:"), gbcP);
        gbcP.gridx = 1;
        txtNombrePaciente = new JTextField(20);
        panelPaciente.add(txtNombrePaciente, gbcP);

        gbcP.gridx = 0;
        gbcP.gridy++;
        panelPaciente.add(new JLabel("Correo:"), gbcP);
        gbcP.gridx = 1;
        txtCorreo = new JTextField(20);
        panelPaciente.add(txtCorreo, gbcP);

        gbcP.gridx = 0;
        gbcP.gridy++;
        panelPaciente.add(new JLabel("Edad:"), gbcP);
        gbcP.gridx = 1;
        txtEdad = new JTextField(20);
        panelPaciente.add(txtEdad, gbcP);

        gbcP.gridx = 0;
        gbcP.gridy++;
        panelPaciente.add(new JLabel("Teléfono:"), gbcP);
        gbcP.gridx = 1;
        txtTelefono = new JTextField(20);
        panelPaciente.add(txtTelefono, gbcP);

        gbcP.gridx = 0;
        gbcP.gridy++;
        panelPaciente.add(new JLabel("Sexo:"), gbcP);
        gbcP.gridx = 1;
        comboSexo = new JComboBox<>(new String[] { "Masculino", "Femenino", "Otro" });
        panelPaciente.add(comboSexo, gbcP);

        gbcP.gridx = 0;
        gbcP.gridy++;
        panelPaciente.add(new JLabel("Dirección:"), gbcP);
        gbcP.gridx = 1;
        txtDireccion = new JTextField(20);
        panelPaciente.add(txtDireccion, gbcP);

        gbcP.gridx = 0;
        gbcP.gridy++;
        panelPaciente.add(new JLabel("Identificación:"), gbcP);
        gbcP.gridx = 1;
        txtIdentificacion = new JTextField(20);
        panelPaciente.add(txtIdentificacion, gbcP);

        // Por defecto ocultos
        panelMedico.setVisible(false);
        panelPaciente.setVisible(false);

        // Mostrar dinámicamente el formulario según rol
        comboRol.addActionListener(e -> {
            String rol = comboRol.getSelectedItem().toString().toLowerCase();
            panelMedico.setVisible(rol.equals("medico"));
            panelPaciente.setVisible(rol.equals("paciente"));
            revalidate();
            repaint();
        });

        // ========================
        // BOTONES
        // ========================
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.setOpaque(false);
        btnRegistrar = new JButton("Registrar");
        btnAtras = new JButton("Atrás");
        panelInferior.add(btnRegistrar);
        panelInferior.add(btnAtras);

        btnAtras.addActionListener(e -> {
            dispose();
            new Login();
        });

        btnRegistrar.addActionListener(e -> registrarUsuario());

        // ========================
        // ENSAMBLAR PANELES
        // ========================
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setOpaque(false);
        panelCentro.add(registro, BorderLayout.NORTH);
        panelCentro.add(panelMedico, BorderLayout.CENTER);
        panelCentro.add(panelPaciente, BorderLayout.SOUTH);

        fondo.add(panelCentro, BorderLayout.CENTER);
        fondo.add(panelInferior, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void registrarUsuario() {
        String usuario = txtUsuario.getText().trim();
        String contraseña = new String(txtContraseña.getPassword());
        String rolSeleccionado = comboRol.getSelectedItem().toString().toLowerCase();

        if (usuario.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y contraseña son obligatorios.");
            return;
        }

        try {
            UsuarioDao usuarioDAO = new UsuarioDao();
            Usuario nuevoUsuario = new Usuario(0,0, null, null, null);
            nuevoUsuario.setNombreUsuario(usuario);
            nuevoUsuario.setContraseña(contraseña);

            // Asignar ID de rol según selección
            int idRol = switch (rolSeleccionado) {
                case "administrador" -> 1;
                case "medico" -> 2;
                case "gestor de citas" -> 3;
                case "paciente" -> 4;
                default -> -1;
            };
            nuevoUsuario.setIdRol(idRol);
            System.out.println("Registrando usuario con rol ID: " + idRol);

            int idUsuario = usuarioDAO.insertarUsuario(nuevoUsuario);
            if (idUsuario == -1) {
                JOptionPane.showMessageDialog(this, "Error al registrar usuario.");
                return;
            }
            nuevoUsuario.setIdUsuario(idUsuario);

            // Crear médico o paciente según rol
            if (idRol == 2) { // MÉDICO
                String nombre = txtNombreMedico.getText().trim();
                String especialidad = comboEspecialidad.getSelectedItem().toString();
                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El nombre es obligatorio para médicos.");
                    return;
                }

                MedicoDao medicoDAO = new MedicoDao();
                if (medicoDAO.insertarMedico(new Medico(0, nombre, especialidad, nuevoUsuario.getIdUsuario()))) {
                    JOptionPane.showMessageDialog(this, "Médico registrado con éxito.");
                    System.out.println("Médico registrado con éxito.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar médico.");
                    return;
                }
            } else if (idRol == 4) { // PACIENTE
                String nombre = txtNombrePaciente.getText().trim();
                String correo = txtCorreo.getText().trim();
                String edadStr = txtEdad.getText().trim();
                int edad = Integer.parseInt(edadStr.isEmpty() ? "0" : edadStr);
                String telefono = txtTelefono.getText().trim();
                String sexo = comboSexo.getSelectedItem().toString();
                String direccion = txtDireccion.getText().trim();
                String identificacion = txtIdentificacion.getText().trim();

                if(nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || identificacion.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos del paciente son obligatorios.");
                    return;
                }

                PacienteDao pacienteDAO = new PacienteDao();
                pacienteDAO.insertarPaciente(new Paciente(0, nombre, correo, edad, telefono, sexo, direccion,
                        identificacion, nuevoUsuario.getIdUsuario()));
            }

            JOptionPane.showMessageDialog(this, "Registro exitoso. Ya puedes iniciar sesión.");
            dispose();
            new Login();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al registrar usuario: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new RegistroFrame();
    }
}
