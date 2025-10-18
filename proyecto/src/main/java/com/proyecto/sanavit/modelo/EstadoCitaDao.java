package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstadoCitaDao {

    public boolean insertarEstado(EstadoCita e) {
        boolean state = false;
        String sql = "INSERT INTO estado_cita (nombre_estado) VALUES (?)";
        try (Connection conn = ConexionDatabase.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, e.getNombreEstado());
            int res = pst.executeUpdate();
            if (res > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        e.setIdEstadoCita(rs.getInt(1));
                    }
                }
                state = true;
            }
        } catch (SQLException ex) {
            System.out.println("Error insertarEstado: " + ex.getMessage());

        }
        return state;
    }

    public EstadoCita obtenerPorId (int id_estado_cita){
        EstadoCita e= null;
        String sql = "SELECT * FROM estado_cita WHERE id_estado_cita = ?";

     try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id_estado_cita);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    e = new EstadoCita(0,null);
                    e.setIdEstadoCita(rs.getInt("id_estado_cita"));
                    e.setNombreEstado(rs.getString("nombre_estado"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error obtenerPorId Estado: " + ex.getMessage());
        }
        return e;
    }

    public List<EstadoCita> listarEstados() {
        List<EstadoCita> lista = new ArrayList<>();
        String sql = "SELECT * FROM estado_cita";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                EstadoCita e = new EstadoCita(0,null);
                e.setIdEstadoCita(rs.getInt("id_estado_cita"));
                e.setNombreEstado(rs.getString("nombre_estado"));
                lista.add(e);
            }
        } catch (SQLException ex) {
            System.out.println("Error listarEstados: " + ex.getMessage());
        }
        return lista;
    }

    public boolean updateEstado(EstadoCita e) {
        boolean state = false;
        String sql = "UPDATE estado_cita SET nombre_estado=? WHERE id_estado_cita=?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, e.getNombreEstado());
            pst.setInt(2, e.getIdEstadoCita());
            int res = pst.executeUpdate();
            state = res > 0;
        } catch (SQLException ex) {
            System.out.println("Error updateEstado: " + ex.getMessage());
        }
        return state;
    }

    public boolean deleteEstado(int id) {
        boolean state = false;
        String sql = "DELETE FROM estado_cita WHERE id_estado_cita = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            int res = pst.executeUpdate();
            state = res > 0;
        } catch (SQLException ex) {
            System.out.println("Error deleteEstado: " + ex.getMessage());
        }
        return state;
    }
}