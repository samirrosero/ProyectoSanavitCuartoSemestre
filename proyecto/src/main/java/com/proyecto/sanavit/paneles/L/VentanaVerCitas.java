package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class VentanaVerCitas extends JFrame {

    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;
    private JButton btnCerrar;
    private final CitaDao citaDAO = new CitaDao();

    public VentanaVerCitas(Paciente pacienteActual) {
        setTitle("Mis Citas - Sanavit");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(234, 250, 241)); // Fondo verde muy suave

        // === ENCABEZADO ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(65, 158, 91)); // Verde Sanavit
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel lblTitulo = new JLabel("Citas del Paciente: " + pacienteActual.getNombre(), JLabel.LEFT);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        header.add(lblTitulo, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // === PANEL CENTRAL ===
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(198, 232, 197)); // Verde claro
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // === TABLA ===
        modeloTabla = new DefaultTableModel(new String[]{
            "Fecha", "Hora", "Médico", "Especialidad", "Modalidad", "Estado"
        }, 0);

        tablaCitas = new JTable(modeloTabla);
        tablaCitas.setFillsViewportHeight(true);
        tablaCitas.setRowHeight(30);
        tablaCitas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaCitas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaCitas.getTableHeader().setBackground(new Color(153, 210, 185)); // Verde pastel
        tablaCitas.getTableHeader().setForeground(Color.BLACK);
        tablaCitas.setGridColor(new Color(210, 230, 210));

        JScrollPane scroll = new JScrollPane(tablaCitas);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 220, 190), 2),
                "Listado de Citas",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(30, 60, 30)
        ));
        panelCentral.add(scroll, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        // === BOTÓN CERRAR ===
        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(234, 250, 241));

        btnCerrar = new JButton("Cerrar");
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

        // === CARGAR CITAS ===
        cargarCitas(pacienteActual.getIdPaciente());

        setVisible(true);
    }

    // ===============================================================
    // Método para cargar las citas del paciente actual
    // ===============================================================
    private void cargarCitas(int idPaciente) {
        try {
            List<Cita> citas = citaDAO.obtenerCitaPorPaciente(idPaciente);
            modeloTabla.setRowCount(0);

            for (Cita c : citas) {
                Medico medico = new MedicoDao().obtenerMedicoPorId(c.getIdMedico());
                String nombreMedico = (medico != null) ? medico.getNombre() : "Desconocido";
                String especialidad = (medico != null) ? medico.getEspecialidad() : "N/A";
                String modalidad = (c.getIdModalidad() == 1) ? "Presencial" : "Virtual";
                String estado = citaDAO.obtenerNombreEstado(c.getIdEstadoCita());

                modeloTabla.addRow(new Object[]{
                    c.getFechaCita(),
                    c.getHoraCita(),
                    nombreMedico,
                    especialidad,
                    modalidad,
                    estado
                });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al cargar citas: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
