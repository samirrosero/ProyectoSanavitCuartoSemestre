package com.proyecto.sanavit.controlador;

import java.util.List;

import com.proyecto.sanavit.modelo.HistoriaClinica;
import com.proyecto.sanavit.modelo.HistoriaClinicaDao;

public class HistoriaClinicaController {
    
    private HistoriaClinicaDao historiaClinicaDao;
    
    public HistoriaClinicaController() {
        this.historiaClinicaDao = new HistoriaClinicaDao();
    }

    public boolean registrarHistoriaClinica(com.proyecto.sanavit.modelo.HistoriaClinica historia) {
        if (historia == null) {
            System.err.println("Error: el objeto HistoriaClinica es nulo.");
            return false;
        }
        return historiaClinicaDao.insertarHistoria(historia);
    }

    public HistoriaClinica obtenerHistoriaClinicaPorId(int id) {
        if (id <= 0) {
            System.err.println("Error: ID inválido para búsqueda.");
            return null;
        }
        return historiaClinicaDao.obtenerPorId(id);
    }

    public List<HistoriaClinica> listarHistoriasClinicas() {
        return historiaClinicaDao.listarHistorias();
    }

    public boolean actualizarHistoriaClinica(HistoriaClinica historia) {
        if (historia == null || historia.getIdHistoriaClinica() <= 0) {
            System.err.println("Error: HistoriaClinica inválido para actualización.");
            return false;
        }
        return historiaClinicaDao.updateHistoria(historia);
    }

    public boolean eliminarHistoriaClinica(int idHistoria) {
        if (idHistoria <= 0) {
            System.err.println("Error: ID de HistoriaClinica inválido.");
            return false;
        }
        return historiaClinicaDao.deleteHistoria(idHistoria);
    }


}
