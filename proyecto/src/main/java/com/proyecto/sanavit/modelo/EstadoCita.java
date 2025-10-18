package com.proyecto.sanavit.modelo;

public class EstadoCita {
    private int idEstadoCita;
    private String nombreEstado;

    public EstadoCita(int idEstadoCita, String nombreEstado) {
        this.idEstadoCita = idEstadoCita;
        this.nombreEstado = nombreEstado;
    }

    // Métodos get y set de cada atributo
    public int getIdEstadoCita() {
        return idEstadoCita;
    }
    public void setIdEstadoCita(int idEstadoCita) {
        this.idEstadoCita = idEstadoCita;
    }
    public String getNombreEstado() {
        return nombreEstado;
    }
    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }
    
}
