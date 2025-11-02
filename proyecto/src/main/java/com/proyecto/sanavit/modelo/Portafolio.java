package com.proyecto.sanavit.modelo;

public class Portafolio {
    private int idPortafolio;
    private String salud;
    private String convenios;
    private String afiliaciones;

    public Portafolio(int idPortafolio, String salud, String covenios, String afiliaciones) {
        this.idPortafolio = idPortafolio;
        this.salud = salud;
        this.convenios = covenios;
        this.afiliaciones = afiliaciones;
    }

    // Constructor vacío
    public Portafolio() {
    }
    // Métodos get y set de cada atributo
    public int getIdPortafolio() {
        return idPortafolio;
    }
    public void setIdPortafolio(int idPortafolio) {
        this.idPortafolio = idPortafolio;
    }
    public String getSalud() {
        return salud;
    }
    public void setSalud(String salud) {
        this.salud = salud;
    }
    public String getConvenio() {
        return convenios;
    }
    public void setConvenios(String covenios) {
        this.convenios = covenios;
    }
    public String getAfiliaciones() {
        return afiliaciones;
    }
    public void setAfiliaciones(String afiliaciones) {
        this.afiliaciones = afiliaciones;
    }
    
}
