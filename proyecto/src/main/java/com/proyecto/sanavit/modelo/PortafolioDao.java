package com.proyecto.sanavit.modelo;

import java.sql.*;

public class PortafolioDao {

    public boolean insertarPortafolio(Portafolio portafolio) {
        String sql = "INSERT INTO portafolio (id_paciente, salud, afiliaciones) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, portafolio.getIdPaciente());
            stmt.setString(2, portafolio.getSalud());
            stmt.setString(3, portafolio.getAfiliaciones());

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar portafolio: " + e.getMessage());
            return false;
        }
    }
    public Portafolio obtenerPortafolioPorIdPaciente(int idPaciente) {
        String sql = "SELECT id_portafolio, id_paciente, salud, afiliaciones FROM portafolio WHERE id_paciente = ?";
        Portafolio portafolio = null;

        try (Connection conn = ConexionDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPaciente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    portafolio = new Portafolio();
                    portafolio.setIdPortafolio(rs.getInt("id_portafolio"));
                    portafolio.setIdPaciente(rs.getInt("id_paciente"));
                    portafolio.setSalud(rs.getString("salud"));
                    portafolio.setAfiliaciones(rs.getString("afiliaciones"));
                    
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener portafolio: " + e.getMessage());
        }

        return portafolio;
    }

}