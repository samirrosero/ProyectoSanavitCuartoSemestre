package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;

public class VentanaPaciente extends JFrame {

    private JLabel lblNombre, lblCorreo, lblEdad, lblTelefono, lblSexo, lblDireccion, lblIdentificacion;
    private JButton btnAgendarCita, btnVerHistoriaClinica, btnCerrarSesion;

    public VentanaPaciente(Usuario usuarioActual) {
        setTitle("Panel del Paciente");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Obtener información del paciente
        PacienteDao pacienteDAO = new PacienteDao();
        Paciente pacienteActual = pacienteDAO.obtenerPacientePorIdUsuario(usuarioActual.getIdUsuario());

        // ENCABEZADO
        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(new Color(110, 180, 255));

        JLabel lblBienvenida = new JLabel("Bienvenido(a) " + (pacienteActual != null ? pacienteActual.getNombre() : ""));
        lblBienvenida.setForeground(Color.BLACK);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 16));
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0));
        fondo.add(lblBienvenida, BorderLayout.WEST);

        // Logo
        ImageIcon logoIcon = new ImageIcon("C:\\Users\\samir\\OneDrive\\Escritorio\\OneDrive\\Documentos\\prototipo\\logo2.png");
        Image imagenEscalada = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(imagenEscalada));
        lblLogo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 15));
        fondo.add(lblLogo, BorderLayout.EAST);

        add(fondo, BorderLayout.NORTH);

        // PANEL CENTRAL
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(new Color(230, 245, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 80, 50, 80));

        lblNombre = new JLabel("Nombre: " + (pacienteActual != null ? pacienteActual.getNombre() : "No encontrado"));
        lblCorreo = new JLabel("Correo: " + (pacienteActual != null ? pacienteActual.getCorreo() : "No encontrado"));
        lblEdad = new JLabel("Edad: " + (pacienteActual != null ? pacienteActual.getEdad() : "No encontrada"));
        lblTelefono = new JLabel("Teléfono: " + (pacienteActual != null ? pacienteActual.getTelefono() : "No encontrado"));
        lblSexo = new JLabel("Sexo: " + (pacienteActual != null ? pacienteActual.getSexo() : "No encontrado"));
        lblDireccion = new JLabel("Dirección: " + (pacienteActual != null ? pacienteActual.getDireccion() : "No encontrada"));
        lblIdentificacion = new JLabel("Identificación: " + (pacienteActual != null ? pacienteActual.getIdentificacion() : "No encontrada"));

        panel.add(lblNombre);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblCorreo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblEdad);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblTelefono);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblSexo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblDireccion);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblIdentificacion);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // BOTONES
        btnAgendarCita = new JButton("Agendar Cita");
        btnVerHistoriaClinica = new JButton("Ver Historia Clínica");
        btnCerrarSesion = new JButton("Cerrar Sesión");

        // Acción de Agendar Cita
        btnAgendarCita.addActionListener(e -> {
            new VentanaAgendarCita(pacienteActual);
        });

        // Acción de Ver Historia Clínica
       // btnVerHistoriaClinica.addActionListener(e -> {
            //new VentanaHistoriaClinica(pacienteActual);
        //});

        // Acción de Cerrar Sesión
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new Login(); // Regresar al login
        });

        // Añadir botones al panel
        panel.add(btnAgendarCita);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnVerHistoriaClinica);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnCerrarSesion);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
}