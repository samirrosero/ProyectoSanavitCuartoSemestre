package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class VentanaRecetaMedica {

    private JFrame frame;
    private JTextField txtIdHistoriaClinica;
    private JTextField txtMedicamento;
    private JTextField txtIndicaciones;
    private JButton btnGuardar;

    public VentanaRecetaMedica() {
        frame = new JFrame("Registro de Receta Médica");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(5, 2));

        frame.add(new JLabel("ID Historia Clínica:"));
        txtIdHistoriaClinica = new JTextField();
        frame.add(txtIdHistoriaClinica);

        frame.add(new JLabel("Medicamento:"));
        txtMedicamento = new JTextField();
        frame.add(txtMedicamento);

        frame.add(new JLabel("Indicaciones:"));
        txtIndicaciones = new JTextField();
        frame.add(txtIndicaciones);

        btnGuardar = new JButton("Guardar");
        frame.add(btnGuardar);

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarRecetaMedica();
            }
        });

        frame.setVisible(true);
    }

    private void guardarRecetaMedica() {
        try {
            int idHistoriaClinica = Integer.parseInt(txtIdHistoriaClinica.getText());
            String medicamento = txtMedicamento.getText();
            String indicaciones = txtIndicaciones.getText();

            RecetaMedica receta = new RecetaMedica(0, idHistoriaClinica, medicamento, indicaciones);
            RecetaMedicaDao recetaDao = new RecetaMedicaDao();

            if (recetaDao.insertarReceta(receta)) {
                JOptionPane.showMessageDialog(frame, "Receta médica guardada con éxito.");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Error al guardar la receta médica.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "ID Historia Clínica debe ser un número válido.");
        }
    }

    public static void main(String[] args) {
        new VentanaRecetaMedica();
    }
   

   
    
}
