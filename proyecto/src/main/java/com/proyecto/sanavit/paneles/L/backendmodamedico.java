// package com.proyecto.sanavit.paneles.L;

// public class backendmodamedico {
//   private JTextField txtNombre;
//     private JComboBox<String> comboEspecialidad;
//     private JButton btnGuardar;

//     public ModalMedico(JFrame parent, Usuario usuarioActual) {
//         super(parent, "Registrar Médico", true);
//         setSize(400, 300);
//         setLocationRelativeTo(parent);
//         setLayout(new GridBagLayout());

//         GridBagConstraints gbc = new GridBagConstraints();
//         gbc.insets = new Insets(10, 10, 10, 10);

//         gbc.gridx = 0; gbc.gridy = 0;
//         add(new JLabel("Nombre:"), gbc);
//         gbc.gridx = 1;
//         txtNombre = new JTextField(20);
//         add(txtNombre, gbc);

//         gbc.gridx = 0; gbc.gridy++;
//         add(new JLabel("Especialidad:"), gbc);
//         gbc.gridx = 1;
//         comboEspecialidad = new JComboBox<>(new String[]{
//                 "Medicina General", "Pediatría", "Ginecología", "Cardiología", "Odontología", "Oftalmología"
//         });
//         add(comboEspecialidad, gbc);

//         gbc.gridx = 0; gbc.gridy++;
//         gbc.gridwidth = 2;
//         btnGuardar = new JButton("Guardar Médico");
//         add(btnGuardar, gbc);

//         btnGuardar.addActionListener(e -> {
//             String nombre = txtNombre.getText().trim();
//             String especialidad = comboEspecialidad.getSelectedItem().toString();

//             if (nombre.isEmpty()) {
//                 JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del médico.");
//                 return;
//             }

//             Medico nuevoMedico = new Medico(0, nombre, especialidad, usuarioActual.getIdUsuario());
//             MedicoDao medicoDao = new MedicoDao();

//             if (medicoDao.insertarMedico(nuevoMedico)) {
//                 JOptionPane.showMessageDialog(this, "Médico registrado con éxito.");
//                 dispose();
//                 parent.dispose();
//                 new Login();
//             } else {
//                 JOptionPane.showMessageDialog(this, "Error al registrar médico.");
//             }
//         });

//         setVisible(true);
//     }
// }

