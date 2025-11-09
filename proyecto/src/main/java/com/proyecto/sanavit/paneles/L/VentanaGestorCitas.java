package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class VentanaGestorCitas extends JFrame {

    private JTable tablaPacientes, tablaCitas;
    private DefaultTableModel modeloPacientes, modeloCitas;
    private JButton btnNuevoPaciente, btnEditarPaciente, btnEliminarPaciente, btnBuscarPaciente;
    private JButton btnAgendarCita, btnEditarCita, btnEliminarCita, btnActualizarCitas, btnActualizarPacientes;
    private JTextField txtBuscarPaciente;
    private Usuario usuarioGestor;

    private PacienteDao pacienteDAO = new PacienteDao();
    private CitaDao citaDAO = new CitaDao();

    public VentanaGestorCitas(Usuario usuarioGestor) {
        this.usuarioGestor = usuarioGestor;
        setTitle("Gestor de Citas - Sanavit");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === ENCABEZADO ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(78, 207, 78));
        JLabel lblTitulo = new JLabel("Bienvenido, " + usuarioGestor.getNombreUsuario() + " (Gestor de Citas)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(231, 76, 60));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new Login();
        });

        header.add(lblTitulo, BorderLayout.CENTER);
        header.add(btnCerrarSesion, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // === PESTAÑAS ===
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Pacientes", crearPanelPacientes());
        tabs.addTab("Citas", crearPanelCitas());
        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }

    // PANEL DE PACIENTES
    private JPanel crearPanelPacientes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        modeloPacientes = new DefaultTableModel(new String[]{
                "ID", "Nombre", "Correo", "Edad", "Teléfono", "Sexo", "Dirección", "Identificación"
        }, 0);

        tablaPacientes = new JTable(modeloPacientes);
        tablaPacientes.setRowHeight(25);
        panel.add(new JScrollPane(tablaPacientes), BorderLayout.CENTER);

        // === BOTONES ===
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnNuevoPaciente = crearBoton("Nuevo Paciente", new Color(46, 204, 113));
        btnEditarPaciente = crearBoton("Editar", new Color(52, 152, 219));
        btnEliminarPaciente = crearBoton("Eliminar", new Color(231, 76, 60));
        btnBuscarPaciente = crearBoton("Buscar", new Color(241, 196, 15));
        btnActualizarPacientes = crearBoton("Actualizar Lista", new Color(155, 89, 182));

        txtBuscarPaciente = new JTextField(15);

        botones.add(btnNuevoPaciente);
        botones.add(btnEditarPaciente);
        botones.add(btnEliminarPaciente);
        botones.add(new JLabel("Buscar por nombre:"));
        botones.add(txtBuscarPaciente);
        botones.add(btnBuscarPaciente);
        botones.add(btnActualizarPacientes);

        panel.add(botones, BorderLayout.SOUTH);

        cargarPacientes();

        // ACCIONES
        btnNuevoPaciente.addActionListener(e -> new ModalRegistrarPaciente(this));
        btnActualizarPacientes.addActionListener(e -> cargarPacientes());
        btnBuscarPaciente.addActionListener(e -> buscarPaciente());
        btnEliminarPaciente.addActionListener(e -> eliminarPaciente());

        return panel;
    }

    private void cargarPacientes() {
        modeloPacientes.setRowCount(0);
        List<Paciente> lista = pacienteDAO.obtenerTodosLosPacientes();
        for (Paciente p : lista) {
            modeloPacientes.addRow(new Object[]{
                    p.getIdPaciente(), p.getNombre(), p.getCorreo(),
                    p.getEdad(), p.getTelefono(), p.getSexo(),
                    p.getDireccion(), p.getIdentificacion()
            });
        }
    }

    private void buscarPaciente() {
        String nombre = txtBuscarPaciente.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre para buscar.");
            return;
        }
        modeloPacientes.setRowCount(0);
        List<Paciente> lista = pacienteDAO.buscarPacientePorNombre(nombre);
        for (Paciente p : lista) {
            modeloPacientes.addRow(new Object[]{
                    p.getIdPaciente(), p.getNombre(), p.getCorreo(),
                    p.getEdad(), p.getTelefono(), p.getSexo(),
                    p.getDireccion(), p.getIdentificacion()
            });
        }
    }

    private void eliminarPaciente() {
        int fila = tablaPacientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente.");
            return;
        }
        int id = (int) modeloPacientes.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este paciente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = pacienteDAO.eliminarPaciente(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Paciente eliminado correctamente.");
                cargarPacientes();
            }
        }
    }

    // PANEL DE CITAS
    private JPanel crearPanelCitas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        modeloCitas = new DefaultTableModel(new String[]{
                "ID", "Paciente", "Médico", "Fecha", "Hora", "Modalidad"
        }, 0);

        tablaCitas = new JTable(modeloCitas);
        tablaCitas.setRowHeight(25);
        panel.add(new JScrollPane(tablaCitas), BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAgendarCita = crearBoton("Agendar Cita", new Color(46, 204, 113));
        btnEditarCita = crearBoton("Editar", new Color(52, 152, 219));
        btnEliminarCita = crearBoton("Eliminar", new Color(231, 76, 60));
        btnActualizarCitas = crearBoton("Actualizar Lista", new Color(155, 89, 182));

        botones.add(btnAgendarCita);
        botones.add(btnEditarCita);
        botones.add(btnEliminarCita);
        botones.add(btnActualizarCitas);

        panel.add(botones, BorderLayout.SOUTH);

        cargarCitas();

        btnActualizarCitas.addActionListener(e -> cargarCitas());
        btnAgendarCita.addActionListener(e -> new ModalAgendarCita(this));

        return panel;
    }

    private void cargarCitas() {
        modeloCitas.setRowCount(0);
        List<Cita> citas = citaDAO.selectCita();
        MedicoDao medicoDAO = new MedicoDao();
        PacienteDao pacienteDAO = new PacienteDao();

        for (Cita c : citas) {
            Paciente p = pacienteDAO.obtenerPacientePorId(c.getIdPaciente());
            Medico m = medicoDAO.obtenerMedicoPorId(c.getIdMedico());
            modeloCitas.addRow(new Object[]{
                    c.getIdCita(),
                    p != null ? p.getNombre() : "Desconocido",
                    m != null ? m.getNombre() : "Desconocido",
                    c.getFechaCita(),
                    c.getHoraCita(),
                    (c.getIdModalidad() == 1 ? "Presencial" : "Virtual")
            });
        }
    }

    // BOTÓN ESTÉTICO
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return btn;
    }

    public static void main(String[] args) {
        Usuario dummy = new Usuario(1, 3, "gestor_demo", "1234", "gestor de citas");
        new VentanaGestorCitas(dummy);
    }
}
