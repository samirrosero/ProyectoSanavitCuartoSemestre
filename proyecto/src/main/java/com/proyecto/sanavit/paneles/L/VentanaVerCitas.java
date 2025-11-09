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
    private final CitaDao citaDAO = new CitaDao();

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

        // === Tabla de citas ===
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
        btnCerrar.setBackground(new Color(231, 76, 60));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnCerrar.addActionListener(e -> dispose());

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);
        add(panelBoton, BorderLayout.SOUTH);

        // === Cargar las citas del paciente ===
        cargarCitas(pacienteActual.getIdPaciente());

        setVisible(true);
    }

    // ===============================================================
    // Método para cargar las citas del paciente actual
    // ===============================================================
    private void cargarCitas(int idPaciente) {
        try {
            // ✅ Se obtienen las citas asociadas a ese paciente (no al médico)
            List<Cita> citas = citaDAO.obtenerCitaPorPaciente(idPaciente);
            modeloTabla.setRowCount(0); // Limpiar la tabla

            for (Cita c : citas) {
                // Obtener los datos del médico
                Medico medico = new MedicoDao().obtenerMedicoPorId(c.getIdMedico());
                String nombreMedico = (medico != null) ? medico.getNombre() : "Desconocido";
                String especialidad = (medico != null) ? medico.getEspecialidad() : "N/A";

                // Modalidad (1 = Presencial, 2 = Virtual)
                String modalidad = (c.getIdModalidad() == 1) ? "Presencial" : "Virtual";

                // ✅ Nombre real del estado de la cita
                String estado = citaDAO.obtenerNombreEstado(c.getIdEstadoCita());

                // Agregar fila a la tabla
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
