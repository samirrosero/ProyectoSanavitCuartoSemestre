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

        // ===== ENCABEZADO =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(76, 175, 80)); // Verde Sanavit
        JLabel lblTitulo = new JLabel("👩‍💼 Bienvenido, " + usuarioGestor.getNombreUsuario() + " (Gestor de Citas)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(66, 166, 105));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover efecto botón cerrar sesión
        btnCerrarSesion.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnCerrarSesion.setBackground(new Color(192, 57, 43));
            }

            public void mouseExited(MouseEvent e) {
                btnCerrarSesion.setBackground(new Color(38, 112, 67));
            }
        });

        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new Login();
        });

        header.add(lblTitulo, BorderLayout.CENTER);
        header.add(btnCerrarSesion, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ===== PESTAÑAS =====
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(Color.WHITE);
        tabs.setForeground(new Color(60, 120, 60));

        tabs.addTab("👨‍⚕️ Pacientes", crearPanelPacientes());
        tabs.addTab("📅 Citas", crearPanelCitas());
        add(tabs, BorderLayout.CENTER);

        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }

    // ===== PANEL DE PACIENTES =====
    private JPanel crearPanelPacientes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        modeloPacientes = new DefaultTableModel(new String[]{
                "ID", "Nombre", "Correo", "Edad", "Teléfono", "Sexo", "Dirección", "Identificación"
        }, 0);

        tablaPacientes = new JTable(modeloPacientes);
        tablaPacientes.setRowHeight(28);
        tablaPacientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaPacientes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(new JScrollPane(tablaPacientes), BorderLayout.CENTER);

        // ===== BOTONES =====
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        botones.setBackground(new Color(245, 245, 245));

        btnNuevoPaciente = crearBoton("➕ Nuevo Paciente", new Color(46, 204, 113));
        btnEditarPaciente = crearBoton("✏️ Editar", new Color(66, 166, 105));
        btnEliminarPaciente = crearBoton("🗑️ Eliminar", new Color(66, 166, 105));
        btnBuscarPaciente = crearBoton("🔍 Buscar", new Color(66, 166, 105));
        btnActualizarPacientes = crearBoton("🔄 Actualizar Lista", new Color(66, 166, 105));

        txtBuscarPaciente = new JTextField(18);
        txtBuscarPaciente.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBuscarPaciente.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        botones.add(btnNuevoPaciente);
        botones.add(btnEditarPaciente);
        botones.add(btnEliminarPaciente);
        botones.add(new JLabel("Buscar por nombre:"));
        botones.add(txtBuscarPaciente);
        botones.add(btnBuscarPaciente);
        botones.add(btnActualizarPacientes);

        panel.add(botones, BorderLayout.SOUTH);

        cargarPacientes();

        // ===== ACCIONES =====
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

    // ===== PANEL DE CITAS =====
    private JPanel crearPanelCitas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        modeloCitas = new DefaultTableModel(new String[]{
                "ID", "Paciente", "Médico", "Fecha", "Hora", "Modalidad"
        }, 0);

        tablaCitas = new JTable(modeloCitas);
        tablaCitas.setRowHeight(28);
        tablaCitas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaCitas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(new JScrollPane(tablaCitas), BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        botones.setBackground(new Color(245, 245, 245));

        btnAgendarCita = crearBoton("📅 Agendar Cita", new Color(66, 166, 105));
        btnEditarCita = crearBoton("✏️ Editar", new Color(66, 166, 105));
        btnEliminarCita = crearBoton("🗑️ Eliminar", new Color(66, 166, 105));
        btnActualizarCitas = crearBoton("🔄 Actualizar Lista", new Color(66, 166, 105));

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

    // ===== BOTÓN ESTÉTICO CON EFECTO HOVER =====
    private JButton crearBoton(String texto, Color colorBase) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(colorBase);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(colorBase.darker());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(colorBase);
            }
        });
        return btn;
    }

    public static void main(String[] args) {
        Usuario dummy = new Usuario(1, 3, "gestor_demo", "1234", "gestor de citas");
        new VentanaGestorCitas(dummy);
    }
}
