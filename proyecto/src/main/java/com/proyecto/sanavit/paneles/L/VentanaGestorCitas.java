package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class VentanaGestorCitas extends JFrame {

    private Usuario usuarioActual;
    private JTabbedPane tabs;
    private PacienteDao pacienteDAO = new PacienteDao();
    private CitaDao citaDAO = new CitaDao();
    private MedicoDao medicoDAO = new MedicoDao();

    // === PACIENTES ===
    private JTable tablaPacientes;
    private DefaultTableModel modeloPacientes;
    private JTextField txtBuscarPaciente;
    private JButton btnNuevoPaciente, btnActualizarPacientes;

    // === CITAS ===
    private JTable tablaCitas;
    private DefaultTableModel modeloCitas;
    private JComboBox<String> comboPaciente, comboMedico, comboModalidad;
    private JTextField txtFecha, txtHora;
    private JButton btnAgendar, btnEliminar, btnActualizarCitas;

    public VentanaGestorCitas(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;

        setTitle("Gestor de Citas - Sanavit");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === ENCABEZADO SUPERIOR ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(33, 150, 243));
        header.setPreferredSize(new Dimension(0, 60));

        JLabel lblTitulo = new JLabel("Bienvenido(a), " + usuarioActual.getNombreUsuario() + " - Gestor de Citas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        header.add(lblTitulo, BorderLayout.CENTER);

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(244, 67, 54));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.add(btnCerrarSesion, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new Login();
        });

        // === TABS PRINCIPALES ===
        tabs = new JTabbedPane();
        tabs.addTab("👤 Pacientes", crearPanelPacientes());
        tabs.addTab("📅 Citas", crearPanelCitas());
        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }

    // ==========================================================
    // PANEL PACIENTES
    // ==========================================================
    private JPanel crearPanelPacientes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Barra superior ===
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barra.setBackground(Color.WHITE);
        txtBuscarPaciente = new JTextField(25);
        JButton btnBuscar = crearBoton("Buscar");
        btnNuevoPaciente = crearBoton("Registrar Paciente");
        btnActualizarPacientes = crearBoton("Actualizar");

        barra.add(new JLabel("Buscar por nombre:"));
        barra.add(txtBuscarPaciente);
        barra.add(btnBuscar);
        barra.add(btnNuevoPaciente);
        barra.add(btnActualizarPacientes);
        panel.add(barra, BorderLayout.NORTH);

        // === Tabla ===
        modeloPacientes = new DefaultTableModel(new String[]{
                "ID", "Nombre", "Identificación", "Correo", "Teléfono", "Edad", "Sexo"
        }, 0);
        tablaPacientes = new JTable(modeloPacientes);
        tablaPacientes.setRowHeight(25);
        panel.add(new JScrollPane(tablaPacientes), BorderLayout.CENTER);

        // === Acciones ===
        btnBuscar.addActionListener(e -> buscarPacientes());
        btnActualizarPacientes.addActionListener(e -> cargarPacientes());
        btnNuevoPaciente.addActionListener(e -> registrarPaciente());

        cargarPacientes();
        return panel;
    }

    private void buscarPacientes() {
        String nombre = txtBuscarPaciente.getText().trim();
        modeloPacientes.setRowCount(0);
        if (nombre.isEmpty()) {
            cargarPacientes();
            return;
        }
        List<Paciente> lista = pacienteDAO.buscarPacientePorNombre(nombre);
        for (Paciente p : lista) {
            modeloPacientes.addRow(new Object[]{
                    p.getIdPaciente(), p.getNombre(), p.getIdentificacion(),
                    p.getCorreo(), p.getTelefono(), p.getEdad(), p.getSexo()
            });
        }
    }

    private void cargarPacientes() {
        modeloPacientes.setRowCount(0);
        List<Paciente> lista = pacienteDAO.obtenerTodosLosPacientes();
        for (Paciente p : lista) {
            modeloPacientes.addRow(new Object[]{
                    p.getIdPaciente(), p.getNombre(), p.getIdentificacion(),
                    p.getCorreo(), p.getTelefono(), p.getEdad(), p.getSexo()
            });
        }
    }

    private void registrarPaciente() {
        JTextField nombre = new JTextField();
        JTextField correo = new JTextField();
        JTextField telefono = new JTextField();
        JTextField edad = new JTextField();
        JTextField sexo = new JTextField();
        JTextField direccion = new JTextField();
        JTextField identificacion = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Nombre:")); panel.add(nombre);
        panel.add(new JLabel("Correo:")); panel.add(correo);
        panel.add(new JLabel("Teléfono:")); panel.add(telefono);
        panel.add(new JLabel("Edad:")); panel.add(edad);
        panel.add(new JLabel("Sexo:")); panel.add(sexo);
        panel.add(new JLabel("Dirección:")); panel.add(direccion);
        panel.add(new JLabel("Identificación:")); panel.add(identificacion);

        int res = JOptionPane.showConfirmDialog(this, panel, "Registrar nuevo paciente",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res == JOptionPane.OK_OPTION) {
            try {
                Paciente p = new Paciente(
                        0,
                        nombre.getText(),
                        correo.getText(),
                        Integer.parseInt(edad.getText()),
                        telefono.getText(),
                        sexo.getText(),
                        direccion.getText(),
                        identificacion.getText(),
                        0
                );
                pacienteDAO.insertarPaciente(p);
                JOptionPane.showMessageDialog(this, "✅ Paciente registrado correctamente.");
                cargarPacientes();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Error al registrar paciente: " + ex.getMessage());
            }
        }
    }

    // ==========================================================
    // PANEL CITAS
    // ==========================================================
    private JPanel crearPanelCitas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Formulario ===
        JPanel form = new JPanel(new GridLayout(2, 4, 10, 10));
        form.setBackground(Color.WHITE);

        comboPaciente = new JComboBox<>();
        comboMedico = new JComboBox<>();
        comboModalidad = new JComboBox<>(new String[]{"Presencial", "Virtual"});
        txtFecha = new JTextField();
        txtHora = new JTextField();

        form.add(new JLabel("Paciente:")); form.add(comboPaciente);
        form.add(new JLabel("Médico:")); form.add(comboMedico);
        form.add(new JLabel("Fecha (AAAA-MM-DD):")); form.add(txtFecha);
        form.add(new JLabel("Hora (HH:MM:SS):")); form.add(txtHora);
        form.add(new JLabel("Modalidad:")); form.add(comboModalidad);
        panel.add(form, BorderLayout.NORTH);

        // === Tabla ===
        modeloCitas = new DefaultTableModel(new String[]{
                "ID", "Paciente", "Médico", "Fecha", "Hora", "Modalidad"
        }, 0);
        tablaCitas = new JTable(modeloCitas);
        tablaCitas.setRowHeight(25);
        panel.add(new JScrollPane(tablaCitas), BorderLayout.CENTER);

        // === Botones ===
        JPanel botones = new JPanel();
        botones.setBackground(Color.WHITE);
        btnAgendar = crearBoton("Agendar Cita");
        btnEliminar = crearBoton("Eliminar");
        btnActualizarCitas = crearBoton("Actualizar");

        botones.add(btnAgendar);
        botones.add(btnEliminar);
        botones.add(btnActualizarCitas);
        panel.add(botones, BorderLayout.SOUTH);

        // === Acciones ===
        btnAgendar.addActionListener(e -> agendarCita());
        btnEliminar.addActionListener(e -> eliminarCita());
        btnActualizarCitas.addActionListener(e -> cargarCitas());

        cargarPacientesCombo();
        cargarMedicosCombo();
        cargarCitas();

        return panel;
    }

    private void cargarPacientesCombo() {
        comboPaciente.removeAllItems();
        for (Paciente p : pacienteDAO.obtenerTodosLosPacientes())
            comboPaciente.addItem(p.getNombre());
    }

    private void cargarMedicosCombo() {
        comboMedico.removeAllItems();
        for (Medico m : MedicoDao.listarMedicos())
            comboMedico.addItem(m.getNombre());
    }

    private void cargarCitas() {
        modeloCitas.setRowCount(0);
        List<Cita> lista = citaDAO.selectCita();
        for (Cita c : lista) {
            Paciente p = pacienteDAO.obtenerPacientePorId(c.getIdPaciente());
            Medico m = medicoDAO.obtenerMedicoPorId(c.getIdMedico());
            modeloCitas.addRow(new Object[]{
                    c.getIdCita(),
                    (p != null ? p.getNombre() : "Desconocido"),
                    (m != null ? m.getNombre() : "Desconocido"),
                    c.getFechaCita(),
                    c.getHoraCita(),
                    (c.getIdModalidad() == 1 ? "Presencial" : "Virtual")
            });
        }
    }

    private void agendarCita() {
        try {
            String pacienteNombre = comboPaciente.getSelectedItem().toString();
            String medicoNombre = comboMedico.getSelectedItem().toString();

            Paciente paciente = pacienteDAO.buscarPacientePorNombre(pacienteNombre).get(0);
            Medico medico = medicoDAO.obtenerPorNombre(medicoNombre);

            Date fecha = Date.valueOf(txtFecha.getText());
            Time hora = Time.valueOf(txtHora.getText());
            int modalidad = comboModalidad.getSelectedIndex() == 0 ? 1 : 2;

            Cita c = new Cita(0, medico.getIdMedico(), paciente.getIdPaciente(), 1, modalidad, fecha, hora);
            citaDAO.insertarCita(c);

            JOptionPane.showMessageDialog(this, "✅ Cita agendada correctamente.");
            cargarCitas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error al agendar cita: " + ex.getMessage());
        }
    }

    private void eliminarCita() {
        int fila = tablaCitas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita para eliminar.");
            return;
        }
        int id = (int) modeloCitas.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar cita?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            citaDAO.deleteCita(id);
            JOptionPane.showMessageDialog(this, "✅ Cita eliminada correctamente.");
            cargarCitas();
        }
    }

    // ==========================================================
    // BOTÓN ESTILO SANAVIT
    // ==========================================================
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
