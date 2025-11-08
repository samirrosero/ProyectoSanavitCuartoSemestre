package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaHistoriaClinica extends JFrame {

    private JTextField txtMotivoConsulta;
    private JTextArea txtEnfermedadActual, txtAntecedentes, txtDiagnostico, txtTratamiento, txtEvolucion, txtObservaciones;
    private JButton btnGuardar, btnVerReceta, btnCerrar;
    private Paciente paciente;
    private Cita cita;

    private HistoriaClinica historiaGuardada; // Se usará para pasar su ID a la receta

    public VentanaHistoriaClinica(Paciente paciente, Cita cita) {
        this.paciente = paciente;
        this.cita = cita;

        setTitle("Historia Clínica - Paciente: " + paciente.getNombre());
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // === PANEL DE CAMPOS ===
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new GridLayout(7, 2, 10, 10));
        panelCampos.setBorder(BorderFactory.createTitledBorder("Datos de la Historia Clínica"));

        txtMotivoConsulta = new JTextField();
        txtEnfermedadActual = new JTextArea(3, 20);
        txtAntecedentes = new JTextArea(3, 20);
        txtDiagnostico = new JTextArea(3, 20);
        txtTratamiento = new JTextArea(3, 20);
        txtEvolucion = new JTextArea(3, 20);
        txtObservaciones = new JTextArea(3, 20);

        panelCampos.add(new JLabel("Motivo de consulta:"));
        panelCampos.add(txtMotivoConsulta);
        panelCampos.add(new JLabel("Enfermedad actual:"));
        panelCampos.add(new JScrollPane(txtEnfermedadActual));
        panelCampos.add(new JLabel("Antecedentes:"));
        panelCampos.add(new JScrollPane(txtAntecedentes));
        panelCampos.add(new JLabel("Diagnóstico:"));
        panelCampos.add(new JScrollPane(txtDiagnostico));
        panelCampos.add(new JLabel("Tratamiento:"));
        panelCampos.add(new JScrollPane(txtTratamiento));
        panelCampos.add(new JLabel("Evolución:"));
        panelCampos.add(new JScrollPane(txtEvolucion));
        panelCampos.add(new JLabel("Observaciones:"));
        panelCampos.add(new JScrollPane(txtObservaciones));

        add(panelCampos, BorderLayout.CENTER);

        // === BOTONES ===
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnGuardar = new JButton("Guardar Historia Clínica");
        btnVerReceta = new JButton("Crear Receta Médica");
        btnVerReceta.setEnabled(false); // Se activa solo tras guardar
        btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnVerReceta);
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);

        // === EVENTO GUARDAR ===
        btnGuardar.addActionListener(e -> guardarHistoriaClinica());
        btnVerReceta.addActionListener(e -> abrirReceta());
        btnCerrar.addActionListener(e -> dispose());

        setVisible(true);
    }

    // ======= MÉTODO GUARDAR HISTORIA CLÍNICA =======
    private void guardarHistoriaClinica() {
        try {
            String motivo = txtMotivoConsulta.getText().trim();
            String enfermedad = txtEnfermedadActual.getText().trim();
            String antecedentes = txtAntecedentes.getText().trim();
            String diagnostico = txtDiagnostico.getText().trim();
            String tratamiento = txtTratamiento.getText().trim();
            String evolucion = txtEvolucion.getText().trim();
            String observaciones = txtObservaciones.getText().trim();

            if (motivo.isEmpty() || diagnostico.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Motivo y diagnóstico son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            HistoriaClinica hc = new HistoriaClinica(ALLBITS, ABORT, motivo, enfermedad, antecedentes, diagnostico, tratamiento, evolucion, observaciones);
            hc.setIdEjecucionCita(cita.getIdCita()); // Enlazamos con la cita
            hc.setMotivoConsulta(motivo);
            hc.setEnfermedadActual(enfermedad);
            hc.setAntecedentes(antecedentes);
            hc.setDiagnostico(diagnostico);
            hc.setTratamiento(tratamiento);
            hc.setevolucion(evolucion);
            hc.setObservaciones(observaciones);

            HistoriaClinicaDao dao = new HistoriaClinicaDao();

            boolean existeidGenerado = dao.insertarHistoria(hc);
            if (existeidGenerado) {
                String idGenerado = String.valueOf(hc.getIdHistoriaClinica());
                historiaGuardada = hc;
                JOptionPane.showMessageDialog(this, "Historia clínica guardada con éxito ✅\nID generado: " + idGenerado);
                btnVerReceta.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar historia clínica ❌");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ======= MÉTODO ABRIR RECETA MÉDICA =======
    private void abrirReceta() {
        if (historiaGuardada != null) {
            new VentanaRecetaMedica(historiaGuardada.getIdHistoriaClinica());
        } else {
            JOptionPane.showMessageDialog(this, "Primero guarda la historia clínica.");
        }
    }
}
