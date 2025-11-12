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
    private JButton btnAgendarCita, btnEditarCita, btnEliminarCita, btnActualizarCitas, btnActualizarPacientes, btnCancelarCita;
    private JTextField txtBuscarPaciente;
    private Usuario usuarioGestor;

    private final PacienteDao pacienteDAO = new PacienteDao();
    private final CitaDao citaDAO = new CitaDao();

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
        JLabel lblTitulo = new JLabel("Bienvenido, " + usuarioGestor.getNombreUsuario() + " (Gestor de Citas)", SwingConstants.CENTER);
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

        // Hover
        btnCerrarSesion.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnCerrarSesion.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(MouseEvent e) {
                btnCerrarSesion.setBackground(new Color(66, 166, 105));
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

        ImageIcon iconPacientes = crearIcono("/icons/icons8-usuario-32.png");
        ImageIcon iconCitas = crearIcono("/icons/icons8-citas-32.png");

        tabs.addTab(" Pacientes", iconPacientes, crearPanelPacientes());
        tabs.addTab(" Citas", iconCitas, crearPanelCitas());

        add(tabs, BorderLayout.CENTER);
        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }

    // ===== PANEL PACIENTES =====
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

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        botones.setBackground(new Color(245, 245, 245));

        btnNuevoPaciente = crearBoton("Nuevo Paciente", new Color(46, 204, 113));
        btnEditarPaciente = crearBoton("Editar", new Color(66, 166, 105));
        btnEliminarPaciente = crearBoton("Eliminar", new Color(66, 166, 105));
        btnBuscarPaciente = crearBoton("Buscar", new Color(66, 166, 105));
        btnActualizarPacientes = crearBoton("Actualizar Lista", new Color(66, 166, 105));

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

        btnNuevoPaciente.addActionListener(e -> new ModalRegistrarPaciente(this));
        btnActualizarPacientes.addActionListener(e -> cargarPacientes());
        btnBuscarPaciente.addActionListener(e -> buscarPaciente());
        btnEliminarPaciente.addActionListener(e -> eliminarPaciente());
        btnEditarPaciente.addActionListener(e -> new ModalEditarPaciente(this, obtenerPacienteSeleccionado()));

        return panel;
    }

    private Paciente obtenerPacienteSeleccionado() {
        int fila = tablaPacientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Seleccione un paciente.");
            return null;
        }
        int idPaciente = Integer.parseInt(modeloPacientes.getValueAt(fila, 0).toString());
        return pacienteDAO.obtenerPacientePorId(idPaciente);
    }

    public void cargarPacientes() {
        modeloPacientes.setRowCount(0);
        for (Paciente p : pacienteDAO.obtenerTodosLosPacientes()) {
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
        for (Paciente p : pacienteDAO.buscarPacientePorNombre(nombre)) {
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
        if (confirm == JOptionPane.YES_OPTION && pacienteDAO.eliminarPaciente(id)) {
            JOptionPane.showMessageDialog(this, "Paciente eliminado correctamente.");
            cargarPacientes();
        }
    }

    // ===== PANEL CITAS =====
    private JPanel crearPanelCitas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        modeloCitas = new DefaultTableModel(new String[]{
                "ID", "Paciente", "Médico", "Fecha", "Hora", "Modalidad", "Estado"
        }, 0);

        tablaCitas = new JTable(modeloCitas);
        tablaCitas.setRowHeight(28);
        tablaCitas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaCitas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(new JScrollPane(tablaCitas), BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        botones.setBackground(new Color(245, 245, 245));

        btnAgendarCita = crearBoton("Agendar Cita", new Color(66, 166, 105));
        btnEditarCita = crearBoton("Editar", new Color(66, 166, 105));
        btnCancelarCita = crearBoton("Cancelar Cita", new Color(192, 57, 43));
        btnActualizarCitas = crearBoton("Actualizar Lista", new Color(66, 166, 105));

        botones.add(btnAgendarCita);
        botones.add(btnEditarCita);
        botones.add(btnCancelarCita);
        botones.add(btnActualizarCitas);

        panel.add(botones, BorderLayout.SOUTH);
        cargarCitas();

        btnActualizarCitas.addActionListener(e -> cargarCitas());
        btnAgendarCita.addActionListener(e -> new ModalAgendarCita(this));
        btnEditarCita.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "⚙️ Edición de cita aún no disponible.");
        });
        btnCancelarCita.addActionListener(e -> cancelarCita());

        return panel;
    }

    private void cancelarCita() {
        int fila = tablaCitas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Seleccione una cita para cancelar.");
            return;
        }

        int idCita = Integer.parseInt(modeloCitas.getValueAt(fila, 0).toString());
        String estado = modeloCitas.getValueAt(fila, 6).toString();
        if (estado.equalsIgnoreCase("Cancelada")) {
            JOptionPane.showMessageDialog(this, "Esta cita ya está cancelada.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas cancelar esta cita?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Cita cita = new Cita();
            cita.setIdCita(idCita);
            cita.setIdEstadoCita(2);
            if (citaDAO.actualizarEstadoCita(cita)) {
                JOptionPane.showMessageDialog(this, "✅ Cita cancelada correctamente.");
                cargarCitas();
            } else {
                JOptionPane.showMessageDialog(this, "❌ No se pudo cancelar la cita.");
            }
        }
    }

    private void cargarCitas() {
        modeloCitas.setRowCount(0);
        MedicoDao medicoDAO = new MedicoDao();
        for (Cita c : citaDAO.selectCita()) {
            Paciente p = pacienteDAO.obtenerPacientePorId(c.getIdPaciente());
            Medico m = medicoDAO.obtenerMedicoPorId(c.getIdMedico());
            modeloCitas.addRow(new Object[]{
                    c.getIdCita(),
                    p != null ? p.getNombre() : "Desconocido",
                    m != null ? m.getNombre() : "Desconocido",
                    c.getFechaCita(),
                    c.getHoraCita(),
                    c.getIdModalidad() == 1 ? "Presencial" : "Virtual",
                    c.getIdEstadoCita() == 1 ? "Activa" : "Cancelada"
            });
        }
    }

    // ===== UTILIDADES =====
    private JButton crearBoton(String texto, Color colorBase) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(colorBase);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(colorBase.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(colorBase); }
        });
        return btn;
    }

    private ImageIcon crearIcono(String ruta) {
        java.net.URL url = getClass().getResource(ruta);
        if (url != null) {
            return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
        }
        return null;
    }
}
