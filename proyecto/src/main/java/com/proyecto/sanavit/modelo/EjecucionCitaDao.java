package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class EjecucionCitaDao {

    public boolean insertarEjecucion(EjecucionCita e) {
        boolean state = false;
        String sql = "INSERT INTO ejecucionCita (id_cita, fecha_hora_ingreso, fecha_salida, duracion) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, e.getIdCita());
            pst.setTimestamp(2, e.getFechaHoraIngreso());
            pst.setTimestamp(3, e.getFechaHoraSalida());
            pst.setInt(4, e.getDuracion());

            int res = pst.executeUpdate();
            if (res > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) e.setIdEjecucionCita(rs.getInt(1));
                }
                state = true;
            }
        } catch (SQLException ex) {
            System.out.println("Error insertarEjecucion: " + ex.getMessage());
        }
        return state;
    }

    public EjecucionCita obtenerPorId(int id) {
        EjecucionCita e = null;
        String sql = "SELECT * FROM ejecucionCita WHERE id_ejecucionCita = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    e = new EjecucionCita(0, 0, null, null, 0);
                    e.setIdEjecucionCita(rs.getInt("id_ejecucionCita"));
                    e.setIdCita(rs.getInt("id_cita"));
                    e.setFechaHoraIngreso(rs.getTimestamp("fecha_hora_ingreso"));
                    e.setFechaHoraSalida(rs.getTimestamp("fecha_salida"));
                    e.setDuracion(rs.getInt("duracion"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error obtenerPorId Ejecucion: " + ex.getMessage());
        }
        return e;
    }

    public List<EjecucionCita> listarEjecuciones() {
        List<EjecucionCita> lista = new ArrayList<>();
        String sql = "SELECT * FROM ejecucionCita";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                EjecucionCita e = new EjecucionCita(0, 0, null, null, 0);
                e.setIdEjecucionCita(rs.getInt("id_ejecucionCita"));
                e.setIdCita(rs.getInt("id_cita"));
                e.setFechaHoraIngreso(rs.getTimestamp("fecha_hora_ingreso"));
                e.setFechaHoraSalida(rs.getTimestamp("fecha_salida"));
                e.setDuracion(rs.getInt("duracion"));
                lista.add(e);
            }
        } catch (SQLException ex) {
            System.out.println("Error listarEjecuciones: " + ex.getMessage());
        }
        return lista;
    }

    public boolean updateEjecucion(EjecucionCita e) {
        boolean state = false;
        String sql = "UPDATE ejecucionCita SET id_cita=?, fecha_hora_ingreso=?, fecha_salida=?, duracion=? WHERE id_ejecucionCita=?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, e.getIdCita());
            pst.setTimestamp(2, e.getFechaHoraIngreso());
            pst.setTimestamp(3, e.getFechaHoraSalida());
            pst.setInt(4, e.getDuracion());
            pst.setInt(5, e.getIdEjecucionCita());
            int res = pst.executeUpdate();
            state = res > 0;
        } catch (SQLException ex) {
            System.out.println("Error updateEjecucion: " + ex.getMessage());
        }
        return state;
    }

    public boolean deleteEjecucion(int id) {
        boolean state = false;
        String sql = "DELETE FROM ejecucionCita WHERE id_ejecucionCita = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            int res = pst.executeUpdate();
            state = res > 0;
        } catch (SQLException ex) {
            System.out.println("Error deleteEjecucion: " + ex.getMessage());
        }
        return state;
    }
}
