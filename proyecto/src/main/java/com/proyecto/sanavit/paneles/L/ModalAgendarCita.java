package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModalAgendarCita extends JDialog {

    private JComboBox<String> cmbPaciente, cmbEspecialidad, cmbMedico, cmbHora, cmbModalidad;
    private JDateChooser dateChooser; // ✅ Nuevo selector de fecha
    private JButton btnGuardar, btnCancelar;

    private final CitaDao citaDAO = new CitaDao();
    private final PacienteDao pacienteDAO = new PacienteDao();
    private final MedicoDao medicoDAO = new MedicoDao();

    private List<Medico> listaMedicos; // todos los médicos cargados inicialmente

    public ModalAgendarCita(JFrame parent) {
        super(parent, "Agendar Cita (Gestor)", true);
        setSize(520, 480);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(7, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        cmbPaciente = new JComboBox<>();
        cmbEspecialidad = new JComboBox<>();
        cmbMedico = new JComboBox<>();
        cmbHora = new JComboBox<>();
        cmbModalidad = new JComboBox<>(new String[]{"Presencial", "Virtual"});
        dateChooser = new JDateChooser(); // ✅ reemplazo de cmbFecha
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setMinSelectableDate(new java.util.Date()); // no fechas pasadas

        // Cargar datos iniciales
        cargarPacientes();
        cargarEspecialidades();

        // --- Campos ---
        form.add(new JLabel("Paciente:")); form.add(cmbPaciente);
        form.add(new JLabel("Especialidad:")); form.add(cmbEspecialidad);
        form.add(new JLabel("Médico:")); form.add(cmbMedico);
        form.add(new JLabel("Fecha:")); form.add(dateChooser); // ✅ cambiado
        form.add(new JLabel("Hora disponible:")); form.add(cmbHora);
        form.add(new JLabel("Modalidad:")); form.add(cmbModalidad);

        add(form, BorderLayout.CENTER);

        // === BOTONES ===
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);

        footer.add(btnGuardar);
        footer.add(btnCancelar);
        add(footer, BorderLayout.SOUTH);

        // === EVENTOS ===
        cmbEspecialidad.addActionListener(e -> actualizarMedicosPorEspecialidad());
        cmbMedico.addActionListener(e -> actualizarHorasDisponibles());
        dateChooser.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) actualizarHorasDisponibles();
        });
        btnGuardar.addActionListener(e -> guardarCita());
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    // 🔹 Cargar pacientes
    private void cargarPacientes() {
        cmbPaciente.removeAllItems();
        List<Paciente> list = pacienteDAO.obtenerTodosLosPacientes();
        for (Paciente p : list) cmbPaciente.addItem(p.getNombre());
    }

    // 🔹 Cargar especialidades únicas
    private void cargarEspecialidades() {
        listaMedicos = MedicoDao.listarMedicos();
        Set<String> especialidades = listaMedicos.stream()
                .map(Medico::getEspecialidad)
                .collect(Collectors.toSet());

        cmbEspecialidad.removeAllItems();
        for (String esp : especialidades) {
            cmbEspecialidad.addItem(esp);
        }
    }

    // 🔹 Mostrar médicos según la especialidad seleccionada
    private void actualizarMedicosPorEspecialidad() {
        cmbMedico.removeAllItems();
        String especialidadSeleccionada = (String) cmbEspecialidad.getSelectedItem();
        if (especialidadSeleccionada == null) return;

        List<Medico> filtrados = listaMedicos.stream()
                .filter(m -> m.getEspecialidad().equalsIgnoreCase(especialidadSeleccionada))
                .toList();

        for (Medico m : filtrados) {
            cmbMedico.addItem(m.getNombre());
        }

        cmbHora.removeAllItems(); // limpiar horarios previos
    }

    // 🔹 Cargar horas disponibles según médico y fecha
    private void actualizarHorasDisponibles() {
        cmbHora.removeAllItems();
        try {
            String medicoNombre = (String) cmbMedico.getSelectedItem();
            java.util.Date fechaSeleccionada = dateChooser.getDate();
            if (medicoNombre == null || fechaSeleccionada == null) return;

            Medico m = medicoDAO.obtenerPorNombre(medicoNombre);
            if (m == null) return;

            Date fechaSql = new Date(fechaSeleccionada.getTime());
            List<String> ocupadas = citaDAO.obtenerHorasOcupadas(m.getIdMedico(), fechaSql);

            String[] horas = {"06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00"};
            for (String h : horas) if (!ocupadas.contains(h)) cmbHora.addItem(h);
            if (cmbHora.getItemCount() == 0) cmbHora.addItem("Sin disponibilidad");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔹 Guardar cita
    private void guardarCita() {
        try {
            String pacienteNom = (String) cmbPaciente.getSelectedItem();
            String medicoNom = (String) cmbMedico.getSelectedItem();
            java.util.Date fechaSeleccionada = dateChooser.getDate();
            String horaStr = (String) cmbHora.getSelectedItem();
            String modalidad = (String) cmbModalidad.getSelectedItem();

            if (pacienteNom == null || medicoNom == null || fechaSeleccionada == null || horaStr == null) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }
            if ("Sin disponibilidad".equals(horaStr)) {
                JOptionPane.showMessageDialog(this, "Seleccione otra fecha o médico.");
                return;
            }

            Medico m = medicoDAO.obtenerPorNombre(medicoNom);
            Paciente p = pacienteDAO.buscarPacientePorNombre(pacienteNom).get(0);

            Date fechaSql = new Date(fechaSeleccionada.getTime());
            Time horaSql = Time.valueOf(horaStr + ":00");
            int idModalidad = modalidad.equalsIgnoreCase("Presencial") ? 1 : 2;

            Cita c = new Cita(0, m.getIdMedico(), p.getIdPaciente(), 1, idModalidad, fechaSql, horaSql);
            boolean ok = citaDAO.insertarCita(c);
            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Cita agendada correctamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al agendar cita.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
