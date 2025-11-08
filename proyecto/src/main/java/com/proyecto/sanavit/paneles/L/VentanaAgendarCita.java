package com.proyecto.sanavit.paneles.L;

import javax.swing.*;
import com.proyecto.sanavit.modelo.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class VentanaAgendarCita extends JFrame {

    private JTextField txtPaciente, txtIdentificacion;
    private JComboBox<String> cmbMedico, cmbEspecialidad, cmbFecha, cmbHora, cmbModalidad;
    private JButton btnGuardar, btnCancelar, btnVolver;
    private static final int ID_ESTADO_AGENDADA = 1; // Estado "Agendada"

    private MedicoDao medicoDAO = new MedicoDao();
    private CitaDao citaDAO = new CitaDao();

    public VentanaAgendarCita(Object pacienteActual) {
        setTitle("Agendar Cita - Sanavit");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // === Panel de datos del paciente ===
        JPanel panelPaciente = new JPanel(new GridLayout(2, 2, 10, 10));
        panelPaciente.setBorder(BorderFactory.createTitledBorder("Datos del Paciente"));
        panelPaciente.add(new JLabel("Paciente:"));
        txtPaciente = new JTextField((pacienteActual != null ? ((Paciente) pacienteActual).getNombre() : ""));
        txtPaciente.setEditable(false);
        panelPaciente.add(txtPaciente);
        panelPaciente.add(new JLabel("Identificación:"));
        txtIdentificacion = new JTextField(
                (pacienteActual != null ? ((Paciente) pacienteActual).getIdentificacion() : ""));
        txtIdentificacion.setEditable(false);
        panelPaciente.add(txtIdentificacion);

        // === Panel de detalles de la cita ===
        JPanel panelCita = new JPanel(new GridLayout(7, 2, 10, 10));
        panelCita.setBorder(BorderFactory.createTitledBorder("Detalles de la Cita"));

        cmbMedico = new JComboBox<>();
        cmbEspecialidad = new JComboBox<>();
        cmbFecha = new JComboBox<>(new String[]{
                "05/01/2025", "06/01/2025", "07/01/2025", "17/02/2025", "09/03/2025",
                "22/04/2025", "11/05/2025", "28/06/2025", "14/07/2025", "30/08/2025",
                "19/09/2025", "03/10/2025", "21/11/2025", "07/12/2025"
        });
        cmbHora = new JComboBox<>();
        cmbModalidad = new JComboBox<>(new String[]{"Presencial", "Virtual"});

        // Cargar médicos y especialidades
        cargarMedicos();

        panelCita.add(new JLabel("Especialidad:"));
        panelCita.add(cmbEspecialidad);
        panelCita.add(new JLabel("Médico:"));
        panelCita.add(cmbMedico);
        panelCita.add(new JLabel("Fecha disponible:"));
        panelCita.add(cmbFecha);
        panelCita.add(new JLabel("Hora disponible:"));
        panelCita.add(cmbHora);
        panelCita.add(new JLabel("Modalidad:"));
        panelCita.add(cmbModalidad);

        // === Panel de botones ===
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        btnVolver = new JButton("Volver");

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

        // === Estructura general ===
        add(panelPaciente, BorderLayout.NORTH);
        add(panelCita, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    // === Cargar médicos y especialidades ===
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

    // === Filtrar médicos por especialidad ===
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

    // === Actualizar horas disponibles dinámicamente ===
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

    // === Guardar cita ===
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
        switch (modalidadNombre.toLowerCase()) {
            case "presencial":
                return 1;
            case "virtual":
                return 2;
            default:
                return 0;
        }
    }

    // === Limpiar campos ===
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
