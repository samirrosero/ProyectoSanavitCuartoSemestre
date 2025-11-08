package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;

public class VentanaHistoriaClinica extends JFrame {

    private JTextField txtMotivo, txtDiagnostico, txtTratamiento, txtEvolucion;
    private JTextArea txtEnfermedadActual, txtAntecedentes, txtObservaciones;
    private JButton btnGuardar, btnVerReceta, btnCancelar;

    private Paciente paciente;
    private Cita cita;
    private EjecucionCita ejecucion;
    private HistoriaClinica historiaGuardada;

    public VentanaHistoriaClinica(Paciente paciente, Cita cita, EjecucionCita ejecucion) {
        this.paciente = paciente;
        this.cita = cita;
        this.ejecucion = ejecucion;

        setTitle("Historia Clínica del Paciente: " + paciente.getNombre());
        setSize(750, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // === PANEL SUPERIOR ===
        JLabel lblTitulo = new JLabel("Registro de Historia Clínica", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // === PANEL CENTRAL ===
        JPanel panelCampos = new JPanel(new GridLayout(8, 2, 10, 10));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        panelCampos.add(new JLabel("Motivo de Consulta:"));
        txtMotivo = new JTextField();
        panelCampos.add(txtMotivo);

        panelCampos.add(new JLabel("Enfermedad Actual:"));
        txtEnfermedadActual = new JTextArea(2, 20);
        panelCampos.add(new JScrollPane(txtEnfermedadActual));

        panelCampos.add(new JLabel("Antecedentes:"));
        txtAntecedentes = new JTextArea(2, 20);
        panelCampos.add(new JScrollPane(txtAntecedentes));

        panelCampos.add(new JLabel("Diagnóstico:"));
        txtDiagnostico = new JTextField();
        panelCampos.add(txtDiagnostico);

        panelCampos.add(new JLabel("Tratamiento:"));
        txtTratamiento = new JTextField();
        panelCampos.add(txtTratamiento);

        panelCampos.add(new JLabel("Evolución:"));
        txtEvolucion = new JTextField();
        panelCampos.add(txtEvolucion);

        panelCampos.add(new JLabel("Observaciones:"));
        txtObservaciones = new JTextArea(2, 20);
        panelCampos.add(new JScrollPane(txtObservaciones));

        add(panelCampos, BorderLayout.CENTER);

        // === PANEL DE BOTONES ===
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnGuardar = new JButton("Guardar Historia");
        btnVerReceta = new JButton("Crear Receta Médica");
        btnCancelar = new JButton("Cancelar");

        btnVerReceta.setEnabled(false);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnVerReceta);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        // === ACCIÓN GUARDAR HISTORIA ===
        btnGuardar.addActionListener(e -> guardarHistoriaClinica());

        // === ACCIÓN VER / CREAR RECETA MÉDICA ===
        btnVerReceta.addActionListener(e -> {
            if (historiaGuardada != null) {
                new VentanaRecetaMedica(historiaGuardada);
            } else {
                JOptionPane.showMessageDialog(this, "Primero debe guardar la historia clínica.");
            }
        });

        // === ACCIÓN CANCELAR ===
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void guardarHistoriaClinica() {
        try {
            HistoriaClinica hc = new HistoriaClinica();
            hc.setIdEjecucionCita(ejecucion.getIdEjecucionCita());
            hc.setMotivoConsulta(txtMotivo.getText());
            hc.setEnfermedadActual(txtEnfermedadActual.getText());
            hc.setAntecedentes(txtAntecedentes.getText());
            hc.setDiagnostico(txtDiagnostico.getText());
            hc.setTratamiento(txtTratamiento.getText());
            hc.setEvolucion(txtEvolucion.getText());
            hc.setObservaciones(txtObservaciones.getText());

            HistoriaClinicaDao dao = new HistoriaClinicaDao();
            boolean exito = dao.insertarHistoria(hc);

            if (exito) {
                historiaGuardada = hc;
                JOptionPane.showMessageDialog(this, "Historia clínica guardada correctamente ✅");
                btnVerReceta.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar la historia clínica ❌");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage());
        }
    }
}
