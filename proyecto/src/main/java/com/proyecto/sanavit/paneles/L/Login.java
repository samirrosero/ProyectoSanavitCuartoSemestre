package com.proyecto.sanavit.paneles.L;
import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContraseña;

    public Login() {
        setTitle("Inicio de Sesión");
        setSize(980, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(new Color(35, 210, 43));
        setContentPane(fondo); // Usamos el panel 'fondo' como contenido principal

        // Logo
        ImageIcon logo = new ImageIcon("C:\\Users\\samir\\OneDrive\\Escritorio\\OneDrive\\Documentos\\prototipo\\logo.png");
        JLabel labellogo = new JLabel(logo);
        labellogo.setHorizontalAlignment(SwingConstants.CENTER);
        labellogo.setVerticalAlignment(SwingConstants.CENTER);
        labellogo.setBounds(0, 0, 350, 350);
        labellogo.setIcon(new ImageIcon(logo.getImage().getScaledInstance(labellogo.getWidth(), labellogo.getHeight(), Image.SCALE_SMOOTH)));
        fondo.add(labellogo, BorderLayout.NORTH);
        
        JPanel panelLogin= new JPanel();
        panelLogin.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panelLogin.setOpaque(false);
        gbc.insets= new Insets( 10, 10, 10, 20);
        gbc.fill = GridBagConstraints.CENTER; 
        gbc.gridx= 0;
        gbc.gridy= 0;
        JLabel usuario= new JLabel("Nombre de usuario");
        panelLogin.add(new JLabel("Nombre de usuario"));
        gbc.gridy++;

        txtUsuario = new JTextField(20);
        panelLogin.add(txtUsuario, gbc);
        gbc.gridy++;

        JLabel contraseña= new JLabel("Contraseña");
        panelLogin.add(new JLabel("Contraseña"), gbc);
        gbc.gridy++;

        txtContraseña = new JPasswordField(20);
        panelLogin.add(txtContraseña, gbc);

        //agregamos botones
        gbc.gridy++;
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        JButton btnIniciar = new JButton("Iniciar Sesión");
        btnIniciar.setOpaque(true);
        JButton btnRegistrar = new JButton("Registrarse");
        btnRegistrar.setOpaque(true);
        panelBotones.add(btnIniciar);
        panelBotones.add(btnRegistrar);
        panelLogin.add(panelBotones, gbc);

        btnIniciar.addActionListener(e -> iniciarSesion());
        
        btnRegistrar.addActionListener(e -> {
            RegistroFrame registroFrame = new RegistroFrame();
            fondo.add(registroFrame,BorderLayout.CENTER);
            setVisible(true);
            add(btnRegistrar);
        });
         fondo.add(panelLogin, BorderLayout.CENTER); //CENTER
           setVisible(true);

    }
     
    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContraseña.getPassword()).trim();

            UsuarioDao usuarioDAO = new UsuarioDao();
            Usuario u = usuarioDAO.autenticarUsuario(usuario, contrasena);
            if (u != null) {
                String rol = u.getNombreRol()!= null?
                u.getNombreRol().trim().toLowerCase():"";
                System.out.println("Rol recibido: "+ rol +"");
                JOptionPane.showMessageDialog(this, "Bienvenido, " + u.getNombreUsuario()+ " ("+rol+")");
                    
            switch (rol) {
                case "medico":
                         dispose();
                         SwingUtilities.invokeLater(()->{//Esto obliga a que la nueva ventana se cree en el momento adecuado, 
                            //y en el hilo correcto, después de que la ventana anterior se haya cerrado completamente.
                            VentanaMedico vm= new VentanaMedico(u);
                            vm.setVisible(true);
                            vm.toFront();
                            vm.requestFocus();
                         });
                    break;
               /* case "paciente":
                        dispose();
                        SwingUtilities.invokeLater(() -> {
                        VentanaPaciente vp = new VentanaPaciente(u);
                        vp.setVisible(true);
                        vp.toFront();
                        vp.requestFocus();
                }); 
                   break;*/ 
                //case "gestor":
                  //  new VentanaGestorCitas(u).setVisible(true);
                    //break;
                //case "administrador":
                  //  new VentanaAdministrador(u).setVisible(true);
                    //break;
                default:
                    JOptionPane.showMessageDialog(null, "Rol no reconocido: '" + rol + "'");
                    break;
            }

                 dispose();// Cerrar ventana solo si rol fue reconocido

            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
            }
        setVisible(true);
    }

        public static void main(String[] args) {
        new Login();
    }
}


