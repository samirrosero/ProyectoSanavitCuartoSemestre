package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.Cita;
import com.proyecto.sanavit.modelo.CitaDao;
import com.proyecto.sanavit.modelo.Medico;
import com.proyecto.sanavit.modelo.MedicoDao;
import com.proyecto.sanavit.modelo.Paciente;
import com.proyecto.sanavit.modelo.PacienteDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * Panel de gestión de citas médicas (CRUD)
 * Conexión directa a CitaDao, MedicoDao y PacienteDao
 */
public class PanelCitas extends JPanel {

    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboPaciente, comboMedico, comboModalidad;
    private JTextField txtFecha, txtHora;
    private JButton btnAgregar, btnEditar, btnEliminar, btnActualizarTabla;
    private JLabel lblTotalCitas;

    private CitaDao citaDAO = new CitaDao();
    private MedicoDao medicoDAO = new MedicoDao();
    private PacienteDao pacienteDAO = new PacienteDao();

    private List<Medico> medicosCache;
    private List<Paciente> pacientesCache;

    public PanelCitas() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // === ENCABEZADO ===
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("🗓️ Gestión de Citas Médicas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panelSuperior.setBackground(new Color(200, 250, 200));
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        // === PANEL CENTRAL ===
        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        panelCentro.setBackground(Color.WHITE);

        // === TABLA CITAS ===
        modeloTabla = new DefaultTableModel(new String[]{
                "ID", "Paciente", "Médico", "Fecha", "Hora", "Modalidad"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCitas = new JTable(modeloTabla);
        tablaCitas.setRowHeight(25);
        tablaCitas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCitas.setSelectionBackground(new Color(180, 240, 180));
        panelCentro.add(new JScrollPane(tablaCitas), BorderLayout.CENTER);

        // === TARJETA LATERAL ===
        JPanel panelResumen = new JPanel();
        panelResumen.setBackground(new Color(245, 255, 245));
        panelResumen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 230, 180), 2),
                BorderFactory.createEmptyBorder(30, 20, 30, 20)
        ));
        panelResumen.setLayout(new BoxLayout(panelResumen, BoxLayout.Y_AXIS));

        JLabel lblTituloResumen = new JLabel("Resumen General");
        lblTituloResumen.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTituloResumen.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTotalCitas = new JLabel("Total citas: 0");
        lblTotalCitas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTotalCitas.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTotalCitas.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        panelResumen.add(lblTituloResumen);
        panelResumen.add(lblTotalCitas);
        panelCentro.add(panelResumen, BorderLayout.EAST);

        add(panelCentro, BorderLayout.CENTER);

        // === PANEL INFERIOR ===
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBackground(Color.WHITE);

        comboPaciente = new JComboBox<>();
        comboMedico = new JComboBox<>();
        txtFecha = new JTextField("YYYY-MM-DD");
        txtHora = new JTextField("HH:MM:SS");
        comboModalidad = new JComboBox<>(new String[]{"Presencial", "Virtual"});

        form.add(new JLabel("Paciente:"));
        form.add(comboPaciente);
        form.add(new JLabel("Médico:"));
        form.add(comboMedico);
        form.add(new JLabel("Fecha (YYYY-MM-DD):"));
        form.add(txtFecha);
        form.add(new JLabel("Hora (HH:MM:SS):"));
        form.add(txtHora);
        form.add(new JLabel("Modalidad:"));
        form.add(comboModalidad);

        panelInferior.add(form, BorderLayout.CENTER);

        // === BOTONES ===
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnAgregar = crearBoton("Agregar");
        btnEditar = crearBoton("Editar");
        btnEliminar = crearBoton("Eliminar");
        btnActualizarTabla = crearBoton("Actualizar Tabla");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizarTabla);

        panelInferior.add(panelBotones, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);

        // === CARGAR DATOS ===
        cargarPacientes();
        cargarMedicos();
        cargarCitas();

        // === ACCIONES ===
        btnAgregar.addActionListener(e -> agregarCita());
        btnEditar.addActionListener(e -> editarCita());
        btnEliminar.addActionListener(e -> eliminarCita());
        btnActualizarTabla.addActionListener(e -> cargarCitas());

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

    // === BOTÓN ESTILIZADO ===
    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(123, 229, 144));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(100, 210, 120)); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(new Color(123, 229, 144)); }
        });
        return btn;
    }

    // === CARGAR LISTAS ===
    private void cargarPacientes() {
        comboPaciente.removeAllItems();
        pacientesCache = pacienteDAO.listarPacientes();
        for (Paciente p : pacientesCache) comboPaciente.addItem(p.getNombre());
    }

    private void cargarMedicos() {
        comboMedico.removeAllItems();
        medicosCache = medicoDAO.listarMedicos();
        for (Medico m : medicosCache) comboMedico.addItem(m.getNombre());
    }

    private int getIdPacienteSeleccionado() {
        int i = comboPaciente.getSelectedIndex();
        if (i < 0 || i >= pacientesCache.size()) return -1;
        return pacientesCache.get(i).getIdPaciente();
    }

    private int getIdMedicoSeleccionado() {
        int i = comboMedico.getSelectedIndex();
        if (i < 0 || i >= medicosCache.size()) return -1;
        return medicosCache.get(i).getIdMedico();
    }

    private String getNombrePacientePorId(int id) {
        for (Paciente p : pacientesCache) if (p.getIdPaciente() == id) return p.getNombre();
        return "—";
    }

    private String getNombreMedicoPorId(int id) {
        for (Medico m : medicosCache) if (m.getIdMedico() == id) return m.getNombre();
        return "—";
    }

    // === CARGAR CITAS ===
    private void cargarCitas() {
        modeloTabla.setRowCount(0);
        List<Cita> lista = citaDAO.listarCitas();
        for (Cita c : lista) {
            modeloTabla.addRow(new Object[]{
                    c.getIdCita(),
                    getNombrePacientePorId(c.getIdPaciente()),
                    getNombreMedicoPorId(c.getIdMedico()),
                    c.getFechaCita(),
                    c.getHoraCita(),
                    c.getIdModalidad() == 1 ? "Presencial" : "Virtual"
            });
        }
        lblTotalCitas.setText("Total citas: " + lista.size());
    }

    // === AGREGAR ===
    private void agregarCita() {
        try {
            int idPaciente = getIdPacienteSeleccionado();
            int idMedico = getIdMedicoSeleccionado();
            Date fecha = Date.valueOf(txtFecha.getText().trim());
            Time hora = Time.valueOf(txtHora.getText().trim());
            int modalidad = comboModalidad.getSelectedIndex() == 0 ? 1 : 2;

            Cita nueva = new Cita(0, idPaciente, idMedico, fecha_hora_ingreso, fecha_hora_salida, modalidad);
            boolean ok = citaDAO.insertarCita(nueva);

            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Cita agregada correctamente.");
                cargarCitas();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al agregar cita.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠️ Verifique los datos de fecha y hora (formato correcto).");
        }
    }

    // === EDITAR ===
    private void editarCita() {
        int fila = tablaCitas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita para editar.");
            return;
        }

        try {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            int idPaciente = getIdPacienteSeleccionado();
            int idMedico = getIdMedicoSeleccionado();
            Date fecha = Date.valueOf(txtFecha.getText().trim());
            Time hora = Time.valueOf(txtHora.getText().trim());
            int modalidad = comboModalidad.getSelectedIndex() == 0 ? 1 : 2;

            Cita actualizada = new Cita(id, idPaciente, idMedico, fecha, hora, modalidad);
            boolean ok = citaDAO.actualizarCita(actualizada);

            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Cita actualizada correctamente.");
                cargarCitas();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al actualizar cita.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠️ Verifique los datos de fecha y hora (formato correcto).");
        }
    }

    // === ELIMINAR ===
    private void eliminarCita() {
        int fila = tablaCitas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita para eliminar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar esta cita?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = citaDAO.eliminarCita(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Cita eliminada correctamente.");
                cargarCitas();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al eliminar cita.");
            }
        }
    }

    private void limpiarCampos() {
        if (comboPaciente.getItemCount() > 0) comboPaciente.setSelectedIndex(0);
        if (comboMedico.getItemCount() > 0) comboMedico.setSelectedIndex(0);
        comboModalidad.setSelectedIndex(0);
        txtFecha.setText("YYYY-MM-DD");
        txtHora.setText("HH:MM:SS");
    }
}
