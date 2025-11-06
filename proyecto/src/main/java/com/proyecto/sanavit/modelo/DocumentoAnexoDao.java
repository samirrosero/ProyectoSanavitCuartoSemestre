package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentoAnexoDao {

    // === INSERTAR DOCUMENTO USANDO PROCEDIMIENTO ===
    public boolean insertarDocumento(DocumentoAnexo d) {
        boolean state = false;
        String sql = "{CALL insertar_documento_anexo(?, ?, ?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, d.getIdHistoriaClinica());
            cs.setString(2, d.getTipo());
            cs.setString(3, d.getRutaArchivo());

            boolean tieneResultado = cs.execute();
            if (tieneResultado) {
                try (ResultSet rs = cs.getResultSet()) {
                    if (rs.next()) {
                        d.setIdDocumentoAnexo(rs.getInt("idGenerado"));
                    }
                }
            }
            state = true;

        } catch (SQLException e) {
            System.out.println("❌ Error insertarDocumento (SP): " + e.getMessage());
        }
        return state;
    }

    // === OBTENER DOCUMENTO POR ID ===
    public DocumentoAnexo obtenerPorId(int idDocumentoAnexo) {
        DocumentoAnexo d = null;
        String sql = "{CALL obtener_documento_por_id(?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idDocumentoAnexo);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    d = new DocumentoAnexo(
                        rs.getInt("id_documento"),
                        rs.getInt("id_historia_clinica"),
                        rs.getString("tipo"),
                        rs.getString("ruta_archivo")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error obtenerPorId (SP): " + e.getMessage());
        }
        return d;
    }

    // === LISTAR DOCUMENTOS POR HISTORIA ===
    public List<DocumentoAnexo> listarDocumentosPorHistoria(int idHistoriaClinica) {
        List<DocumentoAnexo> lista = new ArrayList<>();
        String sql = "{CALL listar_documentos_por_historia(?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idHistoriaClinica);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    DocumentoAnexo d = new DocumentoAnexo(
                        rs.getInt("id_documento"),
                        rs.getInt("id_historia_clinica"),
                        rs.getString("tipo"),
                        rs.getString("ruta_archivo")
                    );
                    lista.add(d);
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error listarDocumentosPorHistoria (SP): " + e.getMessage());
        }

        return lista;
    }

    // === ACTUALIZAR DOCUMENTO ===
    public boolean updateDocumento(DocumentoAnexo d) {
        boolean state = false;
        String sql = "{CALL actualizar_documento_anexo(?, ?, ?, ?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, d.getIdDocumentoAnexo());
            cs.setInt(2, d.getIdHistoriaClinica());
            cs.setString(3, d.getTipo());
            cs.setString(4, d.getRutaArchivo());

            int res = cs.executeUpdate();
            state = res > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error updateDocumento (SP): " + e.getMessage());
        }

        return state;
    }

    // === ELIMINAR DOCUMENTO ===
    public boolean deleteDocumento(int idDocumentoAnexo) {
        boolean state = false;
        String sql = "{CALL eliminar_documento_anexo(?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idDocumentoAnexo);
            int res = cs.executeUpdate();
            state = res > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error deleteDocumento (SP): " + e.getMessage());
        }

        return state;
    }
}
