package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentoAnexoDao {

    public boolean insertarDocumento(DocumentoAnexo d) {
        boolean state = false;
        String sql = "INSERT INTO documento_anexo (id_historia_clinica, tipo, ruta_archivo) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDatabase.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, d.getIdHistoriaClinica());
            pst.setString(2, d.getTipo());
            pst.setString(3, d.getRutaArchivo());

            int res = pst.executeUpdate();
            if (res > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        d.setIdDocumentoAnexo(rs.getInt(1));
                    }
                }
                state = true;
            }
        } catch (SQLException e) {
            System.out.println("Error insertarDocumento: " + e.getMessage());
        }
        return state;
    }

    public DocumentoAnexo obtenerPorId(int idDocumentoAnexo) {
        DocumentoAnexo d = null;
        String sql = "SELECT * FROM documento_anexo WHERE id_documento = ?";
        try (Connection conn = ConexionDatabase.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idDocumentoAnexo);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                d = new DocumentoAnexo(0, 0, null, null);
                d.setIdDocumentoAnexo(rs.getInt("id_documento"));
                d.setIdHistoriaClinica(rs.getInt("id_historia_clinica"));
                d.setTipo(rs.getString("tipo"));
                d.setRutaArchivo(rs.getString("ruta_archivo"));
            }

        } catch (SQLException e) {
            System.out.println("Error obtenerPorId Documento: " + e.getMessage());
        }
        return d;
    }

    public List<DocumentoAnexo> listarDocumentosPorHistoria(int idHistoriaClinica) {
        List<DocumentoAnexo> lista = new ArrayList<>();
        String sql = "SELECT * FROM documento_anexo WHERE id_historia_clinica = ?";
        try (Connection conn = ConexionDatabase.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idHistoriaClinica);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    DocumentoAnexo d = new DocumentoAnexo(0, 0, null, null);
                    d.setIdDocumentoAnexo(rs.getInt("id_documento"));
                    d.setIdHistoriaClinica(rs.getInt("id_historia_clinica"));
                    d.setTipo(rs.getString("tipo"));
                    d.setRutaArchivo(rs.getString("ruta_archivo"));
                    lista.add(d);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error listarDocumentosPorHistoria: " + e.getMessage());
        }
        return lista;
    }

    public boolean updateDocumento(DocumentoAnexo d) {
        boolean state = false;
        String sql = "UPDATE documento_anexo SET id_historia_clinica=?, tipo=?, ruta_archivo=? WHERE id_documento=?";
        try (Connection conn = ConexionDatabase.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, d.getIdHistoriaClinica());
            pst.setString(2, d.getTipo());
            pst.setString(3, d.getRutaArchivo());
            pst.setInt(4, d.getIdDocumentoAnexo());
            int res = pst.executeUpdate();
            state = res > 0;
        } catch (SQLException e) {
            System.out.println("Error updateDocumento: " + e.getMessage());
        }
        return state;
    }

    public boolean deleteDocumento(int idDocumentoAnexo) {
        boolean state = false;
        Connection connect = null;
        PreparedStatement pst = null;
        try {
            connect = ConexionDatabase.getConnection();
            if (connect != null) {
                String sql = "DELETE FROM documento_anexo WHERE id_documento = ?";
                pst = connect.prepareStatement(sql);
                pst.setInt(1, idDocumentoAnexo);
                int res = pst.executeUpdate();
                state = res > 0;
            } else {
                System.out.println("conexion fallida");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        }
        return state;
    }
}
