package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModalMedico extends JDialog {

    private JTextField txtNombre;
    private JComboBox<String> comboEspecialidad;
    private JButton btnGuardar;

    public ModalMedico(JFrame parent, Usuario usuarioActual) {
        super(parent, "Registrar Médico - Sanavit", true);
        setSize(450, 320);
        setLocationRelativeTo(parent);

        // === PANEL PRINCIPAL CON DEGRADADO ===
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(78, 207, 78), 
                0, getHeight(), new Color(165, 242, 203)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelPrincipal.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // === TÍTULO ===
        JLabel lblTitulo = new JLabel("Registro de Médico", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(10, 10, 10));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(lblTitulo, gbc);

        // === CAMPO NOMBRE ===
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNombre.setForeground(new Color(10, 10, 10));
        panelPrincipal.add(lblNombre, gbc);

        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNombre.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(102, 187, 106), 1),
        BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panelPrincipal.add(txtNombre, gbc);

        // === CAMPO ESPECIALIDAD ===
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblEspecialidad = new JLabel("Especialidad:");
        lblEspecialidad.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEspecialidad.setForeground(new Color(10, 10, 10));
        panelPrincipal.add(lblEspecialidad, gbc);

        gbc.gridx = 1;
        comboEspecialidad = new JComboBox<>(new String[]{
                "Medicina General", "Pediatría", "Ginecología", "Cardiología", "Odontología", "Oftalmología"
        });
        comboEspecialidad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboEspecialidad.setBackground(Color.WHITE);
        comboEspecialidad.setBorder(BorderFactory.createLineBorder(new Color(102, 187, 106), 1));
        panelPrincipal.add(comboEspecialidad, gbc);

        // === BOTÓN GUARDAR ===
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnGuardar = crearBoton("Guardar Médico", new Color(31, 194, 111));
        panelPrincipal.add(btnGuardar, gbc);

        // === EVENTO GUARDAR ===
        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String especialidad = comboEspecialidad.getSelectedItem().toString();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Debe ingresar el nombre del médico.");
                return;
            }

            Medico nuevoMedico = new Medico(0, nombre, especialidad, usuarioActual.getIdUsuario());
            MedicoDao medicoDao = new MedicoDao();

            if (medicoDao.insertarMedico(nuevoMedico)) {
                JOptionPane.showMessageDialog(this, "✅ Médico registrado con éxito.");
                dispose();
                parent.dispose();
                new Login();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al registrar médico.");
            }
        });

        add(panelPrincipal);
        setVisible(true);
    }

    // === MÉTODO PARA CREAR BOTONES ESTILIZADOS ===
    private JButton crearBoton(String texto, Color colorBase) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(colorBase);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(colorBase.darker(), 1),
        BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));

        // Bordes redondeados
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(btn.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 25, 25);
                g2.setColor(btn.getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int x = (c.getWidth() - fm.stringWidth(btn.getText())) / 2;
                int y = (c.getHeight() + fm.getAscent()) / 2 - 3;
                g2.drawString(btn.getText(), x, y);
                g2.dispose();
            }
        });

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
    public static void main(String[] args) {
        new ModalMedico(null, null);
    }
}
