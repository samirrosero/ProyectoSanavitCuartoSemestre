package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class VentanaMedico extends JFrame {

    private JLabel lblFotoPerfil, lblNombre, lblEspecialidad;
    private JButton btnIniciarAtencion, btnFinalizarAtencion, btnCerrarSesion;
    private JTable tablaCita;
    private DefaultTableModel modeloTabla;

    private Medico medicoActual;
    private EjecucionCita ejecucionActual;
    private LocalDateTime horaInicio;

    public VentanaMedico(Usuario usuarioActual) {
        setTitle("Panel del Médico - Sanavit");
        setSize(950, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(244, 247, 250));

        // === DATOS DEL MÉDICO ===
        MedicoDao medicoDAO = new MedicoDao();
        medicoActual = medicoDAO.obtenerMedicoPorIdUsuario(usuarioActual.getIdUsuario());

        // === PANEL SUPERIOR (Encabezado) ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 123, 255));
        header.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblBienvenida = new JLabel("<html><span style='font-size:18px;'>Bienvenido(a)</span><br>"
                + "<b style='font-size:24px;'>" + medicoActual.getNombre() + "</b><br>"
                + "<span style='font-size:16px;'>" + medicoActual.getEspecialidad() + "</span></html>");
        lblBienvenida.setForeground(Color.WHITE);
        header.add(lblBienvenida, BorderLayout.WEST);

        // === Foto del médico (derecha, circular) ===
        lblFotoPerfil = new JLabel();
        lblFotoPerfil.setPreferredSize(new Dimension(120, 120));
        lblFotoPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblFotoPerfil.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));

        setFotoPerfil("C:\\ruta\\a\\foto_medico.png"); // Ruta temporal

        lblFotoPerfil.addMouseListener(new MouseAdapter() {
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
        header.add(panelFoto, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // === PANEL CENTRAL ===
        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panelCentral.setBackground(new Color(244, 247, 250));

        // === TARJETA INFORMATIVA DEL MÉDICO ===
        JPanel infoPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 18, 18);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 18, 18);
                super.paintComponent(g);
            }
        };
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitulo = new JLabel("Datos del Médico");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(0, 123, 255));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblNombre = new JLabel("👨‍⚕️ " + medicoActual.getNombre());
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblEspecialidad = new JLabel("🩺 Especialidad: " + medicoActual.getEspecialidad());
        lblEspecialidad.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblEspecialidad.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(lblTitulo);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(lblNombre);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(lblEspecialidad);

        panelCentral.add(infoPanel, BorderLayout.NORTH);

        // === TABLA DE CITAS ===
        modeloTabla = new DefaultTableModel(new String[]{"ID Cita", "Paciente", "Fecha", "Hora", "Modalidad"}, 0);
        tablaCita = new JTable(modeloTabla);
        tablaCita.setRowHeight(28);
        tablaCita.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCita.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaCita.getTableHeader().setBackground(new Color(230, 240, 255));

        JScrollPane scroll = new JScrollPane(tablaCita);
        scroll.setBorder(BorderFactory.createTitledBorder("Citas Asignadas"));
        panelCentral.add(scroll, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        // === PANEL DE BOTONES ===
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        panelBotones.setBackground(new Color(244, 247, 250));

        btnIniciarAtencion = crearBoton("Iniciar Atención", new Color(0, 123, 255));
        btnFinalizarAtencion = crearBoton("Finalizar Atención", new Color(40, 167, 69));
        btnCerrarSesion = crearBoton("Cerrar Sesión", new Color(220, 53, 69));

        btnFinalizarAtencion.setEnabled(false);

        panelBotones.add(btnIniciarAtencion);
        panelBotones.add(btnFinalizarAtencion);
        panelBotones.add(btnCerrarSesion);
        add(panelBotones, BorderLayout.SOUTH);

        // === CARGAR CITAS ===
        cargarCitasDelMedico();

        // === EVENTOS ===
        btnIniciarAtencion.addActionListener(e -> iniciarAtencion());
        btnFinalizarAtencion.addActionListener(e -> finalizarAtencion());
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new Login();
        });

        setVisible(true);
    }

    // ------------------- MÉTODOS -------------------

    private void setFotoPerfil(String ruta) {
        try {
            BufferedImage original = ImageIO.read(new File(ruta));
            int size = Math.min(original.getWidth(), original.getHeight());
            BufferedImage circular = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circular.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape clip = new Ellipse2D.Float(0, 0, 120, 120);
            g2.setClip(clip);
            g2.drawImage(original, 0, 0, 120, 120, null);
            g2.dispose();
            lblFotoPerfil.setIcon(new ImageIcon(circular));
            lblFotoPerfil.repaint();
        } catch (IOException e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
        }
    }

    private JButton crearBoton(String texto, Color colorBase) {
        JButton btn = new JButton(texto);
        btn.setBackground(colorBase);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(colorBase.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(colorBase);
            }
        });
        return btn;
    }

    private void cargarCitasDelMedico() {
        modeloTabla.setRowCount(0);
        CitaDao citaDao = new CitaDao();
        PacienteDao pacienteDao = new PacienteDao();
        List<Cita> citas = citaDao.obtenerCitaPorMedico(medicoActual.getIdMedico());

        for (Cita c : citas) {
            Paciente p = pacienteDao.obtenerPacientePorId(c.getIdPaciente());
            modeloTabla.addRow(new Object[]{
                c.getIdCita(),
                (p != null ? p.getNombre() : "Desconocido"),
                c.getFechaCita(),
                c.getHoraCita(),
                c.getIdModalidad() == 1 ? "Presencial" : "Virtual"
            });
        }
    }

    private void iniciarAtencion() {
        int fila = tablaCita.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita para iniciar atención.");
            return;
        }

        int idCita = (int) modeloTabla.getValueAt(fila, 0);
        CitaDao citaDao = new CitaDao();
        Cita cita = citaDao.obtenerCitaPorId(idCita);
        horaInicio = LocalDateTime.now();

        EjecucionCita ejec = new EjecucionCita();
        ejec.setIdCita(idCita);
        ejec.setFechaHoraIngreso(Timestamp.valueOf(horaInicio));
        ejec.setFechaHoraSalida(null);
        ejec.setDuracion(0);

        EjecucionCitaDao ejecDao = new EjecucionCitaDao();
        boolean ok = ejecDao.insertarEjecucion(ejec);

        if (ok) {
            ejecucionActual = ejec;
            JOptionPane.showMessageDialog(this, "Atención iniciada correctamente.");
            btnIniciarAtencion.setEnabled(false);
            btnFinalizarAtencion.setEnabled(true);

            PacienteDao pacienteDao = new PacienteDao();
            Paciente paciente = pacienteDao.obtenerPacientePorId(cita.getIdPaciente());
            new VentanaHistoriaClinica(paciente, cita, ejecucionActual);
        } else {
            JOptionPane.showMessageDialog(this, "Error al iniciar atención.");
        }
    }

    private void finalizarAtencion() {
        if (ejecucionActual == null) {
            JOptionPane.showMessageDialog(this, "No hay atención activa.");
            return;
        }

        LocalDateTime horaFin = LocalDateTime.now();
        int duracionMinutos = (int) Duration.between(horaInicio, horaFin).toMinutes();

        ejecucionActual.setFechaHoraSalida(Timestamp.valueOf(horaFin));
        ejecucionActual.setDuracion(duracionMinutos);

        EjecucionCitaDao ejecDao = new EjecucionCitaDao();
        boolean ok = ejecDao.updateEjecucion(ejecucionActual);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Atención finalizada.\nDuración: " + duracionMinutos + " minutos.");
            btnIniciarAtencion.setEnabled(true);
            btnFinalizarAtencion.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Error al finalizar atención.");
        }
    }
}
