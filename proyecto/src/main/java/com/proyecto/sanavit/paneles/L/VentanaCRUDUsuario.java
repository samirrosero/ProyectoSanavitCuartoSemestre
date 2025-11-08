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
        setTitle("Gestión de Usuarios - Sanavit");
        setSize(750, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // === PANEL SUPERIOR ===
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel(" Administración de Usuarios", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panelSuperior.setBackground(new Color(92, 184, 92)); 
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        // === PANEL CENTRAL (TABLA) ===
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Rol", "Usuario", "Contraseña"}, 0);
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaUsuarios.setRowHeight(23);
        tablaUsuarios.setSelectionBackground(new Color(10, 10, 10 ));
        tablaUsuarios.setSelectionForeground(Color.WHITE);
        tablaUsuarios.getTableHeader().setBackground(new Color(40, 180, 99));
        tablaUsuarios.getTableHeader().setForeground(Color.WHITE);
        tablaUsuarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(tablaUsuarios);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scroll.getViewport().setBackground(new Color(121, 199, 170));
        add(scroll, BorderLayout.CENTER);

        // === PANEL INFERIOR ===
        JPanel panelInferior = new JPanel(new GridBagLayout());
        panelInferior.setBackground(new Color(229, 255, 237));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombreUsuario = new JTextField();
        txtContraseña = new JTextField();
        comboRol = new JComboBox<>();

        JLabel lblUsuario = new JLabel("Nombre de Usuario:");
        JLabel lblContrasena = new JLabel("Contraseña:");
        JLabel lblRol = new JLabel("Rol:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblContrasena.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRol.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Columna 1
        gbc.gridx = 0; gbc.gridy = 0; panelInferior.add(lblUsuario, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panelInferior.add(txtNombreUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelInferior.add(lblContrasena, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelInferior.add(txtContraseña, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelInferior.add(lblRol, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panelInferior.add(comboRol, gbc);

        // === PANEL BOTONES ===
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(240, 255, 244));

        btnAgregar = crearBoton("Agregar", new Color(23, 153, 118));
        btnEditar = crearBoton("Editar", new Color(23, 153, 118));
        btnEliminar = crearBoton("Eliminar", new Color(23, 153, 118));
        btnActualizarTabla = crearBoton("Actualizar", new Color(23, 153, 118));

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
        btnAgregar.addActionListener(e -> agregarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnActualizarTabla.addActionListener(e -> cargarUsuarios());

        // Click en la tabla
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

        // === FONDO GENERAL ===
        getContentPane().setBackground(new Color(235, 255, 243));
        setVisible(true);
    }

    // === MÉTODO: Crear botones con color y hover ===
    private JButton crearBoton(String texto, Color colorBase) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setBackground(colorBase);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBase.darker(), 1),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));

        // Hover
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(colorBase.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(colorBase);
            }
        });
        return btn;
    }

    // === Cargar usuarios ===
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

    // === Cargar roles ===
    private void cargarRoles() {
        comboRol.removeAllItems();
        Map<String, Integer> roles = usuarioDAO.obtenerRoles();
        for (String nombreRol : roles.keySet()) {
            comboRol.addItem(nombreRol);
        }
    }

    // === Agregar usuario ===
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

    // === Editar usuario ===
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

    // === Eliminar usuario ===
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

    // === Limpiar campos ===
    private void limpiarCampos() {
        txtNombreUsuario.setText("");
        txtContraseña.setText("");
        if (comboRol.getItemCount() > 0) comboRol.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new VentanaCRUDUsuario();
    }
}
