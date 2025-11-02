package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PortafolioDao {

    public boolean insertarPortafolio(Portafolio p) {
        boolean state = false;
        String sql = "INSERT INTO portafolio (salud, convenios, afiliaciones) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, p.getSalud());
            pst.setString(2, p.getConvenio());
            pst.setString(3, p.getAfiliaciones());

            int res = pst.executeUpdate();
            if (res > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) p.setIdPortafolio(rs.getInt(1));
                }
                state = true;
            }
        } catch (SQLException e) {
            System.out.println("Error insertarPortafolio: " + e.getMessage());
        }
        return state;
    }

    public Portafolio obtenerPorId(int idPortafolio) {
        Portafolio p = null;
        String sql = "SELECT * FROM portafolio WHERE id_portafolio = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idPortafolio);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    p = new Portafolio(0, null, null, null);
                    p.setIdPortafolio(rs.getInt("id_portafolio"));
                    p.setSalud(rs.getString("salud"));
                    p.setConvenios(rs.getString("convenios"));
                    p.setAfiliaciones(rs.getString("afiliaciones"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error obtenerPorId Portafolio: " + e.getMessage());
        }
        return p;
    }

    public List<Portafolio> listarPortafolios() {
        List<Portafolio> lista = new ArrayList<>();
        String sql = "SELECT * FROM portafolio";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Portafolio p = new Portafolio(0, null, null, null);
                p.setIdPortafolio(rs.getInt("id_portafolio"));
                p.setSalud(rs.getString("salud"));
                p.setConvenios(rs.getString("convenios"));
                p.setAfiliaciones(rs.getString("afiliaciones"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error listarPortafolios: " + e.getMessage());
        }
        return lista;
    }

    public boolean updatePortafolio(Portafolio p) {
        boolean state = false;
        String sql = "UPDATE portafolio SET salud=?, convenios=?, afiliaciones=? WHERE id_portafolio=?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, p.getSalud());
            pst.setString(2, p.getConvenio());
            pst.setString(3, p.getAfiliaciones());
            pst.setInt(4, p.getIdPortafolio());
            int res = pst.executeUpdate();
            state = res > 0;
        } catch (SQLException e) {
            System.out.println("Error updatePortafolio: " + e.getMessage());
        }
        return state;
    }

    public boolean deletePortafolio(int idPortafolio) {
        boolean state = false;
        String sql = "DELETE FROM portafolio WHERE id_portafolio = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idPortafolio);
            int res = pst.executeUpdate();
            state = res > 0;
        } catch (SQLException e) {
            System.out.println("Error delete Portafolio: " + e.getMessage());
        }
        return state;
    }
    // 🔹 Mapeo del ResultSet → Objeto Portafolio
    private Portafolio mapPortafolio(ResultSet rs) throws SQLException {
        return new Portafolio(
            rs.getInt("id_portafolio"),
            rs.getString("salud"),
            rs.getString("convenioss"),
            rs.getString("afiliaciones")
        );
    }
}
