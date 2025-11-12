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

        setTitle("Historia Clínica - " + paciente.getNombre());
        setSize(780, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(244, 247, 250)); 

        // === PANEL SUPERIOR ===
        JLabel lblTitulo = new JLabel("Registro de Historia Clínica", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(65, 158, 91));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // === PANEL CENTRAL ===
        JPanel panelCampos = new JPanel(new GridLayout(8, 2, 12, 12));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelCampos.setBackground(new Color(198, 232, 197));

        // Campos
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
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(250, 250, 255));

        btnGuardar = new JButton("Guardar Historia");
        btnVerReceta = new JButton("Crear Receta Médica");
        btnCancelar = new JButton("Cancelar");

        // Desactivar botón de receta hasta guardar
        btnVerReceta.setEnabled(false);

        // Colores principales
        Color verde = new Color(76, 175, 80);

        // Aplicar estilos base
        configurarBoton(btnGuardar, verde);
        configurarBoton(btnVerReceta, verde);
        configurarBoton(btnCancelar, verde);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnVerReceta);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        // === ACCIONES ===
        btnGuardar.addActionListener(e -> guardarHistoriaClinica());
        btnVerReceta.addActionListener(e -> {
            if (historiaGuardada != null) {
                new VentanaRecetaMedica(historiaGuardada);
            } else {
                JOptionPane.showMessageDialog(this, "Primero debe guardar la historia clínica.");
            }
        });
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    // ============================================================
    // MÉTODO PARA GUARDAR LA HISTORIA CLÍNICA
    // ============================================================
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
                JOptionPane.showMessageDialog(this, "Historia clínica guardada correctamente.");
                btnVerReceta.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, " Error al guardar la historia clínica.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error inesperado: " + ex.getMessage());
        }
    }

    // ============================================================
    // MÉTODO PARA DAR ESTILO Y HOVER A LOS BOTONES
    // ============================================================
    private void configurarBoton(JButton boton, Color colorBase) {
        boton.setFocusPainted(false);
        boton.setBackground(colorBase);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color colorHover = colorBase.darker();
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorHover);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorBase);
            }
        });
    }
}
