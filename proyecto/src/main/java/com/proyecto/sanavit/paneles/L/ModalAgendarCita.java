package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;

public class ModalAgendarCita extends JDialog {

    private JComboBox<String> cmbPaciente, cmbMedico, cmbFecha, cmbHora, cmbModalidad;
    private JButton btnGuardar, btnCancelar;
    private final CitaDao citaDAO = new CitaDao();
    private final PacienteDao pacienteDAO = new PacienteDao();
    private final MedicoDao medicoDAO = new MedicoDao();

    public ModalAgendarCita(JFrame parent) {
        super(parent, "Agendar Cita (Gestor)", true);
        setSize(520, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(6,2,8,8));
        form.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        cmbPaciente = new JComboBox<>();
        cmbMedico = new JComboBox<>();
        cmbFecha = new JComboBox<>();
        cmbHora = new JComboBox<>();
        cmbModalidad = new JComboBox<>(new String[]{"Presencial","Virtual"});

        cargarPacientes();
        cargarMedicos();
        cargarFechas();

        form.add(new JLabel("Paciente:")); form.add(cmbPaciente);
        form.add(new JLabel("Médico:")); form.add(cmbMedico);
        form.add(new JLabel("Fecha:")); form.add(cmbFecha);
        form.add(new JLabel("Hora disponible:")); form.add(cmbHora);
        form.add(new JLabel("Modalidad:")); form.add(cmbModalidad);

        add(form, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        btnGuardar.setBackground(new Color(46,204,113)); btnGuardar.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(231,76,60)); btnCancelar.setForeground(Color.WHITE);
        footer.add(btnGuardar); footer.add(btnCancelar);
        add(footer, BorderLayout.SOUTH);

        cmbMedico.addActionListener(e -> actualizarHorasDisponibles());
        cmbFecha.addActionListener(e -> actualizarHorasDisponibles());
        btnGuardar.addActionListener(e -> guardarCita());
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void cargarPacientes() {
        cmbPaciente.removeAllItems();
        List<Paciente> list = pacienteDAO.obtenerTodosLosPacientes();
        for (Paciente p : list) cmbPaciente.addItem(p.getNombre());
    }

    private void cargarMedicos() {
        cmbMedico.removeAllItems();
        List<Medico> list = MedicoDao.listarMedicos();
        for (Medico m : list) cmbMedico.addItem(m.getNombre());
    }

    private void cargarFechas() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cmbFecha.removeAllItems();
        for (int i=0; i<14; i++) {
            cmbFecha.addItem(sdf.format(cal.getTime()));
            cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
        }
    }

    private void actualizarHorasDisponibles() {
        cmbHora.removeAllItems();
        try {
            String medicoNombre = (String)cmbMedico.getSelectedItem();
            String fechaStr = (String)cmbFecha.getSelectedItem();
            if (medicoNombre==null || fechaStr==null) return;
            Medico m = medicoDAO.obtenerPorNombre(medicoNombre);
            if (m==null) return;
            java.util.Date parsed = new SimpleDateFormat("dd/MM/yyyy").parse(fechaStr);
            Date fechaSql = new Date(parsed.getTime());
            List<String> ocupadas = citaDAO.obtenerHorasOcupadas(m.getIdMedico(), fechaSql);
            String[] horas = {"06:00","07:00","08:00","09:00","10:00","11:00","13:00","14:00","15:00","16:00"};
            for (String h: horas) if (!ocupadas.contains(h)) cmbHora.addItem(h);
            if (cmbHora.getItemCount()==0) cmbHora.addItem("Sin disponibilidad");
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void guardarCita() {
        try {
            String pacienteNom = (String)cmbPaciente.getSelectedItem();
            String medicoNom = (String)cmbMedico.getSelectedItem();
            String fechaStr = (String)cmbFecha.getSelectedItem();
            String horaStr = (String)cmbHora.getSelectedItem();
            String modalidad = (String)cmbModalidad.getSelectedItem();

            if (pacienteNom==null || medicoNom==null || fechaStr==null || horaStr==null) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }
            if ("Sin disponibilidad".equals(horaStr)) {
                JOptionPane.showMessageDialog(this, "Seleccione otra fecha o médico.");
                return;
            }

            Medico m = medicoDAO.obtenerPorNombre(medicoNom);
            Paciente p = pacienteDAO.buscarPacientePorNombre(pacienteNom).get(0);

            java.util.Date parsed = new SimpleDateFormat("dd/MM/yyyy").parse(fechaStr);
            Date fechaSql = new Date(parsed.getTime());
            Time horaSql = Time.valueOf(horaStr + ":00");
            int idModalidad = modalidad.equalsIgnoreCase("Presencial") ? 1 : 2;

            Cita c = new Cita(0, m.getIdMedico(), p.getIdPaciente(), 1, idModalidad, fechaSql, horaSql);
            boolean ok = citaDAO.insertarCita(c);
            if (ok) { JOptionPane.showMessageDialog(this, "Cita agendada correctamente."); dispose(); }
            else JOptionPane.showMessageDialog(this, "Error al agendar cita.");
        } catch (Exception e) { e.printStackTrace(); JOptionPane.showMessageDialog(this, "Error: "+e.getMessage()); }
    }
}
