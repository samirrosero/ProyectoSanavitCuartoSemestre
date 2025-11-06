package com.proyecto.sanavit.modelo;

public class Portafolio {
    private int idPortafolio;
    private String salud;
    private String afiliaciones;
    private int idPaciente;

    public Portafolio() {}

    public Portafolio(int idPortafolio, String salud, String afiliaciones, int idPaciente) {
        this.idPortafolio = idPortafolio;
        this.salud = salud;
        this.afiliaciones = afiliaciones;
        this.idPaciente = idPaciente;
    }

    public int getIdPortafolio() { return idPortafolio; }
    public void setIdPortafolio(int idPortafolio) { this.idPortafolio = idPortafolio; }

    public String getSalud() { return salud; }
    public void setSalud(String salud) { this.salud = salud; }

    public String getAfiliaciones() { return afiliaciones; }
    public void setAfiliaciones(String afiliaciones) { this.afiliaciones = afiliaciones; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }
}
