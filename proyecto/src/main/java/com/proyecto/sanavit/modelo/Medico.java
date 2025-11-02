package com.proyecto.sanavit.modelo;

public class Medico {
    private int idMedico;
    private String nombre;
    private String especialidad;
    private int idUsuario;

    public Medico(int idMedico, String nombre, String especialidad, int idUsuario) {
        this.idMedico = idMedico;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.idUsuario = idUsuario;

    }

    // Métodos get y set de cada atributo
    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void atender(Cita cita) {
        System.out.println("El médico " + nombre + " está atendiendo la cita " + cita.getIdCita());
        cita.setIdEstadoCita(2); 
    }

    public RecetaMedica emitirReceta(Cita cita, String medicamento, String indicaciones) {
        RecetaMedica receta = new RecetaMedica(idMedico, idMedico, indicaciones, indicaciones);
        receta.setIdReceta(cita.getIdCita());
        receta.setMedicamento(medicamento);
        receta.setIndicaciones(indicaciones);
        System.out.println("Receta emitida por el Dr. " + nombre);
        return receta;
    }
}