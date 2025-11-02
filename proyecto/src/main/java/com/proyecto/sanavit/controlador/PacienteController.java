package com.proyecto.sanavit.controlador;

import com.proyecto.sanavit.modelo.Paciente;
import com.proyecto.sanavit.modelo.PacienteDao;
import java.util.List;

public class PacienteController {

    private PacienteDao pacienteDao;

    public PacienteController() {
        this.pacienteDao = new PacienteDao();
    }

    // ✅ Crear nuevo paciente
    public boolean registrarPaciente(Paciente paciente) {
        if (paciente == null) {
            System.err.println("Error: el objeto Paciente es nulo.");
            return false;
        }

        if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
            System.err.println("Error: el nombre del paciente es obligatorio.");
            return false;
        }

        return pacienteDao.insertarPaciente(paciente);
    }

    // ✅ Actualizar un paciente existente
    public boolean actualizarPaciente(Paciente paciente) {
        if (paciente == null || paciente.getIdPaciente() <= 0) {
            System.err.println("Error: paciente inválido para actualización.");
            return false;
        }
        return pacienteDao.actualizarPaciente(paciente);
    }

    // ✅ Eliminar paciente por ID
    public boolean eliminarPaciente(int idPaciente) {
        if (idPaciente <= 0) {
            System.err.println("Error: ID de paciente inválido.");
            return false;
        }
        return pacienteDao.eliminarPaciente(idPaciente);
    }

    // ✅ Obtener paciente por ID
    public Paciente obtenerPacientePorId(int idPaciente) {
        if (idPaciente <= 0) {
            System.err.println("Error: ID inválido para búsqueda.");
            return null;
        }
        return pacienteDao.obtenerPacientePorId(idPaciente);
    }

    // ✅ Listar todos los pacientes
    public List<Paciente> listarPacientes() {
        return pacienteDao.obtenerTodosLosPacientes();
    }

    // ✅ Buscar paciente por nombre
    public List<Paciente> buscarPacientesPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return listarPacientes();
        }
        return pacienteDao.buscarPacientePorNombre(nombre);
    }
}
