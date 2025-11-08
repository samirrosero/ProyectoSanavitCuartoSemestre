package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDao {

    // === INSERTAR ===
    public boolean insertarCita(Cita cita) {
        boolean state = false;
        String sql = "{CALL insertar_cita(?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement pst = conn.prepareCall(sql)) {

            pst.setInt(1, cita.getIdMedico());
            pst.setInt(2, cita.getIdPaciente());
            pst.setInt(3, cita.getIdEstadoCita());
            pst.setInt(4, cita.getIdModalidad());
            pst.setDate(5, new java.sql.Date(cita.getFechaCita().getTime()));
            pst.setTime(6, cita.getHoraCita());

            boolean tieneResultados = pst.execute();

            if (tieneResultados) {
                try (ResultSet rs = pst.getResultSet()) {
                    if (rs.next()) {
                        cita.setIdCita(rs.getInt("id_generado"));
                        state = true;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar cita: " + e.getMessage());
        }

        return state;
    }

    // === ACTUALIZAR ESTADO DE UNA CITA ===
    public boolean actualizarEstadoCita(int idCita, int nuevoEstado) {
        String sql = "{CALL actualizar_estado_cita(?, ?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement pst = conn.prepareCall(sql)) {

            pst.setInt(1, idCita);
            pst.setInt(2, nuevoEstado);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar estado de la cita: " + e.getMessage());
            return false;
        }
    }

    // === ACTUALIZAR CITA ===
    public boolean updateCita(Cita cita) {
        String sql = "{CALL actualizar_cita(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement pst = conn.prepareCall(sql)) {

            pst.setInt(1, cita.getIdCita());
            pst.setInt(2, cita.getIdMedico());
            pst.setInt(3, cita.getIdPaciente());
            pst.setInt(4, cita.getIdEstadoCita());
            pst.setInt(5, cita.getIdModalidad());
            pst.setDate(6, new java.sql.Date(cita.getFechaCita().getTime()));
            pst.setTime(7, cita.getHoraCita());

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar cita: " + e.getMessage());
            return false;
        }
    }

    // === ELIMINAR CITA ===
    public boolean deleteCita(int idCita) {
        String sql = "{CALL eliminar_cita(?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement pst = conn.prepareCall(sql)) {

            pst.setInt(1, idCita);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar cita: " + e.getMessage());
            return false;
        }
    }

    // === LISTAR TODAS LAS CITAS ===
    public List<Cita> selectCita() {
        List<Cita> lista = new ArrayList<>();
        String sql = "{CALL listar_citas()}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement pst = conn.prepareCall(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(mapCita(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar citas: " + e.getMessage());
        }

        return lista;
    }

    // === OBTENER CITA POR ID ===
    public Cita obtenerCitaPorId(int idCita) {
        Cita cita = null;
        String sql = "{CALL obtener_cita_por_id(?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement pst = conn.prepareCall(sql)) {

            pst.setInt(1, idCita);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    cita = mapCita(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al obtener cita: " + e.getMessage());
        }

        return cita;
    }

    // === OBTENER CITAS POR PACIENTE ===
    public List<Cita> obtenerCitaPorPaciente(int idPaciente) {
        List<Cita> lista = new ArrayList<>();
        String sql = "{CALL obtener_citas_por_paciente(?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement pst = conn.prepareCall(sql)) {

            pst.setInt(1, idPaciente);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapCita(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al obtener citas del paciente: " + e.getMessage());
        }

        return lista;
    }

    // === OBTENER CITAS POR MÉDICO ===
    public List<Cita> obtenerCitaPorMedico(int idMedico) {
        List<Cita> lista = new ArrayList<>();
        String sql = "{CALL obtener_citas_por_medico(?)}";

        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement pst = conn.prepareCall(sql)) {

            pst.setInt(1, idMedico);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapCita(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al obtener citas del médico: " + e.getMessage());
        }

        return lista;
    }

    // === MAPEAR RESULTSET A OBJETO CITA ===
    private Cita mapCita(ResultSet rs) throws SQLException {
        return new Cita(
            rs.getInt("id_cita"),
            rs.getInt("id_medico"),
            rs.getInt("id_paciente"),
            rs.getInt("id_estado_cita"),
            rs.getInt("id_modalidad"),
            rs.getDate("fecha_cita"),
            rs.getTime("hora_cita")
        );
    }
}
