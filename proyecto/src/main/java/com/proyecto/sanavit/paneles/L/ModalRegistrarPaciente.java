package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ModalRegistrarPaciente extends JDialog {

    private JTextField txtNombre, txtCorreo, txtEdad, txtTelefono, txtDireccion,
            txtIdentificacion, txtContraseña;
    private JComboBox<String> comboSexo, comboSalud, comboAfiliacion;
    private JButton btnGuardar, btnCancelar;

    private PacienteDao pacienteDAO = new PacienteDao();
    private UsuarioDao usuarioDAO = new UsuarioDao();
    private PortafolioDao portafolioDAO = new PortafolioDao();

    public ModalRegistrarPaciente(JFrame parent) {
        super(parent, "Registrar Paciente", true);
        setSize(520, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // === PANEL PRINCIPAL CON DEGRADADO ===
        JPanel fondo = new JPanel() {
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
        fondo.setLayout(new GridBagLayout());
        fondo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // === TÍTULO ===
        JLabel lblTitulo = new JLabel("Registro de Paciente", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        fondo.add(lblTitulo, gbc);
        gbc.gridwidth = 1;

        // === CAMPOS ===
        int fila = 1;
        agregarCampo(fondo, gbc, fila++, "Nombre:", txtNombre = new JTextField());
        agregarCampo(fondo, gbc, fila++, "Correo:", txtCorreo = new JTextField());
        agregarCampo(fondo, gbc, fila++, "Edad:", txtEdad = new JTextField());
        agregarCampo(fondo, gbc, fila++, "Teléfono:", txtTelefono = new JTextField());

        // Sexo
        JLabel lblSexo = new JLabel("Sexo:");
        lblSexo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = fila;
        fondo.add(lblSexo, gbc);
        comboSexo = new JComboBox<>(new String[]{"Masculino", "Femenino", "Otro"});
        estilizarCombo(comboSexo);
        gbc.gridx = 1;
        fondo.add(comboSexo, gbc);
        fila++;

        agregarCampo(fondo, gbc, fila++, "Dirección:", txtDireccion = new JTextField());
        agregarCampo(fondo, gbc, fila++, "Identificación:", txtIdentificacion = new JTextField());

        // Salud (EPS)
        JLabel lblSalud = new JLabel("EPS:");
        lblSalud.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = fila;
        fondo.add(lblSalud, gbc);
        comboSalud = new JComboBox<>(new String[]{"Salud Total", "Sura", "Coomeva", "Sanitas", "Nueva EPS"});
        estilizarCombo(comboSalud);
        gbc.gridx = 1;
        fondo.add(comboSalud, gbc);
        fila++;

        // Afiliación
        JLabel lblAfiliacion = new JLabel("Afiliación:");
        lblAfiliacion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = fila;
        fondo.add(lblAfiliacion, gbc);
        comboAfiliacion = new JComboBox<>(new String[]{"Contributivo", "Subsidiado", "Particular"});
        estilizarCombo(comboAfiliacion);
        gbc.gridx = 1;
        fondo.add(comboAfiliacion, gbc);
        fila++;

        // Contraseña
        agregarCampo(fondo, gbc, fila++, "Contraseña temporal:", txtContraseña = new JTextField());

        // === BOTONES ===
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setOpaque(false); // fondo transparente para mantener el degradado

        btnGuardar = crearBotonConHover("Guardar Paciente", new Color(31, 148, 78), new Color(39, 174, 96));
        btnCancelar = crearBotonConHover("Cancelar", new Color(31, 148, 78), new Color(192, 57, 43));

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        fondo.add(panelBotones, gbc);

        add(fondo);

        // === EVENTOS ===
        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> guardarPaciente());

        setVisible(true);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String texto, JTextField campo) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = fila;
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createLineBorder(new Color(130, 200, 130)));
        panel.add(campo, gbc);
    }

    private void estilizarCombo(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(new Color(130, 200, 130)));
    }

    // === BOTONES CON HOVER ===
    private JButton crearBotonConHover(String texto, Color colorBase, Color colorHover) {
        JButton btn = new JButton(texto);
        btn.setBackground(colorBase);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // efecto hover
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(colorHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(colorBase);
            }
        });

        return btn;
    }

    private void guardarPaciente() {
        try {
            String nombre = txtNombre.getText().trim();
            String correo = txtCorreo.getText().trim();
            int edad = Integer.parseInt(txtEdad.getText().trim());
            String telefono = txtTelefono.getText().trim();
            String sexo = comboSexo.getSelectedItem().toString();
            String direccion = txtDireccion.getText().trim();
            String identificacion = txtIdentificacion.getText().trim();
            String eps = comboSalud.getSelectedItem().toString();
            String afiliacion = comboAfiliacion.getSelectedItem().toString();
            String contraseña = txtContraseña.getText().trim();

            if (nombre.isEmpty() || correo.isEmpty() || contraseña.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Complete los campos obligatorios.");
                return;
            }

            // Crear usuario primero
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombreUsuario(nombre);
            nuevoUsuario.setContraseña(contraseña);
            nuevoUsuario.setIdRol(4); // Rol de paciente

            int idUsuario = usuarioDAO.insertarUsuario(nuevoUsuario);
            if (idUsuario <= 0) {
                JOptionPane.showMessageDialog(this, "Error al crear usuario.");
                return;
            }

            // Crear paciente
            Paciente nuevo = new Paciente(0, nombre, correo, edad, telefono, sexo, direccion, identificacion, idUsuario);
            boolean pacienteOK = pacienteDAO.insertarPaciente(nuevo);

            // Crear portafolio
            Portafolio port = new Portafolio(0, eps, afiliacion, nuevo.getIdPaciente());
            boolean portOK = portafolioDAO.insertarPortafolio(port);

            if (pacienteOK) {
                JOptionPane.showMessageDialog(this, "✅ Paciente registrado correctamente con usuario: " + nombre);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al registrar paciente.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
