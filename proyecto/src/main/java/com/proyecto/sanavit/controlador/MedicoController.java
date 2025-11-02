package com.proyecto.sanavit.controlador;

import java.util.List;

import com.proyecto.sanavit.modelo.Cita;
import com.proyecto.sanavit.modelo.CitaDao;
import com.proyecto.sanavit.modelo.Medico;
import com.proyecto.sanavit.modelo.MedicoDao;
import com.proyecto.sanavit.modelo.RecetaMedica;
import com.proyecto.sanavit.modelo.RecetaMedicaDao;

public class MedicoController {

    private MedicoDao medicoDao;

    public MedicoController() {
        this.medicoDao = new MedicoDao();
    }

    public boolean registrarMedico(Medico medico) {
        if (medico == null) {
            System.err.println("Error: el objeto Medico es nulo.");
            return false;
        }

        if (medico.getNombre() == null || medico.getNombre().trim().isEmpty()) {
            System.err.println("Error: el nombre del medico es obligatorio.");
            return false;
        }

        return medicoDao.insertarMedico(medico);
    }

    public List<Medico> listarMedicos() {
        return medicoDao.listarMedicos();
    }

    public Medico obtenerMedicoPorId(int idMedico) {
        if (idMedico <= 0) {
            System.err.println("Error: ID inválido para búsqueda.");
            return null;
        }
        return medicoDao.obtenerMedicoPorId(idMedico);
    }

    public Medico obtenerMedicoPorUsuarioId(int idUsuario) {
        if (idUsuario <= 0) {
            System.err.println("Error: ID de usuario inválido para búsqueda.");
            return null;
        }
        return medicoDao.obtenerMedicoPorIdUsuario(idUsuario);
    }

    public boolean actualizarMedico(Medico medico) {
        if (medico == null || medico.getIdMedico() <= 0) {
            System.err.println("Error: medico inválido para actualización.");
            return false;
        }
        return medicoDao.updateMedico(medico);
    }

    public boolean eliminarMedico(int idMedico) {
        if (idMedico <= 0) {
            System.err.println("Error: ID de medico inválido.");
            return false;
        }
        return medicoDao.deleteMedico(idMedico);
    }

    public void atenderCita(Medico medico, Cita cita) {
        medico.atender(cita);
        new CitaDao().actualizarEstado(cita); // ejemplo, si tienes un CitaDao
    }

    public RecetaMedica emitirReceta(Medico medico, Cita cita, String medicamento, String indicaciones) {
        RecetaMedica receta = medico.emitirReceta(cita, medicamento, indicaciones);
        new RecetaMedicaDao().insertarReceta(receta); // si la guardas en BD
        return receta;
    }
}
