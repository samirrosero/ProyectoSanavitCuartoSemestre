package com.proyecto.sanavit.controlador;
import com.proyecto.sanavit.modelo.*;
import java.util.List;

public class CitaController {
    
    private CitaDao citaDao;
    private MedicoDao medicoDao;
    private PacienteDao pacienteDao;

    public CitaController() {
        this.citaDao = new CitaDao();
        this.medicoDao = new MedicoDao();
        this.pacienteDao = new PacienteDao();
    }

    // ✅ Registrar nueva cita
    public boolean registrarCita(Cita cita) {
        if (cita == null) {
            System.err.println("Error: el objeto Cita es nulo.");
            return false;
        }
        // Validaciones adicionales (ej: existencia de medico, paciente, etc.)
        if (medicoDao.obtenerMedicoPorId(cita.getIdMedico()) == null) {
            System.err.println("Error: El médico con ID " + cita.getIdMedico() + " no existe.");
            return false;
        }
        if (pacienteDao.obtenerPacientePorId(cita.getIdPaciente()) == null) {
            System.err.println("Error: El paciente con ID " + cita.getIdPaciente() + " no existe.");
            return false;
        }
        return citaDao.insertarCita(cita);
    }

    // Cancelar cita == 3
    public boolean cancelarCita(int idCita) {
        if (idCita <= 0) {
            System.err.println("Error: ID de cita inválido.");
            return false;
        }
        // Suponiendo que el ID del estado "Cancelada" es 3
        int estadoCanceladaId = 3;
        return citaDao.actualizarEstadoCita(idCita, estadoCanceladaId);
    }
    // Confirmar cita == 2
    public boolean confirmarCita (int idCita) {
        if (idCita <= 0) {
            System.err.println("Error: ID de cita inválido.");
            return false;
        }
        // Suponiendo que el ID del estado "Confirmada" es 2
        int estadoConfirmadaId = 2;
        return citaDao.actualizarEstadoCita(idCita, estadoConfirmadaId);
    }
    // finalizar cita
    public boolean finalizarCita(int idCita){
        if(idCita <= 0){
            System.out.println("Error: ID de cita invalido");
            return false;
        }
        int estadoFinalizarCitaId= 4;
        return citaDao.actualizarEstadoCita(idCita, estadoFinalizarCitaId);
    }  
// actualizarCita, eliminarCita, obtenerCitaPorId, listarCitas, etc. pueden implementarse de manera similar con validaciones adecuadas.

public boolean actualizarCita(Cita cita) {
        if (cita == null || cita.getIdCita() <= 0) {
            System.err.println("Error: cita inválida para actualización.");
            return false;
        }
        return citaDao.updateCita(cita);
    }
    public boolean eliminarCita(int idCita) {
        if (idCita <= 0) {
            System.err.println("Error: ID de cita inválido.");
            return false;
        }
        return citaDao.deleteCita(idCita);
    }
    public Cita obtenerCitaPorId(int idCita) {
        if (idCita <= 0) {
            System.err.println("Error: ID inválido para búsqueda.");
            return null;
        }
        return citaDao.obtenerCitaPorId(idCita);
    }
    public List<Cita> listarCitas() {
        return citaDao.selectCita();
    }
    public List<Cita> obtenerCitaPorPaciente(int idPaciente) {
        if (idPaciente <= 0) {
            System.err.println("Error: ID de pÑaciente inválido.");
            return null;
        }
        return citaDao.obtenerCitaPorPaciente(idPaciente);
    }
    public List<Cita> obtenerCitaPorMedico(int idMedico) {
        if (idMedico <= 0) {
            System.err.println("Error: ID de médico inválido.");
            return null;
        }
        return citaDao.obtenerCitaPorMedico(idMedico);
    }

}
