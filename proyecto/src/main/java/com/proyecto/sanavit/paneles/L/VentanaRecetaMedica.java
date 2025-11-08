package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class VentanaRecetaMedica {

    private JFrame frame;
    private JTextField txtMedicamento;
    private JTextArea txtIndicaciones;
    private JButton btnGuardar;
    private int idHistoriaClinica;

    // Recibe el id de historia clínica al crear la ventana
    public VentanaRecetaMedica(int idHistoriaClinica) {
        this.idHistoriaClinica = idHistoriaClinica;

        frame = new JFrame("Registro de Receta Médica");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 2, 10, 10));

        frame.add(new JLabel("Medicamento:"));
        txtMedicamento = new JTextField();
        frame.add(txtMedicamento);

        frame.add(new JLabel("Indicaciones:"));
        txtIndicaciones = new JTextArea(3, 20);
        frame.add(new JScrollPane(txtIndicaciones));

        btnGuardar = new JButton("Guardar");
        frame.add(btnGuardar);

        btnGuardar.addActionListener(e -> guardarRecetaMedica());

        frame.setVisible(true);
    }

    private void guardarRecetaMedica() {
        try {
            String medicamento = txtMedicamento.getText();
            String indicaciones = txtIndicaciones.getText();

            RecetaMedica receta = new RecetaMedica(0, idHistoriaClinica, medicamento, indicaciones);
            RecetaMedicaDao recetaDao = new RecetaMedicaDao();

            if (recetaDao.insertarReceta(receta)) {
                JOptionPane.showMessageDialog(frame, "Receta guardada con éxito ✅\nID generado: " + receta.getIdReceta());
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Error al guardar la receta ❌");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }
}