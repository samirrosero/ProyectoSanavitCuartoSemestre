package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModalMedico extends JDialog {

    private JTextField txtNombre;
    private JComboBox<String> comboEspecialidad;
    private JButton btnGuardar;

    public ModalMedico(JFrame parent, Usuario usuarioActual) {
        super(parent, "Registrar Médico - Sanavit", true);
        setSize(520, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // === Fondo degradado ===
        JPanel fondo = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(221, 247, 233),
                        0, getHeight(), new Color(165, 228, 194));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        add(fondo, BorderLayout.CENTER);

        // === Tarjeta blanca central ===
        JPanel tarjeta = new JPanel(new GridBagLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setPreferredSize(new Dimension(400, 320));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 230, 210), 1),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // === Imagen o logo ===
        // 🖼️ Aquí puedes colocar tu ícono o imagen de médico
        JLabel lblImagen = new JLabel(new ImageIcon("ruta/a/tu/imagen_medico.png"));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        tarjeta.add(lblImagen, gbc);

        // === Título ===
        JLabel lblTitulo = new JLabel("Registro de Médico", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(26, 94, 67));
        gbc.gridy++;
        tarjeta.add(lblTitulo, gbc);

        // === Campo nombre ===
        gbc.gridwidth = 1;
        gbc.gridy++; gbc.gridx = 0;
        tarjeta.add(crearEtiqueta("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = crearCampo();
        tarjeta.add(txtNombre, gbc);

        // === Campo especialidad ===
        gbc.gridy++; gbc.gridx = 0;
        tarjeta.add(crearEtiqueta("Especialidad:"), gbc);
        gbc.gridx = 1;
        comboEspecialidad = new JComboBox<>(new String[]{
                "Medicina General", "Pediatría", "Ginecología", "Cardiología", "Odontología", "Oftalmología"
        });
        estilizarCombo(comboEspecialidad);
        tarjeta.add(comboEspecialidad, gbc);

        // === Botón guardar ===
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        btnGuardar = crearBoton("Guardar Médico", new Color(26, 94, 67));
        tarjeta.add(btnGuardar, gbc);

        fondo.add(tarjeta);

        // === Evento guardar ===
        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String especialidad = comboEspecialidad.getSelectedItem().toString();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Ingrese el nombre del médico.");
                return;
            }

            Medico nuevoMedico = new Medico(0, nombre, especialidad, usuarioActual.getIdUsuario());
            MedicoDao dao = new MedicoDao();

            if (dao.insertarMedico(nuevoMedico)) {
                JOptionPane.showMessageDialog(this, "✅ Médico registrado con éxito.");
                dispose();
                parent.dispose();
                new Login();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al registrar médico.");
            }
        });

        setVisible(true);
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(46, 64, 46));
        return lbl;
    }

    private JTextField crearCampo() {
        JTextField txt = new JTextField(20);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(165, 228, 194), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return txt;
    }

    private void estilizarCombo(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(new Color(165, 228, 194), 1));
    }

    private JButton crearBoton(String texto, Color colorBase) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(colorBase);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(colorBase.brighter());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(colorBase);
            }
        });
        return btn;
    }

    public static void main(String[] args) {
        new ModalMedico(null, null);
    }
}
