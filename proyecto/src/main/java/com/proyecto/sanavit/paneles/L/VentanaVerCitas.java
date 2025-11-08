package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaVerCitas extends JFrame {

    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;
    private JButton btnCerrar;
    private CitaDao citaDAO = new CitaDao();

    public VentanaVerCitas(Paciente pacienteActual) {
        setTitle("Mis Citas - Sanavit");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // === Encabezado ===
        JLabel lblTitulo = new JLabel("Citas del Paciente: " + pacienteActual.getNombre(), JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(110, 180, 255));
        lblTitulo.setForeground(Color.BLACK);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // === Tabla ===
        modeloTabla = new DefaultTableModel(new String[]{
            "Fecha", "Hora", "Médico", "Especialidad", "Modalidad", "Estado"
        }, 0);
        tablaCitas = new JTable(modeloTabla);
        tablaCitas.setFillsViewportHeight(true);
        tablaCitas.setRowHeight(28);
        tablaCitas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(new JScrollPane(tablaCitas), BorderLayout.CENTER);

        // === Botón de cierre ===
        btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);
        add(panelBoton, BorderLayout.SOUTH);

        // === Cargar citas ===
        cargarCitas(pacienteActual.getIdPaciente());

        setVisible(true);
    }

    private void cargarCitas(int idPaciente) {
        try {
            List<Cita> citas = citaDAO.obtenerCitaPorMedico(idPaciente);
            modeloTabla.setRowCount(0);

            for (Cita c : citas) {
                Medico medico = new MedicoDao().obtenerMedicoPorId(c.getIdMedico());
                String nombreMedico = (medico != null) ? medico.getNombre() : "Desconocido";
                String especialidad = (medico != null) ? medico.getEspecialidad() : "N/A";
                String modalidad = (c.getIdModalidad() == 1) ? "Presencial" : "Virtual";
                String estado = "Agendada"; // o puedes obtenerlo con JOIN si tienes el estado en texto

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
            JOptionPane.showMessageDialog(this, "Error al cargar citas: " + ex.getMessage());
        }
    }
}
