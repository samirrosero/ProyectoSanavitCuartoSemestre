package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class MedicoDao {

    // 🔹 Insertar médico (usa CALL insertar_medico)
    public boolean insertarMedico(Medico medico) {
        boolean state = false;
        String sql = "CALL insertar_medico(?, ?, ?)";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, medico.getNombre());
            pst.setString(2, medico.getEspecialidad());
            pst.setInt(3, medico.getIdUsuario());

            boolean hasResultSet = pst.execute();

            if (hasResultSet) {
                try (ResultSet rs = pst.getResultSet()) {
                    if (rs.next()) {
                        medico.setIdMedico(rs.getInt("id_medico"));
                        state = true;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error insertarMedico (procedimiento almacenado): " + e.getMessage());
        }
        return state;
    }

    // 🔹 Listar todos los médicos
    public static List<Medico> listarMedicos() {
        List<Medico> lista = new ArrayList<>();
        String sql = "CALL listar_medicos()";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Medico m = new Medico(
                        rs.getInt("id_medico"),
                        rs.getString("nombre"),
                        rs.getString("especialidad"),
                        rs.getInt("id_usuario")
                );
                lista.add(m);
            }

        } catch (SQLException e) {
            System.err.println("Error listarMedicos: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 Obtener médico por ID
    public Medico obtenerMedicoPorId(int idMedico) {
        Medico m = null;
        String sql = "CALL obtener_medico_por_id(?)";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idMedico);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    m = new Medico(
                            rs.getInt("id_medico"),
                            rs.getString("nombre"),
                            rs.getString("especialidad"),
                            rs.getInt("id_usuario")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error obtenerMedicoPorId: " + e.getMessage());
        }

        return m;
    }

    // 🔹 Obtener médico por ID de usuario
    public Medico obtenerMedicoPorIdUsuario(int idUsuario) {
        Medico m = null;
        String sql = "CALL obtener_medico_por_id_usuario(?)";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idUsuario);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    m = new Medico(
                            rs.getInt("id_medico"),
                            rs.getString("nombre"),
                            rs.getString("especialidad"),
                            rs.getInt("id_usuario")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error obtenerMedicoPorIdUsuario: " + e.getMessage());
        }

        return m;
    }

    // 🔹 Actualizar médico
    public boolean updateMedico(Medico medico) {
        boolean state = false;
        String sql = "CALL actualizar_medico(?, ?, ?)";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, medico.getIdMedico());
            pst.setString(2, medico.getNombre());
            pst.setString(3, medico.getEspecialidad());

            int res = pst.executeUpdate();
            state = res > 0;

        } catch (SQLException e) {
            System.err.println("Error updateMedico: " + e.getMessage());
        }

        return state;
    }

    // 🔹 Eliminar médico
    public boolean deleteMedico(int idMedico) {
        boolean state = false;
        String sql = "CALL eliminar_medico(?)";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idMedico);
            int res = pst.executeUpdate();
            state = res > 0;

        } catch (SQLException e) {
            System.err.println("Error deleteMedico: " + e.getMessage());
        }

        return state;
    }

    // 🔹 Obtener médico por nombre
    public Medico obtenerPorNombre(String nombreMedico) {
        Medico medico = null;
        String sql = "CALL obtener_medico_por_nombre(?)";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, nombreMedico);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    medico = new Medico(
                            rs.getInt("id_medico"),
                            rs.getString("nombre"),
                            rs.getString("especialidad"),
                            rs.getInt("id_usuario")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al buscar médico por nombre: " + e.getMessage());
        }

        return medico;
    }
}
