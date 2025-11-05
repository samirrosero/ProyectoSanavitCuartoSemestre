package com.proyecto.sanavit.paneles.L;
import com.proyecto.sanavit.modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class VentanaMedico extends JFrame {

    private JLabel lblNombre, lblEspecialidad;
    private JButton btnNuevaHistoria;
    private JTable tablaCita;
    private DefaultTableModel modeloTabla;

    public VentanaMedico(Usuario usuarioActual) {
        setTitle("Panel del Médico");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === DATOS DEL MÉDICO ===
        MedicoDao medicoDAO = new MedicoDao();
        Medico medicoActual = medicoDAO.obtenerMedicoPorIdUsuario(usuarioActual.getIdUsuario());

        // === ENCABEZADO (logo + bienvenida) ===
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(123, 229, 144));

        JLabel lblBienvenida = new JLabel("Bienvenido (a) " + (medicoActual != null ? medicoActual.getNombre() : ""));
        lblBienvenida.setForeground(Color.BLACK);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 16));
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0));
        headerPanel.add(lblBienvenida, BorderLayout.WEST);

        ImageIcon logoIcon = new ImageIcon("C:\\Users\\samir\\OneDrive\\Escritorio\\OneDrive\\Documentos\\prototipo\\logo2.png");
        Image imagenEscalada = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(imagenEscalada));
        lblLogo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 15));
        headerPanel.add(lblLogo, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // === PANEL CENTRAL: tabla de Cita ===
        modeloTabla = new DefaultTableModel(new String[] { "id_cita", "id_Paciente", "fecha_cita", "hora_cita", "modalidad" }, 0);
        tablaCita = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaCita);
        add(scroll, BorderLayout.CENTER);

        // === PANEL INFERIOR ===
        JPanel panelInferior = new JPanel();
        btnNuevaHistoria = new JButton("Crear Historia Clínica");
        panelInferior.add(btnNuevaHistoria);
        add(panelInferior, BorderLayout.SOUTH);

        // === PANEL IZQUIERDO (nombre y especialidad) ===
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(235, 255, 235));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblNombre = new JLabel("Nombre: " + (medicoActual != null ? medicoActual.getNombre() : "No encontrado"));
        lblEspecialidad = new JLabel("Especialidad: " + (medicoActual != null ? medicoActual.getEspecialidad() : "No encontrada"));

        lblNombre.setFont(new Font("Arial", Font.BOLD, 18));
        lblEspecialidad.setFont(new Font("Arial", Font.PLAIN, 16));

        panel.add(lblNombre);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblEspecialidad);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        add(panel, BorderLayout.WEST);

         // === LLENAR TABLA CON Cita DEL MÉDICO ===
        CitaDao CitaDao = new CitaDao();
        PacienteDao PacienteDAO = new PacienteDao();
        List<Cita> CitaDelMedico = CitaDao.obtenerCitaPorMedico(medicoActual.getIdMedico());

        for (Cita c : CitaDelMedico) {
            Paciente p = PacienteDAO.obtenerPacientePorId(c.getIdPaciente());
            modeloTabla.addRow(new Object[] {
                c.getIdCita(),
                p != null ? p.getNombre() : "Desconocido",
                c.getFechaCita().toString(),
                c.getHoraCita().toString(),
                c.getIdModalidad()
            });
        }

        //  === BOTÓN HISTORIA CLÍNICA ===
       /*  btnNuevaHistoria.addActionListener(e -> {
            int filaSeleccionada = tablaCita.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una cita de la tabla.");
                return;
            }

            int id_cita = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            Cita Citaeleccionada = null;
            Paciente PacienteSeleccionado = null;

            for (Cita c : CitaDelMedico) {
                if (c.getIdCita() == id_cita) {
                    Citaeleccionada = c;
                    PacienteSeleccionado = PacienteDAO.obtenerPacientePorId(c.getIdPaciente());
                    break;
                }
            }

            if (Citaeleccionada != null && PacienteSeleccionado != null) {
               // new VentanaHistoriaClinica(PacienteSeleccionado, Citaeleccionada);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo obtener la información del Paciente.");
            }
        });*/

        setVisible(true);
        System.out.println("ID del médico actual: " + medicoActual.getIdMedico());

    }
}
    