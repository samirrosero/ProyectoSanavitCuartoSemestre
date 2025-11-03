package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao {

    // Crear nuevo paciente
    public boolean insertarPaciente(Paciente paciente) {
        String sql = "INSERT INTO paciente (nombre, correo, edad, telefono, sexo, direccion, identificacion, id_usuario) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, paciente.getNombre());
            stmt.setString(2, paciente.getCorreo());
            stmt.setInt(3, paciente.getEdad());
            stmt.setString(4, paciente.getTelefono());
            stmt.setString(5, paciente.getSexo());
            stmt.setString(6, paciente.getDireccion());
            stmt.setString(7, paciente.getIdentificacion());
            stmt.setInt(8, paciente.getIdUsuario());

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        paciente.setIdPaciente(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar paciente: " + e.getMessage());
        }
        return false;
    }

    // Actualizar paciente
    public boolean actualizarPaciente(Paciente paciente) {
        String sql = "UPDATE paciente SET nombre=?, correo=?, edad=?, telefono=?, sexo=?, direccion=?, identificacion=?, id_usuario=? " +
                     "WHERE id_paciente=?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, paciente.getNombre());
            stmt.setString(2, paciente.getCorreo());
            stmt.setInt(3, paciente.getEdad());
            stmt.setString(4, paciente.getTelefono());
            stmt.setString(5, paciente.getSexo());
            stmt.setString(6, paciente.getDireccion());
            stmt.setString(7, paciente.getIdentificacion());
            stmt.setInt(8, paciente.getIdUsuario());
            stmt.setInt(9, paciente.getIdPaciente());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar paciente: " + e.getMessage());
        }
        return false;
    }

    // Eliminar paciente
    public boolean eliminarPaciente(int idPaciente) {
        String sql = "DELETE FROM paciente WHERE id_paciente = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPaciente);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar paciente: " + e.getMessage());
        }
        return false;
    }

    // Obtener paciente por ID
    public Paciente obtenerPacientePorId(int idPaciente) {
        String sql = "SELECT * FROM paciente WHERE id_paciente = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPaciente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Paciente(
                        rs.getInt("id_paciente"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getInt("edad"),
                        rs.getString("telefono"),
                        rs.getString("sexo"),
                        rs.getString("direccion"),
                        rs.getString("identificacion"),
                        rs.getInt("id_usuario")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener paciente: " + e.getMessage());
        }
        return null;
    }
// Obtener paciente por ID de usuario
    public Paciente obtenerPacientePorIdUsuario(int idUsuario) {
        String sql = "SELECT * FROM paciente WHERE id_usuario = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Paciente(
                        rs.getInt("id_paciente"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getInt("edad"),
                        rs.getString("telefono"),
                        rs.getString("sexo"),
                        rs.getString("direccion"),
                        rs.getString("identificacion"),
                        rs.getInt("id_usuario")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener paciente por ID de usuario: " + e.getMessage());
        }
        return null;
    }

    // Listar todos los pacientes
    public List<Paciente> obtenerTodosLosPacientes() {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM paciente";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Paciente p = new Paciente(
                    rs.getInt("id_paciente"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getInt("edad"),
                    rs.getString("telefono"),
                    rs.getString("sexo"),
                    rs.getString("direccion"),
                    rs.getString("identificacion"),
                    rs.getInt("id_usuario")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar pacientes: " + e.getMessage());
        }
        return lista;
    }

    // Buscar paciente por nombre (opcional, para tabla Swing)
    public List<Paciente> buscarPacientePorNombre(String nombre) {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM paciente WHERE nombre LIKE ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nombre + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Paciente p = new Paciente(
                        rs.getInt("id_paciente"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getInt("edad"),
                        rs.getString("telefono"),
                        rs.getString("sexo"),
                        rs.getString("direccion"),
                        rs.getString("identificacion"),
                        rs.getInt("id_usuario")
                    );
                    lista.add(p);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar pacientes: " + e.getMessage());
        }
        return lista;
    }
}
