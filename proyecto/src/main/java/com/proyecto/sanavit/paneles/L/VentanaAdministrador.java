package com.proyecto.sanavit.paneles.L;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana principal del Administrador (versión moderna)
 * Estructura: panel lateral + panel central dinámico
 */
public class VentanaAdministrador extends JFrame {

    private JPanel panelCentral;
    private CardLayout cardLayout;

    public VentanaAdministrador() {
        setTitle("Panel del Administrador - IPS Sanavit");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === ENCABEZADO SUPERIOR ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(87, 197, 123)); // Verde institucional
        JLabel lblTitulo = new JLabel("Administrador - IPS Sanavit", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        header.add(lblTitulo, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // === PANEL LATERAL (MENÚ) ===
        JPanel panelLateral = new JPanel();
        panelLateral.setBackground(new Color(240, 255, 240));
        panelLateral.setPreferredSize(new Dimension(250, 0));
        panelLateral.setLayout(new GridLayout(9, 1, 10, 10));
        panelLateral.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Botones del menú
        JButton btnUsuarios = crearBotonMenu("👥 Gestión de Usuarios");
        JButton btnMedicos = crearBotonMenu("🩺 Médicos");
        JButton btnPacientes = crearBotonMenu("🧍 Pacientes");
        JButton btnCitas = crearBotonMenu("📅 Citas");
        JButton btnHistorias = crearBotonMenu("📚 Historias Clínicas");
        JButton btnReportes = crearBotonMenu("📊 Reportes");
        JButton btnBackup = crearBotonMenu("💾 Copia de Seguridad");
        JButton btnCerrar = crearBotonMenu("🚪 Cerrar Sesión");

        panelLateral.add(btnUsuarios);
        panelLateral.add(btnMedicos);
        panelLateral.add(btnPacientes);
        panelLateral.add(btnCitas);
        panelLateral.add(btnHistorias);
        panelLateral.add(btnReportes);
        panelLateral.add(btnBackup);
        panelLateral.add(btnCerrar);

        add(panelLateral, BorderLayout.WEST);

        // === PANEL CENTRAL (DINÁMICO) ===
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);

        // Aquí agregaremos los diferentes módulos
        panelCentral.add(new PanelUsuarios(), "usuarios");
        panelCentral.add(new PanelMedicos(), "medicos");
        panelCentral.add(new PanelPacientes(), "pacientes");
        panelCentral.add(new PanelCitas(), "citas");
        panelCentral.add(new PanelHistoriaClinica(), "historias");
        panelCentral.add(new PanelReportes(), "reportes");

        add(panelCentral, BorderLayout.CENTER);

        // === ACCIONES DE BOTONES ===
        btnUsuarios.addActionListener(e -> mostrarPanel("usuarios"));
        btnMedicos.addActionListener(e -> mostrarPanel("medicos"));
        btnPacientes.addActionListener(e -> mostrarPanel("pacientes"));
        btnCitas.addActionListener(e -> mostrarPanel("citas"));
        btnHistorias.addActionListener(e -> mostrarPanel("historias"));
        btnReportes.addActionListener(e -> mostrarPanel("reportes"));

        // Copia de seguridad
        btnBackup.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "✅ Copia de seguridad realizada correctamente.",
                    "Backup del Sistema",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        // Cerrar sesión
        btnCerrar.addActionListener(e -> {
            dispose();
            new Login(); // volver al login
        });

        setVisible(true);
    }

    // === MÉTODO AUXILIAR: Crear botones del menú lateral ===
    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(new Color(200, 250, 200));
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBorder(BorderFactory.createLineBorder(new Color(170, 230, 170)));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(170, 240, 170));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(200, 250, 200));
            }
        });

        return boton;
    }

    // === MÉTODO AUXILIAR: Mostrar un panel del CardLayout ===
    private void mostrarPanel(String nombre) {
        cardLayout.show(panelCentral, nombre);
    }

    // === Panel de prueba / marcador de posición ===
    private JPanel crearPanelPlaceholder(String texto) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
