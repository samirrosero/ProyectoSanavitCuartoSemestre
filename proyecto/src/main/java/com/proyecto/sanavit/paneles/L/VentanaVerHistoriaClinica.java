package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaVerHistoriaClinica extends JFrame {

    public VentanaVerHistoriaClinica(Paciente paciente) {
        setTitle("Historias Clínicas de " + paciente.getNombre());
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel("📋 Historial Médico del Paciente", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitulo, BorderLayout.NORTH);

        // === OBTENER HISTORIAS ===
        HistoriaClinicaDao dao = new HistoriaClinicaDao();
        List<HistoriaClinica> lista = dao.obtenerPorPaciente(paciente.getIdPaciente());

        String[] columnas = {"ID", "Motivo de Consulta", "Diagnóstico", "Tratamiento", "Médico"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        MedicoDao medicoDao = new MedicoDao();

        for (HistoriaClinica h : lista) {
            // Buscar el médico relacionado a esta historia
            String nombreMedico = "Desconocido";
            try {
                EjecucionCitaDao ejecDao = new EjecucionCitaDao();
                EjecucionCita ejec = ejecDao.obtenerPorId(h.getIdEjecucionCita());
                if (ejec != null) {
                    CitaDao citaDao = new CitaDao();
                    Cita cita = citaDao.obtenerCitaPorId(ejec.getIdCita());
                    if (cita != null) {
                        Medico m = medicoDao.obtenerMedicoPorId(cita.getIdMedico());
                        if (m != null) nombreMedico = m.getNombre();
                    }
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error obteniendo médico: " + e.getMessage());
            }

            modelo.addRow(new Object[]{
                h.getIdHistoriaClinica(),
                h.getMotivoConsulta(),
                h.getDiagnostico(),
                h.getTratamiento(),
                nombreMedico
            });
        }

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // === DOBLE CLICK EN FILA PARA ABRIR DETALLE ===
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && tabla.getSelectedRow() != -1) {
                    int fila = tabla.getSelectedRow();
                    int idHistoria = (int) modelo.getValueAt(fila, 0);
                    HistoriaClinica historia = dao.obtenerPorId(idHistoria);
                    new VentanaDetalleHistoria(historia, paciente);
                }
            }
        });

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);
        add(panelBoton, BorderLayout.SOUTH);

        setVisible(true);
    }
}
