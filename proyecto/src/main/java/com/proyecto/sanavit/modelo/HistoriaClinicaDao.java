package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoriaClinicaDao {

    // === INSERTAR HISTORIA CLÍNICA ===
    public boolean insertarHistoria(HistoriaClinica h) {
        boolean state = false;
        String sql = "{CALL sp_insertar_historia_clinica(?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            if (h.getIdEjecucionCita() > 0)
                cs.setInt(1, h.getIdEjecucionCita());
            else
                cs.setNull(1, Types.INTEGER);

            cs.setString(2, h.getMotivoConsulta());
            cs.setString(3, h.getEnfermedadActual());
            cs.setString(4, h.getAntecedentes());
            cs.setString(5, h.getDiagnostico());
            cs.setString(6, h.getTratamiento());
            cs.setString(7, h.getevolucion());
            cs.setString(8, h.getObservaciones());

            cs.execute();
            state = true;

        } catch (SQLException e) {
            System.out.println("Error insertarHistoria (SP): " + e.getMessage());
        }
        return state;
    }

    // === OBTENER HISTORIA POR ID ===
    public HistoriaClinica obtenerPorId(int id) {
        HistoriaClinica h = null;
        String sql = "{CALL obtener_historia_por_id(?)}";
        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    h = new HistoriaClinica(0, 0, null, null, null, null, null, null, null);
                    h.setIdHistoriaClinica(rs.getInt("id_historia_clinica"));
                    h.setIdEjecucionCita(rs.getInt("id_ejecucionCita"));
                    h.setMotivoConsulta(rs.getString("motivo_consulta"));
                    h.setEnfermedadActual(rs.getString("enfermedad_actual"));
                    h.setAntecedentes(rs.getString("antecedentes"));
                    h.setDiagnostico(rs.getString("diagnostico"));
                    h.setTratamiento(rs.getString("tratamiento"));
                    h.setevolucion(rs.getString("evolucion"));
                    h.setObservaciones(rs.getString("observaciones"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error obtenerPorId Historia (SP): " + e.getMessage());
        }
        return h;
    }

    // === OBTENER HISTORIAS CLÍNICAS POR PACIENTE ===
    public List<HistoriaClinica> obtenerPorPaciente(int idPaciente) {
        List<HistoriaClinica> lista = new ArrayList<>();
        String sql = "{CALL sp_historia_por_paciente(?)}";
        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idPaciente);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    HistoriaClinica h = new HistoriaClinica(0, 0, null, null, null, null, null, null, null);
                    h.setIdHistoriaClinica(rs.getInt("id_historia_clinica"));
                    h.setIdEjecucionCita(rs.getInt("id_ejecucionCita"));
                    h.setMotivoConsulta(rs.getString("motivo_consulta"));
                    h.setEnfermedadActual(rs.getString("enfermedad_actual"));
                    h.setAntecedentes(rs.getString("antecedentes"));
                    h.setDiagnostico(rs.getString("diagnostico"));
                    h.setTratamiento(rs.getString("tratamiento"));
                    h.setevolucion(rs.getString("evolucion"));
                    h.setObservaciones(rs.getString("observaciones"));
                    lista.add(h);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error obtenerPorPaciente (SP): " + e.getMessage());
        }
        return lista;
    }

    // === LISTAR TODAS LAS HISTORIAS ===
    public List<HistoriaClinica> listarHistorias() {
        List<HistoriaClinica> lista = new ArrayList<>();
        String sql = "{CALL sp_listar_historias_clinicas()}";
        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                HistoriaClinica h = new HistoriaClinica(0, 0, null, null, null, null, null, null, null);
                h.setIdHistoriaClinica(rs.getInt("id_historia_clinica"));
                h.setIdEjecucionCita(rs.getInt("id_ejecucionCita"));
                h.setMotivoConsulta(rs.getString("motivo_consulta"));
                h.setEnfermedadActual(rs.getString("enfermedad_actual"));
                h.setAntecedentes(rs.getString("antecedentes"));
                h.setDiagnostico(rs.getString("diagnostico"));
                h.setTratamiento(rs.getString("tratamiento"));
                h.setevolucion(rs.getString("evolucion"));
                h.setObservaciones(rs.getString("observaciones"));
                lista.add(h);
            }

        } catch (SQLException e) {
            System.out.println("Error listarHistorias (SP): " + e.getMessage());
        }
        return lista;
    }

    // === ACTUALIZAR HISTORIA CLÍNICA ===
    public boolean updateHistoria(HistoriaClinica h) {
        boolean state = false;
        String sql = "{CALL sp_actualizar_historia_clinica(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, h.getIdHistoriaClinica());
            if (h.getIdEjecucionCita() > 0)
                cs.setInt(2, h.getIdEjecucionCita());
            else
                cs.setNull(2, Types.INTEGER);
            cs.setString(3, h.getMotivoConsulta());
            cs.setString(4, h.getEnfermedadActual());
            cs.setString(5, h.getAntecedentes());
            cs.setString(6, h.getDiagnostico());
            cs.setString(7, h.getTratamiento());
            cs.setString(8, h.getevolucion());
            cs.setString(9, h.getObservaciones());

            cs.execute();
            state = true;

        } catch (SQLException e) {
            System.out.println("Error updateHistoria (SP): " + e.getMessage());
        }
        return state;
    }

    // === ELIMINAR HISTORIA CLÍNICA ===
    public boolean deleteHistoria(int idHistoria) {
        boolean state = false;
        String sql = "{CALL sp_eliminar_historia_clinica(?)}";
        try (Connection conn = ConexionDatabase.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idHistoria);
            cs.execute();
            state = true;

        } catch (SQLException e) {
            System.out.println("Error deleteHistoria (SP): " + e.getMessage());
        }
        return state;
    }
}
