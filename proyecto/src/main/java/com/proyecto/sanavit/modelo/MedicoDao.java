package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDao {

    // Insertar
    public boolean insertarMedico(Medico medico) {
        boolean state = false;
        String sql = "INSERT INTO medico (nombre, especialidad, id_usuario) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, medico.getNombre());
            pst.setString(2, medico.getEspecialidad());
            pst.setInt(3, medico.getIdUsuario());

            int res = pst.executeUpdate();
            if (res > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        medico.setIdMedico(rs.getInt(1));
                    }
                }
                state = true;
            }
        } catch (SQLException e) {
            System.out.println("Error insertarMedico: " + e.getMessage());
        }
        return state;
    }

    // Listar todos
    public List<Medico> listarMedicos() {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT * FROM medico";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Medico m = new Medico(0, null, null, 0);
                m.setIdMedico(rs.getInt("id_medico"));
                m.setNombre(rs.getString("nombre"));
                m.setEspecialidad(rs.getString("especialidad"));
                m.setIdUsuario(rs.getInt("id_usuario"));
                lista.add(m);
            }
        } catch (SQLException e) {
            System.out.println("Error listarMedicos: " + e.getMessage());
        }
        return lista;
    }

    // Obtener por id
    public Medico obtenerMedicoPorId(int idMedico) {
        Medico m = null;
        String sql = "SELECT * FROM medico WHERE id_medico = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idMedico);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    m = new Medico(0, null, null, 0);
                    m.setIdMedico(rs.getInt("id_medico"));
                    m.setNombre(rs.getString("nombre"));
                    m.setEspecialidad(rs.getString("especialidad"));
                    m.setIdUsuario(rs.getInt("id_usuario"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error obtenerMedicoPorId: " + e.getMessage());
        }
        return m;
    }

    // Listar por id_usuario
    public Medico obtenerMedicoPorIdUsuario(int idUsuario) {
        Medico m = null;
        String sql = "SELECT * FROM medico WHERE id_usuario = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idUsuario);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    m = new Medico(0, null, null, 0);
                    m.setIdMedico(rs.getInt("id_medico"));
                    m.setNombre(rs.getString("nombre"));
                    m.setEspecialidad(rs.getString("especialidad"));
                    m.setIdUsuario(rs.getInt("id_usuario"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error obtenerMedicoPorIdUsuario: " + e.getMessage());
        }
        return m;
    }

    // Actualizar
    public boolean updateMedico(Medico medico) {
        boolean state = false;
        String sql = "UPDATE medico SET nombre = ?, especialidad = ? WHERE id_medico = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, medico.getNombre());
            pst.setString(2, medico.getEspecialidad());
            pst.setInt(3, medico.getIdMedico());
            int res = pst.executeUpdate();
            state = res > 0;
        } catch (SQLException e) {
            System.out.println("Error updateMedico: " + e.getMessage());
        }
        return state;
    }

    // Eliminar
    public boolean deleteMedico(int idMedico) {
        boolean state = false;
        String sql = "DELETE FROM medico WHERE id_medico = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idMedico);
            int res = pst.executeUpdate();
            state = res > 0;
        } catch (SQLException e) {
            System.out.println("Error deleteMedico: " + e.getMessage());
        }
        return state;
    }
}
