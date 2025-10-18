package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecetaMedicaDao {

    public boolean insertarReceta(RecetaMedica r) {
        boolean state = false;
        String sql = "INSERT INTO receta_medica (id_historia_clinica, medicamento, indicaciones) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, r.getIdHistoriaClinica());
            pst.setString(2, r.getMedicamento());
            pst.setString(3, r.getIndicaciones());

            int res = pst.executeUpdate();
            if (res > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) r.setIdReceta(rs.getInt(1));
                }
                state = true;
            }
        } catch (SQLException e) {
            System.out.println("Error insertarReceta: " + e.getMessage());
        }
        return state;
    }

    public RecetaMedica obtenerPorId(int idReceta) {
        RecetaMedica r = null;
        String sql = "SELECT * FROM receta_medica WHERE id_receta = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idReceta);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    r = new RecetaMedica(0, 0, null, null);
                    r.setIdReceta(rs.getInt("id_receta"));
                    r.setIdHistoriaClinica(rs.getInt("id_historia_clinica"));
                    r.setMedicamento(rs.getString("medicamento"));
                    r.setIndicaciones(rs.getString("indicaciones"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error obtenerPorId Receta: " + e.getMessage());
        }
        return r;
    }

    public List<RecetaMedica> listarRecetas() {
        List<RecetaMedica> lista = new ArrayList<>();
        String sql = "SELECT * FROM receta_medica";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                RecetaMedica r = new RecetaMedica(0, 0, null, null);
                r.setIdReceta(rs.getInt("id_receta"));
                r.setIdHistoriaClinica(rs.getInt("id_historia_clinica"));
                r.setMedicamento(rs.getString("medicamento"));
                r.setIndicaciones(rs.getString("indicaciones"));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.out.println("Error listarRecetas: " + e.getMessage());
        }
        return lista;
    }

    public boolean updateReceta(RecetaMedica r) {
        boolean state = false;
        String sql = "UPDATE receta_medica SET id_historia_clinica=?, medicamento=?, indicaciones=? WHERE id_receta=?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, r.getIdHistoriaClinica());
            pst.setString(2, r.getMedicamento());
            pst.setString(3, r.getIndicaciones());
            pst.setInt(4, r.getIdReceta());
            int res = pst.executeUpdate();
            state = res > 0;
        } catch (SQLException e) {
            System.out.println("Error updateReceta: " + e.getMessage());
        }
        return state;
    }

    public boolean deleteReceta(int idReceta) {
        boolean state = false;
        String sql = "DELETE FROM receta_medica WHERE id_receta = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idReceta);
            int res = pst.executeUpdate();
            state = res > 0;
        } catch (SQLException e) {
            System.out.println("Error deleteReceta: " + e.getMessage());
        }
        return state;
    }
}
