package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;

public class VentanaRecetaMedica extends JFrame {

    private JTextField txtMedicamento;
    private JTextArea txtIndicaciones;
    private JButton btnGuardar;
    private HistoriaClinica historia;

    public VentanaRecetaMedica(HistoriaClinica historia) {
        this.historia = historia;

        setTitle("Receta Médica - Historia #" + historia.getIdHistoriaClinica());
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ===== PANEL TÍTULO =====
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(65, 158, 91));
        JLabel lblTitulo = new JLabel("Registrar Receta Médica", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // ===== PANEL CENTRAL =====
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBackground(Color.WHITE);
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Medicamento
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblMedicamento = new JLabel("Medicamento:");
        lblMedicamento.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelCampos.add(lblMedicamento, gbc);

        gbc.gridx = 1;
        txtMedicamento = new JTextField(20);
        txtMedicamento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMedicamento.setBorder(BorderFactory.createLineBorder(new Color(180, 220, 180), 1));
        panelCampos.add(txtMedicamento, gbc);

        // Indicaciones
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblIndicaciones = new JLabel("Indicaciones:");
        lblIndicaciones.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelCampos.add(lblIndicaciones, gbc);

        gbc.gridx = 1;
        txtIndicaciones = new JTextArea(6, 20); // ← Campo más amplio
        txtIndicaciones.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtIndicaciones.setLineWrap(true);
        txtIndicaciones.setWrapStyleWord(true);
        JScrollPane scrollIndicaciones = new JScrollPane(txtIndicaciones);
        scrollIndicaciones.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panelCampos.add(scrollIndicaciones, gbc);

        add(panelCampos, BorderLayout.CENTER);

        // ===== PANEL BOTÓN GUARDAR =====
        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(60, 120, 60));

        btnGuardar = new JButton("Guardar Receta");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBackground(new Color(76, 175, 80));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(180, 40));

        // Efecto hover
        btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardar.setBackground(new Color(67, 160, 71));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGuardar.setBackground(new Color(76, 175, 80));
            }
        });

        panelBoton.add(btnGuardar);
        add(panelBoton, BorderLayout.SOUTH);

        // Acción Guardar
        btnGuardar.addActionListener(e -> guardarReceta());

        setVisible(true);
    }

    private void guardarReceta() {
        try {
            String medicamento = txtMedicamento.getText().trim();
            String indicaciones = txtIndicaciones.getText().trim();

            if (medicamento.isEmpty() || indicaciones.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }

            RecetaMedica receta = new RecetaMedica();
            receta.setIdHistoriaClinica(historia.getIdHistoriaClinica());
            receta.setMedicamento(medicamento);
            receta.setIndicaciones(indicaciones);

            RecetaMedicaDao dao = new RecetaMedicaDao();
            boolean ok = dao.insertarReceta(receta);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Receta guardada correctamente ✅");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar la receta ❌");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
