package com.proyecto.sanavit.modelo;

import java.sql.Date;
import java.sql.Time;

public class Cita {
    private int idCita;
    private int idMedico;
    private int idPaciente;
    private int idEstadoCita;
    private int idModalidad;
    private Date fechaCita;
    private Time horaCita;


    // Constructor completo
    public Cita(int idCita, int idMedico, int idPaciente, int idEstadoCita, int idModalidad, Date fechaCita, Time horaCita) {
        this.idCita = idCita;
        this.idMedico = idMedico;
        this.idPaciente = idPaciente;
        this.idEstadoCita = idEstadoCita;
        this.idModalidad = idModalidad;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
    }
    // Constructor vacío
    public Cita() {
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
    

    // metodos para pedir, cancelar y actualizar estado
    public void pedirCita (Date fechaCita, Time horaCita, int modalidad){
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.idModalidad = modalidad;
        this.idEstadoCita = 1;
    }
    public void cancelarCita (){
        this.idEstadoCita = 3;
    }
    public void confirmarCita (){
        this.idEstadoCita = 2;
    }
    public void finalizarCita (){
        this.idEstadoCita = 4;
    }

}
