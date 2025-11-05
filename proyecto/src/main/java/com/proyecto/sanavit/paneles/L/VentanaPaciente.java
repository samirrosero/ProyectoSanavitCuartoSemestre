package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;

public class VentanaPaciente extends JFrame {

    private JLabel lblNombre, lblCorreo, lblEdad, lblTelefono, lblSexo, lblDireccion, lblIdentificacion;
    private JLabel lblSalud, lblAfiliacion; // 👈 nuevos campos del portafolio
    private JButton btnAgendarCita, btnCerrarSesion;

    public VentanaPaciente(Usuario usuarioActual) {
        setTitle("Panel del Paciente");
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // === OBTENER DATOS DEL PACIENTE ===
        PacienteDao pacienteDAO = new PacienteDao();
        Paciente pacienteActual = pacienteDAO.obtenerPacientePorIdUsuario(usuarioActual.getIdUsuario());

        // === OBTENER PORTAFOLIO (EPS / AFILIACIÓN) ===
        Portafolio portafolioActual = null;
        if (pacienteActual != null) {
            PortafolioDao portafolioDAO = new PortafolioDao();
            portafolioActual = portafolioDAO.obtenerPortafolioPorIdPaciente(pacienteActual.getIdPaciente());
        }

        // === ENCABEZADO ===
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

        // === PANEL DE INFORMACIÓN ===
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(new Color(230, 245, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 80, 50, 80));

        lblNombre = new JLabel("Nombre: " + safe(pacienteActual != null ? pacienteActual.getNombre() : null));
        lblCorreo = new JLabel("Correo: " + safe(pacienteActual != null ? pacienteActual.getCorreo() : null));
        lblEdad = new JLabel("Edad: " + safeInt(pacienteActual != null ? pacienteActual.getEdad() : 0));
        lblTelefono = new JLabel("Teléfono: " + safe(pacienteActual != null ? pacienteActual.getTelefono() : null));
        lblSexo = new JLabel("Sexo: " + safe(pacienteActual != null ? pacienteActual.getSexo() : null));
        lblDireccion = new JLabel("Dirección: " + safe(pacienteActual != null ? pacienteActual.getDireccion() : null));
        lblIdentificacion = new JLabel("Identificación: " + safe(pacienteActual != null ? pacienteActual.getIdentificacion() : null));

        // === NUEVOS CAMPOS (Portafolio) ===
        lblSalud = new JLabel("EPS / Salud: " + safe(portafolioActual != null ? portafolioActual.getSalud() : null));
        lblAfiliacion = new JLabel("Afiliación: " + safe(portafolioActual != null ? portafolioActual.getAfiliaciones() : null));

        // === BOTÓN AGENDAR CITA ===
        btnAgendarCita = new JButton("Agendar Cita");
        btnAgendarCita.addActionListener(e -> new VentanaAgendarCita(pacienteActual));

        // === AGREGAR ETIQUETAS AL PANEL ===
        JLabel[] etiquetas = {
            lblNombre, lblCorreo, lblEdad, lblTelefono, lblSexo, lblDireccion, lblIdentificacion,
            lblSalud, lblAfiliacion
        };

        for (JLabel label : etiquetas) {
            label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            panel.add(label);
            panel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        panel.add(btnAgendarCita);
        add(panel);

        setVisible(true);
            System.out.println("Portafolio obtenido → Salud: " +
        (portafolioActual != null ? portafolioActual.getSalud() : "NULL") +
        " | Afiliación: " +
        (portafolioActual != null ? portafolioActual.getAfiliaciones() : "NULL"));

    }

    // === Métodos auxiliares para evitar nulls ===
    private String safe(String text) {
        return (text == null || text.isEmpty()) ? "No registrado" : text;
    }

    private String safeInt(int value) {
        return value == 0 ? "No registrado" : String.valueOf(value);
    }
    
}
