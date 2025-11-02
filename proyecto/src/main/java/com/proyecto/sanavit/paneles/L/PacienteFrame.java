package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.Paciente;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PacienteFrame extends JFrame {

    private JLabel lblNombre, lblCorreo, lblEdad, lblTelefono, lblSexo, lblDireccion, lblIdentificacion;
    private JButton btnAgendarCita, btnCerrarSesion;

    public PacienteFrame(Paciente paciente) {
        setTitle("Panel del Paciente - Sanavit");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // 🎨 Colores base
        Color azulClaro = new Color(227, 241, 255);
        Color azulCabecera = new Color(79, 140, 255);

        // 🔹 Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(azulClaro);

        // 🔹 Cabecera
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(azulCabecera);
        cabecera.setPreferredSize(new Dimension(700, 100));

        JLabel lblTitulo = new JLabel("Bienvenido(a) " + paciente.getNombre(), SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Logo
      //  ImageIcon icono = new ImageIcon(getClass().getResource("/imagenes/sanavit_logo.png")); // Asegúrate de tener esta imagen
      //  Image img = icono.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        // JLabel lblLogo = new JLabel(new ImageIcon(img));
        // lblLogo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));

        cabecera.add(lblTitulo, BorderLayout.WEST);
       // cabecera.add(lblLogo, BorderLayout.EAST);

        // 🔹 Panel de información
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(azulClaro);
        panelInfo.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        lblNombre = new JLabel("Nombre: " + paciente.getNombre());
        lblCorreo = new JLabel("Correo: " + paciente.getCorreo());
        lblEdad = new JLabel("Edad: " + paciente.getEdad());
        lblTelefono = new JLabel("Teléfono: " + paciente.getTelefono());
        lblSexo = new JLabel("Sexo: " + paciente.getSexo());
        lblDireccion = new JLabel("Dirección: " + paciente.getDireccion());
        lblIdentificacion = new JLabel("Identificación: " + paciente.getIdentificacion());

        JLabel[] etiquetas = {lblNombre, lblCorreo, lblEdad, lblTelefono, lblSexo, lblDireccion, lblIdentificacion};
        for (JLabel label : etiquetas) {
            label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        }

        // 🔹 Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
        panelBotones.setBackground(azulClaro);

        btnAgendarCita = new JButton("🗓️ Agendar Cita");
        btnAgendarCita.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAgendarCita.setBackground(new Color(66, 133, 244));
        btnAgendarCita.setForeground(Color.WHITE);
        btnAgendarCita.setFocusPainted(false);
        btnAgendarCita.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnAgendarCita.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnCerrarSesion = new JButton("🚪 Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCerrarSesion.setBackground(new Color(255, 77, 77));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnCerrarSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // // Acción botón "Agendar Cita"
        // btnAgendarCita.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         dispose(); // Cierra esta ventana
        //         new CitaFrame(paciente).setVisible(true); // Abre la vista para agendar cita
        //     }
        // });

        // Acción botón "Cerrar Sesión"
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            JOptionPane.showMessageDialog(null, "Sesión cerrada correctamente.");
            // Aquí puedes regresar a la vista de login
        });

        panelBotones.add(btnAgendarCita);
        panelBotones.add(btnCerrarSesion);

        // Agregar componentes al panel principal
        for (JLabel label : etiquetas) panelInfo.add(label);
        panelInfo.add(panelBotones);

        panelPrincipal.add(cabecera, BorderLayout.NORTH);
        panelPrincipal.add(panelInfo, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    // Para probar la vista
    public static void main(String[] args) {
        Paciente p = new Paciente(1, "Samir Rosero", "samir@gmail.com", 22, "3005757332", "Masculino", "Cra 27 b4 323 432", "1234567890", 1);
        new PacienteFrame(p).setVisible(true);
    }
}
