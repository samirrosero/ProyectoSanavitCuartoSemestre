package com.proyecto.sanavit.controlador;

import com.proyecto.sanavit.modelo.*;
import java.util.List;

public class PortafolioController {

    private PortafolioDao portafolioDao = new PortafolioDao();

    // registrar

    public boolean registrarPortafolio(Portafolio portafolio) {
        if (portafolio == null) {
            System.err.println("Error: el objeto Portafolio es nulo.");
            return false;
        }
        return portafolioDao.insertarPortafolio(portafolio);
    }

    // ACTUALIZAR
    public boolean actualizarPortafolio(Portafolio portafolio) {
        if (portafolio == null || portafolio.getIdPortafolio() <= 0) {
            System.err.println("Error: Portafolio inválido para actualización.");
            return false;
        }
        return portafolioDao.updatePortafolio(portafolio);
    }

    // ELIMINAR
    public boolean eliminarPortafolio(int idPortafolio) {
        if (idPortafolio <= 0) {
            System.err.println("Error: ID de Portafolio inválido.");
            return false;
        }
        return portafolioDao.deletePortafolio(idPortafolio);
    }

    // OBTENER POR ID
    public Portafolio obtenerPortafolioPorId(int idPortafolio) {
        if (idPortafolio <= 0) {
            System.err.println("Error: ID inválido para búsqueda.");
            return null;
        }
        return portafolioDao.obtenerPorId(idPortafolio);
    }

    // LISTAR TODOS
    public List<Portafolio> listarPortafolios() {
        return portafolioDao.listarPortafolios();
    }

}
