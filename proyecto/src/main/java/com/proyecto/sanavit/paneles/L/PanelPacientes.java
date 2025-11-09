package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.Paciente;
import com.proyecto.sanavit.modelo.PacienteDao;
import com.proyecto.sanavit.modelo.Usuario;
import com.proyecto.sanavit.modelo.UsuarioDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Panel de gestión de pacientes (CRUD)
 * Adaptado al constructor: (idPaciente, nombre, correo, edad, telefono, sexo, direccion, identificacion, idUsuario)
 */
public class PanelPacientes extends JPanel {

    private JTable tablaPacientes;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtIdentificacion, txtCorreo, txtEdad, txtTelefono, txtDireccion;
    private JComboBox<String> comboSexo, comboUsuario;
    private JButton btnAgregar, btnEditar, btnEliminar, btnActualizarTabla;
    private JLabel lblTotalPacientes;

    private PacienteDao pacienteDAO = new PacienteDao();
    private UsuarioDao usuarioDAO = new UsuarioDao();
    private List<Usuario> usuariosCache;

    public PanelPacientes() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // === ENCABEZADO ===
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("👩‍⚕️ Gestión de Pacientes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panelSuperior.setBackground(new Color(200, 250, 200));
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        // === PANEL CENTRAL ===
        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        panelCentro.setBackground(Color.WHITE);

        // === TABLA PACIENTES ===
        modeloTabla = new DefaultTableModel(new String[]{
                "ID", "Nombre", "Correo", "Edad", "Teléfono", "Sexo", "Dirección", "Identificación", "Usuario Asociado"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPacientes = new JTable(modeloTabla);
        tablaPacientes.setRowHeight(25);
        tablaPacientes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaPacientes.setSelectionBackground(new Color(180, 240, 180));
        panelCentro.add(new JScrollPane(tablaPacientes), BorderLayout.CENTER);

        // === PANEL LATERAL: Resumen ===
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

        lblTotalPacientes = new JLabel("Total pacientes: 0");
        lblTotalPacientes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTotalPacientes.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTotalPacientes.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        panelResumen.add(lblTituloResumen);
        panelResumen.add(lblTotalPacientes);
        panelCentro.add(panelResumen, BorderLayout.EAST);

        add(panelCentro, BorderLayout.CENTER);

        // === PANEL INFERIOR: Formulario + Botones ===
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(8, 2, 10, 10));
        form.setBackground(Color.WHITE);

        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        txtEdad = new JTextField();
        txtTelefono = new JTextField();
        txtDireccion = new JTextField();
        txtIdentificacion = new JTextField();
        comboSexo = new JComboBox<>(new String[]{"Masculino", "Femenino", "Otro"});
        comboUsuario = new JComboBox<>();

        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Correo:"));
        form.add(txtCorreo);
        form.add(new JLabel("Edad:"));
        form.add(txtEdad);
        form.add(new JLabel("Teléfono:"));
        form.add(txtTelefono);
        form.add(new JLabel("Sexo:"));
        form.add(comboSexo);
        form.add(new JLabel("Dirección:"));
        form.add(txtDireccion);
        form.add(new JLabel("Identificación:"));
        form.add(txtIdentificacion);
        form.add(new JLabel("Usuario Asociado:"));
        form.add(comboUsuario);

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
        cargarUsuarios();
        cargarPacientes();

        // === ACCIONES ===
        btnAgregar.addActionListener(e -> agregarPaciente());
        btnEditar.addActionListener(e -> editarPaciente());
        btnEliminar.addActionListener(e -> eliminarPaciente());
        btnActualizarTabla.addActionListener(e -> cargarPacientes());

        tablaPacientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaPacientes.getSelectedRow();
                if (fila != -1) {
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtCorreo.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtEdad.setText(modeloTabla.getValueAt(fila, 3).toString());
                    txtTelefono.setText(modeloTabla.getValueAt(fila, 4).toString());
                    comboSexo.setSelectedItem(modeloTabla.getValueAt(fila, 5).toString());
                    txtDireccion.setText(modeloTabla.getValueAt(fila, 6).toString());
                    txtIdentificacion.setText(modeloTabla.getValueAt(fila, 7).toString());
                    comboUsuario.setSelectedItem(modeloTabla.getValueAt(fila, 8).toString());
                }
            }
        });
    }

    // === BOTONES ESTILIZADOS ===
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

    // === CARGAR USUARIOS ===
    private void cargarUsuarios() {
        comboUsuario.removeAllItems();
        usuariosCache = usuarioDAO.listarUsuarios();
        for (Usuario u : usuariosCache) {
            comboUsuario.addItem(u.getNombreUsuario());
        }
    }

    private int obtenerIdUsuarioSeleccionado() {
        int index = comboUsuario.getSelectedIndex();
        if (index < 0 || index >= usuariosCache.size()) return -1;
        return usuariosCache.get(index).getIdUsuario();
    }

    private String obtenerNombreUsuarioPorId(int idUsuario) {
        for (Usuario u : usuariosCache) {
            if (u.getIdUsuario() == idUsuario)
                return u.getNombreUsuario();
        }
        return "—";
    }

    // === CARGAR PACIENTES ===
    private void cargarPacientes() {
        modeloTabla.setRowCount(0);
        List<Paciente> lista = pacienteDAO.obtenerTodosLosPacientes();
        for (Paciente p : lista) {
            modeloTabla.addRow(new Object[]{
                    p.getIdPaciente(),
                    p.getNombre(),
                    p.getCorreo(),
                    p.getEdad(),
                    p.getTelefono(),
                    p.getSexo(),
                    p.getDireccion(),
                    p.getIdentificacion(),
                    obtenerNombreUsuarioPorId(p.getIdUsuario())
            });
        }
        lblTotalPacientes.setText("Total pacientes: " + lista.size());
    }

    // === AGREGAR PACIENTE ===
    private void agregarPaciente() {
        try {
            String nombre = txtNombre.getText().trim();
            String correo = txtCorreo.getText().trim();
            int edad = Integer.parseInt(txtEdad.getText().trim());
            String tel = txtTelefono.getText().trim();
            String sexo = comboSexo.getSelectedItem().toString();
            String direccion = txtDireccion.getText().trim();
            String iden = txtIdentificacion.getText().trim();
            int idUsuario = obtenerIdUsuarioSeleccionado();

            if (nombre.isEmpty() || correo.isEmpty() || iden.isEmpty() || idUsuario <= 0) {
                JOptionPane.showMessageDialog(this, "⚠️ Todos los campos son obligatorios.");
                return;
            }

            Paciente nuevo = new Paciente(0, nombre, correo, edad, tel, sexo, direccion, iden, idUsuario);
            boolean ok = pacienteDAO.insertarPaciente(nuevo);
            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Paciente agregado correctamente.");
                cargarPacientes();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al agregar paciente.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "⚠️ La edad debe ser un número válido.");
        }
    }

    // === EDITAR PACIENTE ===
    private void editarPaciente() {
        int fila = tablaPacientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente para editar.");
            return;
        }

        try {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            String nombre = txtNombre.getText().trim();
            String correo = txtCorreo.getText().trim();
            int edad = Integer.parseInt(txtEdad.getText().trim());
            String tel = txtTelefono.getText().trim();
            String sexo = comboSexo.getSelectedItem().toString();
            String direccion = txtDireccion.getText().trim();
            String iden = txtIdentificacion.getText().trim();
            int idUsuario = obtenerIdUsuarioSeleccionado();

            Paciente actualizado = new Paciente(id, nombre, correo, edad, tel, sexo, direccion, iden, idUsuario);
            boolean ok = pacienteDAO.actualizarPaciente(actualizado);
            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Paciente actualizado correctamente.");
                cargarPacientes();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al actualizar paciente.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "⚠️ La edad debe ser un número válido.");
        }
    }

    // === ELIMINAR PACIENTE ===
    private void eliminarPaciente() {
        int fila = tablaPacientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente para eliminar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar este paciente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = pacienteDAO.eliminarPaciente(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Paciente eliminado correctamente.");
                cargarPacientes();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al eliminar paciente.");
            }
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtCorreo.setText("");
        txtEdad.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtIdentificacion.setText("");
        comboSexo.setSelectedIndex(0);
        if (comboUsuario.getItemCount() > 0) comboUsuario.setSelectedIndex(0);
    }
}
