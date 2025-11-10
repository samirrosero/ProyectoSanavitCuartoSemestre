package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;

public class RegistroFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContraseña;
    private JComboBox<String> comboRol;
    private JButton btnRegistrar, btnAtras;

    public RegistroFrame() {
        setTitle("Registro de Usuario - Sanavit");
        setSize(980, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === PANEL PRINCIPAL DIVIDIDO (Imagen izquierda / Formulario derecha) ===
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2, 0, 0));
        add(panelPrincipal);

        // === PANEL IZQUIERDO (Imagen desde resources) ===
        JPanel panelIzquierdo = new JPanel() {
            private final Image imagen;

            {
                Image temp = null;
                try {
                    // ✅ Carga la imagen desde resources
                    temp = new ImageIcon(getClass().getResource("/images/imgRegistrar.png")).getImage();
                } catch (Exception e) {
                    System.err.println("⚠️ Imagen no encontrada: /images/imgRegistrar.png");
                }
                imagen = temp;
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagen != null) {
                    g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Fondo alternativo si no hay imagen
                    g.setColor(new Color(200, 240, 220));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panelIzquierdo.setBackground(Color.WHITE);
        panelPrincipal.add(panelIzquierdo);

        // === PANEL DERECHO (Formulario con fondo degradado) ===
        JPanel panelFormulario = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(221, 247, 233),
                        0, getHeight(), new Color(165, 228, 194)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panelPrincipal.add(panelFormulario);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // === Título ===
        JLabel lblTitulo = new JLabel("Registro de Usuario", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(26, 94, 67));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelFormulario.add(lblTitulo, gbc);

        // === Campo Usuario ===
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        panelFormulario.add(crearEtiqueta("Usuario:"), gbc);
        gbc.gridx = 1;
        txtUsuario = crearCampo();
        panelFormulario.add(txtUsuario, gbc);

        // === Campo Contraseña ===
        gbc.gridy++;
        gbc.gridx = 0;
        panelFormulario.add(crearEtiqueta("Contraseña:"), gbc);
        gbc.gridx = 1;
        txtContraseña = new JPasswordField(20);
        txtContraseña.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtContraseña.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(165, 228, 194), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        panelFormulario.add(txtContraseña, gbc);

        // === Rol ===
        gbc.gridy++;
        gbc.gridx = 0;
        panelFormulario.add(crearEtiqueta("Rol:"), gbc);
        gbc.gridx = 1;
        comboRol = new JComboBox<>(new String[]{
                "Administrador", "Medico", "Gestor de Citas", "Paciente"
        });
        comboRol.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboRol.setBackground(Color.WHITE);
        comboRol.setBorder(BorderFactory.createLineBorder(new Color(165, 228, 194), 1));
        panelFormulario.add(comboRol, gbc);

        // === Botones ===
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBtns.setOpaque(false);
        btnRegistrar = crearBoton("Registrar", new Color(26, 94, 67));
        btnAtras = crearBoton("Atrás", new Color(140, 140, 140));
        panelBtns.add(btnRegistrar);
        panelBtns.add(btnAtras);
        panelFormulario.add(panelBtns, gbc);

        // === Eventos ===
        btnRegistrar.addActionListener(e -> registrarUsuario());
        btnAtras.addActionListener(e -> {
            dispose();
            new Login();
        });

        setVisible(true);
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
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

    private JButton crearBoton(String texto, Color colorBase) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(colorBase);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(colorBase.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(colorBase);
            }
        });
        return btn;
    }

    // === Tu lógica original sin cambios ===
    private void registrarUsuario() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContraseña.getPassword()).trim();
        String rolSeleccionado = comboRol.getSelectedItem().toString().toLowerCase();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y contraseña son obligatorios.");
            return;
        }

        try {
            UsuarioDao usuarioDAO = new UsuarioDao();
            Usuario nuevoUsuario = new Usuario(0, 0, usuario, contrasena, rolSeleccionado);

            int idRol = switch (rolSeleccionado) {
                case "administrador" -> 1;
                case "medico" -> 2;
                case "gestor de citas" -> 3;
                case "paciente" -> 4;
                default -> -1;
            };
            nuevoUsuario.setIdRol(idRol);

            int idUsuario = usuarioDAO.insertarUsuario(nuevoUsuario);
            if (idUsuario == -1) {
                JOptionPane.showMessageDialog(this, "Error al registrar usuario en la base de datos.");
                return;
            }
            nuevoUsuario.setIdUsuario(idUsuario);

            switch (idRol) {
                case 2 -> new ModalMedico(this, nuevoUsuario);
                case 4 -> new ModalPaciente(this, nuevoUsuario);
                default -> JOptionPane.showMessageDialog(this, "Registro completado solo con credenciales.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al registrar usuario: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new RegistroFrame();
    }
}
