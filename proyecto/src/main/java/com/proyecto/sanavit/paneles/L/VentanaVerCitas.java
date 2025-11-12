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
    private JButton btnCerrar, btnCancelar;
    private final CitaDao citaDAO = new CitaDao();
    private final Paciente pacienteActual; // 🔹 Guardamos el paciente actual

    public VentanaVerCitas(Paciente pacienteActual) {
        this.pacienteActual = pacienteActual; // lo guardamos para usar después

        setTitle("Mis Citas - Sanavit");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(244, 247, 250));

        // === ENCABEZADO ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(65, 158, 91));
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel lblTitulo = new JLabel("Citas del Paciente: " + pacienteActual.getNombre(), JLabel.LEFT);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        header.add(lblTitulo, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // === PANEL CENTRAL ===
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(new Color(198, 232, 197));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        modeloTabla = new DefaultTableModel(new String[]{
            "ID", "Fecha", "Hora", "Médico", "Especialidad", "Modalidad", "Estado"
        }, 0);

        tablaCitas = new JTable(modeloTabla);
        tablaCitas.setFillsViewportHeight(true);
        tablaCitas.setRowHeight(30);
        tablaCitas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaCitas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaCitas.getTableHeader().setBackground(new Color(153, 210, 185));
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

        // === PANEL DE BOTONES ===
        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(234, 250, 241));

        // Botón Cancelar Cita
        btnCancelar = new JButton("Cancelar Cita");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setBackground(new Color(66, 166, 105));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        btnCancelar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCancelar.setBackground(new Color(66, 166, 105));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnCancelar.setBackground(new Color(66, 166, 105));
            }
        });

        btnCancelar.addActionListener(e -> cancelarCita());

        // Botón Cerrar
        btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setBackground(new Color(231, 76, 60));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        btnCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCerrar.setBackground(new Color(192, 57, 43));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnCerrar.setBackground(new Color(231, 76, 60));
            }
        });

        btnCerrar.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                null,
                "✔ Tu cita fue agendada exitosamente.\nRecuerda estar 30 minutos antes.",
                "Cita Agendada",
                JOptionPane.INFORMATION_MESSAGE
            );
            dispose();
        });

        panelBoton.add(btnCancelar);
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
                    c.getIdCita(),
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

    // ===============================================================
    // Método para cancelar una cita seleccionada
    // ===============================================================
    private void cancelarCita() {
        int fila = tablaCitas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "⚠️ Selecciona una cita para cancelar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idCita = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        String estado = modeloTabla.getValueAt(fila, 6).toString();

        if (estado.equalsIgnoreCase("Cancelada")) {
            JOptionPane.showMessageDialog(this,
                    "La cita ya está cancelada.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas cancelar esta cita?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                citaDAO.actualizarEstadoCita(idCita, 3); // 3 = Cancelada
                JOptionPane.showMessageDialog(this,
                        "✅ Cita cancelada exitosamente.",
                        "Cita Cancelada", JOptionPane.INFORMATION_MESSAGE);
                cargarCitas(pacienteActual.getIdPaciente()); // 🔹 recarga usando el mismo paciente
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error al cancelar la cita: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
