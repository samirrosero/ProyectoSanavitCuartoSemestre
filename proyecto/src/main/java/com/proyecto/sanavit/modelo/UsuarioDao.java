package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.*;

public class UsuarioDao {

    // === AUTENTICAR USUARIO ===
    public Usuario autenticarUsuario(String nombreUsuario, String contrasena) {
        Usuario u = null;
        String sql = """
            SELECT u.id_Usuario, u.id_rol, u.nombre_Usuario, u.contraseña, r.nombre_rol AS nombreRol
            FROM Usuario u
            JOIN rol r ON u.id_rol = r.id_rol
            WHERE u.nombre_Usuario = ? AND u.contraseña = ?
        """;

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombreUsuario);
            stmt.setString(2, contrasena);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    u = new Usuario(
                        rs.getInt("id_Usuario"),
                        rs.getInt("id_rol"),
                        rs.getString("nombre_Usuario"),
                        rs.getString("contraseña"),
                        rs.getString("nombreRol")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al autenticar usuario: " + e.getMessage());
        }

        return u;
    }

    // === INSERTAR USUARIO ===
    public int insertarUsuario(Usuario u) {
        int idGenerado = -1;
        String sql = "INSERT INTO Usuario (id_rol, nombre_Usuario, contraseña) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, u.getIdRol());
            stmt.setString(2, u.getNombreUsuario());
            stmt.setString(3, u.getContraseña());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idGenerado = generatedKeys.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar usuario: " + e.getMessage());
        }

        return idGenerado;
    }

    // === OBTENER USUARIO POR CREDENCIALES ===
    public Usuario obtenerUsuario(String nombre, String contrasena) {
        Usuario u = null;
        String sql = "SELECT * FROM Usuario WHERE nombre_Usuario = ? AND contraseña = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, contrasena);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    u = new Usuario(0, 0, null, null, null);
                    u.setIdUsuario(rs.getInt("id_Usuario"));
                    u.setIdRol(rs.getInt("id_rol"));
                    u.setNombreUsuario(rs.getString("nombre_Usuario"));
                    u.setContraseña(rs.getString("contraseña"));

                  }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener usuario: " + e.getMessage());
        }

        return u;
    }

    // === OBTENER ID DE ROL POR NOMBRE ===
    public int obtenerIdRolPorNombre(String nombreRol) {
        int idRol = -1;
        String sql = "SELECT id_rol FROM rol WHERE LOWER(nombre_rol) = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombreRol.toLowerCase());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idRol = rs.getInt("id_rol");
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener ID del rol: " + e.getMessage());
        }

        return idRol;
    }
    // Cambiar la contraseña de un usuario
    public boolean cambiarContraseña(int idUsuario, String nuevaContraseña) {
    String sql = "UPDATE usuario SET contraseña = ? WHERE id_usuario = ?";
    try (Connection conn = ConexionDatabase.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setString(1, nuevaContraseña);
        pst.setInt(2, idUsuario);
        return pst.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error al cambiar contraseña: " + e.getMessage());
        return false;
    }
}


    // === OBTENER TODOS LOS ROLES ===
    public Map<String, Integer> obtenerRoles() {
        Map<String, Integer> roles = new HashMap<>();
        String sql = "SELECT id_rol, nombre_rol FROM rol";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                roles.put(rs.getString("nombre_rol"), rs.getInt("id_rol"));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener roles: " + e.getMessage());
        }

        return roles;
    }
}
