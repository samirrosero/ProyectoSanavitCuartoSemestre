package com.proyecto.sanavit.modelo;
    
public class Modalidad {
    private int idModalidad;
    private String nombreModalidad;

    public Modalidad(int idModalidad, String nombreModalidad) {
        this.idModalidad = idModalidad;
        this.nombreModalidad = nombreModalidad;
    }
    
    // Métodos get y set de cada atributo
    public int getIdModalidad() {
        return idModalidad;
    }
    public void setIdModalidad(int idModalidad) {
        this.idModalidad = idModalidad;
    }
    public String getNombreModalidad() {
        return nombreModalidad;
    }
    public void setNombreModalidad(String nombreModalidad) {
        this.nombreModalidad = nombreModalidad;
    }
    
}
