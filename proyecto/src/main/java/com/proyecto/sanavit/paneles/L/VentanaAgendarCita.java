package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class VentanaAgendarCita extends JFrame {

    private JTextField txtPaciente, txtIdentificacion;
    private JComboBox<String> cmbMedico, cmbEspecialidad, cmbFecha, cmbHora, cmbModalidad;
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

        // 🎨 Panel de fondo con degradado y bordes suaves
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

        JLabel lblPaciente = new JLabel("Paciente:");
        JLabel lblIdentificacion = new JLabel("Identificación:");
        lblPaciente.setForeground(textoPrincipal);
        lblIdentificacion.setForeground(textoPrincipal);

        txtPaciente = new JTextField((pacienteActual != null ? ((Paciente) pacienteActual).getNombre() : ""));
        txtPaciente.setEditable(false);
        txtPaciente.setBorder(bordeCampo());
        txtIdentificacion = new JTextField(
                (pacienteActual != null ? ((Paciente) pacienteActual).getIdentificacion() : ""));
        txtIdentificacion.setEditable(false);
        txtIdentificacion.setBorder(bordeCampo());

        panelPaciente.add(lblPaciente);
        panelPaciente.add(txtPaciente);
        panelPaciente.add(lblIdentificacion);
        panelPaciente.add(txtIdentificacion);

        // === Panel Cita ===
        JPanel panelCita = crearPanelBase("Detalles de la Cita", fondoPanel);
        panelCita.setLayout(new GridLayout(6, 2, 10, 10));

        cmbMedico = new JComboBox<>();
        cmbEspecialidad = new JComboBox<>();
        cmbFecha = new JComboBox<>(new String[]{
                "05/01/2025", "06/01/2025", "07/01/2025", "17/02/2025", "09/03/2025",
                "22/04/2025", "11/05/2025", "28/06/2025", "14/07/2025", "30/08/2025",
                "19/09/2025", "03/10/2025", "21/11/2025", "07/12/2025"
        });
        cmbHora = new JComboBox<>();
        cmbModalidad = new JComboBox<>(new String[]{"Presencial", "Virtual"});

        estilizarCombo(cmbMedico);
        estilizarCombo(cmbEspecialidad);
        estilizarCombo(cmbFecha);
        estilizarCombo(cmbHora);
        estilizarCombo(cmbModalidad);

        cargarMedicos();

        panelCita.add(crearLabel("Especialidad:", textoPrincipal));
        panelCita.add(cmbEspecialidad);
        panelCita.add(crearLabel("Médico:", textoPrincipal));
        panelCita.add(cmbMedico);
        panelCita.add(crearLabel("Fecha disponible:", textoPrincipal));
        panelCita.add(cmbFecha);
        panelCita.add(crearLabel("Hora disponible:", textoPrincipal));
        panelCita.add(cmbHora);
        panelCita.add(crearLabel("Modalidad:", textoPrincipal));
        panelCita.add(cmbModalidad);

        // === Panel Botones ===
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(0, 0, 0, 0));

        btnGuardar = crearBoton("Guardar", new Color(34, 197, 94));
        btnCancelar = crearBoton("Cancelar", new Color(59, 130, 246));
        btnVolver = crearBoton("Volver", new Color(168, 85, 247));

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnVolver);

        // === Listeners ===
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> limpiarCampos());
        btnVolver.addActionListener(e -> dispose());
        cmbEspecialidad.addActionListener(e -> filtrarMedicosPorEspecialidad());
        cmbMedico.addActionListener(e -> actualizarHorasDisponibles());
        cmbFecha.addActionListener(e -> actualizarHorasDisponibles());

        fondo.add(panelPaciente, BorderLayout.NORTH);
        fondo.add(panelCita, BorderLayout.CENTER);
        fondo.add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    // === Helpers visuales ===
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

        // Bordes redondeados y sombra
        boton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(colorBase.darker());
                g2.fillRoundRect(2, 4, c.getWidth() - 4, c.getHeight() - 4, 18, 18);
                g2.setColor(colorBase);
                g2.fillRoundRect(0, 0, c.getWidth() - 4, c.getHeight() - 6, 18, 18);
                super.paint(g2, c);
            }
        });

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(colorBase.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(colorBase);
            }
        });
        return boton;
    }

    // === Cargar médicos ===
    private void cargarMedicos() {
        List<Medico> medicos = MedicoDao.listarMedicos();
        cmbMedico.removeAllItems();
        cmbEspecialidad.removeAllItems();

        if (medicos.isEmpty()) {
            cmbMedico.addItem("No hay médicos registrados");
            return;
        }

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

        if (cmbMedico.getItemCount() == 0) {
            cmbMedico.addItem("Sin médicos disponibles");
        }
    }

    private void actualizarHorasDisponibles() {
        String medicoNombre = (String) cmbMedico.getSelectedItem();
        String fechaStr = (String) cmbFecha.getSelectedItem();

        if (medicoNombre == null || fechaStr == null ||
                medicoNombre.isEmpty() || fechaStr.isEmpty() ||
                medicoNombre.equals("Sin médicos disponibles")) {
            return;
        }

        try {
            Medico medico = medicoDAO.obtenerPorNombre(medicoNombre);
            if (medico == null) return;

            java.util.Date parsedDate = new SimpleDateFormat("dd/MM/yyyy").parse(fechaStr);
            java.sql.Date fecha = new java.sql.Date(parsedDate.getTime());

            List<String> horasOcupadas = citaDAO.obtenerHorasOcupadas(medico.getIdMedico(), fecha);

            cmbHora.removeAllItems();

            String[] todasLasHoras = {
                    "06:00", "07:00", "08:00", "09:00", "10:00",
                    "11:00", "12:00", "13:00", "14:00", "15:00",
                    "16:00", "17:00", "18:00", "19:00", "20:00"
            };

            for (String hora : todasLasHoras) {
                if (!horasOcupadas.contains(hora)) {
                    cmbHora.addItem(hora);
                }
            }

            if (cmbHora.getItemCount() == 0) {
                cmbHora.addItem("Sin horas disponibles");
                JOptionPane.showMessageDialog(this,
                        "Este médico no tiene horas disponibles para la fecha seleccionada.",
                        "Sin disponibilidad", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void guardar() {
        try {
            String pacienteNombre = txtPaciente.getText().trim();
            String identificacion = txtIdentificacion.getText().trim();
            String medicoNombre = (String) cmbMedico.getSelectedItem();
            String especialidad = (String) cmbEspecialidad.getSelectedItem();
            String fechaStr = (String) cmbFecha.getSelectedItem();
            String horaStr = (String) cmbHora.getSelectedItem();
            String modalidadNombre = (String) cmbModalidad.getSelectedItem();

            if (pacienteNombre.isEmpty() || identificacion.isEmpty() ||
                    medicoNombre == null || especialidad == null ||
                    fechaStr == null || horaStr == null || modalidadNombre == null ||
                    horaStr.equals("Sin horas disponibles")) {
                JOptionPane.showMessageDialog(this,
                        "Por favor complete todos los campos obligatorios.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            java.util.Date parsedDate = new SimpleDateFormat("dd/MM/yyyy").parse(fechaStr);
            java.sql.Date fecha = new java.sql.Date(parsedDate.getTime());
            java.sql.Time hora = java.sql.Time.valueOf(horaStr + ":00");

            Medico medico = medicoDAO.obtenerPorNombre(medicoNombre);
            if (medico == null) {
                JOptionPane.showMessageDialog(this, "Médico no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Paciente paciente = PacienteDao.obtenerPorIdentificacion(identificacion);
            if (paciente == null) {
                JOptionPane.showMessageDialog(this, "Paciente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idModalidad = obtenerIdModalidadPorNombre(modalidadNombre);

            Cita nuevaCita = new Cita();
            nuevaCita.setIdPaciente(paciente.getIdPaciente());
            nuevaCita.setIdMedico(medico.getIdMedico());
            nuevaCita.setIdEstadoCita(ID_ESTADO_AGENDADA);
            nuevaCita.setFechaCita(fecha);
            nuevaCita.setHoraCita(hora);
            nuevaCita.setIdModalidad(idModalidad);

            boolean exito = citaDAO.insertarCita(nuevaCita);

            if (exito) {
                JOptionPane.showMessageDialog(this, "Cita agendada correctamente ✅");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar la cita ❌");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int obtenerIdModalidadPorNombre(String modalidadNombre) {
        return switch (modalidadNombre.toLowerCase()) {
            case "presencial" -> 1;
            case "virtual" -> 2;
            default -> 0;
        };
    }

    private void limpiarCampos() {
        cmbEspecialidad.setSelectedIndex(-1);
        cmbMedico.removeAllItems();
        cmbFecha.setSelectedIndex(-1);
        cmbHora.removeAllItems();
        cmbModalidad.setSelectedIndex(-1);
    }

    public static void main(String[] args) {
        Object pacienteActual = null;
        new VentanaAgendarCita(pacienteActual);
    }
}
