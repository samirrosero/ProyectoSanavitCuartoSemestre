// package com.proyecto.sanavit.paneles.L;

// public class backendmodalpaciente {
//  private JTextField txtNombre, txtCorreo, txtEdad, txtTelefono, txtDireccion, txtIdentificacion;
//     private JComboBox<String> comboSexo, comboSalud, comboAfiliacion;
//     private JButton btnGuardar;

//     public ModalPaciente(JFrame parent, Usuario usuarioActual) {
//         super(parent, "Registrar Paciente", true);
//         setSize(500, 550);
//         setLocationRelativeTo(parent);
//         setLayout(new GridBagLayout());

//         GridBagConstraints gbc = new GridBagConstraints();
//         gbc.insets = new Insets(10, 10, 10, 10);
//         gbc.fill = GridBagConstraints.HORIZONTAL;

//         gbc.gridx = 0; gbc.gridy = 0;
//         add(new JLabel("Nombre:"), gbc);
//         gbc.gridx = 1;
//         txtNombre = new JTextField(20);
//         add(txtNombre, gbc);

//         gbc.gridx = 0; gbc.gridy++;
//         add(new JLabel("Correo:"), gbc);
//         gbc.gridx = 1;
//         txtCorreo = new JTextField(20);
//         add(txtCorreo, gbc);

//         gbc.gridx = 0; gbc.gridy++;
//         add(new JLabel("Edad:"), gbc);
//         gbc.gridx = 1;
//         txtEdad = new JTextField(20);
//         add(txtEdad, gbc);

//         gbc.gridx = 0; gbc.gridy++;
//         add(new JLabel("Teléfono:"), gbc);
//         gbc.gridx = 1;
//         txtTelefono = new JTextField(20);
//         add(txtTelefono, gbc);

//         gbc.gridx = 0; gbc.gridy++;
//         add(new JLabel("Sexo:"), gbc);
//         gbc.gridx = 1;
//         comboSexo = new JComboBox<>(new String[]{"Masculino", "Femenino", "Otro"});
//         add(comboSexo, gbc);

//         gbc.gridx = 0; gbc.gridy++;
//         add(new JLabel("Dirección:"), gbc);
//         gbc.gridx = 1;
//         txtDireccion = new JTextField(20);
//         add(txtDireccion, gbc);

//         gbc.gridx = 0; gbc.gridy++;
//         add(new JLabel("Identificación:"), gbc);
//         gbc.gridx = 1;
//         txtIdentificacion = new JTextField(20);
//         add(txtIdentificacion, gbc);

//         gbc.gridx = 0; gbc.gridy++;
//         add(new JLabel("Salud (EPS):"), gbc);
//         gbc.gridx = 1;
//         comboSalud = new JComboBox<>(new String[]{
//                 "Salud Total", "Sura", "Coomeva", "Sanitas", "Nueva EPS"
//         });
//         add(comboSalud, gbc);

//         gbc.gridx = 0; gbc.gridy++;
//         add(new JLabel("Afiliación:"), gbc);
//         gbc.gridx = 1;
//         comboAfiliacion = new JComboBox<>(new String[]{
//                 "Contributivo", "Subsidiado", "Particular"
//         });
//         add(comboAfiliacion, gbc);

//         gbc.gridx = 0; gbc.gridy++;
//         gbc.gridwidth = 2;
//         btnGuardar = new JButton("Guardar Paciente");
//         add(btnGuardar, gbc);

    
//      btnGuardar.addActionListener(e -> {
//     try {
//         String nombre = txtNombre.getText().trim();
//         String correo = txtCorreo.getText().trim();
//         int edad = Integer.parseInt(txtEdad.getText().trim());
//         String telefono = txtTelefono.getText().trim();
//         String sexo = comboSexo.getSelectedItem().toString();
//         String direccion = txtDireccion.getText().trim();
//         String identificacion = txtIdentificacion.getText().trim();
//         String salud = comboSalud.getSelectedItem().toString();
//         String afiliacion = comboAfiliacion.getSelectedItem().toString();

//         if (nombre.isEmpty() || correo.isEmpty()) {
//             JOptionPane.showMessageDialog(this, "Nombre y correo son obligatorios.");
//             return;
//         }

//         Paciente paciente = new Paciente(0, nombre, correo, edad, telefono, sexo, direccion,
//                 identificacion, usuarioActual.getIdUsuario());

//         PacienteDao pacienteDao = new PacienteDao();
//         boolean pacienteInsertado = pacienteDao.insertarPaciente(paciente);

//         if (pacienteInsertado) {
//             paciente.setIdPaciente(pacienteDao.obtenerPacientePorIdUsuario(usuarioActual.getIdUsuario()).getIdPaciente());

//             // Crear portafolio y vincular con paciente
//             Portafolio portafolio = new Portafolio(0, salud, afiliacion, paciente.getIdPaciente());
//             PortafolioDao portafolioDao = new PortafolioDao();

//             boolean portafolioOk = portafolioDao.insertarPortafolio(portafolio);

//             if (portafolioOk) {
//                 JOptionPane.showMessageDialog(this, "✅ Paciente y portafolio registrados correctamente.");
//             } else {
//                 JOptionPane.showMessageDialog(this, "⚠️ Paciente registrado, pero error al guardar portafolio.");
//             }

//             dispose();
//             parent.dispose();
//             new Login();

//         } else {
//             JOptionPane.showMessageDialog(this, "❌ Error al registrar paciente en la base de datos.");
//         }

//     } catch (NumberFormatException ex) {
//         JOptionPane.showMessageDialog(this, "Edad inválida.");
//     }
// });
//         setVisible(true);
//     }
// }
