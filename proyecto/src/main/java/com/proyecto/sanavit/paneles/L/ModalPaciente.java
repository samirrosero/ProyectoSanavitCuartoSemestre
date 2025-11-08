package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModalPaciente extends JDialog {

    private JTextField txtNombre, txtCorreo, txtEdad, txtTelefono, txtDireccion, txtIdentificacion;
    private JComboBox<String> comboSexo, comboSalud, comboAfiliacion;
    private JButton btnGuardar;

    public ModalPaciente(JFrame parent, Usuario usuarioActual) {
        super(parent, "Registrar Paciente - Sanavit", true);
        setSize(520, 600);
        setLocationRelativeTo(parent);

        // === PANEL PRINCIPAL CON DEGRADADO ===
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(78, 207, 78),
                        0, getHeight(), new Color(165, 242, 203)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelPrincipal.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // === TÍTULO ===
        JLabel lblTitulo = new JLabel("Registro de Paciente", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(10, 10, 10));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(lblTitulo, gbc);

        // === CAMPOS ===
        gbc.gridwidth = 1;

        agregarCampo(panelPrincipal, gbc, 1, "Nombre:", txtNombre = new JTextField(20));
        agregarCampo(panelPrincipal, gbc, 2, "Correo:", txtCorreo = new JTextField(20));
        agregarCampo(panelPrincipal, gbc, 3, "Edad:", txtEdad = new JTextField(20));
        agregarCampo(panelPrincipal, gbc, 4, "Teléfono:", txtTelefono = new JTextField(20));

        // Combo Sexo
        gbc.gridy = 5; gbc.gridx = 0;
        JLabel lblSexo = crearEtiqueta("Sexo:");
        panelPrincipal.add(lblSexo, gbc);
        gbc.gridx = 1;
        comboSexo = new JComboBox<>(new String[]{"Masculino", "Femenino", "Otro"});
        estilizarCombo(comboSexo);
        panelPrincipal.add(comboSexo, gbc);

        agregarCampo(panelPrincipal, gbc, 6, "Dirección:", txtDireccion = new JTextField(20));
        agregarCampo(panelPrincipal, gbc, 7, "Identificación:", txtIdentificacion = new JTextField(20));

        // Combo Salud
        gbc.gridy = 8; gbc.gridx = 0;
        JLabel lblSalud = crearEtiqueta("Salud (EPS):");
        panelPrincipal.add(lblSalud, gbc);
        gbc.gridx = 1;
        comboSalud = new JComboBox<>(new String[]{"Salud Total", "Sura", "Coomeva", "Sanitas", "Nueva EPS"});
        estilizarCombo(comboSalud);
        panelPrincipal.add(comboSalud, gbc);

        // Combo Afiliación
        gbc.gridy = 9; gbc.gridx = 0;
        JLabel lblAfiliacion = crearEtiqueta("Afiliación:");
        panelPrincipal.add(lblAfiliacion, gbc);
        gbc.gridx = 1;
        comboAfiliacion = new JComboBox<>(new String[]{"Contributivo", "Subsidiado", "Particular"});
        estilizarCombo(comboAfiliacion);
        panelPrincipal.add(comboAfiliacion, gbc);

        // === BOTÓN GUARDAR ===
        gbc.gridy = 10;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnGuardar = crearBoton("Guardar Paciente", new Color(39, 174, 96));
        panelPrincipal.add(btnGuardar, gbc);

        // === EVENTO GUARDAR ===
        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText().trim();
                String correo = txtCorreo.getText().trim();
                int edad = Integer.parseInt(txtEdad.getText().trim());
                String telefono = txtTelefono.getText().trim();
                String sexo = comboSexo.getSelectedItem().toString();
                String direccion = txtDireccion.getText().trim();
                String identificacion = txtIdentificacion.getText().trim();
                String salud = comboSalud.getSelectedItem().toString();
                String afiliacion = comboAfiliacion.getSelectedItem().toString();

                if (nombre.isEmpty() || correo.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "⚠️ Nombre y correo son obligatorios.");
                    return;
                }

                Paciente paciente = new Paciente(0, nombre, correo, edad, telefono, sexo, direccion,
                        identificacion, usuarioActual.getIdUsuario());

                PacienteDao pacienteDao = new PacienteDao();
                boolean pacienteInsertado = pacienteDao.insertarPaciente(paciente);

                if (pacienteInsertado) {
                    paciente.setIdPaciente(
                            pacienteDao.obtenerPacientePorIdUsuario(usuarioActual.getIdUsuario()).getIdPaciente()
                    );

                    // Crear portafolio
                    Portafolio portafolio = new Portafolio(0, salud, afiliacion, paciente.getIdPaciente());
                    PortafolioDao portafolioDao = new PortafolioDao();
                    boolean portafolioOk = portafolioDao.insertarPortafolio(portafolio);

                    if (portafolioOk) {
                        JOptionPane.showMessageDialog(this, "✅ Paciente y portafolio registrados correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(this, "⚠️ Paciente registrado, pero error al guardar portafolio.");
                    }

                    dispose();
                    parent.dispose();
                    new Login();

                } else {
                    JOptionPane.showMessageDialog(this, "❌ Error al registrar paciente en la base de datos.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Edad inválida. Ingrese un número.");
            }
        });

        add(panelPrincipal);
        setVisible(true);
    }

    // === MÉTODOS DE ESTILO ===

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(10, 10, 10));
        return lbl;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String texto, JTextField campo) {
        gbc.gridy = fila;
        gbc.gridx = 0;
        JLabel lbl = crearEtiqueta(texto);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(102, 187, 106), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panel.add(campo, gbc);
    }

    private void estilizarCombo(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(new Color(102, 187, 106), 1));
    }

    private JButton crearBoton(String texto, Color colorBase) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(colorBase);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBase.darker(), 1),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));

        // Bordes redondeados + hover
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(btn.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 25, 25);
                g2.setColor(btn.getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int x = (c.getWidth() - fm.stringWidth(btn.getText())) / 2;
                int y = (c.getHeight() + fm.getAscent()) / 2 - 3;
                g2.drawString(btn.getText(), x, y);
                g2.dispose();
            }
        });

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(colorBase.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(colorBase);
            }
        });

        return btn;
    }
    public static void main(String[] args) {
        new ModalPaciente(null, null);
    }
}
