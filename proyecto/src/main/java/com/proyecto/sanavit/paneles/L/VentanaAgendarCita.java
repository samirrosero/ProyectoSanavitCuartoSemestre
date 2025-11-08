package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class VentanaAgendarCita extends JFrame {

    private JTextField txtPaciente, txtIdentificacion;
    private JComboBox<String> cmbMedico, cmbEspecialidad, cmbFecha, cmbHora, cmbModalidad;
    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;
    private JButton btnGuardar, btnCancelar, btnVolver;
    private static final int ID_ESTADO_AGENDADA = 1; // Estado "Agendada"

    private MedicoDao medicoDAO = new MedicoDao();
    private CitaDao citaDAO = new CitaDao();

    public VentanaAgendarCita(Object pacienteActual) {
        setTitle("Agendar Cita - Sanavit");
        setSize(670, 510);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // 🎨 Fondo con degradado vertical (profesional)
        JPanel fondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();

                Color colorInicio = new Color(43, 182, 115); 
                Color colorFinal = Color.WHITE;         
                GradientPaint gradiente = new GradientPaint(0, 0, colorInicio, 0, height, colorFinal);
                g2d.setPaint(gradiente);
                g2d.fillRect(0, 0, width, height);
            }
        };
        fondo.setLayout(new BorderLayout(10, 10));
        setContentPane(fondo);

        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        Color fondoPanel = new Color(228, 237, 232); 
        Color textoPrincipal = new Color(5, 5, 5); 

        // 🔹 Panel datos del paciente
        JPanel panelPaciente = new JPanel(new GridLayout(3, 2, 10, 15));
        panelPaciente.setBorder(BorderFactory.createTitledBorder("Datos del Paciente"));
        panelPaciente.setBackground(fondoPanel);

        JLabel lblPaciente = new JLabel("Paciente:");
        lblPaciente.setForeground(textoPrincipal);
        JLabel lblIdentificacion = new JLabel("Identificación:");
        lblIdentificacion.setForeground(textoPrincipal);

        txtPaciente = new JTextField((pacienteActual != null ? ((Paciente) pacienteActual).getNombre() : ""));
        txtIdentificacion = new JTextField(
                (pacienteActual != null ? ((Paciente) pacienteActual).getIdentificacion() : ""));

        panelPaciente.add(lblPaciente);
        panelPaciente.add(txtPaciente);
        panelPaciente.add(lblIdentificacion);
        panelPaciente.add(txtIdentificacion);

        // 🔹 Panel cita
        JPanel panelCita = new JPanel(new GridLayout(5, 2, 10, 10));
        panelCita.setBorder(BorderFactory.createTitledBorder("Detalles de la Cita"));
        panelCita.setBackground(fondoPanel);

        cmbMedico = new JComboBox<>();
        cmbEspecialidad = new JComboBox<>();
        cmbFecha = new JComboBox<>(new String[]{
                "05/01/2025", "06/01/2025", "07/01/2025", "17/02/2025", "09/03/2025",
                "22/04/2025", "11/05/2025", "28/06/2025", "14/07/2025", "30/08/2025",
                "19/09/2025", "03/10/2025", "21/11/2025", "07/12/2025"
        });
        cmbHora = new JComboBox<>(new String[]{
                "06:00", "07:00", "08:00", "09:00", "10:00",
                "11:00", "12:00", "13:00", "14:00", "15:00",
                "16:00", "17:00", "18:00", "19:00", "20:00"
        });
        cmbModalidad = new JComboBox<>(new String[]{"Presencial", "Virtual"});

        cargarMedicos();

        JLabel[] etiquetas = {
                new JLabel("Médico:"), new JLabel("Especialidad:"),
                new JLabel("Fecha disponible:"), new JLabel("Hora disponible:"),
                new JLabel("Modalidad:")
        };
        for (JLabel lbl : etiquetas) {
            lbl.setForeground(textoPrincipal);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            panelCita.add(lbl);
            switch (lbl.getText()) {
                case "Médico:" -> panelCita.add(cmbMedico);
                case "Especialidad:" -> panelCita.add(cmbEspecialidad);
                case "Fecha disponible:" -> panelCita.add(cmbFecha);
                case "Hora disponible:" -> panelCita.add(cmbHora);
                case "Modalidad:" -> panelCita.add(cmbModalidad);
            }
        }

        // 🔹 Panel botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(new Color(0xDDEDF0));

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        btnVolver = new JButton("Volver");

        configurarBoton(btnGuardar, new Color(92, 184, 92));    
        configurarBoton(btnCancelar, new Color(70, 130, 180));  
        configurarBoton(btnVolver, new Color(92, 184, 92));       

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnVolver);

        // 🔹 Acciones
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> limpiarCampos());
        btnVolver.addActionListener(e -> dispose());

        // 🔹 Estructura general
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setOpaque(false);
        panelCentro.add(panelCita, BorderLayout.NORTH);

        add(panelPaciente, BorderLayout.NORTH);
        add(panelCentro, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        cmbEspecialidad.addActionListener(e -> filtrarMedicosPorEspecialidad());

        setVisible(true);
    }

    // 🎨 Configura los botones con color base y hover
    private void configurarBoton(JButton boton, Color colorBase) {
        boton.setBackground(colorBase);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { boton.setBackground(colorBase.brighter()); }
            public void mouseExited(MouseEvent evt) { boton.setBackground(colorBase); }
        });
    }

    // 🔹 Cargar médicos y especialidades
    private void cargarMedicos() {
        List<Medico> medicos = MedicoDao.listarMedicos();
        cmbMedico.removeAllItems();
        cmbEspecialidad.removeAllItems();

        if (medicos.isEmpty()) {
            cmbMedico.addItem("No hay médicos registrados");
        } else {
            java.util.Set<String> especialidadesUnicas = new java.util.HashSet<>();
            for (Medico m : medicos) {
                cmbMedico.addItem(m.getNombre());
                especialidadesUnicas.add(m.getEspecialidad());
            }
            for (String esp : especialidadesUnicas) {
                cmbEspecialidad.addItem(esp);
            }
        }
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

    private int obtenerIdModalidadPorNombre(String modalidadNombre) {
        return switch (modalidadNombre.toLowerCase()) {
            case "presencial" -> 1;
            case "virtual" -> 2;
            default -> 0;
        };
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

            if (pacienteNombre.isEmpty() || identificacion.isEmpty() || medicoNombre == null || especialidad == null
                    || fechaStr == null || horaStr == null || modalidadNombre == null) {
                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos obligatorios.", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "Cita guardada correctamente ✅");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar la cita ❌");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void limpiarCampos() {
        txtPaciente.setText("");
        txtIdentificacion.setText("");
        cmbMedico.setSelectedIndex(-1);
        cmbEspecialidad.setSelectedIndex(-1);
        cmbFecha.setSelectedIndex(-1);
        cmbHora.setSelectedIndex(-1);
        cmbModalidad.setSelectedIndex(-1);
    }

    public static void main(String[] args) {
        Object pacienteActual = null;
        new VentanaAgendarCita(pacienteActual);
    }
}
