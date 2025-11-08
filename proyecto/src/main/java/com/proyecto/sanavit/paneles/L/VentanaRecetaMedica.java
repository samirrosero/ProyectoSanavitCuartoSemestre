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
        setSize(450, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Medicamento:"));
        txtMedicamento = new JTextField();
        add(txtMedicamento);

        add(new JLabel("Indicaciones:"));
        txtIndicaciones = new JTextArea();
        add(new JScrollPane(txtIndicaciones));

        btnGuardar = new JButton("Guardar Receta");
        add(new JLabel());
        add(btnGuardar);

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
