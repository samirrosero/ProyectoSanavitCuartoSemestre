package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.Usuario;
import com.proyecto.sanavit.modelo.UsuarioDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

/**
 * Panel de gestión de usuarios (versión JPanel)
 * Integrable dentro de VentanaAdministrador
 */
public class PanelUsuarios extends JPanel {

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombreUsuario, txtContraseña;
    private JComboBox<String> comboRol;
    private JButton btnAgregar, btnEditar, btnEliminar, btnActualizarTabla;

    private UsuarioDao usuarioDAO = new UsuarioDao();

    public PanelUsuarios() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // === TÍTULO SUPERIOR ===
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("👥 Gestión de Usuarios", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panelSuperior.setBackground(new Color(200, 250, 200));
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        // === TABLA DE USUARIOS ===
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Rol", "Usuario", "Contraseña"}, 0);
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setRowHeight(25);
        tablaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaUsuarios.setSelectionBackground(new Color(180, 240, 180));
        add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);

        // === PANEL INFERIOR (FORMULARIO + BOTONES) ===
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        txtNombreUsuario = new JTextField();
        txtContraseña = new JTextField();
        comboRol = new JComboBox<>();

        form.add(new JLabel("Nombre de Usuario:"));
        form.add(txtNombreUsuario);
        form.add(new JLabel("Contraseña:"));
        form.add(txtContraseña);
        form.add(new JLabel("Rol:"));
        form.add(comboRol);

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
        cargarRoles();
        cargarUsuarios();

        // === ACCIONES ===
        btnAgregar.addActionListener(e -> agregarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnActualizarTabla.addActionListener(e -> cargarUsuarios());

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
    }

    // === MÉTODO: Crear botón con estilo uniforme ===
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

    // === MÉTODO: Cargar usuarios desde BD ===
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

    // === MÉTODO: Cargar roles ===
    private void cargarRoles() {
        comboRol.removeAllItems();
        Map<String, Integer> roles = usuarioDAO.obtenerRoles();
        for (String nombreRol : roles.keySet()) {
            comboRol.addItem(nombreRol);
        }
    }

    // === CRUD ===
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

    private void eliminarUsuario() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar este usuario?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

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

    private void limpiarCampos() {
        txtNombreUsuario.setText("");
        txtContraseña.setText("");
        comboRol.setSelectedIndex(0);
    }
}
