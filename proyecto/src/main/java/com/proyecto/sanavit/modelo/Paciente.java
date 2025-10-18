package com.proyecto.sanavit.modelo;

public class Paciente {
    private int idPaciente;
    private String nombre;
    private String correo;
    private int edad;
    private String telefono;
    private String sexo;
    private String direccion;
    private String identificacion;
    private int idUsuario;


    public Paciente(int idPaciente, String nombre, String correo, int edad, String telefono, String sexo, String direccion, String identificacion, int idUsuario) {
        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.correo = correo;
        this.edad = edad;
        this.telefono = telefono;
        this.sexo = sexo;
        this.direccion = direccion;
        this.identificacion = identificacion;
        this.idUsuario = idUsuario;
    }
    

    // Métodos get y set de cada atributo
    public int getIdPaciente() {
        return idPaciente;
    }   
    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public int getEdad() {
        return edad;
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getSexo() {
        return sexo;
    }
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    public String getDireccion() {
        return direccion;
    }   
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getIdentificacion() {
        return identificacion;
    }
    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }
        public int getIdUsuario(){
        return idUsuario;
    }
    public void setIdUsuario(int id_usuario){
        this.idUsuario= idUsuario;
    }
}
