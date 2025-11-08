package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class VentanaMedico extends JFrame {

    private JLabel lblNombre, lblEspecialidad;
    private JButton btnIniciarAtencion, btnFinalizarAtencion, btnCerrarSesion;
    private JTable tablaCita;
    private DefaultTableModel modeloTabla;

    private Medico medicoActual;
    private EjecucionCita ejecucionActual;
    private LocalDateTime horaInicio;

    public VentanaMedico(Usuario usuarioActual) {
        setTitle("Panel del Médico");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // === DATOS DEL MÉDICO ===
        MedicoDao medicoDAO = new MedicoDao();
        medicoActual = medicoDAO.obtenerMedicoPorIdUsuario(usuarioActual.getIdUsuario());

        // === ENCABEZADO ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(180, 240, 180));
        JLabel lblBienvenida = new JLabel("Bienvenido(a): " + medicoActual.getNombre(), SwingConstants.LEFT);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 16));
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0));
        header.add(lblBienvenida, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // === PANEL IZQUIERDO ===
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.setBackground(new Color(235, 255, 235));

        lblNombre = new JLabel("Nombre: " + medicoActual.getNombre());
        lblEspecialidad = new JLabel("Especialidad: " + medicoActual.getEspecialidad());

        lblNombre.setFont(new Font("Arial", Font.BOLD, 16));
        lblEspecialidad.setFont(new Font("Arial", Font.PLAIN, 15));

        infoPanel.add(lblNombre);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(lblEspecialidad);

        add(infoPanel, BorderLayout.WEST);

        // === TABLA DE CITAS ===
        modeloTabla = new DefaultTableModel(new String[]{"ID Cita", "Paciente", "Fecha", "Hora", "Modalidad"}, 0);
        tablaCita = new JTable(modeloTabla);
        add(new JScrollPane(tablaCita), BorderLayout.CENTER);

        // === BOTONES ===
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnIniciarAtencion = new JButton("Iniciar Atención");
        btnFinalizarAtencion = new JButton("Finalizar Atención");
        btnCerrarSesion = new JButton("Cerrar Sesión");

        btnFinalizarAtencion.setEnabled(false);
        panelBotones.add(btnIniciarAtencion);
        panelBotones.add(btnFinalizarAtencion);
        panelBotones.add(btnCerrarSesion);
        add(panelBotones, BorderLayout.SOUTH);

        // === CARGAR CITAS DEL MÉDICO ===
        cargarCitasDelMedico();

        // === EVENTOS ===
        btnIniciarAtencion.addActionListener(e -> iniciarAtencion());
        btnFinalizarAtencion.addActionListener(e -> finalizarAtencion());
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new Login();
        });

        setVisible(true);
    }

    private void cargarCitasDelMedico() {
        modeloTabla.setRowCount(0);
        CitaDao citaDao = new CitaDao();
        PacienteDao pacienteDao = new PacienteDao();
        List<Cita> citas = citaDao.obtenerCitaPorMedico(medicoActual.getIdMedico());

        for (Cita c : citas) {
            Paciente p = pacienteDao.obtenerPacientePorId(c.getIdPaciente());
            modeloTabla.addRow(new Object[]{
                    c.getIdCita(),
                    (p != null ? p.getNombre() : "Desconocido"),
                    c.getFechaCita(),
                    c.getHoraCita(),
                    c.getIdModalidad() == 1 ? "Presencial" : "Virtual"
            });
        }
    }

    // === INICIAR ATENCIÓN ===
    private void iniciarAtencion() {
        int fila = tablaCita.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita para iniciar atención.");
            return;
        }

        int idCita = (int) modeloTabla.getValueAt(fila, 0);
        CitaDao citaDao = new CitaDao();
        Cita cita = citaDao.obtenerCitaPorId(idCita);

        horaInicio = LocalDateTime.now();

        EjecucionCita ejec = new EjecucionCita();
        ejec.setIdCita(idCita);
        ejec.setFechaHoraIngreso(Timestamp.valueOf(horaInicio));
        ejec.setFechaHoraSalida(null);
        ejec.setDuracion(0);

        EjecucionCitaDao ejecDao = new EjecucionCitaDao();
        boolean ok = ejecDao.insertarEjecucion(ejec);

        if (ok) {
            ejecucionActual = ejec;
            JOptionPane.showMessageDialog(this, "Atención iniciada correctamente.");
            btnIniciarAtencion.setEnabled(false);
            btnFinalizarAtencion.setEnabled(true);

            // Obtener el paciente y abrir ventana de historia clínica
            PacienteDao pacienteDao = new PacienteDao();
            Paciente paciente = pacienteDao.obtenerPacientePorId(cita.getIdPaciente());
            new VentanaHistoriaClinica(paciente, cita, ejecucionActual);

        } else {
            JOptionPane.showMessageDialog(this, "Error al iniciar atención.");
        }
    }

    // === FINALIZAR ATENCIÓN ===
    private void finalizarAtencion() {
        if (ejecucionActual == null) {
            JOptionPane.showMessageDialog(this, "No hay una atención activa.");
            return;
        }

        LocalDateTime horaFin = LocalDateTime.now();
        int duracionMinutos = (int) Duration.between(horaInicio, horaFin).toMinutes();

        ejecucionActual.setFechaHoraSalida(Timestamp.valueOf(horaFin));
        ejecucionActual.setDuracion(duracionMinutos);

        EjecucionCitaDao ejecDao = new EjecucionCitaDao();
        boolean ok = ejecDao.updateEjecucion(ejecucionActual);

        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Atención finalizada.\nDuración: " + duracionMinutos + " minutos.");
            btnIniciarAtencion.setEnabled(true);
            btnFinalizarAtencion.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Error al finalizar atención.");
        }
    }
}
