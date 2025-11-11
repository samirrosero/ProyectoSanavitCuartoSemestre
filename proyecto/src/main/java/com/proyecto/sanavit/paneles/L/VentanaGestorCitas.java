package com.proyecto.sanavit.paneles.L;

import com.proyecto.sanavit.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class VentanaGestorCitas extends JFrame {

    private JTable tablaPacientes, tablaCitas;
    private DefaultTableModel modeloPacientes, modeloCitas;
    private JButton btnNuevoPaciente, btnEditarPaciente, btnEliminarPaciente, btnBuscarPaciente;
    private JButton btnAgendarCita, btnEditarCita, btnEliminarCita, btnActualizarCitas, btnActualizarPacientes;
    private JTextField txtBuscarPaciente;
    private Usuario usuarioGestor;

    private PacienteDao pacienteDAO = new PacienteDao();
    private CitaDao citaDAO = new CitaDao();

    public VentanaGestorCitas(Usuario usuarioGestor) {
        this.usuarioGestor = usuarioGestor;
        setTitle("Gestor de Citas - Sanavit");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== ENCABEZADO =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(76, 175, 80)); // Verde Sanavit
        JLabel lblTitulo = new JLabel("Bienvenido, " + usuarioGestor.getNombreUsuario() + " (Gestor de Citas)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(66, 166, 105));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover
        btnCerrarSesion.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnCerrarSesion.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(MouseEvent e) {
                btnCerrarSesion.setBackground(new Color(66, 166, 105));
            }
        });

        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new Login();
        });

        header.add(lblTitulo, BorderLayout.CENTER);
        header.add(btnCerrarSesion, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ===== PESTAÑAS =====
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(Color.WHITE);
        tabs.setForeground(new Color(60, 120, 60));

        // === ÍCONOS ===
        ImageIcon iconPacientes = crearIcono("/icons/icons8-usuario-32.png");
        ImageIcon iconCitas = crearIcono("/icons/icons8-citas-32.png");

        tabs.addTab(" Pacientes", iconPacientes, crearPanelPacientes());
        tabs.addTab(" Citas", iconCitas, crearPanelCitas());

        add(tabs, BorderLayout.CENTER);

        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }

    // ===== PANEL DE PACIENTES =====
    private JPanel crearPanelPacientes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        modeloPacientes = new DefaultTableModel(new String[]{
                "ID", "Nombre", "Correo", "Edad", "Teléfono", "Sexo", "Dirección", "Identificación"
        }, 0);

        tablaPacientes = new JTable(modeloPacientes);
        tablaPacientes.setRowHeight(28);
        tablaPacientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaPacientes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(new JScrollPane(tablaPacientes), BorderLayout.CENTER);

        // ===== BOTONES =====
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        botones.setBackground(new Color(245, 245, 245));

        btnNuevoPaciente = crearBoton("Nuevo Paciente", new Color(46, 204, 113));
        btnEditarPaciente = crearBoton("Editar", new Color(66, 166, 105));
        btnEliminarPaciente = crearBoton("Eliminar", new Color(66, 166, 105));
        btnBuscarPaciente = crearBoton("Buscar", new Color(66, 166, 105));
        btnActualizarPacientes = crearBoton("Actualizar Lista", new Color(66, 166, 105));

        txtBuscarPaciente = new JTextField(18);
        txtBuscarPaciente.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBuscarPaciente.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        botones.add(btnNuevoPaciente);
        botones.add(btnEditarPaciente);
        botones.add(btnEliminarPaciente);
        botones.add(new JLabel("Buscar por nombre:"));
        botones.add(txtBuscarPaciente);
        botones.add(btnBuscarPaciente);
        botones.add(btnActualizarPacientes);

        panel.add(botones, BorderLayout.SOUTH);
        cargarPacientes();

        btnNuevoPaciente.addActionListener(e -> new ModalRegistrarPaciente(this));
        btnActualizarPacientes.addActionListener(e -> cargarPacientes());
        btnBuscarPaciente.addActionListener(e -> buscarPaciente());
        btnEliminarPaciente.addActionListener(e -> eliminarPaciente());
        btnEditarPaciente.addActionListener(e -> editarPaciente());


        return panel;
    }

    private void cargarPacientes() {
        modeloPacientes.setRowCount(0);
        List<Paciente> lista = pacienteDAO.obtenerTodosLosPacientes();
        for (Paciente p : lista) {
            modeloPacientes.addRow(new Object[]{
                    p.getIdPaciente(), p.getNombre(), p.getCorreo(),
                    p.getEdad(), p.getTelefono(), p.getSexo(),
                    p.getDireccion(), p.getIdentificacion()
            });
        }
    }

    private void editarPaciente() {
    int fila = tablaPacientes.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "⚠️ Seleccione un paciente para editar.");
        return;
    }

    // Obtener el paciente seleccionado desde la base de datos
    int idPaciente = Integer.parseInt(modeloPacientes.getValueAt(fila, 0).toString());
    Paciente paciente = pacienteDAO.obtenerPacientePorId(idPaciente);

    if (paciente == null) {
        JOptionPane.showMessageDialog(this, "❌ No se encontró el paciente en la base de datos.");
        return;
    }

    // 🌿 Ventana de edición moderna
    JDialog ventanaEditar = new JDialog(this, "Editar Paciente - Sanavit", true);
    ventanaEditar.setSize(450, 550);
    ventanaEditar.setLocationRelativeTo(this);
    ventanaEditar.setLayout(new BorderLayout());
    ventanaEditar.setResizable(false);

    // 🟢 ENCABEZADO
    JPanel encabezado = new JPanel();
    encabezado.setBackground(new Color(0, 153, 102));
    encabezado.setPreferredSize(new Dimension(400, 60));
    JLabel lblTitulo = new JLabel("Editar Paciente");
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
    lblTitulo.setForeground(Color.WHITE);
    encabezado.add(lblTitulo);

    // 📋 CAMPOS DE FORMULARIO
    JPanel panelCampos = new JPanel(new GridLayout(7, 2, 10, 12));
    panelCampos.setBackground(Color.WHITE);
    panelCampos.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

    JLabel[] etiquetas = {
        new JLabel("Nombre:"), new JLabel("Correo:"),
        new JLabel("Edad:"), new JLabel("Teléfono:"),
        new JLabel("Sexo:"), new JLabel("Dirección:"),
        new JLabel("Identificación:")
    };

    JTextField txtNombre = new JTextField(paciente.getNombre());
    JTextField txtCorreo = new JTextField(paciente.getCorreo());
    JTextField txtEdad = new JTextField(String.valueOf(paciente.getEdad()));
    JTextField txtTelefono = new JTextField(paciente.getTelefono());
    JComboBox<String> cbSexo = new JComboBox<>(new String[]{"Masculino", "Femenino"});
    cbSexo.setSelectedItem(paciente.getSexo());
    JTextField txtDireccion = new JTextField(paciente.getDireccion());
    JTextField txtIdentificacion = new JTextField(paciente.getIdentificacion());

    Font fuente = new Font("Segoe UI", Font.PLAIN, 14);
    for (JLabel lbl : etiquetas) {
        lbl.setFont(fuente);
        lbl.setForeground(new Color(40, 70, 40));
    }

    JTextField[] campos = {txtNombre, txtCorreo, txtEdad, txtTelefono, txtDireccion, txtIdentificacion};
    for (JTextField campo : campos) {
        campo.setFont(fuente);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 220, 180), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    panelCampos.add(etiquetas[0]); panelCampos.add(txtNombre);
    panelCampos.add(etiquetas[1]); panelCampos.add(txtCorreo);
    panelCampos.add(etiquetas[2]); panelCampos.add(txtEdad);
    panelCampos.add(etiquetas[3]); panelCampos.add(txtTelefono);
    panelCampos.add(etiquetas[4]); panelCampos.add(cbSexo);
    panelCampos.add(etiquetas[5]); panelCampos.add(txtDireccion);
    panelCampos.add(etiquetas[6]); panelCampos.add(txtIdentificacion);

    // 🎛️ BOTONES
    JPanel panelBotones = new JPanel();
    panelBotones.setBackground(Color.WHITE);
    panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

    JButton btnGuardar = new JButton("Guardar Cambios");
    JButton btnCancelar = new JButton("Cancelar");

    Color verde = new Color(0, 153, 102);

    btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 15));
    btnGuardar.setBackground(verde);
    btnGuardar.setForeground(Color.WHITE);
    btnGuardar.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
    btnGuardar.setFocusPainted(false);
    btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 15));
    btnCancelar.setBackground(verde);
    btnCancelar.setForeground(Color.WHITE);
    btnCancelar.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
    btnCancelar.setFocusPainted(false);
    btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    // Efecto hover
    btnGuardar.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent e) { btnGuardar.setBackground(new Color(0, 180, 120)); }
        public void mouseExited(MouseEvent e) { btnGuardar.setBackground(verde); }
    });
    btnCancelar.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent e) { btnCancelar.setBackground(new Color(255, 60, 60)); }
        public void mouseExited(MouseEvent e) { btnCancelar.setBackground(verde); }
    });

    panelBotones.add(btnGuardar);
    panelBotones.add(btnCancelar);

    // 📦 ENSAMBLAR TODO
    ventanaEditar.add(encabezado, BorderLayout.NORTH);
    ventanaEditar.add(panelCampos, BorderLayout.CENTER);
    ventanaEditar.add(panelBotones, BorderLayout.SOUTH);

    // 🧠 FUNCIONALIDAD
    btnGuardar.addActionListener(e -> {
        try {
            paciente.setNombre(txtNombre.getText().trim());
            paciente.setCorreo(txtCorreo.getText().trim());
            paciente.setEdad(Integer.parseInt(txtEdad.getText().trim()));
            paciente.setTelefono(txtTelefono.getText().trim());
            paciente.setSexo(cbSexo.getSelectedItem().toString());
            paciente.setDireccion(txtDireccion.getText().trim());
            paciente.setIdentificacion(txtIdentificacion.getText().trim());

            boolean ok = pacienteDAO.actualizarPaciente(paciente);
            if (ok) {
                JOptionPane.showMessageDialog(ventanaEditar, "✅ Paciente actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                ventanaEditar.dispose();
                cargarPacientes();
            } else {
                JOptionPane.showMessageDialog(ventanaEditar, "❌ No se pudo actualizar el paciente.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(ventanaEditar, "⚠️ La edad debe ser un número válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(ventanaEditar, "⚠️ Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    btnCancelar.addActionListener(e -> ventanaEditar.dispose());

    // Mostrar ventana
    ventanaEditar.setVisible(true);
}



    private void buscarPaciente() {
        String nombre = txtBuscarPaciente.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre para buscar.");
            return;
        }
        modeloPacientes.setRowCount(0);
        List<Paciente> lista = pacienteDAO.buscarPacientePorNombre(nombre);
        for (Paciente p : lista) {
            modeloPacientes.addRow(new Object[]{
                    p.getIdPaciente(), p.getNombre(), p.getCorreo(),
                    p.getEdad(), p.getTelefono(), p.getSexo(),
                    p.getDireccion(), p.getIdentificacion()
            });
        }
    }

    private void eliminarPaciente() {
        int fila = tablaPacientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente.");
            return;
        }
        int id = (int) modeloPacientes.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este paciente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = pacienteDAO.eliminarPaciente(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Paciente eliminado correctamente.");
                cargarPacientes();
            }
        }
    }

    // ===== PANEL DE CITAS =====
    private JPanel crearPanelCitas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        modeloCitas = new DefaultTableModel(new String[]{
                "ID", "Paciente", "Médico", "Fecha", "Hora", "Modalidad"
        }, 0);

        tablaCitas = new JTable(modeloCitas);
        tablaCitas.setRowHeight(28);
        tablaCitas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaCitas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(new JScrollPane(tablaCitas), BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        botones.setBackground(new Color(245, 245, 245));

        btnAgendarCita = crearBoton("Agendar Cita", new Color(66, 166, 105));
        btnEditarCita = crearBoton("Editar", new Color(66, 166, 105));
        btnEliminarCita = crearBoton("Eliminar", new Color(66, 166, 105));
        btnActualizarCitas = crearBoton("Actualizar Lista", new Color(66, 166, 105));

        botones.add(btnAgendarCita);
        botones.add(btnEditarCita);
        botones.add(btnEliminarCita);
        botones.add(btnActualizarCitas);

        panel.add(botones, BorderLayout.SOUTH);
        cargarCitas();

        btnActualizarCitas.addActionListener(e -> cargarCitas());
        btnAgendarCita.addActionListener(e -> new ModalAgendarCita(this));
        btnEditarCita.addActionListener(e -> editarCita());


        return panel;
    }

    private void cargarCitas() {
        modeloCitas.setRowCount(0);
        List<Cita> citas = citaDAO.selectCita();
        MedicoDao medicoDAO = new MedicoDao();
        PacienteDao pacienteDAO = new PacienteDao();

        for (Cita c : citas) {
            Paciente p = pacienteDAO.obtenerPacientePorId(c.getIdPaciente());
            Medico m = medicoDAO.obtenerMedicoPorId(c.getIdMedico());
            modeloCitas.addRow(new Object[]{
                    c.getIdCita(),
                    p != null ? p.getNombre() : "Desconocido",
                    m != null ? m.getNombre() : "Desconocido",
                    c.getFechaCita(),
                    c.getHoraCita(),
                    (c.getIdModalidad() == 1 ? "Presencial" : "Virtual")
            });
        }
    }

private void editarCita() {
    int fila = tablaCitas.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "⚠️ Seleccione una cita para editar.");
        return;
    }

    // Obtener datos actuales desde la tabla
    int idCita = Integer.parseInt(modeloCitas.getValueAt(fila, 0).toString());
    String pacienteNombre = modeloCitas.getValueAt(fila, 1).toString();
    String medicoNombre = modeloCitas.getValueAt(fila, 2).toString();
    String fechaActual = modeloCitas.getValueAt(fila, 3).toString();
    String horaActual = modeloCitas.getValueAt(fila, 4).toString();
    String modalidadActual = modeloCitas.getValueAt(fila, 5).toString();

    // Crear ventana de edición moderna
    JDialog ventanaEditar = new JDialog(this, "Editar Cita - Sanavit", true);
    ventanaEditar.setSize(400, 400);
    ventanaEditar.setLocationRelativeTo(this);
    ventanaEditar.setLayout(new BorderLayout());
    ventanaEditar.setResizable(false);

    // 🟢 Encabezado
    JPanel encabezado = new JPanel();
    encabezado.setBackground(new Color(0, 153, 102));
    encabezado.setPreferredSize(new Dimension(400, 60));
    JLabel lblTitulo = new JLabel("Editar Cita");
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
    lblTitulo.setForeground(Color.WHITE);
    encabezado.add(lblTitulo);

    // 📅 Panel central
    JPanel panelCampos = new JPanel(new GridLayout(3, 2, 10, 10));
    panelCampos.setBackground(Color.WHITE);
    panelCampos.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

    JTextField txtFecha = new JTextField(fechaActual);
    JTextField txtHora = new JTextField(horaActual);
    JComboBox<String> cbModalidad = new JComboBox<>(new String[]{"Presencial", "Virtual"});
    cbModalidad.setSelectedItem(modalidadActual);

    panelCampos.add(new JLabel("Nueva Fecha (YYYY-MM-DD):"));
    panelCampos.add(txtFecha);
    panelCampos.add(new JLabel("Nueva Hora (HH:MM:SS):"));
    panelCampos.add(txtHora);
    panelCampos.add(new JLabel("Modalidad:"));
    panelCampos.add(cbModalidad);

    // 🎛️ Panel de botones
    JPanel panelBotones = new JPanel();
    panelBotones.setBackground(Color.WHITE);

    JButton btnGuardar = new JButton("Guardar Cambios");
    JButton btnCancelar = new JButton("Cancelar");

    Color colorPrincipal = new Color(0, 153, 102);
    btnGuardar.setBackground(colorPrincipal);
    btnGuardar.setForeground(Color.WHITE);
    btnGuardar.setFocusPainted(false);
    btnGuardar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    btnCancelar.setBackground(new Color(200, 0, 0));
    btnCancelar.setForeground(Color.WHITE);
    btnCancelar.setFocusPainted(false);
    btnCancelar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    // Efecto hover
    btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            btnGuardar.setBackground(new Color(0, 153, 102));
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            btnGuardar.setBackground(colorPrincipal);
        }
    });

    btnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            btnCancelar.setBackground(new Color(200, 0, 0));
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            btnCancelar.setBackground(new Color(0, 153, 102));
        }
    });

    panelBotones.add(btnGuardar);
    panelBotones.add(btnCancelar);

    // 📦 Añadir todo a la ventana
    ventanaEditar.add(encabezado, BorderLayout.NORTH);
    ventanaEditar.add(panelCampos, BorderLayout.CENTER);
    ventanaEditar.add(panelBotones, BorderLayout.SOUTH);

    // 🧠 Funcionalidad de botones
    btnGuardar.addActionListener(e -> {
        try {
            String nuevaFecha = txtFecha.getText().trim();
            String nuevaHora = txtHora.getText().trim();
            String nuevaModalidad = cbModalidad.getSelectedItem().toString();
            int idModalidad = nuevaModalidad.equals("Presencial") ? 1 : 2;

            // Obtener paciente y médico desde la BD
            PacienteDao pacienteDAO = new PacienteDao();
            MedicoDao medicoDAO = new MedicoDao();

            List<Paciente> pacientes = pacienteDAO.buscarPacientePorNombre(pacienteNombre);
            Paciente paciente = (pacientes != null && !pacientes.isEmpty()) ? pacientes.get(0) : null;
            Medico medico = medicoDAO.obtenerPorNombre(medicoNombre);

            if (paciente == null || medico == null) {
                JOptionPane.showMessageDialog(ventanaEditar, "⚠️ No se encontró al paciente o médico.");
                return;
            }

            // Crear objeto Cita
            Cita cita = new Cita();
            cita.setIdCita(idCita);
            cita.setIdMedico(medico.getIdMedico());
            cita.setIdPaciente(paciente.getIdPaciente());
            cita.setIdEstadoCita(1);
            cita.setIdModalidad(idModalidad);
            cita.setFechaCita(java.sql.Date.valueOf(nuevaFecha));
            cita.setHoraCita(java.sql.Time.valueOf(nuevaHora));

            // Actualizar BD
            boolean actualizado = citaDAO.updateCita(cita);

            if (actualizado) {
                JOptionPane.showMessageDialog(ventanaEditar, "✅ Cita actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarCitas();
                ventanaEditar.dispose();
            } else {
                JOptionPane.showMessageDialog(ventanaEditar, "❌ No se pudo actualizar la cita.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(ventanaEditar, "⚠️ Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    });

    btnCancelar.addActionListener(e -> ventanaEditar.dispose());

    // Mostrar ventana
    ventanaEditar.setVisible(true);
}



    // ===== BOTÓN CON EFECTO HOVER =====
    private JButton crearBoton(String texto, Color colorBase) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(colorBase);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(colorBase.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(colorBase); }
        });
        return btn;
    }

    // ===== CARGAR ÍCONO =====
    private ImageIcon crearIcono(String ruta) {
        java.net.URL url = getClass().getResource(ruta);
        if (url != null) {
            return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
        }
        return null;
    }
}
