package com.proyecto.sanavit.modelo;

import java.sql.Date;
import java.sql.Time;

public class Cita {
    private int idCita;
    private int idMedico;
    private int idPaciente;
    private int idEstadoCita;
    private int idModalidad;
    private int idPortafolio;
    private Date fechaCita;
    private Time horaCita;

    public Cita(int idCita, int idMedico, int idPaciente, int idEstadoCita, int idModalidad, int idPortafolio, Date fechaCita, Time horaCita) {
        this.idCita = idCita;
        this.idMedico = idMedico;
        this.idPaciente = idPaciente;
        this.idEstadoCita = idEstadoCita;
        this.idModalidad = idModalidad;
        this.idPortafolio = idPortafolio;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
    }

    // Métodos get y set de cada atributo
    public int getIdCita() {
        return idCita;
    }
    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }
    public int getIdMedico() {
        return idMedico;
    }
    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }
    public int getIdPaciente() {
        return idPaciente;
    }
    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }
    public int getIdEstadoCita() {
        return idEstadoCita;
    }
    public void setIdEstadoCita(int idEstadoCita) {
        this.idEstadoCita = idEstadoCita;
    }
    public int getIdModalidad() {
        return idModalidad;
    }
    public void setIdModalidad(int idModalidad) {
        this.idModalidad = idModalidad;
    }
    public int getIdPortafolio() {
        return idPortafolio;
    }
    public void setIdPortafolio(int idPortafolio) {
        this.idPortafolio = idPortafolio;
    }
    public Date getFechaCita() {
        return fechaCita;
    }
    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }
    public Time getHoraCita() {
        return horaCita;
    }
    public void setHoraCita(Time horaCita) {
        this.horaCita = horaCita;
    }
    

}
