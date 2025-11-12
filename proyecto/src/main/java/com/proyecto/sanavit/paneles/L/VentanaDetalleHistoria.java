package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaDetalleHistoria extends JFrame {

    public VentanaDetalleHistoria(HistoriaClinica historia, Paciente paciente) {
        setTitle("Detalle de Historia Clínica #" + historia.getIdHistoriaClinica());
        setSize(780, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // 🎨 Fondo degradado
        JPanel fondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(29, 151, 108),
                        0, h, new Color(220, 250, 235));
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);
            }
        };
        fondo.setLayout(new BorderLayout(10, 10));
        fondo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(fondo);

        // === Título ===
        JLabel lblTitulo = new JLabel("🩺 Historia Clínica - " + paciente.getNombre(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(20, 70, 50));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        // === Panel de contenido ===
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(185, 227, 177), 1, true),
                BorderFactory.createEmptyBorder(25, 40, 25, 40)
        ));

        // === Datos del paciente ===
        panelContenido.add(crearLabel("Nombre: " + paciente.getNombre(), true));
        panelContenido.add(crearLabel("Correo: " + paciente.getCorreo(), false));
        panelContenido.add(crearLabel("Teléfono: " + paciente.getTelefono(), false));
        panelContenido.add(crearLabel("Edad: " + paciente.getEdad(), false));
        panelContenido.add(Box.createRigidArea(new Dimension(0, 15)));

        // === Médico tratante ===
        String medicoNombre = "Desconocido";
        try {
            EjecucionCitaDao ejecDao = new EjecucionCitaDao();
            EjecucionCita ejec = ejecDao.obtenerPorId(historia.getIdEjecucionCita());
            if (ejec != null) {
                CitaDao citaDao = new CitaDao();
                Cita cita = citaDao.obtenerCitaPorId(ejec.getIdCita());
                if (cita != null) {
                    MedicoDao medicoDao = new MedicoDao();
                    Medico m = medicoDao.obtenerMedicoPorId(cita.getIdMedico());
                    if (m != null) medicoNombre = m.getNombre();
                }
            }
        } catch (Exception e) {
            System.out.println("Error obteniendo médico: " + e.getMessage());
        }

        panelContenido.add(crearSeccion("Médico tratante: " + medicoNombre));
        panelContenido.add(Box.createRigidArea(new Dimension(0, 10)));

        // === Datos de la historia clínica ===
        panelContenido.add(crearLabel("Motivo de consulta:", true));
        panelContenido.add(crearTexto(historia.getMotivoConsulta()));

        panelContenido.add(crearLabel("Enfermedad actual:", true));
        panelContenido.add(crearTexto(historia.getEnfermedadActual()));

        panelContenido.add(crearLabel("Antecedentes:", true));
        panelContenido.add(crearTexto(historia.getAntecedentes()));

        panelContenido.add(crearLabel("Diagnóstico:", true));
        panelContenido.add(crearTexto(historia.getDiagnostico()));

        panelContenido.add(crearLabel("Tratamiento:", true));
        panelContenido.add(crearTexto(historia.getTratamiento()));

        panelContenido.add(crearLabel("Evolución:", true));
        panelContenido.add(crearTexto(historia.getevolucion()));

        panelContenido.add(crearLabel("Observaciones:", true));
        panelContenido.add(crearTexto(historia.getObservaciones()));

        panelContenido.add(Box.createRigidArea(new Dimension(0, 15)));

        // === Receta médica ===
        RecetaMedicaDao recetaDao = new RecetaMedicaDao();
        List<RecetaMedica> recetas = recetaDao.listarRecetas();

        boolean encontrada = false;
        for (RecetaMedica r : recetas) {
            if (r.getIdHistoriaClinica() == historia.getIdHistoriaClinica()) {
                panelContenido.add(crearSeccion("Receta Médica"));
                panelContenido.add(crearLabel("Medicamento: " + r.getMedicamento(), false));
                panelContenido.add(crearLabel("Indicaciones: " + r.getIndicaciones(), false));
                encontrada = true;
            }
        }
        if (!encontrada) {
            panelContenido.add(crearLabel("Sin receta asociada.", false));
        }

        // === Scroll con diseño ===
        JScrollPane scroll = new JScrollPane(panelContenido);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        fondo.add(scroll, BorderLayout.CENTER);

        // === Botón cerrar con hover ===
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBackground(new Color(61, 143, 46));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        agregarHover(btnCerrar, new Color(61, 143, 46), new Color(81, 173, 66));
        btnCerrar.addActionListener(e -> dispose());

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(0, 0, 0, 0));
        panelBoton.add(btnCerrar);
        fondo.add(panelBoton, BorderLayout.SOUTH);

        setVisible(true);
    }

    // === Métodos visuales ===
    private JLabel crearLabel(String texto, boolean titulo) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", titulo ? Font.BOLD : Font.PLAIN, titulo ? 15 : 14));
        lbl.setForeground(titulo ? new Color(30, 60, 40) : new Color(40, 40, 40));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        return lbl;
    }

    private JLabel crearSeccion(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        lbl.setForeground(new Color(0, 90, 130));
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextArea crearTexto(String contenido) {
        JTextArea area = new JTextArea(contenido);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setBackground(new Color(247, 252, 249));
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 230, 210), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        area.setAlignmentX(Component.LEFT_ALIGNMENT);
        return area;
    }

    private void agregarHover(JButton boton, Color colorNormal, Color colorHover) {
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(colorHover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(colorNormal);
            }
        });
    }
}
