package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class PanelCitas extends JPanel {

    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboPaciente, comboMedico, comboModalidad;
    private JTextField txtFecha, txtHora;
    private JButton btnAgregar, btnEditar, btnEliminar, btnActualizar;
    private JLabel lblTotal;

    private CitaDao citaDAO = new CitaDao();
    private PacienteDao pacienteDAO = new PacienteDao();
    private MedicoDao medicoDAO = new MedicoDao();

    public PanelCitas() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // === ENCABEZADO ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(123, 229, 144));
        JLabel lblTitulo = new JLabel("Gestión de Citas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        header.add(lblTitulo, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // === TABLA ===
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Paciente", "Médico", "Fecha", "Hora", "Modalidad"}, 0);
        tablaCitas = new JTable(modeloTabla);
        tablaCitas.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(tablaCitas);
        add(scroll, BorderLayout.CENTER);

        // === PANEL INFERIOR ===
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelInferior.setBackground(Color.WHITE);

        JPanel form = new JPanel(new GridLayout(3, 4, 10, 10));
        form.setBackground(Color.WHITE);

        comboPaciente = new JComboBox<>();
        comboMedico = new JComboBox<>();
        comboModalidad = new JComboBox<>(new String[]{"Presencial", "Virtual"});
        txtFecha = new JTextField();
        txtHora = new JTextField();

        form.add(new JLabel("Paciente:"));
        form.add(comboPaciente);
        form.add(new JLabel("Médico:"));
        form.add(comboMedico);

        form.add(new JLabel("Fecha (AAAA-MM-DD):"));
        form.add(txtFecha);
        form.add(new JLabel("Hora (HH:MM:SS):"));
        form.add(txtHora);

        form.add(new JLabel("Modalidad:"));
        form.add(comboModalidad);

        panelInferior.add(form, BorderLayout.CENTER);

        // === BOTONES ===
        JPanel panelBotones = new JPanel();
        btnAgregar = crearBoton("Agregar");
        btnEditar = crearBoton("Editar");
        btnEliminar = crearBoton("Eliminar");
        btnActualizar = crearBoton("Actualizar Tabla");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);

        panelInferior.add(panelBotones, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);

        // === CONTADOR TOTAL ===
        lblTotal = new JLabel("Total de citas: 0");
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblTotal.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 20));
        add(lblTotal, BorderLayout.SOUTH);

        // === CARGAR DATOS ===
        cargarPacientes();
        cargarMedicos();
        cargarCitas();

        // === ACCIONES ===
        btnAgregar.addActionListener(e -> agregarCita());
        btnEditar.addActionListener(e -> editarCita());
        btnEliminar.addActionListener(e -> eliminarCita());
        btnActualizar.addActionListener(e -> cargarCitas());

        tablaCitas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaCitas.getSelectedRow();
                if (fila != -1) {
                    comboPaciente.setSelectedItem(modeloTabla.getValueAt(fila, 1).toString());
                    comboMedico.setSelectedItem(modeloTabla.getValueAt(fila, 2).toString());
                    txtFecha.setText(modeloTabla.getValueAt(fila, 3).toString());
                    txtHora.setText(modeloTabla.getValueAt(fila, 4).toString());
                    comboModalidad.setSelectedItem(modeloTabla.getValueAt(fila, 5).toString());
                }
            }
        });
    }

    // === Cargar datos ===
    private void cargarCitas() {
        modeloTabla.setRowCount(0);
        List<Cita> citas = citaDAO.selectCita();
        for (Cita c : citas) {
            Paciente p = pacienteDAO.obtenerPacientePorId(c.getIdPaciente());
            Medico m = medicoDAO.obtenerMedicoPorId(c.getIdMedico());
            modeloTabla.addRow(new Object[]{
                    c.getIdCita(),
                    (p != null ? p.getNombre() : "Desconocido"),
                    (m != null ? m.getNombre() : "Desconocido"),
                    c.getFechaCita(),
                    c.getHoraCita(),
                    (c.getIdModalidad() == 1 ? "Presencial" : "Virtual")
            });
        }
        lblTotal.setText("Total de citas: " + modeloTabla.getRowCount());
    }

    private void cargarPacientes() {
        comboPaciente.removeAllItems();
        List<Paciente> lista = pacienteDAO.obtenerTodosLosPacientes();
        for (Paciente p : lista) {
            comboPaciente.addItem(p.getNombre());
        }
    }

    private void cargarMedicos() {
        comboMedico.removeAllItems();
        List<Medico> lista = medicoDAO.listarMedicos();
        for (Medico m : lista) {
            comboMedico.addItem(m.getNombre());
        }
    }

    // === Agregar Cita ===
    private void agregarCita() {
        try {
            String pacienteNombre = comboPaciente.getSelectedItem().toString();
            String medicoNombre = comboMedico.getSelectedItem().toString();

            // Usar tus DAOs reales
            Medico medico = medicoDAO.obtenerPorNombre(medicoNombre);
            List<Paciente> pacientes = pacienteDAO.buscarPacientePorNombre(pacienteNombre);
            Paciente paciente = (pacientes.isEmpty()) ? null : pacientes.get(0);

            if (paciente == null || medico == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el paciente o médico seleccionado.");
                return;
            }

            int idPaciente = paciente.getIdPaciente();
            int idMedico = medico.getIdMedico();

            Date fecha = Date.valueOf(txtFecha.getText().trim());
            Time hora = Time.valueOf(txtHora.getText().trim());
            int modalidad = comboModalidad.getSelectedIndex() == 0 ? 1 : 2;

            Cita nueva = new Cita(
                    0,
                    idMedico,
                    idPaciente,
                    1, // estado por defecto (ej. programada)
                    modalidad,
                    fecha,
                    hora
            );

            boolean ok = citaDAO.insertarCita(nueva);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Cita registrada correctamente.");
                cargarCitas();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar cita.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, " Verifique los datos ingresados.");
            e.printStackTrace();
        }
    }

    // === Editar Cita ===
    private void editarCita() {
        int fila = tablaCitas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita para editar.");
            return;
        }

        try {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            String pacienteNombre = comboPaciente.getSelectedItem().toString();
            String medicoNombre = comboMedico.getSelectedItem().toString();

            Medico medico = medicoDAO.obtenerPorNombre(medicoNombre);
            List<Paciente> pacientes = pacienteDAO.buscarPacientePorNombre(pacienteNombre);
            Paciente paciente = (pacientes.isEmpty()) ? null : pacientes.get(0);

            if (paciente == null || medico == null) {
                JOptionPane.showMessageDialog(this, "⚠️ No se encontró el paciente o médico seleccionado.");
                return;
            }

            int idPaciente = paciente.getIdPaciente();
            int idMedico = medico.getIdMedico();

            Date fecha = Date.valueOf(txtFecha.getText().trim());
            Time hora = Time.valueOf(txtHora.getText().trim());
            int modalidad = comboModalidad.getSelectedIndex() == 0 ? 1 : 2;

            Cita actualizada = new Cita(
                    id,
                    idMedico,
                    idPaciente,
                    1,
                    modalidad,
                    fecha,
                    hora
            );

            boolean ok = citaDAO.updateCita(actualizada);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Cita actualizada correctamente.");
                cargarCitas();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar cita.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en los datos ingresados.");
            e.printStackTrace();
        }
    }

    // === Eliminar Cita ===
    private void eliminarCita() {
        int fila = tablaCitas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita para eliminar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar esta cita?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = citaDAO.deleteCita(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Cita eliminada correctamente.");
                cargarCitas();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar cita.");
            }
        }
    }

    // === Botón moderno ===
    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(33, 150, 243));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return btn;
    }
}
