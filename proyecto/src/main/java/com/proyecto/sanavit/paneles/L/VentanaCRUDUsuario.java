package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.Usuario;
import com.proyecto.sanavit.modelo.UsuarioDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

public class VentanaCRUDUsuario extends JFrame {

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombreUsuario, txtContraseña;
    private JComboBox<String> comboRol;
    private JButton btnAgregar, btnEditar, btnEliminar, btnActualizarTabla;

    private UsuarioDao usuarioDAO = new UsuarioDao();

    public VentanaCRUDUsuario() {
        setTitle("Gestión de Usuarios");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // === PANEL SUPERIOR ===
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Administración de Usuarios", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panelSuperior.setBackground(new Color(123, 229, 144));
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        // === PANEL CENTRAL (TABLA) ===
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Rol", "Usuario", "Contraseña"}, 0);
        tablaUsuarios = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaUsuarios);
        add(scroll, BorderLayout.CENTER);

        // === PANEL INFERIOR (FORMULARIO + BOTONES) ===
        JPanel panelInferior = new JPanel(new GridLayout(3, 2, 10, 10));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        txtNombreUsuario = new JTextField();
        txtContraseña = new JTextField();
        comboRol = new JComboBox<>();

        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnActualizarTabla = new JButton("Actualizar Tabla");

        panelInferior.add(new JLabel("Nombre de Usuario:"));
        panelInferior.add(txtNombreUsuario);

        panelInferior.add(new JLabel("Contraseña:"));
        panelInferior.add(txtContraseña);

        panelInferior.add(new JLabel("Rol:"));
        panelInferior.add(comboRol);

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizarTabla);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.add(panelInferior, BorderLayout.CENTER);
        panelSur.add(panelBotones, BorderLayout.SOUTH);

        add(panelSur, BorderLayout.SOUTH);

        // === CARGAR DATOS ===
        cargarRoles();
        cargarUsuarios();

        // === ACCIONES ===

        // Agregar usuario
        btnAgregar.addActionListener(e -> agregarUsuario());

        // Editar usuario seleccionado
        btnEditar.addActionListener(e -> editarUsuario());

        // Eliminar usuario seleccionado
        btnEliminar.addActionListener(e -> eliminarUsuario());

        // Actualizar tabla
        btnActualizarTabla.addActionListener(e -> cargarUsuarios());

        // Cargar datos de fila seleccionada
        tablaUsuarios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaUsuarios.getSelectedRow();
                if (fila != -1) {
                    txtNombreUsuario.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtContraseña.setText(modeloTabla.getValueAt(fila, 3).toString());
                    comboRol.setSelectedItem(modeloTabla.getValueAt(fila, 1).toString());
                }
            }
        });

        setVisible(true);
    }

    // === MÉTODO: Cargar usuarios desde la BD ===
    private void cargarUsuarios() {
        modeloTabla.setRowCount(0);
        List<Usuario> lista = usuarioDAO.listarUsuarios();

        for (Usuario u : lista) {
            modeloTabla.addRow(new Object[]{
                u.getIdUsuario(),
                u.getNombreRol(),
                u.getNombreUsuario(),
                u.getContraseña()
            });
        }
    }

    // === MÉTODO: Cargar roles al comboBox ===
    private void cargarRoles() {
        comboRol.removeAllItems();
        Map<String, Integer> roles = usuarioDAO.obtenerRoles();
        for (String nombreRol : roles.keySet()) {
            comboRol.addItem(nombreRol);
        }
    }

    // === MÉTODO: Agregar usuario ===
    private void agregarUsuario() {
        String nombre = txtNombreUsuario.getText().trim();
        String contrasena = txtContraseña.getText().trim();
        String rol = (String) comboRol.getSelectedItem();

        if (nombre.isEmpty() || contrasena.isEmpty() || rol == null) {
            JOptionPane.showMessageDialog(this, "⚠️ Todos los campos son obligatorios.");
            return;
        }

        int idRol = usuarioDAO.obtenerIdRolPorNombre(rol);
        Usuario nuevo = new Usuario(0, idRol, nombre, contrasena, rol);
        int idGenerado = usuarioDAO.insertarUsuario(nuevo);

        if (idGenerado > 0) {
            JOptionPane.showMessageDialog(this, "✅ Usuario registrado correctamente.");
            cargarUsuarios();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al registrar usuario.");
        }
    }

    // === MÉTODO: Editar usuario seleccionado ===
    private void editarUsuario() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para editar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = txtNombreUsuario.getText().trim();
        String contrasena = txtContraseña.getText().trim();
        String rol = (String) comboRol.getSelectedItem();
        int idRol = usuarioDAO.obtenerIdRolPorNombre(rol);

        Usuario actualizado = new Usuario(id, idRol, nombre, contrasena, rol);
        boolean ok = usuarioDAO.actualizarUsuario(actualizado);

        if (ok) {
            JOptionPane.showMessageDialog(this, "✅ Usuario actualizado correctamente.");
            cargarUsuarios();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al actualizar usuario.");
        }
    }

    // === MÉTODO: Eliminar usuario seleccionado ===
    private void eliminarUsuario() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar este usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = usuarioDAO.eliminarUsuario(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Usuario eliminado correctamente.");
                cargarUsuarios();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al eliminar usuario.");
            }
        }
    }

    // === MÉTODO: Limpiar campos ===
    private void limpiarCampos() {
        txtNombreUsuario.setText("");
        txtContraseña.setText("");
        comboRol.setSelectedIndex(0);
    }
}
