package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class VentanaVerHistoriaClinica extends JFrame {

    public VentanaVerHistoriaClinica(Paciente paciente) {
        setTitle("Historias Clínicas de " + paciente.getNombre());
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(234, 250, 241)); // Fondo general

        // === ENCABEZADO ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(65, 158, 91)); // Verde institucional Sanavit
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel lblTitulo = new JLabel("Historial Médico del Paciente: " + paciente.getNombre());
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        header.add(lblTitulo, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // === PANEL CENTRAL ===
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(198, 232, 197));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

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
                System.out.println("Error obteniendo médico: " + e.getMessage());
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
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(new Color(153, 210, 185));
        tabla.getTableHeader().setForeground(Color.BLACK);
        tabla.setGridColor(new Color(210, 230, 210));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 220, 190), 2),
                "Listado de Historias Clínicas",
                0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(30, 60, 30)
        ));

        panelCentral.add(scroll, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        // === DOBLE CLICK EN FILA PARA ABRIR DETALLE ===
        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && tabla.getSelectedRow() != -1) {
                    int fila = tabla.getSelectedRow();
                    int idHistoria = (int) modelo.getValueAt(fila, 0);
                    HistoriaClinica historia = dao.obtenerPorId(idHistoria);
                    new VentanaDetalleHistoria(historia, paciente);
                }
            }
        });

        // === BOTÓN CERRAR ===
        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(234, 250, 241));

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setBackground(new Color(231, 76, 60));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // 🔹 Efecto hover
        btnCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCerrar.setBackground(new Color(192, 57, 43));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnCerrar.setBackground(new Color(41, 110, 29));
            }
        });

        btnCerrar.addActionListener(e -> dispose());
        panelBoton.add(btnCerrar);
        add(panelBoton, BorderLayout.SOUTH);

        setVisible(true);
    }
}
