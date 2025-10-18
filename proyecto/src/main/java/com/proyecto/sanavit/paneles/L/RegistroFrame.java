package com.proyecto.sanavit.paneles.L;
import com.proyecto.sanavit.modelo.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistroFrame extends JFrame {
    private JTextField txtUsuario, txtNombreMedico, txtEspecialidadMedico;
    private JTextField txtNombrePaciente, txtCorreo, txtEdad, txtTelefono, txtSexo, txtDireccion, txtIdentificacion;
    private JPasswordField txtContraseña;
    private JComboBox<String> comboRol;
    private JButton btnRegistrar, btnAtras;

    public RegistroFrame() {
        setTitle("Registro de Usuario");
        setSize(980, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(new Color(35, 210, 43));
        setContentPane(fondo);

        ImageIcon logo = new ImageIcon("C:\\Users\\samir\\OneDrive\\Escritorio\\OneDrive\\Documentos\\prototipo\\logo.png");
        JLabel labellogo = new JLabel();
        labellogo.setHorizontalAlignment(SwingConstants.CENTER);
        labellogo.setVerticalAlignment(SwingConstants.CENTER);
        labellogo.setBounds(10, 40, 400, 350);
        labellogo.setIcon(new ImageIcon(logo.getImage().getScaledInstance(labellogo.getWidth(), labellogo.getHeight(), Image.SCALE_SMOOTH)));
        fondo.add(labellogo, BorderLayout.NORTH);

        JPanel registro = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        registro.setOpaque(false);
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
        comboRol = new JComboBox<>(new String[]{"Administrador", "Medico", "Gestor de Citas", "Paciente"});
        registro.add(comboRol, gbc);

        // Panel médico
        JPanel camposMedico = new JPanel(new GridBagLayout());
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.insets = new Insets(10, 10, 10, 20);
        gbc1.fill = GridBagConstraints.HORIZONTAL;
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        camposMedico.setOpaque(false);

        camposMedico.add(new JLabel("Nombre:"), gbc1);
        gbc1.gridx = 1;
        txtNombreMedico = new JTextField(20);
        camposMedico.add(txtNombreMedico, gbc1);

        gbc1.gridx = 0;
        gbc1.gridy++;
        camposMedico.add(new JLabel("Especialidad:"), gbc1);
        gbc1.gridx = 1;
        txtEspecialidadMedico = new JTextField(20);
        camposMedico.add(txtEspecialidadMedico, gbc1);

        // Panel paciente
        JPanel campoPaciente = new JPanel(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5);
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        campoPaciente.setOpaque(false);

        campoPaciente.add(new JLabel("Nombre:"), gbc2);
        gbc2.gridx = 1;
        txtNombrePaciente = new JTextField(30);
        campoPaciente.add(txtNombrePaciente, gbc2);

        gbc2.gridx = 0;
        gbc2.gridy++;
        campoPaciente.add(new JLabel("Correo:"), gbc2);
        gbc2.gridx = 1;
        txtCorreo = new JTextField(20);
        campoPaciente.add(txtCorreo, gbc2);

        gbc2.gridx = 0;
        gbc2.gridy++;
        campoPaciente.add(new JLabel("Edad:"), gbc2);
        gbc2.gridx = 1;
        txtEdad = new JTextField(20);
        campoPaciente.add(txtEdad, gbc2);

        gbc2.gridx = 0;
        gbc2.gridy++;
        campoPaciente.add(new JLabel("Teléfono:"), gbc2);
        gbc2.gridx = 1;
        txtTelefono = new JTextField(20);
        campoPaciente.add(txtTelefono, gbc2);

        gbc2.gridx = 0;
        gbc2.gridy++;
        campoPaciente.add(new JLabel("Sexo:"), gbc2);
        gbc2.gridx = 1;
        txtSexo = new JTextField(20);
        campoPaciente.add(txtSexo, gbc2);

        gbc2.gridx = 0;
        gbc2.gridy++;
        campoPaciente.add(new JLabel("Dirección:"), gbc2);
        gbc2.gridx = 1;
        txtDireccion = new JTextField(20);
        campoPaciente.add(txtDireccion, gbc2);

        gbc2.gridx = 0;
        gbc2.gridy++;
        campoPaciente.add(new JLabel("Identificación:"), gbc2);
        gbc2.gridx = 1;
        txtIdentificacion = new JTextField(20);
        campoPaciente.add(txtIdentificacion, gbc2);

        camposMedico.setVisible(false);
        campoPaciente.setVisible(false);

        comboRol.addActionListener(e -> {
            String rol = ((String) comboRol.getSelectedItem()).toLowerCase();
            camposMedico.setVisible(rol.equals("medico"));
            campoPaciente.setVisible(rol.equals("paciente"));
            revalidate();
            repaint();
        });

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.setOpaque(false);
        btnRegistrar = new JButton("Registrar");
        btnAtras = new JButton("Atrás");

        btnAtras.addActionListener(e -> {
            dispose(); // cerrar esta ventana
            new Login(); // abrir login
        });

        btnRegistrar.addActionListener(e -> registrarUsuario());

        panelInferior.add(btnRegistrar);
        panelInferior.add(btnAtras);

        JPanel panelCamposExtra = new JPanel(new BorderLayout());
        panelCamposExtra.setOpaque(false);
        panelCamposExtra.add(camposMedico, BorderLayout.NORTH);
        panelCamposExtra.add(campoPaciente, BorderLayout.CENTER);

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);
        panelCentral.add(registro, BorderLayout.NORTH);
        panelCentral.add(panelCamposExtra, BorderLayout.CENTER);
        panelCentral.add(panelInferior, BorderLayout.SOUTH);

        fondo.add(panelCentral, BorderLayout.CENTER);

        setVisible(true);
    }

    private void registrarUsuario() {
        String usuario = txtUsuario.getText().trim();
        String contraseña = new String(txtContraseña.getPassword());
        int idRol = comboRol.getSelectedIndex() + 1;

        if (usuario.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y contraseña son obligatorios.");
            return;
        }

        try {
            UsuarioDao usuarioDAO = new UsuarioDao();
            Usuario nuevoUsuario = new Usuario(idRol, idRol, usuario, contraseña, contraseña);
            int idUsuario = usuarioDAO.insertarUsuario(nuevoUsuario);

            if (idRol == 2) { // Médico
                String nombre = txtNombreMedico.getText().trim();
                String especialidad = txtEspecialidadMedico.getText().trim();
                if (nombre.isEmpty() || especialidad.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nombre y especialidad son obligatorios para médicos.");
                    return;
                }
                MedicoDao medicoDAO = new MedicoDao();
                medicoDAO.insertarMedico(new Medico(0, nombre, especialidad, idUsuario));

            } else if (idRol == 4) { // Paciente
                String nombre = txtNombrePaciente.getText().trim();
                String correo = txtCorreo.getText().trim();
                int edad = Integer.parseInt(txtEdad.getText().trim());
                String telefono = txtTelefono.getText().trim();
                String sexo = txtSexo.getText().trim();
                String direccion = txtDireccion.getText().trim();
                String identificacion = txtIdentificacion.getText().trim();

                PacienteDao pacienteDAO = new PacienteDao();
                pacienteDAO.insertarPaciente(new Paciente(0, nombre, correo, edad, telefono, sexo, direccion, identificacion, idUsuario));
            }

            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
            dispose();
            new Login();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al registrar usuario: " + ex.getMessage());
        }
    }
}

