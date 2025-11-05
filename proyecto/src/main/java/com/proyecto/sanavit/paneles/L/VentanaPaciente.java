package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPaciente extends JFrame {

    private JLabel lblNombre, lblCorreo, lblEdad, lblTelefono, lblSexo, lblDireccion, lblIdentificacion;
    private JButton btnAgendarCita, btnCerrarSesion;

     public VentanaPaciente(Usuario usuarioActual){
        setTitle("Panel del Paciente");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        

        PacienteDao pacienteDAO = new PacienteDao();
        Paciente pacienteActual = pacienteDAO.obtenerPacientePorIdUsuario(usuarioActual.getIdUsuario());

        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(new Color( 110, 180, 255)); // 0, 123, 255 Azul similar a Salud Total

        JLabel lblBienvenida = new JLabel("Bienvenido (a) " + (pacienteActual != null ? pacienteActual.getNombre() : ""));
        lblBienvenida.setForeground(Color.BLACK);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 16));
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0));
        fondo.add(lblBienvenida, BorderLayout.WEST);


        // Cargar el logo
        ImageIcon logoIcon = new ImageIcon("C:\\Users\\samir\\OneDrive\\Escritorio\\OneDrive\\Documentos\\prototipo\\logo2.png"); // CAMBIA esto por la ruta real
        Image imagenEscalada = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(imagenEscalada));
        lblLogo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 15));
        fondo.add(lblLogo, BorderLayout.EAST);

        add(fondo, BorderLayout.NORTH);
        // FIN ENCABEZADO
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(new Color(230, 245, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 80, 50, 80));

        lblNombre = new JLabel("Nombre: "+ (pacienteActual != null ? pacienteActual.getNombre(): "No encontrado"));
        lblCorreo = new JLabel("Correo: "+ (pacienteActual != null ? pacienteActual.getCorreo(): "No encontrado"));
        lblEdad = new JLabel("Edad: "+ (pacienteActual != null? pacienteActual.getEdad():"No encontrada"));
        lblTelefono = new JLabel("Telefono: "+ (pacienteActual!= null ? pacienteActual.getTelefono():"no genero"));
        lblSexo = new JLabel("Sexo: "+ (pacienteActual != null ? pacienteActual.getSexo(): "No encontrado"));
        lblDireccion = new JLabel("Direccion: "+ (pacienteActual != null? pacienteActual.getDireccion():"No encontrada"));
        lblIdentificacion = new JLabel("Identificacion: "+ (pacienteActual!= null ? pacienteActual.getIdentificacion():"no genero"));

        // Agregar Boton para agendar cita
        btnAgendarCita = new JButton("Agendar Cita");
        btnAgendarCita.addActionListener(e -> {
            new VentanaAgendarCita(pacienteActual);
        });
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
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnAgendarCita);
        add(panel);
        setVisible(true);

    }
}
