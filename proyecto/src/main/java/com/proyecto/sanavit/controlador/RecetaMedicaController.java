package com.proyecto.sanavit.controlador;

import com.proyecto.sanavit.modelo.RecetaMedica;
import com.proyecto.sanavit.modelo.RecetaMedicaDao;
import java.util.List;

public class RecetaMedicaController {

    private RecetaMedicaDao recetaDao;

    public RecetaMedicaController() {
        this.recetaDao = new RecetaMedicaDao();
    }

    // === CREAR UNA NUEVA RECETA MÉDICA ===
    public boolean crearReceta(int idHistoriaClinica, String medicamento, String indicaciones) {
        if (medicamento == null || medicamento.trim().isEmpty()) {
            System.err.println("❌ El medicamento no puede estar vacío.");
            return false;
        }

        RecetaMedica receta = new RecetaMedica(0, idHistoriaClinica, medicamento, indicaciones);
        return recetaDao.insertarReceta(receta);
    }

    // === OBTENER UNA RECETA POR SU ID ===
    public RecetaMedica obtenerRecetaPorId(int idReceta) {
        return recetaDao.obtenerPorId(idReceta);
    }

    // === LISTAR TODAS LAS RECETAS ===
    public List<RecetaMedica> listarRecetas() {
        return recetaDao.listarRecetas();
    }

    // === ACTUALIZAR UNA RECETA ===
    public boolean actualizarReceta(int idReceta, int idHistoriaClinica, String medicamento, String indicaciones) {
        RecetaMedica receta = new RecetaMedica(idReceta, idHistoriaClinica, medicamento, indicaciones);
        return recetaDao.updateReceta(receta);
    }

    // === ELIMINAR UNA RECETA ===
    public boolean eliminarReceta(int idReceta) {
        return recetaDao.deleteReceta(idReceta);
    }

}
