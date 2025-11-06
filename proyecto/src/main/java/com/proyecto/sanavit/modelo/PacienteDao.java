package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao {

   public boolean insertarPaciente(Paciente paciente) {
    String sql = "{CALL insertar_paciente(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    try (Connection conn = ConexionDatabase.getConnection();
         CallableStatement stmt = conn.prepareCall(sql)) {

        // Parámetros de entrada
        stmt.setString(1, paciente.getNombre());
        stmt.setString(2, paciente.getCorreo());
        stmt.setInt(3, paciente.getEdad());
        stmt.setString(4, paciente.getTelefono());
        stmt.setString(5, paciente.getSexo());
        stmt.setString(6, paciente.getDireccion());
        stmt.setString(7, paciente.getIdentificacion());
        stmt.setInt(8, paciente.getIdUsuario());

        // Parámetro de salida (id generado)
        stmt.registerOutParameter(9, java.sql.Types.INTEGER);

        stmt.execute();

        // Obtener el ID generado desde el OUT del procedimiento
        int idGenerado = stmt.getInt(9);
        paciente.setIdPaciente(idGenerado);

        return true;
    } catch (SQLException e) {
        System.err.println("Error al insertar paciente (SP): " + e.getMessage());
    }
    return false;
}

// Actualizar paciente
public boolean actualizarPaciente(Paciente paciente) {
    String sql = "{CALL actualizar_paciente(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    try (Connection conn = ConexionDatabase.getConnection();
         CallableStatement stmt = conn.prepareCall(sql)) {

        stmt.setInt(1, paciente.getIdPaciente());
        stmt.setString(2, paciente.getNombre());
        stmt.setString(3, paciente.getCorreo());
        stmt.setInt(4, paciente.getEdad());
        stmt.setString(5, paciente.getTelefono());
        stmt.setString(6, paciente.getSexo());
        stmt.setString(7, paciente.getDireccion());
        stmt.setString(8, paciente.getIdentificacion());
        stmt.setInt(9, paciente.getIdUsuario());

        stmt.execute();
        return true;
    } catch (SQLException e) {
        System.err.println("Error al actualizar paciente (SP): " + e.getMessage());
    }
    return false;
}

// Eliminar paciente
public boolean eliminarPaciente(int idPaciente) {
    String sql = "{CALL eliminar_paciente(?)}";
    try (Connection conn = ConexionDatabase.getConnection();
         CallableStatement stmt = conn.prepareCall(sql)) {

        stmt.setInt(1, idPaciente);
        stmt.execute();
        return true;
    } catch (SQLException e) {
        System.err.println("Error al eliminar paciente (SP): " + e.getMessage());
    }
    return false;
}

// Obtener paciente por ID
public Paciente obtenerPacientePorId(int idPaciente) {
    String sql = "{CALL obtener_paciente_por_id(?)}";
    try (Connection conn = ConexionDatabase.getConnection();
         CallableStatement stmt = conn.prepareCall(sql)) {

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
        System.err.println("Error al obtener paciente por ID (SP): " + e.getMessage());
    }
    return null;
}

// Obtener paciente por ID de usuario
public Paciente obtenerPacientePorIdUsuario(int idUsuario) {
    String sql = "{CALL obtener_paciente_por_id_usuario(?)}";
    try (Connection conn = ConexionDatabase.getConnection();
         CallableStatement stmt = conn.prepareCall(sql)) {

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
        System.err.println("Error al obtener paciente por ID de usuario (SP): " + e.getMessage());
    }
    return null;
}

// Listar todos los pacientes
public List<Paciente> obtenerTodosLosPacientes() {
    List<Paciente> lista = new ArrayList<>();
    String sql = "{CALL listar_pacientes()}";

    try (Connection conn = ConexionDatabase.getConnection();
         CallableStatement stmt = conn.prepareCall(sql);
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
        System.err.println("Error al listar pacientes (SP): " + e.getMessage());
    }
    return lista;
}

// Buscar paciente por nombre
public List<Paciente> buscarPacientePorNombre(String nombre) {
    List<Paciente> lista = new ArrayList<>();
    String sql = "{CALL buscar_paciente_por_nombre(?)}";

    try (Connection conn = ConexionDatabase.getConnection();
         CallableStatement stmt = conn.prepareCall(sql)) {

        stmt.setString(1, nombre);
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
        System.err.println("Error al buscar pacientes (SP): " + e.getMessage());
    }
    return lista;
}

// Obtener paciente por identificación
public static Paciente obtenerPorIdentificacion(String identificacion) {
    Paciente paciente = null;
    String sql = "{CALL obtener_paciente_por_identificacion(?)}";
    try (Connection conn = ConexionDatabase.getConnection();
         CallableStatement stmt = conn.prepareCall(sql)) {

        stmt.setString(1, identificacion);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                paciente = new Paciente(
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
        System.err.println("Error al obtener paciente por identificación (SP): " + e.getMessage());
    }
    return paciente;
}

}
