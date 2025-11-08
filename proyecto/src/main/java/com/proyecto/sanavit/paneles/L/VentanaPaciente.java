package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class VentanaPaciente extends JFrame {

    private JLabel lblFotoPerfil, lblBienvenida;
    private JButton btnAgendarCita, btnVerHistoriaClinica, btnVerCitas, btnCerrarSesion;

    public VentanaPaciente(Usuario usuarioActual) {
        setTitle("Panel del Paciente - Sanavit");
        setSize(950, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(244, 247, 250)); // Fondo general

        // Obtener información del paciente
        PacienteDao pacienteDAO = new PacienteDao();
        Paciente pacienteActual = pacienteDAO.obtenerPacientePorIdUsuario(usuarioActual.getIdUsuario());

        Portafolio portafolioActual = null;
        if (pacienteActual != null) {
            PortafolioDao portafolioDAO = new PortafolioDao();
            portafolioActual = portafolioDAO.obtenerPortafolioPorIdPaciente(pacienteActual.getIdPaciente());
        }

        // ------------------- PANEL SUPERIOR -------------------
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(0, 123, 255)); // Azul principal
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Bienvenida
        lblBienvenida = new JLabel("<html><span style='font-size:18px;'>Bienvenido(a)</span><br>"
                + "<b style='font-size:24px;'>" + (pacienteActual != null ? pacienteActual.getNombre() : "") + "</b></html>");
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setVerticalAlignment(SwingConstants.CENTER);

        panelSuperior.add(lblBienvenida, BorderLayout.WEST);

        // Foto de perfil (derecha, circular)
        lblFotoPerfil = new JLabel();
        lblFotoPerfil.setPreferredSize(new Dimension(120, 120));
        lblFotoPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblFotoPerfil.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        lblFotoPerfil.setOpaque(false);

        // Cargar foto inicial (opcional)
        setFotoPerfil("C:\\ruta\\a\\foto_perfil.png");

        lblFotoPerfil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    setFotoPerfil(file.getAbsolutePath());
                }
            }
        });

        JPanel panelFoto = new JPanel();
        panelFoto.setOpaque(false);
        panelFoto.add(lblFotoPerfil);
        panelSuperior.add(panelFoto, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // ------------------- PANEL CENTRAL -------------------
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(new Color(244, 247, 250));
        panelCentral.setLayout(new GridLayout(2, 4, 25, 25));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        panelCentral.add(crearCard("Nombre", pacienteActual != null ? pacienteActual.getNombre() : ""));
        panelCentral.add(crearCard("Identificación", pacienteActual != null ? pacienteActual.getIdentificacion() : ""));
        panelCentral.add(crearCard("Correo", pacienteActual != null ? pacienteActual.getCorreo() : ""));
        panelCentral.add(crearCard("Teléfono", pacienteActual != null ? pacienteActual.getTelefono() : ""));
        panelCentral.add(crearCard("Edad", pacienteActual != null ? String.valueOf(pacienteActual.getEdad()) : ""));
        panelCentral.add(crearCard("Sexo", pacienteActual != null ? pacienteActual.getSexo() : ""));
        panelCentral.add(crearCard("EPS / Salud", portafolioActual != null ? portafolioActual.getSalud() : ""));
        panelCentral.add(crearCard("Afiliación", portafolioActual != null ? portafolioActual.getAfiliaciones() : ""));

        add(panelCentral, BorderLayout.CENTER);

        // ------------------- PANEL INFERIOR -------------------
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
        panelInferior.setBackground(new Color(244, 247, 250));

        btnAgendarCita = crearBoton("Agendar Cita");
        btnVerHistoriaClinica = crearBoton("Ver Historia Clínica");
        btnVerCitas = crearBoton("Ver mis Citas");
        btnCerrarSesion = crearBoton("Cerrar Sesión");

        panelInferior.add(btnAgendarCita);
        panelInferior.add(btnVerHistoriaClinica);
        panelInferior.add(btnVerCitas);
        panelInferior.add(btnCerrarSesion);

        add(panelInferior, BorderLayout.SOUTH);

        // ------------------- ACCIONES -------------------
        btnAgendarCita.addActionListener(e -> new VentanaAgendarCita(pacienteActual));
        btnVerHistoriaClinica.addActionListener(e -> new VentanaVerHistoriaClinica(pacienteActual));
        btnVerCitas.addActionListener(e -> new VentanaVerCitas(pacienteActual));
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new Login();
        });

        setVisible(true);
    }

    // ------------------- TARJETAS CON SOMBRA -------------------
    private JPanel crearCard(String titulo, String valor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sombra suave
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 18, 18);

                // Fondo blanco
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 18, 18);

                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(100, 100, 100));

        JLabel lblValor = new JLabel("<html><b style='font-size:14px; color:#333333;'>" + valor + "</b></html>");
        lblValor.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);
        return card;
    }

    // ------------------- FOTO CIRCULAR CON CORRECCIÓN -------------------
    private void setFotoPerfil(String ruta) {
        try {
            BufferedImage original = ImageIO.read(new File(ruta));
            int size = Math.min(original.getWidth(), original.getHeight());

            // Crear imagen circular
            BufferedImage circular = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circular.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Shape clip = new Ellipse2D.Float(0, 0, 120, 120);
            g2.setClip(clip);
            g2.drawImage(original, 0, 0, 120, 120, null);
            g2.dispose();

            lblFotoPerfil.setIcon(new ImageIcon(circular));
            lblFotoPerfil.repaint(); // Forzar repintado
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }
    }

    // ------------------- BOTONES -------------------
    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(0, 123, 255));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // Hover
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(0, 86, 179));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(0, 123, 255));
            }
        });
        return btn;
    }
}
