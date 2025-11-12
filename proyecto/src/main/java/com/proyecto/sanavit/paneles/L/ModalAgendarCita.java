package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModalAgendarCita extends JDialog {

    private JComboBox<String> cmbPaciente, cmbEspecialidad, cmbMedico, cmbHora, cmbModalidad;
    private JDateChooser dateChooser;
    private JButton btnGuardar, btnCancelar;

    private final CitaDao citaDAO = new CitaDao();
    private final PacienteDao pacienteDAO = new PacienteDao();
    private final MedicoDao medicoDAO = new MedicoDao();

    private List<Medico> listaMedicos;

    public ModalAgendarCita(JFrame parent) {
        super(parent, "Agendar Cita (Gestor)", true);
        setSize(520, 480);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setUndecorated(false);

        // 🎨 COLORES BASE
        Color fondoPrincipal = new Color(209, 232, 218);
        Color negroSanavit = new Color(12, 13, 13);
        Color verdeSanavit = new Color(46, 204, 113);
        Color grisTexto = new Color(12, 13, 13);

        // 🔹 PANEL PRINCIPAL
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(fondoPrincipal);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        add(contentPanel);

        // 🔹 TÍTULO
        JLabel lblTitulo = new JLabel("Agendar nueva cita", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20));
        lblTitulo.setForeground(negroSanavit);
        contentPanel.add(lblTitulo, BorderLayout.NORTH);

        // 🔹 FORMULARIO
        JPanel form = new JPanel(new GridLayout(7, 2, 10, 14));
        form.setBackground(fondoPrincipal);

        JLabel[] labels = {
            new JLabel("Paciente:"), new JLabel("Especialidad:"), new JLabel("Médico:"),
            new JLabel("Fecha:"), new JLabel("Hora disponible:"), new JLabel("Modalidad:")
        };

        for (JLabel label : labels) {
            label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            label.setForeground(grisTexto);
        }

        cmbPaciente = new JComboBox<>();
        cmbEspecialidad = new JComboBox<>();
        cmbMedico = new JComboBox<>();
        cmbHora = new JComboBox<>();
        cmbModalidad = new JComboBox<>(new String[]{"Presencial", "Virtual"});
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setMinSelectableDate(new java.util.Date());

        // Estilo combos
        JComboBox<?>[] combos = {cmbPaciente, cmbEspecialidad, cmbMedico, cmbHora, cmbModalidad};
        for (JComboBox<?> combo : combos) {
            combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            combo.setBackground(Color.WHITE);
            combo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        }

        // Cargar datos
        cargarPacientes();
        cargarEspecialidades();

        // Añadir al formulario
        form.add(labels[0]); form.add(cmbPaciente);
        form.add(labels[1]); form.add(cmbEspecialidad);
        form.add(labels[2]); form.add(cmbMedico);
        form.add(labels[3]); form.add(dateChooser);
        form.add(labels[4]); form.add(cmbHora);
        form.add(labels[5]); form.add(cmbModalidad);

        contentPanel.add(form, BorderLayout.CENTER);

        // 🔹 BOTONES
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        footer.setBackground(fondoPrincipal);

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        estilizarBoton(btnGuardar, verdeSanavit, Color.WHITE);
        estilizarBoton(btnCancelar, verdeSanavit, Color.WHITE);

        footer.add(btnGuardar);
        footer.add(btnCancelar);

        contentPanel.add(footer, BorderLayout.SOUTH);

        // 🔹 EVENTOS
        cmbEspecialidad.addActionListener(e -> actualizarMedicosPorEspecialidad());
        cmbMedico.addActionListener(e -> actualizarHorasDisponibles());
        dateChooser.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) actualizarHorasDisponibles();
        });
        btnGuardar.addActionListener(e -> guardarCita());
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    // 🎨 Método para dar estilo y hover a los botones
    private void estilizarBoton(JButton boton, Color fondo, Color texto) {
        boton.setBackground(fondo);
        boton.setForeground(texto);
        boton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        boton.setOpaque(true);
        boton.setPreferredSize(new Dimension(120, 38));

        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(fondo.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(fondo);
            }
        });
    }

    // 🔹 Cargar pacientes
    private void cargarPacientes() {
        cmbPaciente.removeAllItems();
        List<Paciente> list = pacienteDAO.obtenerTodosLosPacientes();
        for (Paciente p : list) cmbPaciente.addItem(p.getNombre());
    }

    // 🔹 Cargar especialidades
    private void cargarEspecialidades() {
        listaMedicos = MedicoDao.listarMedicos();
        Set<String> especialidades = listaMedicos.stream()
                .map(Medico::getEspecialidad)
                .collect(Collectors.toSet());
        cmbEspecialidad.removeAllItems();
        for (String esp : especialidades) cmbEspecialidad.addItem(esp);
    }

    // 🔹 Filtrar médicos
    private void actualizarMedicosPorEspecialidad() {
        cmbMedico.removeAllItems();
        String especialidad = (String) cmbEspecialidad.getSelectedItem();
        if (especialidad == null) return;

        List<Medico> filtrados = listaMedicos.stream()
                .filter(m -> m.getEspecialidad().equalsIgnoreCase(especialidad))
                .toList();

        for (Medico m : filtrados) cmbMedico.addItem(m.getNombre());
        cmbHora.removeAllItems();
    }

    // 🔹 Horas disponibles
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
                JOptionPane.showMessageDialog(this, "Cita agendada correctamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al agendar cita.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
