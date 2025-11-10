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

    private JLabel lblFotoPerfil;
    private BufferedImage imagenPerfil;
    private JButton btnAgendarCita, btnVerHistoriaClinica, btnVerCitas, btnCerrarSesion;

    public VentanaPaciente(Usuario usuarioActual) {
        setTitle("Panel del Paciente - Sanavit");
        setSize(950, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(244, 247, 250)); // Verde menta suave

        // === Obtener información ===
        PacienteDao pacienteDAO = new PacienteDao();
        Paciente pacienteActual = pacienteDAO.obtenerPacientePorIdUsuario(usuarioActual.getIdUsuario());
        Portafolio portafolio = new PortafolioDao().obtenerPortafolioPorIdPaciente(pacienteActual.getIdPaciente());

        // === ENCABEZADO ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(65, 158, 91));
        header.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        JLabel lblTitulo = new JLabel("<html><div style='color:white;'>"
                + "<h2 style='margin:0;'>Bienvenido(a)</h2>"
                + "<h1 style='margin:0; font-weight:bold;'>" + pacienteActual.getNombre() + "</h1>"
                + "</div></html>");
        header.add(lblTitulo, BorderLayout.WEST);

        // === FOTO PERFIL ===
        lblFotoPerfil = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenPerfil != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    int size = Math.min(getWidth(), getHeight());
                    int x = (getWidth() - size) / 2;
                    int y = (getHeight() - size) / 2;

                    Shape clip = new Ellipse2D.Double(x, y, size, size);
                    g2.setClip(clip);
                    g2.drawImage(imagenPerfil, x, y, size, size, null);

                    g2.setClip(null);
                    g2.setStroke(new BasicStroke(4f));
                    g2.setColor(Color.WHITE);
                    g2.draw(clip);
                    g2.dispose();
                }
            }
        };
        lblFotoPerfil.setPreferredSize(new Dimension(120, 120));
        lblFotoPerfil.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFotoPerfil("C:\\Users\\samir\\Downloads\\usuario.png");

        lblFotoPerfil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    setFotoPerfil(fc.getSelectedFile().getAbsolutePath());
                }
            }
        });

        JPanel panelFoto = new JPanel();
        panelFoto.setOpaque(false);
        panelFoto.add(lblFotoPerfil);
        header.add(panelFoto, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // === PANEL CENTRAL (ficha limpia con grid 2 columnas) ===
        JPanel panelDatos = new JPanel(new GridLayout(2, 1, 15, 15));
        panelDatos.setOpaque(false);

        // --- Columna izquierda ---
        JPanel columnaIzquierda = new JPanel(new GridLayout(6, 1, 5, 5));
        columnaIzquierda.setBackground(Color.WHITE);
        columnaIzquierda.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        columnaIzquierda.add(crearLinea("Nombre:", pacienteActual.getNombre()));
        columnaIzquierda.add(crearLinea("Identificación:", pacienteActual.getIdentificacion()));
        columnaIzquierda.add(crearLinea("Correo:", pacienteActual.getCorreo()));
        columnaIzquierda.add(crearLinea("Teléfono:", pacienteActual.getTelefono()));
        columnaIzquierda.add(crearLinea("Edad:", String.valueOf(pacienteActual.getEdad())));
        columnaIzquierda.add(crearLinea("Sexo:", pacienteActual.getSexo()));

        // --- Columna derecha ---
        JPanel columnaDerecha = new JPanel(new GridLayout(2, 1, 5, 5));
        columnaDerecha.setBackground(Color.WHITE);
        columnaDerecha.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        columnaDerecha.add(crearLinea("EPS:", portafolio.getSalud()));
        columnaDerecha.add(crearLinea("Afiliación:", portafolio.getAfiliaciones()));

        // --- Panel combinado ---
        JPanel panelPerfil = new JPanel(new GridLayout(1, 2, 20, 0));
        panelPerfil.setBackground(Color.WHITE);
        panelPerfil.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 230, 210), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        panelPerfil.add(columnaIzquierda);
        panelPerfil.add(columnaDerecha);

        // --- Contenedor con sombra suave ---
        JPanel contenedorCentral = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(0, 0, 0, 25));
                g2.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 25, 25);
            }
        };
        contenedorCentral.setOpaque(false);
        contenedorCentral.add(panelPerfil);
        add(contenedorCentral, BorderLayout.CENTER);

        // === BOTONES ===
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelBotones.setBackground(new Color(233, 247, 239));

        btnAgendarCita = crearBoton("Agendar Cita");
        btnVerHistoriaClinica = crearBoton("Historia Clínica");
        btnVerCitas = crearBoton("Ver Citas");
        btnCerrarSesion = crearBoton("Cerrar Sesión");

        panelBotones.add(btnAgendarCita);
        panelBotones.add(btnVerHistoriaClinica);
        panelBotones.add(btnVerCitas);
        panelBotones.add(btnCerrarSesion);
        add(panelBotones, BorderLayout.SOUTH);

        // === ACCIONES ===
        btnAgendarCita.addActionListener(e -> new VentanaAgendarCita(pacienteActual));
        btnVerHistoriaClinica.addActionListener(e -> new VentanaVerHistoriaClinica(pacienteActual));
        btnVerCitas.addActionListener(e -> new VentanaVerCitas(pacienteActual));
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new Login();
        });

        setVisible(true);
    }

    // ---------------------- COMPONENTES AUXILIARES ----------------------
    private JPanel crearLinea(String label, String valor) {
        JPanel linea = new JPanel(new BorderLayout());
        linea.setOpaque(false);

        JLabel lblCampo = new JLabel(label);
        lblCampo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblCampo.setForeground(new Color(80, 100, 90));

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblValor.setForeground(new Color(40, 60, 50));

        linea.add(lblCampo, BorderLayout.WEST);
        linea.add(lblValor, BorderLayout.EAST);

        // Línea divisoria sutil debajo
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(210, 235, 210));
        linea.add(sep, BorderLayout.SOUTH);

        return linea;
    }

    private void setFotoPerfil(String ruta) {
        try {
            imagenPerfil = ImageIO.read(new File(ruta));
            lblFotoPerfil.repaint();
        } catch (IOException e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
        }
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(65, 107, 74));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(68, 128, 80)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(new Color(65, 107, 74)); }
        });
        return btn;
    }
}
