package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.*;

public class UsuarioDao {

    // === AUTENTICAR USUARIO ===
    public Usuario autenticarUsuario(String nombreUsuario, String contrasena) {
        Usuario u = null;
        String sql = "{CALL autenticar_usuario(?, ?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, nombreUsuario);
            stmt.setString(2, contrasena);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    u = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getInt("id_rol"),
                        rs.getString("nombre_usuario"),
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
        String sql = "{CALL insertar_usuario(?, ?, ?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, u.getIdRol());
            stmt.setString(2, u.getNombreUsuario());
            stmt.setString(3, u.getContraseña());

            boolean tieneResultados = stmt.execute();

            if (tieneResultados) {
                try (ResultSet rs = stmt.getResultSet()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt("id_generado");
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
        String sql = "{CALL obtener_usuario(?, ?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, contrasena);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    u = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getInt("id_rol"),
                        rs.getString("nombre_usuario"),
                        rs.getString("contraseña"),
                        null
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener usuario: " + e.getMessage());
        }

        return u;
    }

    // === BUSCAR USUARIO POR NOMBRE ===
    public Usuario buscarUsuarioPorNombre(String nombre) {
        Usuario u = null;
        String sql = "{CALL buscar_usuario_por_nombre(?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, nombre);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    u = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getInt("id_rol"),
                        rs.getString("nombre_usuario"),
                        rs.getString("contraseña"),
                        null
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar usuario: " + e.getMessage());
        }

        return u;
    }

    // === OBTENER ID DE ROL POR NOMBRE ===
    public int obtenerIdRolPorNombre(String nombreRol) {
        int idRol = -1;
        String sql = "{CALL obtener_id_rol_por_nombre(?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, nombreRol);

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

    // === CAMBIAR CONTRASEÑA ===
    public boolean cambiarContraseña(int idUsuario, String nuevaContraseña) {
        String sql = "{CALL cambiar_contraseña(?, ?)}";
        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setString(2, nuevaContraseña);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al cambiar contraseña: " + e.getMessage());
            return false;
        }
    }

    // === OBTENER TODOS LOS ROLES ===
    public Map<String, Integer> obtenerRoles() {
        Map<String, Integer> roles = new HashMap<>();
        String sql = "{CALL obtener_roles()}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
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
