package com.proyecto.sanavit.paneles.L;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaAdministrador extends JFrame {

    private JButton btnUsuarios, btnBackup, btnCerrarSesion;

    public VentanaAdministrador () {
        setTitle("Panel del Administrador del Sistema");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === ENCABEZADO ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(123, 229, 144));
        JLabel lblTitulo = new JLabel("Panel del Administrador del Sistema", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        header.add(lblTitulo, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // === PANEL CENTRAL ===
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new GridLayout(3, 1, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        btnUsuarios = new JButton("Gestión de Usuarios");
        btnBackup = new JButton("Realizar Copia de Seguridad");
        btnCerrarSesion = new JButton("Cerrar Sesión");

        panelCentral.add(btnUsuarios);
        panelCentral.add(btnBackup);
        panelCentral.add(btnCerrarSesion);

        add(panelCentral, BorderLayout.CENTER);

        // === ACCIONES DE LOS BOTONES ===

        // Abrir ventana CRUD de usuario
        btnUsuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaCRUDUsuario(); // abre la ventana de gestión
            }
        });

        // Simular copia de seguridad (más adelante se puede conectar a backup real)
        btnBackup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                    null,
                    "✅ Copia de seguridad realizada correctamente.",
                    "Backup del Sistema",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        // Cerrar sesión
        btnCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // cierra esta ventana
                new Login(); // vuelve al login
            }
        });

        setVisible(true);
    }
}
