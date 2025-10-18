package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModalidadDao {

    public boolean insertarModalidad(Modalidad m) {
        boolean state = false;
        String sql = "INSERT INTO modalidad (nombre_modalidad) VALUES (?)";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, m.getNombreModalidad());
            int res = pst.executeUpdate();
            if (res > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) m.setIdModalidad(rs.getInt(1));
                }
                state = true;
            }
        } catch (SQLException ex) {
            System.out.println("Error insertarModalidad: " + ex.getMessage());
        }
        return state;
    }

    public Modalidad obtenerPorId(int id) {
        Modalidad m = null;
        String sql = "SELECT * FROM modalidad WHERE id_modalidad = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    m = new Modalidad(0, null);
                    m.setIdModalidad(rs.getInt("id_modalidad"));
                    m.setNombreModalidad(rs.getString("nombre_modalidad"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error obtenerPorId Modalidad: " + ex.getMessage());
        }
        return m;
    }

    public List<Modalidad> listarModalidades() {
        List<Modalidad> lista = new ArrayList<>();
        String sql = "SELECT * FROM modalidad";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Modalidad m = new Modalidad(0, null);
                m.setIdModalidad(rs.getInt("id_modalidad"));
                m.setNombreModalidad(rs.getString("nombre_modalidad"));
                lista.add(m);
            }
        } catch (SQLException ex) {
            System.out.println("Error listarModalidades: " + ex.getMessage());
        }
        return lista;
    }

    public boolean updateModalidad(Modalidad m) {
        boolean state = false;
        String sql = "UPDATE modalidad SET nombre_modalidad=? WHERE id_modalidad=?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, m.getNombreModalidad());
            pst.setInt(2, m.getIdModalidad());
            int res = pst.executeUpdate();
            state = res > 0;
        } catch (SQLException ex) {
            System.out.println("Error updateModalidad: " + ex.getMessage());
        }
        return state;
    }

    public boolean deleteModalidad(int id) {
        boolean state = false;
        String sql = "DELETE FROM modalidad WHERE id_modalidad = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            int res = pst.executeUpdate();
            state = res > 0;
        } catch (SQLException ex) {
            System.out.println("Error deleteModalidad: " + ex.getMessage());
        }
        return state;
    }
}
