package com.proyecto.sanavit.modelo;

public class DocumentoAnexo {
    private int idDocumentoAnexo;
    private int idHistoriaClinica;
    private String tipo;
    private String rutaArchivo;

    public DocumentoAnexo(int idHistoria, int idHistoria2, String sql, String sql2) {
        this.idDocumentoAnexo = idDocumentoAnexo;
        this.idHistoriaClinica = idHistoriaClinica;
        this.tipo = tipo;
        this.rutaArchivo = rutaArchivo;
    }

    // Métodos get y set de cada atributo
    public int getIdDocumentoAnexo() {
        return idDocumentoAnexo;
    }
    public void setIdDocumentoAnexo(int idDocumentoAnexo) {
        this.idDocumentoAnexo = idDocumentoAnexo;
    }
    public int getIdHistoriaClinica() {
        return idHistoriaClinica;
    }
    public void setIdHistoriaClinica(int idHistoriaClinica) {
        this.idHistoriaClinica = idHistoriaClinica;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getRutaArchivo() {
        return rutaArchivo;
    }
    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
    
}

