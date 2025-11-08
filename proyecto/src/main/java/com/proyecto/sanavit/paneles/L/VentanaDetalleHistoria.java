package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaDetalleHistoria extends JFrame {

    public VentanaDetalleHistoria(HistoriaClinica historia, Paciente paciente) {
        setTitle("Detalle de Historia Clínica #" + historia.getIdHistoriaClinica());
        setSize(750, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel("🩺 Historia Clínica - " + paciente.getNombre(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Datos del paciente
        panel.add(crearLabel("👤 Nombre: " + paciente.getNombre(), true));
        panel.add(crearLabel("📧 Correo: " + paciente.getCorreo(), false));
        panel.add(crearLabel("📞 Teléfono: " + paciente.getTelefono(), false));
        panel.add(crearLabel("🎂 Edad: " + paciente.getEdad(), false));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Médico tratante
        String medicoNombre = "Desconocido";
        try {
            EjecucionCitaDao ejecDao = new EjecucionCitaDao();
            EjecucionCita ejec = ejecDao.obtenerPorId(historia.getIdEjecucionCita());
            if (ejec != null) {
                CitaDao citaDao = new CitaDao();
                Cita cita = citaDao.obtenerCitaPorId(ejec.getIdCita());
                if (cita != null) {
                    MedicoDao medicoDao = new MedicoDao();
                    Medico m = medicoDao.obtenerMedicoPorId(cita.getIdMedico());
                    if (m != null) medicoNombre = m.getNombre();
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error obteniendo médico: " + e.getMessage());
        }

        panel.add(crearSeccion("👨‍⚕️ Médico tratante: " + medicoNombre));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Datos de la historia
        panel.add(crearLabel("Motivo de consulta:", true));
        panel.add(crearLabel(historia.getMotivoConsulta(), false));
        panel.add(crearLabel("Enfermedad actual:", true));
        panel.add(crearLabel(historia.getEnfermedadActual(), false));
        panel.add(crearLabel("Antecedentes:", true));
        panel.add(crearLabel(historia.getAntecedentes(), false));
        panel.add(crearLabel("Diagnóstico:", true));
        panel.add(crearLabel(historia.getDiagnostico(), false));
        panel.add(crearLabel("Tratamiento:", true));
        panel.add(crearLabel(historia.getTratamiento(), false));
        panel.add(crearLabel("Evolución:", true));
        panel.add(crearLabel(historia.getevolucion(), false));
        panel.add(crearLabel("Observaciones:", true));
        panel.add(crearLabel(historia.getObservaciones(), false));

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Receta médica
        RecetaMedicaDao recetaDao = new RecetaMedicaDao();
        List<RecetaMedica> recetas = recetaDao.listarRecetas();

        boolean encontrada = false;
        for (RecetaMedica r : recetas) {
            if (r.getIdHistoriaClinica() == historia.getIdHistoriaClinica()) {
                panel.add(crearSeccion("💊 Receta Médica"));
                panel.add(crearLabel("Medicamento: " + r.getMedicamento(), false));
                panel.add(crearLabel("Indicaciones: " + r.getIndicaciones(), false));
                encontrada = true;
            }
        }
        if (!encontrada) {
            panel.add(crearLabel("Sin receta asociada.", false));
        }

        JScrollPane scroll = new JScrollPane(panel);
        add(scroll, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnCerrar);
        add(panelBoton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JLabel crearLabel(String texto, boolean titulo) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", titulo ? Font.BOLD : Font.PLAIN, titulo ? 15 : 14));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        return lbl;
    }

    private JLabel crearSeccion(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        lbl.setForeground(new Color(0, 70, 140));
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
}
