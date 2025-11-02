package com.proyecto.sanavit.controlador;

import java.util.List;

import com.proyecto.sanavit.modelo.EjecucionCita;
import com.proyecto.sanavit.modelo.EjecucionCitaDao;

public class EjecucionCitaController {
    
    private EjecucionCitaDao ejecucionCitaDao;

    public EjecucionCitaController() {
        this.ejecucionCitaDao = new EjecucionCitaDao();
    }

    public boolean registrarEjecucion(com.proyecto.sanavit.modelo.EjecucionCita ejecucion) {
        if (ejecucion == null) {
            System.err.println("Error: el objeto EjecucionCita es nulo.");
            return false;
        }
        return ejecucionCitaDao.insertarEjecucion(ejecucion);
    }

    public EjecucionCita obtenerEjecucionPorId(int id) {
        if (id <= 0) {
            System.err.println("Error: ID inválido para búsqueda.");
            return null;
        }
        return ejecucionCitaDao.obtenerPorId(id);
    }

    public List<EjecucionCita> listarEjecuciones() {
        return ejecucionCitaDao.listarEjecuciones();
    }  
    
    public boolean actualizarEjecucion(EjecucionCita ejecucion) {
        if (ejecucion == null || ejecucion.getIdEjecucionCita() <= 0) {
            System.err.println("Error: EjecucionCita inválido para actualización.");
            return false;
        }
        return ejecucionCitaDao.updateEjecucion(ejecucion);
    }
    
    public boolean eliminarEjecucion(int idEjecucion) {
        if (idEjecucion <= 0) {
            System.err.println("Error: ID de EjecucionCita inválido.");
            return false;
        }
        return ejecucionCitaDao.deleteEjecucion(idEjecucion);
    }

}
