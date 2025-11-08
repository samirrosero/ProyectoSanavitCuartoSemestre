package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EjecucionCitaDao {

    public boolean insertarEjecucion(EjecucionCita e) {
        boolean state = false;
        String sql = "{CALL sp_insertar_ejecucion_cita(?, ?, ?, ?)}";
        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, e.getIdCita());
            cs.setTimestamp(2, e.getFechaHoraIngreso());
            cs.setTimestamp(3, e.getFechaHoraSalida());
            cs.setInt(4, e.getDuracion());

            boolean hasResult = cs.execute();
            if (hasResult) {
                try (ResultSet rs = cs.getResultSet()) {
                    if (rs.next()) e.setIdEjecucionCita(rs.getInt("id_ejecucionCita"));
                }
                state = true;
            }
        } catch (SQLException ex) {
            System.out.println("Error insertarEjecucion (SP): " + ex.getMessage());
        }
        return state;
    }

    public EjecucionCita obtenerPorId(int id) {
        EjecucionCita e = null;
        String sql = "{CALL obtenerEjecucionPorId(?)}";
        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    e = new EjecucionCita(
                            rs.getInt("id_ejecucionCita"),
                            rs.getInt("id_cita"),
                            rs.getTimestamp("fecha_hora_ingreso"),
                            rs.getTimestamp("fecha_salida"),
                            rs.getInt("duracion")
                    );
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error obtenerPorId (SP): " + ex.getMessage());
        }
        return e;
    }

    public List<EjecucionCita> listarEjecuciones() {
        List<EjecucionCita> lista = new ArrayList<>();
        String sql = "{CALL sp_listar_ejecuciones_cita()}";
        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                EjecucionCita e = new EjecucionCita(
                        rs.getInt("id_ejecucionCita"),
                        rs.getInt("id_cita"),
                        rs.getTimestamp("fecha_hora_ingreso"),
                        rs.getTimestamp("fecha_salida"),
                        rs.getInt("duracion")
                );
                lista.add(e);
            }
        } catch (SQLException ex) {
            System.out.println("Error listarEjecuciones (SP): " + ex.getMessage());
        }
        return lista;
    }

    public boolean updateEjecucion(EjecucionCita e) {
        boolean state = false;
        String sql = "{CALL sp_actualizar_ejecucion_cita(?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, e.getIdEjecucionCita());
            cs.setInt(2, e.getIdCita());
            cs.setTimestamp(3, e.getFechaHoraIngreso());
            cs.setTimestamp(4, e.getFechaHoraSalida());
            cs.setInt(5, e.getDuracion());
            int res = cs.executeUpdate();
            state = res > 0;
        } catch (SQLException ex) {
            System.out.println("Error updateEjecucion (SP): " + ex.getMessage());
        }
        return state;
    }

    public boolean deleteEjecucion(int id) {
        boolean state = false;
        String sql = "{CALL sp_eliminar_ejecucion_cita(?)}";
        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, id);
            int res = cs.executeUpdate();
            state = res > 0;
        } catch (SQLException ex) {
            System.out.println("Error deleteEjecucion (SP): " + ex.getMessage());
        }
        return state;
    }
}
