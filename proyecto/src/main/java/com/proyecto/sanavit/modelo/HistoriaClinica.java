package com.proyecto.sanavit.modelo;

public class HistoriaClinica {
    private int idHistoriaClinica;
    private int idEjecucionCita;
    private String motivoConsulta;
    private String enfermedadActual;
    private String antecedentes;
    private String diagnostico;
    private String tratamiento; 
    private String evolucion;
    private String observaciones;

    // Constructor
    public HistoriaClinica() {
    }
    public HistoriaClinica(int idHistoriaClinica, int idEjecucionCita, String motivoConsulta, String enfermedadActual, String antecedentes, String diagnostico, String tratamiento, String evolucion, String observaciones) {
        this.idHistoriaClinica = idHistoriaClinica;
        this.idEjecucionCita = idEjecucionCita;
        this.motivoConsulta = motivoConsulta;
        this.enfermedadActual = enfermedadActual;
        this.antecedentes = antecedentes;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.evolucion = evolucion;
        this.observaciones = observaciones;
    }

    // Métodos get y set de cada atributo
    public int getIdHistoriaClinica() {
        return idHistoriaClinica;
    }
    public void setIdHistoriaClinica(int idHistoriaClinica) {
        this.idHistoriaClinica = idHistoriaClinica;
    }
    public int getIdEjecucionCita() {
        return idEjecucionCita;
    }

    public void setIdEjecucionCita(int idEjecucionCita) {
        this.idEjecucionCita = idEjecucionCita;
    }
    public String getMotivoConsulta() {
        return motivoConsulta;
    }
    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }
    public String getEnfermedadActual() {
        return enfermedadActual;
    }
    public void setEnfermedadActual(String enfermedadActual) {
        this.enfermedadActual = enfermedadActual;
    }
    public String getAntecedentes() {
        return antecedentes;
    }
    public void setAntecedentes(String antecedentes) {
        this.antecedentes = antecedentes;
    }
    public String getDiagnostico() {
        return diagnostico;
    }
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
    public String getTratamiento() {
        return tratamiento;
    }
    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }
    public String getevolucion() {
        return evolucion;
    }
    public void setEvolucion(String evolucion) {
        this.evolucion = evolucion;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}

