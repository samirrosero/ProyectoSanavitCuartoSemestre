package com.proyecto.sanavit.controlador;

import com.proyecto.sanavit.modelo.DocumentoAnexo;
import com.proyecto.sanavit.modelo.DocumentoAnexoDao;
import java.util.List;

public class DocumentoAnexoController {

    private DocumentoAnexoDao documentoDao;

    public DocumentoAnexoController() {
        this.documentoDao = new DocumentoAnexoDao();
    }

    // === MÉTODO CORRESPONDIENTE A subirDocumento() ===
    public void subirDocumento(int idHistoriaClinica, String tipo, String rutaArchivo) {
        if (tipo == null || tipo.isEmpty() || rutaArchivo == null || rutaArchivo.isEmpty()) {
            System.err.println("❌ Error: tipo o ruta del archivo vacíos.");
            return;
        }

        DocumentoAnexo doc = new DocumentoAnexo(0, idHistoriaClinica, tipo, rutaArchivo);
        boolean exito = documentoDao.insertarDocumento(doc);

        if (exito) {
            System.out.println("✅ Documento subido correctamente con ID: " + doc.getIdDocumentoAnexo());
        } else {
            System.err.println("❌ No se pudo subir el documento.");
        }
    }

    // === MÉTODO CORRESPONDIENTE A consultarDocumento() ===
    public String consultarDocumento(int idDocumento) {
        DocumentoAnexo doc = documentoDao.obtenerPorId(idDocumento);
        if (doc != null) {
            return "📄 Documento encontrado:\n" +
                    "ID: " + doc.getIdDocumentoAnexo() + "\n" +
                    "Historia Clínica: " + doc.getIdHistoriaClinica() + "\n" +
                    "Tipo: " + doc.getTipo() + "\n" +
                    "Ruta: " + doc.getRutaArchivo();
        } else {
            return "⚠️ Documento no encontrado.";
        }
    }

    // === OTROS MÉTODOS DE APOYO ===

    public List<DocumentoAnexo> listarPorHistoria(int idHistoriaClinica) {
        return documentoDao.listarDocumentosPorHistoria(idHistoriaClinica);
    }

    public boolean actualizarDocumento(int idDocumentoAnexo, int idHistoriaClinica, String tipo, String rutaArchivo) {
        DocumentoAnexo doc = new DocumentoAnexo(idDocumentoAnexo, idHistoriaClinica, tipo, rutaArchivo);
        return documentoDao.updateDocumento(doc);
    }

    public boolean eliminarDocumento(int idDocumentoAnexo) {
        return documentoDao.deleteDocumento(idDocumentoAnexo);
    }
}
