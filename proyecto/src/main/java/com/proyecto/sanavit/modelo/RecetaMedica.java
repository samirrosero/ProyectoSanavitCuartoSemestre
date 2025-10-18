package com.proyecto.sanavit.modelo;


public class RecetaMedica {
    private int idReceta;
    private int idHistoriaClinica;
    private String medicamento;
    private String indicaciones;

    public RecetaMedica(int idReceta, int idHistoriaClinica, String medicamento, String indicaciones) {
        this.idReceta = idReceta;
        this.idHistoriaClinica = idHistoriaClinica;
        this.medicamento = medicamento;
        this.indicaciones = indicaciones;
    }

    // Métodos get y set de cada atributo
    public int getIdReceta() {
        return idReceta;
    }
    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }
    public int getIdHistoriaClinica() {
        return idHistoriaClinica;
    }

    public void setIdHistoriaClinica(int idHistoriaClinica) {
        this.idHistoriaClinica = idHistoriaClinica;
    }
    public String getMedicamento() {
        return medicamento;
    }
    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }
    public String getIndicaciones() {
        return indicaciones;
    }
    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }
}
