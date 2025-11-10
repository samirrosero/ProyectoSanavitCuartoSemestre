package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class VentanaAgendarCita extends JFrame {

    private JTextField txtPaciente, txtIdentificacion;
    private JComboBox<String> cmbMedico, cmbEspecialidad, cmbHora, cmbModalidad;
    private JDateChooser dateChooser;
    private JButton btnGuardar, btnCancelar, btnVolver;
    private static final int ID_ESTADO_AGENDADA = 1;

    private MedicoDao medicoDAO = new MedicoDao();
    private CitaDao citaDAO = new CitaDao();

    public VentanaAgendarCita(Object pacienteActual) {
        setTitle("Agendar Cita - Sanavit");
        setSize(700, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setUndecorated(false);

        // Fondo degradado
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
                g2.fillRoundRect(0, 0, w, h, 25, 25);
            }
        };
        fondo.setLayout(new BorderLayout(10, 10));
        fondo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(fondo);

        Color fondoPanel = new Color(239, 247, 243);
        Color textoPrincipal = new Color(20, 20, 20);

        // === Panel Paciente ===
        JPanel panelPaciente = crearPanelBase("Datos del Paciente", fondoPanel);
        panelPaciente.setLayout(new GridLayout(2, 2, 10, 10));

        txtPaciente = new JTextField((pacienteActual != null ? ((Paciente) pacienteActual).getNombre() : ""));
        txtPaciente.setEditable(false);
        txtPaciente.setBorder(bordeCampo());
        txtIdentificacion = new JTextField(
                (pacienteActual != null ? ((Paciente) pacienteActual).getIdentificacion() : ""));
        txtIdentificacion.setEditable(false);
        txtIdentificacion.setBorder(bordeCampo());

        panelPaciente.add(new JLabel("Paciente:"));
        panelPaciente.add(txtPaciente);
        panelPaciente.add(new JLabel("Identificación:"));
        panelPaciente.add(txtIdentificacion);

        // === Panel Cita ===
        JPanel panelCita = crearPanelBase("Detalles de la Cita", fondoPanel);
        panelCita.setLayout(new GridLayout(6, 2, 10, 10));

        cmbMedico = new JComboBox<>();
        cmbEspecialidad = new JComboBox<>();
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        cmbHora = new JComboBox<>();
        cmbModalidad = new JComboBox<>(new String[]{"Presencial", "Virtual"});

        estilizarCombo(cmbMedico);
        estilizarCombo(cmbEspecialidad);
        estilizarCombo(cmbHora);
        estilizarCombo(cmbModalidad);

        cargarMedicos();

        panelCita.add(crearLabel("Especialidad:", textoPrincipal));
        panelCita.add(cmbEspecialidad);
        panelCita.add(crearLabel("Médico:", textoPrincipal));
        panelCita.add(cmbMedico);
        panelCita.add(crearLabel("Fecha disponible:", textoPrincipal));
        panelCita.add(dateChooser);
        panelCita.add(crearLabel("Hora disponible:", textoPrincipal));
        panelCita.add(cmbHora);
        panelCita.add(crearLabel("Modalidad:", textoPrincipal));
        panelCita.add(cmbModalidad);

        // === Panel Botones ===
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(0, 0, 0, 0));

        btnGuardar = crearBoton("Guardar", new Color(65, 163, 47));
        btnCancelar = crearBoton("Cancelar", new Color(65, 163, 47));
        btnVolver = crearBoton("Volver", new Color(65, 163, 47));

        // 🎨 Efectos hover distintos para cada botón
        agregarHover(btnGuardar, new Color(61, 143, 46), new Color(114, 214, 96));
        agregarHover(btnCancelar, new Color(65, 163, 47), new Color(230, 90, 90));
        agregarHover(btnVolver, new Color(65, 163, 47), new Color(114, 214, 96));

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnVolver);

        // === Listeners ===
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> limpiarCampos());
        btnVolver.addActionListener(e -> dispose());
        cmbEspecialidad.addActionListener(e -> filtrarMedicosPorEspecialidad());
        cmbMedico.addActionListener(e -> actualizarHorasDisponibles());
        dateChooser.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) actualizarHorasDisponibles();
        });

        fondo.add(panelPaciente, BorderLayout.NORTH);
        fondo.add(panelCita, BorderLayout.CENTER);
        fondo.add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    // === Métodos visuales ===
    private JPanel crearPanelBase(String titulo, Color fondo) {
        JPanel panel = new JPanel();
        panel.setBackground(fondo);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 230, 210)),
                titulo));
        return panel;
    }

    private JLabel crearLabel(String texto, Color color) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(color);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return lbl;
    }

    private Border bordeCampo() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 190), 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        );
    }

    private void estilizarCombo(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBorder(bordeCampo());
        combo.setBackground(Color.WHITE);
    }

    private JButton crearBoton(String texto, Color colorBase) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorBase);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    // 🎨 Método para aplicar hover (efecto al pasar el mouse)
    private void agregarHover(JButton boton, Color colorNormal, Color colorHover) {
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(colorHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(colorNormal);
            }
        });
    }

    // === Cargar médicos y especialidades ===
    private void cargarMedicos() {
        List<Medico> medicos = MedicoDao.listarMedicos();
        cmbMedico.removeAllItems();
        cmbEspecialidad.removeAllItems();

        java.util.Set<String> especialidadesUnicas = new java.util.HashSet<>();
        for (Medico m : medicos) {
            especialidadesUnicas.add(m.getEspecialidad());
        }
        for (String esp : especialidadesUnicas) {
            cmbEspecialidad.addItem(esp);
        }

        filtrarMedicosPorEspecialidad();
    }

    private void filtrarMedicosPorEspecialidad() {
        String especialidadSeleccionada = (String) cmbEspecialidad.getSelectedItem();
        if (especialidadSeleccionada == null) return;

        List<Medico> medicos = MedicoDao.listarMedicos();
        cmbMedico.removeAllItems();
        for (Medico m : medicos) {
            if (especialidadSeleccionada.equals(m.getEspecialidad())) {
                cmbMedico.addItem(m.getNombre());
            }
        }
    }

    private void actualizarHorasDisponibles() {
        try {
            String medicoNombre = (String) cmbMedico.getSelectedItem();
            java.util.Date fechaSeleccionada = dateChooser.getDate();

            if (medicoNombre == null || fechaSeleccionada == null) return;

            Medico medico = medicoDAO.obtenerPorNombre(medicoNombre);
            if (medico == null) return;

            java.sql.Date fechaSQL = new java.sql.Date(fechaSeleccionada.getTime());
            List<String> horasOcupadas = citaDAO.obtenerHorasOcupadas(medico.getIdMedico(), fechaSQL);

            cmbHora.removeAllItems();
            String[] todasLasHoras = {
                    "06:00", "07:00", "08:00", "09:00", "10:00",
                    "11:00", "12:00", "13:00", "14:00", "15:00",
                    "16:00", "17:00", "18:00"
            };

            for (String h : todasLasHoras) {
                if (!horasOcupadas.contains(h)) cmbHora.addItem(h);
            }

            if (cmbHora.getItemCount() == 0) cmbHora.addItem("Sin horas disponibles");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void guardar() {
        try {
            String pacienteNombre = txtPaciente.getText().trim();
            String identificacion = txtIdentificacion.getText().trim();
            String medicoNombre = (String) cmbMedico.getSelectedItem();
            String horaStr = (String) cmbHora.getSelectedItem();
            String modalidad = (String) cmbModalidad.getSelectedItem();
            java.util.Date fechaSeleccionada = dateChooser.getDate();

            if (pacienteNombre.isEmpty() || identificacion.isEmpty() ||
                    medicoNombre == null || fechaSeleccionada == null ||
                    horaStr == null || modalidad == null ||
                    "Sin horas disponibles".equals(horaStr)) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }

            Medico medico = medicoDAO.obtenerPorNombre(medicoNombre);
            Paciente paciente = PacienteDao.obtenerPorIdentificacion(identificacion);
            java.sql.Date fecha = new java.sql.Date(fechaSeleccionada.getTime());
            java.sql.Time hora = java.sql.Time.valueOf(horaStr + ":00");
            int idModalidad = modalidad.equalsIgnoreCase("Presencial") ? 1 : 2;

            Cita nueva = new Cita(0, medico.getIdMedico(), paciente.getIdPaciente(),
                    ID_ESTADO_AGENDADA, idModalidad, fecha, hora);

            if (citaDAO.insertarCita(nueva))
                JOptionPane.showMessageDialog(this, "✅ Cita agendada correctamente.");
            else
                JOptionPane.showMessageDialog(this, "❌ Error al guardar la cita.");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        cmbEspecialidad.setSelectedIndex(-1);
        cmbMedico.removeAllItems();
        dateChooser.setDate(null);
        cmbHora.removeAllItems();
        cmbModalidad.setSelectedIndex(-1);
    }

    public static void main(String[] args) {
        new VentanaAgendarCita(null);
    }
}
