package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoriaClinicaDao {

    public boolean insertarHistoria(HistoriaClinica h) {
        boolean state = false;
        String sql = "INSERT INTO historia_clinica (id_ejecucionCita, motivo_consulta, enfermedad_actual, antecedentes, diagnostico, tratamiento, evolucion, observaciones) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (h.getIdEjecucionCita() > 0) pst.setInt(1, h.getIdEjecucionCita()); else pst.setNull(1, Types.INTEGER);
            pst.setString(2, h.getMotivoConsulta());
            pst.setString(3, h.getEnfermedadActual());
            pst.setString(4, h.getAntecedentes());
            pst.setString(5, h.getDiagnostico());
            pst.setString(6, h.getTratamiento());
            pst.setString(7, h.getevolucion());
            pst.setString(8, h.getObservaciones());

            int res = pst.executeUpdate();
            if (res > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) h.setIdHistoriaClinica(rs.getInt(1));
                }
                state = true;
            }
        } catch (SQLException e) {
            System.out.println("Error insertarHistoria: " + e.getMessage());
        }
        return state;
    }

    public HistoriaClinica obtenerPorId(int id) {
        HistoriaClinica h = null;
        String sql = "SELECT * FROM historia_clinica WHERE id_historia_clinica = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
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
            System.out.println("Error obtenerPorId Historia: " + e.getMessage());
        }
        return h;
    }
    //Obtener Historias clínicas por paciente
    public List<HistoriaClinica> obtenerPorPaciente(int idPaciente) {
        List<HistoriaClinica> lista = new ArrayList<>();
        String sql= "SELECT * FROM historia_clinica WHERE id_paciente = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idPaciente);
            try (ResultSet rs = pst.executeQuery()) {
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
            System.out.println("Error obtenerPorPaciente: " + e.getMessage());
        }
        return lista;
    }

    public List<HistoriaClinica> listarHistorias() {
        List<HistoriaClinica> lista = new ArrayList<>();
        String sql = "SELECT * FROM historia_clinica";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
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
            System.out.println("Error listarHistorias: " + e.getMessage());
        }
        return lista;
    }

    public boolean updateHistoria(HistoriaClinica h) {
        boolean state = false;
        String sql = "UPDATE historia_clinica SET id_ejecucionCita=?, motivo_consulta=?, enfermedad_actual=?, antecedentes=?, diagnostico=?, tratamiento=?, evolucion=?, observaciones=? WHERE id_historia_clinica=?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            if (h.getIdEjecucionCita() > 0) pst.setInt(1, h.getIdEjecucionCita()); else pst.setNull(1, Types.INTEGER);
            pst.setString(2, h.getMotivoConsulta());
            pst.setString(3, h.getEnfermedadActual());
            pst.setString(4, h.getAntecedentes());
            pst.setString(5, h.getDiagnostico());
            pst.setString(6, h.getTratamiento());
            pst.setString(7, h.getevolucion());
            pst.setString(8, h.getObservaciones());
            pst.setInt(9, h.getIdHistoriaClinica());

            int res = pst.executeUpdate();
            state = res > 0;
        } catch (SQLException e) {
            System.out.println("Error updateHistoria: " + e.getMessage());
        }
        return state;
    }

    public boolean deleteHistoria(int idHistoria) {
        boolean state = false;
        String sql = "DELETE FROM historia_clinica WHERE id_historia_clinica = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idHistoria);
            int res = pst.executeUpdate();
            state = res > 0;
        } catch (SQLException e) {
            System.out.println("Error deleteHistoria: " + e.getMessage());
        }
        return state;
    }
}
