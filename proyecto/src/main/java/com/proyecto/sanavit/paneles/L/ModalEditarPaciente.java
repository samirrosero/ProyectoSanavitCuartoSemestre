package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModalEditarPaciente extends JDialog {

    private final PacienteDao pacienteDAO = new PacienteDao();

    public ModalEditarPaciente(JFrame parent, Paciente paciente) {
        super(parent, "Editar Paciente - Sanavit", true);
        if (paciente == null) {
            JOptionPane.showMessageDialog(parent, "Seleccione un paciente válido.");
            return;
        }

        setSize(450, 550);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);
        setResizable(false);

        // ENCABEZADO
        JPanel header = new JPanel();
        header.setBackground(new Color(0, 153, 102));
        JLabel lbl = new JLabel("Editar Paciente");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setForeground(Color.WHITE);
        header.add(lbl);

        // CAMPOS
        JPanel campos = new JPanel(new GridLayout(7, 2, 10, 10));
        campos.setBackground(Color.WHITE);
        campos.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JTextField txtNombre = new JTextField(paciente.getNombre());
        JTextField txtCorreo = new JTextField(paciente.getCorreo());
        JTextField txtEdad = new JTextField(String.valueOf(paciente.getEdad()));
        JTextField txtTelefono = new JTextField(paciente.getTelefono());
        JComboBox<String> cbSexo = new JComboBox<>(new String[]{"Masculino", "Femenino"});
        cbSexo.setSelectedItem(paciente.getSexo());
        JTextField txtDireccion = new JTextField(paciente.getDireccion());
        JTextField txtIdentificacion = new JTextField(paciente.getIdentificacion());

        Font fuente = new Font("Segoe UI", Font.PLAIN, 14);
        for (Component c : new Component[]{txtNombre, txtCorreo, txtEdad, txtTelefono, txtDireccion, txtIdentificacion}) {
            ((JTextField)c).setFont(fuente);
            ((JTextField)c).setBorder(BorderFactory.createLineBorder(new Color(180, 220, 180), 1));
        }

        campos.add(new JLabel("Nombre:")); campos.add(txtNombre);
        campos.add(new JLabel("Correo:")); campos.add(txtCorreo);
        campos.add(new JLabel("Edad:")); campos.add(txtEdad);
        campos.add(new JLabel("Teléfono:")); campos.add(txtTelefono);
        campos.add(new JLabel("Sexo:")); campos.add(cbSexo);
        campos.add(new JLabel("Dirección:")); campos.add(txtDireccion);
        campos.add(new JLabel("Identificación:")); campos.add(txtIdentificacion);

        // BOTONES
        JPanel panelBtns = new JPanel();
        panelBtns.setBackground(Color.WHITE);

        JButton btnGuardar = new JButton("Guardar Cambios");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.setBackground(new Color(0, 153, 102));
        btnGuardar.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(192, 57, 43));
        btnCancelar.setForeground(Color.WHITE);

        btnGuardar.addActionListener(e -> {
            try {
                paciente.setNombre(txtNombre.getText().trim());
                paciente.setCorreo(txtCorreo.getText().trim());
                paciente.setEdad(Integer.parseInt(txtEdad.getText().trim()));
                paciente.setTelefono(txtTelefono.getText().trim());
                paciente.setSexo(cbSexo.getSelectedItem().toString());
                paciente.setDireccion(txtDireccion.getText().trim());
                paciente.setIdentificacion(txtIdentificacion.getText().trim());

                if (pacienteDAO.actualizarPaciente(paciente)) {
                    JOptionPane.showMessageDialog(this, "✅ Paciente actualizado correctamente.");
                    dispose();
                    ((VentanaGestorCitas)parent).cargarPacientes();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el paciente.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Edad inválida.");
            }
        });

        btnCancelar.addActionListener(e -> dispose());

        for (JButton btn : new JButton[]{btnGuardar, btnCancelar}) {
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { btn.setBackground(btn.getBackground().darker()); }
                public void mouseExited(MouseEvent e) { btn.setBackground(btn.getBackground().brighter()); }
            });
            panelBtns.add(btn);
        }

        add(header, BorderLayout.NORTH);
        add(campos, BorderLayout.CENTER);
        add(panelBtns, BorderLayout.SOUTH);

        setVisible(true);
    }
}
