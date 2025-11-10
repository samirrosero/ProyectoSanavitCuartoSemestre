package com.proyecto.sanavit.modelo;

import java.sql.Timestamp;

public class EjecucionCita {
    private int idEjecucionCita;
    private int idCita;
    private Timestamp fechaHoraIngreso;
    private Timestamp fechaHoraSalida;   
    private int duracion;
    
    // Constructor
    public EjecucionCita() {
    } 
    public EjecucionCita(int idEjecucionCita, int idCita, Timestamp fechaHoraIngreso, Timestamp fechaHoraSalida, int duracion) {
        this.idEjecucionCita = idEjecucionCita;
        this.idCita = idCita;
        this.fechaHoraIngreso = fechaHoraIngreso;
        this.fechaHoraSalida = fechaHoraSalida;
        this.duracion = duracion;
    }

    // Métodos get y set de cada atributo
    public int getIdEjecucionCita() {
        return idEjecucionCita;
    }
    public void setIdEjecucionCita(int idEjecucionCita) {
        this.idEjecucionCita = idEjecucionCita;
    }
    public int getIdCita() {
        return idCita;
    }
    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }
    public Timestamp getFechaHoraIngreso() {
        return fechaHoraIngreso;
    }
    public void setFechaHoraIngreso(Timestamp fechaHoraIngreso) {
        this.fechaHoraIngreso = fechaHoraIngreso;
    }
    public Timestamp getFechaHoraSalida() {
        return fechaHoraSalida;
    }
    public void setFechaHoraSalida(Timestamp fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }
    public int getDuracion() {
        return duracion;
    }
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
    

}

