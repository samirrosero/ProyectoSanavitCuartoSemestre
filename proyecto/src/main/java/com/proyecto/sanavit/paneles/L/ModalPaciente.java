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
        setSize(600, 650);
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

        // === Tarjeta blanca con scroll ===
        JPanel tarjeta = new JPanel(new GridBagLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 230, 210), 1),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        JScrollPane scroll = new JScrollPane(tarjeta);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        fondo.add(scroll);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // === Imagen ===
        // 🖼️ Aquí puedes poner un ícono o imagen de paciente
        JLabel lblImagen = new JLabel(new ImageIcon("ruta/a/tu/imagen_paciente.png"));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        tarjeta.add(lblImagen, gbc);

        // === Título ===
        JLabel lblTitulo = new JLabel("Registro de Paciente", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(26, 94, 67));
        gbc.gridy++;
        tarjeta.add(lblTitulo, gbc);

        // === Campos ===
        gbc.gridwidth = 1;
        agregarCampo(tarjeta, gbc, 2, "Nombre:", txtNombre = new JTextField(20));
        agregarCampo(tarjeta, gbc, 3, "Correo:", txtCorreo = new JTextField(20));
        agregarCampo(tarjeta, gbc, 4, "Edad:", txtEdad = new JTextField(20));
        agregarCampo(tarjeta, gbc, 5, "Teléfono:", txtTelefono = new JTextField(20));

        gbc.gridy = 6; gbc.gridx = 0;
        tarjeta.add(crearEtiqueta("Sexo:"), gbc);
        gbc.gridx = 1;
        comboSexo = new JComboBox<>(new String[]{"Masculino", "Femenino", "Otro"});
        estilizarCombo(comboSexo);
        tarjeta.add(comboSexo, gbc);

        agregarCampo(tarjeta, gbc, 7, "Dirección:", txtDireccion = new JTextField(20));
        agregarCampo(tarjeta, gbc, 8, "Identificación:", txtIdentificacion = new JTextField(20));

        gbc.gridy = 9; gbc.gridx = 0;
        tarjeta.add(crearEtiqueta("Salud (EPS):"), gbc);
        gbc.gridx = 1;
        comboSalud = new JComboBox<>(new String[]{"Sura", "Sanitas", "Nueva EPS", "Coomeva", "Salud Total", "Comfandi SOS"});
        estilizarCombo(comboSalud);
        tarjeta.add(comboSalud, gbc);

        gbc.gridy = 10; gbc.gridx = 0;
        tarjeta.add(crearEtiqueta("Afiliación:"), gbc);
        gbc.gridx = 1;
        comboAfiliacion = new JComboBox<>(new String[]{"Contributivo", "Subsidiado", "Particular"});
        estilizarCombo(comboAfiliacion);
        tarjeta.add(comboAfiliacion, gbc);

        // === Botón ===
        gbc.gridy = 11; gbc.gridx = 0; gbc.gridwidth = 2;
        btnGuardar = crearBoton("Guardar Paciente", new Color(26, 94, 67));
        tarjeta.add(btnGuardar, gbc);

        // === Evento guardar ===
        btnGuardar.addActionListener(e -> guardarPaciente(usuarioActual, parent));

        setVisible(true);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String texto, JTextField campo) {
        gbc.gridy = fila; gbc.gridx = 0;
        panel.add(crearEtiqueta(texto), gbc);
        gbc.gridx = 1;
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(165, 228, 194), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        panel.add(campo, gbc);
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(46, 64, 46));
        return lbl;
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
            public void mouseEntered(MouseEvent e) { btn.setBackground(colorBase.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(colorBase); }
        });
        return btn;
    }

    private void guardarPaciente(Usuario usuarioActual, JFrame parent) {
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

            PacienteDao pacienteDao = new PacienteDao();
            Paciente p = new Paciente(0, nombre, correo, edad, telefono, sexo, direccion, identificacion, usuarioActual.getIdUsuario());

            if (pacienteDao.insertarPaciente(p)) {
                Portafolio port = new Portafolio(0, salud, afiliacion,
                        pacienteDao.obtenerPacientePorIdUsuario(usuarioActual.getIdUsuario()).getIdPaciente());
                new PortafolioDao().insertarPortafolio(port);
                JOptionPane.showMessageDialog(this, "✅ Paciente registrado correctamente.");
                dispose();
                parent.dispose();
                new Login();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al registrar paciente.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Edad inválida.");
        }
    }

    public static void main(String[] args) {
        new ModalPaciente(null, null);
    }
}
