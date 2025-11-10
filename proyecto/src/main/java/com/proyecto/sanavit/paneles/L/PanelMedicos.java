package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.Medico;
import com.proyecto.sanavit.modelo.MedicoDao;
import com.proyecto.sanavit.modelo.Usuario;
import com.proyecto.sanavit.modelo.UsuarioDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Panel de gestión de médicos (CRUD) - CORREGIDO para usar solo métodos existentes
 */
public class PanelMedicos extends JPanel {

    private JTable tablaMedicos;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtEspecialidad;
    private JComboBox<String> comboUsuario;
    private JButton btnAgregar, btnEditar, btnEliminar, btnActualizarTabla;

    private MedicoDao medicoDAO = new MedicoDao();
    private UsuarioDao usuarioDAO = new UsuarioDao();

    // cache de usuarios cargados (para obtener idUsuario según selección)
    private List<Usuario> usuariosCache;

    public PanelMedicos() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // === TÍTULO SUPERIOR ===
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Gestión de Médicos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panelSuperior.setBackground(new Color(200, 250, 200));
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        // === TABLA DE MÉDICOS ===
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Especialidad", "Usuario Asociado"}, 0) {
            // evitar edición directa en la tabla
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaMedicos = new JTable(modeloTabla);
        tablaMedicos.setRowHeight(25);
        tablaMedicos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaMedicos.setSelectionBackground(new Color(180, 240, 180));

        JScrollPane scroll = new JScrollPane(tablaMedicos);
        add(scroll, BorderLayout.CENTER);

        // === PANEL INFERIOR (FORMULARIO + BOTONES) ===
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        txtNombre = new JTextField();
        txtEspecialidad = new JTextField();
        comboUsuario = new JComboBox<>();

        form.add(new JLabel("Nombre del Médico:"));
        form.add(txtNombre);
        form.add(new JLabel("Especialidad:"));
        form.add(txtEspecialidad);
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
        cargarUsuarios();   // llena comboUsuario y usuariosCache
        cargarMedicos();    // llena la tabla

        // === ACCIONES ===
        btnAgregar.addActionListener(e -> agregarMedico());
        btnEditar.addActionListener(e -> editarMedico());
        btnEliminar.addActionListener(e -> eliminarMedico());
        btnActualizarTabla.addActionListener(e -> cargarMedicos());

        tablaMedicos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaMedicos.getSelectedRow();
                if (fila != -1) {
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtEspecialidad.setText(modeloTabla.getValueAt(fila, 2).toString());

                    String usuarioNombre = modeloTabla.getValueAt(fila, 3).toString();
                    comboUsuario.setSelectedItem(usuarioNombre);
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
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(100, 210, 120));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(123, 229, 144));
            }
        });
        return btn;
    }

    // === CARGAR LISTADO DE MÉDICOS EN LA TABLA ===
    private void cargarMedicos() {
        modeloTabla.setRowCount(0);
        List<Medico> lista = MedicoDao.listarMedicos(); // tu DAO tiene listarMedicos()
        for (Medico m : lista) {
            String nombreUsuario = obtenerNombreUsuarioPorId(m.getIdUsuario());
            modeloTabla.addRow(new Object[]{
                    m.getIdMedico(),
                    m.getNombre(),
                    m.getEspecialidad(),
                    nombreUsuario != null ? nombreUsuario : "—"
            });
        }
    }

    // === CARGAR USUARIOS EN EL COMBO y guardar en cache ===
    private void cargarUsuarios() {
        comboUsuario.removeAllItems();
        usuariosCache = usuarioDAO.listarUsuarios(); // obtiene lista de Usuario
        if (usuariosCache == null || usuariosCache.isEmpty()) {
            comboUsuario.addItem("No hay usuarios");
            comboUsuario.setEnabled(false);
            return;
        }
        for (Usuario u : usuariosCache) {
            comboUsuario.addItem(u.getNombreUsuario());
        }
        comboUsuario.setEnabled(true);
    }

    // Obtener nombre de usuario por id recorriendo el cache
    private String obtenerNombreUsuarioPorId(int idUsuario) {
        if (usuariosCache == null) {
            usuariosCache = usuarioDAO.listarUsuarios();
        }
        if (usuariosCache == null) return null;
        for (Usuario u : usuariosCache) {
            if (u.getIdUsuario() == idUsuario) return u.getNombreUsuario();
        }
        return null;
    }

    // Obtener idUsuario según la selección del combo (por índice)
    private int obtenerIdUsuarioSeleccionado() {
        int idx = comboUsuario.getSelectedIndex();
        if (usuariosCache == null || usuariosCache.isEmpty() || idx < 0 || idx >= usuariosCache.size()) {
            return -1;
        }
        return usuariosCache.get(idx).getIdUsuario();
    }

    // === AGREGAR MÉDICO ===
    private void agregarMedico() {
        String nombre = txtNombre.getText().trim();
        String especialidad = txtEspecialidad.getText().trim();

        if (nombre.isEmpty() || especialidad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Nombre y especialidad son obligatorios.");
            return;
        }

        int idUsuario = obtenerIdUsuarioSeleccionado();
        if (idUsuario <= 0) {
            JOptionPane.showMessageDialog(this, "⚠️ Seleccione un usuario asociado válido.");
            return;
        }

        // Constructor correcto: (idMedico, nombre, especialidad, idUsuario)
        Medico nuevo = new Medico(0, nombre, especialidad, idUsuario);
        boolean ok = medicoDAO.insertarMedico(nuevo);

        if (ok) {
            JOptionPane.showMessageDialog(this, "✅ Médico registrado correctamente.");
            cargarMedicos();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al registrar médico.");
        }
    }

    // === EDITAR MÉDICO ===
    private void editarMedico() {
        int fila = tablaMedicos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un médico para editar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = txtNombre.getText().trim();
        String especialidad = txtEspecialidad.getText().trim();

        if (nombre.isEmpty() || especialidad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Nombre y especialidad son obligatorios.");
            return;
        }

        int idUsuario = obtenerIdUsuarioSeleccionado();
        if (idUsuario <= 0) {
            JOptionPane.showMessageDialog(this, "⚠️ Seleccione un usuario asociado válido.");
            return;
        }

        // Constructor correcto: (idMedico, nombre, especialidad, idUsuario)
        Medico actualizado = new Medico(id, nombre, especialidad, idUsuario);
        boolean ok = medicoDAO.updateMedico(actualizado); // tu DAO tiene updateMedico()

        if (ok) {
            JOptionPane.showMessageDialog(this, "✅ Médico actualizado correctamente.");
            cargarMedicos();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al actualizar médico.");
        }
    }

    // === ELIMINAR MÉDICO ===
    private void eliminarMedico() {
        int fila = tablaMedicos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un médico para eliminar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar este médico?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = medicoDAO.deleteMedico(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Médico eliminado correctamente.");
                cargarMedicos();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al eliminar médico.");
            }
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtEspecialidad.setText("");
        if (comboUsuario.getItemCount() > 0) comboUsuario.setSelectedIndex(0);
    }
}
