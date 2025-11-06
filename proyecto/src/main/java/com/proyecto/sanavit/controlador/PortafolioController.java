package com.proyecto.sanavit.controlador;
import com.proyecto.sanavit.modelo.*;


public class PortafolioController {
    private PortafolioDao portafolioDao;

    public PortafolioController() {
        this.portafolioDao = new PortafolioDao();
    }

    public boolean registrarPortafolio(Portafolio portafolio) {
        if (portafolio == null) {
            System.err.println("Error: el objeto Portafolio es nulo.");
            return false;
        }
        // Aquí podrías añadir validaciones adicionales si fueran necesarias
        return portafolioDao.insertarPortafolio(portafolio);
    }

}
