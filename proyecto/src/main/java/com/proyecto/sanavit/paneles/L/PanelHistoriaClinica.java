package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PanelHistoriaClinica extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JComboBox<String> comboPaciente, comboMedico;
    private JTextField txtMotivo, txtEnfermedad, txtAntecedentes, txtDiagnostico, txtTratamiento, txtEvolucion;
    private JTextArea txtObservaciones;
    private JButton btnAgregar, btnEditar, btnEliminar, btnActualizar;
    private JLabel lblTotal;

    private HistoriaClinicaDao historiaDAO = new HistoriaClinicaDao();
    private PacienteDao pacienteDAO = new PacienteDao();
    private MedicoDao medicoDAO = new MedicoDao();

    public PanelHistoriaClinica() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // === ENCABEZADO ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(123, 229, 144));
        JLabel lblTitulo = new JLabel("Gestión de Historias Clínicas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        header.add(lblTitulo, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // === TABLA ===
        modelo = new DefaultTableModel(new String[]{
                "ID", "Paciente", "Médico", "Motivo", "Diagnóstico", "Tratamiento"
        }, 0);
        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // === PANEL INFERIOR ===
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelInferior.setBackground(Color.WHITE);

        JPanel form = new JPanel(new GridLayout(4, 4, 10, 10));
        form.setBackground(Color.WHITE);

        comboPaciente = new JComboBox<>();
        comboMedico = new JComboBox<>();
        txtMotivo = new JTextField();
        txtEnfermedad = new JTextField();
        txtAntecedentes = new JTextField();
        txtDiagnostico = new JTextField();
        txtTratamiento = new JTextField();
        txtEvolucion = new JTextField();
        txtObservaciones = new JTextArea(3, 20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        form.add(new JLabel("Paciente:"));
        form.add(comboPaciente);
        form.add(new JLabel("Médico:"));
        form.add(comboMedico);

        form.add(new JLabel("Motivo Consulta:"));
        form.add(txtMotivo);
        form.add(new JLabel("Enfermedad Actual:"));
        form.add(txtEnfermedad);

        form.add(new JLabel("Antecedentes:"));
        form.add(txtAntecedentes);
        form.add(new JLabel("Diagnóstico:"));
        form.add(txtDiagnostico);

        form.add(new JLabel("Tratamiento:"));
        form.add(txtTratamiento);
        form.add(new JLabel("Evolución:"));
        form.add(txtEvolucion);

        panelInferior.add(form, BorderLayout.CENTER);

        JPanel obsPanel = new JPanel(new BorderLayout());
        obsPanel.setBorder(BorderFactory.createTitledBorder("Observaciones"));
        obsPanel.add(new JScrollPane(txtObservaciones), BorderLayout.CENTER);
        panelInferior.add(obsPanel, BorderLayout.SOUTH);

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

        add(panelBotones, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);

        // === CONTADOR ===
        lblTotal = new JLabel("Total de historias: 0");
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblTotal.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        add(lblTotal, BorderLayout.PAGE_END);

        // === CARGAR DATOS ===
        cargarPacientes();
        cargarMedicos();
        cargarHistorias();

        // === ACCIONES ===
        btnAgregar.addActionListener(e -> agregarHistoria());
        btnEditar.addActionListener(e -> editarHistoria());
        btnEliminar.addActionListener(e -> eliminarHistoria());
        btnActualizar.addActionListener(e -> cargarHistorias());

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                    comboPaciente.setSelectedItem(modelo.getValueAt(fila, 1).toString());
                    comboMedico.setSelectedItem(modelo.getValueAt(fila, 2).toString());
                    txtMotivo.setText(modelo.getValueAt(fila, 3).toString());
                    txtDiagnostico.setText(modelo.getValueAt(fila, 4).toString());
                    txtTratamiento.setText(modelo.getValueAt(fila, 5).toString());
                }
            }
        });
    }

    // === Cargar Pacientes y Médicos ===
    private void cargarPacientes() {
        comboPaciente.removeAllItems();
        List<Paciente> lista = pacienteDAO.obtenerTodosLosPacientes();
        for (Paciente p : lista) comboPaciente.addItem(p.getNombre());
    }

    private void cargarMedicos() {
        comboMedico.removeAllItems();
        List<Medico> lista = MedicoDao.listarMedicos();
        for (Medico m : lista) comboMedico.addItem(m.getNombre());
    }

    // === Cargar Historias ===
    private void cargarHistorias() {
        modelo.setRowCount(0);
        List<HistoriaClinica> lista = historiaDAO.listarHistorias();
        for (HistoriaClinica h : lista) {
            modelo.addRow(new Object[]{
                    h.getIdHistoriaClinica(),
                    "Paciente #" + h.getIdEjecucionCita(), // (No tenemos idPaciente directo)
                    "Médico asignado",
                    h.getMotivoConsulta(),
                    h.getDiagnostico(),
                    h.getTratamiento()
            });
        }
        lblTotal.setText("Total de historias: " + modelo.getRowCount());
    }

    // === Agregar ===
    private void agregarHistoria() {
        try {
            String pacienteNombre = comboPaciente.getSelectedItem().toString();
            String medicoNombre = comboMedico.getSelectedItem().toString();

            List<Paciente> pacientes = pacienteDAO.buscarPacientePorNombre(pacienteNombre);
            Paciente paciente = (pacientes.isEmpty()) ? null : pacientes.get(0);
            Medico medico = medicoDAO.obtenerPorNombre(medicoNombre);

            if (paciente == null || medico == null) {
                JOptionPane.showMessageDialog(this, "⚠️ Debe seleccionar paciente y médico válidos.");
                return;
            }

            HistoriaClinica h = new HistoriaClinica(
                    0,
                    1, // idEjecucionCita temporal o controlado por procedimiento
                    txtMotivo.getText(),
                    txtEnfermedad.getText(),
                    txtAntecedentes.getText(),
                    txtDiagnostico.getText(),
                    txtTratamiento.getText(),
                    txtEvolucion.getText(),
                    txtObservaciones.getText()
            );

            boolean ok = historiaDAO.insertarHistoria(h);
            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Historia clínica agregada correctamente.");
                cargarHistorias();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al agregar historia clínica.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠️ Error: " + e.getMessage());
        }
    }

    // === Editar ===
    private void editarHistoria() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una historia para editar.");
            return;
        }

        try {
            int id = (int) modelo.getValueAt(fila, 0);
            HistoriaClinica h = new HistoriaClinica(
                    id,
                    1,
                    txtMotivo.getText(),
                    txtEnfermedad.getText(),
                    txtAntecedentes.getText(),
                    txtDiagnostico.getText(),
                    txtTratamiento.getText(),
                    txtEvolucion.getText(),
                    txtObservaciones.getText()
            );

            boolean ok = historiaDAO.updateHistoria(h);
            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Historia clínica actualizada correctamente.");
                cargarHistorias();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al actualizar historia clínica.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠️ Error: " + e.getMessage());
        }
    }

    // === Eliminar ===
    private void eliminarHistoria() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una historia para eliminar.");
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar esta historia?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = historiaDAO.deleteHistoria(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Historia clínica eliminada correctamente.");
                cargarHistorias();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al eliminar historia clínica.");
            }
        }
    }

    // === Botón Moderno ===
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
