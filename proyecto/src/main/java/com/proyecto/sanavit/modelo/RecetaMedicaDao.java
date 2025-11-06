package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecetaMedicaDao {

    // === INSERTAR RECETA MÉDICA USANDO PROCEDIMIENTO ===
    public boolean insertarReceta(RecetaMedica r) {
        boolean state = false;
        String sql = "{CALL insertar_receta(?, ?, ?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, r.getIdHistoriaClinica());
            cs.setString(2, r.getMedicamento());
            cs.setString(3, r.getIndicaciones());

            boolean tieneResultado = cs.execute();
            if (tieneResultado) {
                try (ResultSet rs = cs.getResultSet()) {
                    if (rs.next()) {
                        r.setIdReceta(rs.getInt("idGenerado"));
                    }
                }
            }
            state = true;

        } catch (SQLException e) {
            System.out.println("❌ Error insertarReceta (SP): " + e.getMessage());
        }
        return state;
    }

    // === OBTENER RECETA POR ID ===
    public RecetaMedica obtenerPorId(int idReceta) {
        RecetaMedica r = null;
        String sql = "{CALL obtener_receta_por_id(?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idReceta);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    r = new RecetaMedica(
                        rs.getInt("id_receta"),
                        rs.getInt("id_historia_clinica"),
                        rs.getString("medicamento"),
                        rs.getString("indicaciones")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error obtenerPorId Receta (SP): " + e.getMessage());
        }
        return r;
    }

    // === LISTAR TODAS LAS RECETAS ===
    public List<RecetaMedica> listarRecetas() {
        List<RecetaMedica> lista = new ArrayList<>();
        String sql = "{CALL listar_recetas()}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                RecetaMedica r = new RecetaMedica(
                    rs.getInt("id_receta"),
                    rs.getInt("id_historia_clinica"),
                    rs.getString("medicamento"),
                    rs.getString("indicaciones")
                );
                lista.add(r);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error listarRecetas (SP): " + e.getMessage());
        }

        return lista;
    }

    // === ACTUALIZAR RECETA ===
    public boolean updateReceta(RecetaMedica r) {
        boolean state = false;
        String sql = "{CALL actualizar_receta(?, ?, ?, ?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, r.getIdReceta());
            cs.setInt(2, r.getIdHistoriaClinica());
            cs.setString(3, r.getMedicamento());
            cs.setString(4, r.getIndicaciones());

            int res = cs.executeUpdate();
            state = res > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error updateReceta (SP): " + e.getMessage());
        }

        return state;
    }

    // === ELIMINAR RECETA ===
    public boolean deleteReceta(int idReceta) {
        boolean state = false;
        String sql = "{CALL eliminar_receta(?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idReceta);
            int res = cs.executeUpdate();
            state = res > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error deleteReceta (SP): " + e.getMessage());
        }

        return state;
    }
}
