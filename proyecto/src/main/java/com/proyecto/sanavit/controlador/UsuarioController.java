package com.proyecto.sanavit.controlador;

import java.util.Map;

import com.proyecto.sanavit.modelo.Usuario;
import com.proyecto.sanavit.modelo.UsuarioDao;

public class UsuarioController {
    
    private UsuarioDao usuarioDao;

    public UsuarioController() {
        this.usuarioDao = new UsuarioDao();
    }

    public Usuario autenticarUsuario(String nombreUsuario, String contrasena) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty() ||
            contrasena == null || contrasena.trim().isEmpty()) {
            System.err.println("Error: Nombre de usuario o contraseña inválidos.");
            return null;
        }
        return usuarioDao.autenticarUsuario(nombreUsuario, contrasena);
    }

    public int registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            System.err.println("Error: el objeto Usuario es nulo.");
            return -1;
        }
        if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().trim().isEmpty() ||
            usuario.getContraseña() == null || usuario.getContraseña().trim().isEmpty()) {
            System.err.println("Error: Nombre de usuario o contraseña inválidos.");
            return -1;
        }
        return usuarioDao.insertarUsuario(usuario);
    }

    public Usuario obteneUsuario(String nombre, String contrasena) {
        if (nombre == null || nombre.trim().isEmpty() ||
            contrasena == null || contrasena.trim().isEmpty()) {
            System.err.println("Error: Nombre de usuario o contraseña inválidos.");
            return null;
        }
        return usuarioDao.obtenerUsuario(nombre, contrasena);

    }

    public int obtenerIdRolPorNombre(String nombreRol) {
        if (nombreRol == null || nombreRol.trim().isEmpty()) {
            System.err.println("Error: Nombre de rol inválido.");
            return -1;
        }
        return usuarioDao.obtenerIdRolPorNombre(nombreRol);
    }

    public Map<String, Integer> obtenerRoles() {
        return usuarioDao.obtenerRoles();
    }
}
