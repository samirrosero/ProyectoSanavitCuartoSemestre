package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaVerHistoriaClinica extends JFrame {

    public VentanaVerHistoriaClinica(Paciente paciente) {
        setTitle("Historia Clínica del Paciente");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // === TÍTULO ===
        JLabel lblTitulo = new JLabel("Historia Clínica de " + paciente.getNombre(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // === OBTENER HISTORIAS CLÍNICAS DEL PACIENTE ===
        HistoriaClinicaDao dao = new HistoriaClinicaDao();
        List<HistoriaClinica> lista = dao.obtenerPorPaciente(paciente.getIdPaciente());

        // === TABLA DE HISTORIAS CLÍNICAS ===
        String[] columnas = {"ID", "Motivo", "Diagnóstico", "Tratamiento", "Evolución"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (HistoriaClinica h : lista) {
            modelo.addRow(new Object[]{
                h.getIdHistoriaClinica(),
                h.getMotivoConsulta(),
                h.getDiagnostico(),
                h.getTratamiento(),
                h.getevolucion()
            });
        }

        JTable tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // === BOTÓN CERRAR ===
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);
        add(panelBoton, BorderLayout.SOUTH);

        setVisible(true);
    }
}
