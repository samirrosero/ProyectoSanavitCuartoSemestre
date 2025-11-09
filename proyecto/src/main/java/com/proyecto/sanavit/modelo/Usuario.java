package com.proyecto.sanavit.modelo;


public class Usuario {
    private int idUsuario;
    private int idRol;
    private String nombreUsuario;
    private String contraseña;
    private String nombreRol;

    //constructor
    public Usuario() {
    }
public Usuario( int idUsuario, int idRol, String nombreUsuario, String contraseña, String nombreRol){
        this.idUsuario= idUsuario;
        this.idRol= idRol;
        this.nombreUsuario= nombreUsuario;
        this.contraseña= contraseña;
        this.nombreRol = nombreRol;
    }
        //metodos get y set de cada atributo 
        public int getIdUsuario(){
            return idUsuario;
        }   
        public void setIdUsuario( int idUsuario){
                this.idUsuario = idUsuario;
            }
        
                public int getIdRol (){
                return idRol;
            }
            public void setIdRol( int idRol){
                this.idRol = idRol;

            }    public String getNombreUsuario(){
                return nombreUsuario;
            }            
            
            public void setNombreUsuario( String nombreUsuario){
                this.nombreUsuario= nombreUsuario;
            }
                public String getContraseña (){
                return contraseña;
            }
            public void setContraseña( String contraseña){
                this.contraseña= contraseña;
            }
            public String getNombreRol() {
                return nombreRol;
            }
            public void setNombreRol(String nombreRol) {
                this.nombreRol = nombreRol;
            }


}

