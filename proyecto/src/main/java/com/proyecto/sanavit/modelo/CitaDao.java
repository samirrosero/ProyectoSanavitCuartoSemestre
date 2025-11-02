package com.proyecto.sanavit.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDao {

    // === INSERTAR ===
    public boolean insertarCita(Cita cita) {
        boolean state = false;
        String sql = "INSERT INTO cita (id_medico, id_paciente, id_estado_cita, id_modalidad, id_portafolio, fecha_cita, hora_cita) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDatabase.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, cita.getIdMedico());
            pst.setInt(2, cita.getIdPaciente());
            pst.setInt(3, cita.getIdEstadoCita());
            pst.setInt(4, cita.getIdModalidad());
            pst.setInt(5, cita.getIdPortafolio());
            pst.setDate(6, new java.sql.Date(cita.getFechaCita().getTime()));
            pst.setTime(7, cita.getHoraCita());

            int res = pst.executeUpdate();
            if (res > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        cita.setIdCita(rs.getInt(1));
                    }
                }
                state = true;
            }

        } catch (SQLException e) {
            System.out.println("Error al insertar cita: " + e.getMessage());
        }

        return state;
    }
    // ACTUALIZAR ESTADO DE UNA CITA (CONFIRMAR, CANCELAR, ETC)
        public boolean actualizarEstadoCita(int idCita, int nuevoEstado) {
            String sql = "UPDATE cita SET id_estado_cita = ? WHERE id_cita = ?";
            try (Connection conn = ConexionDatabase.getConnection();
                    PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setInt(1, nuevoEstado);
                pst.setInt(2, idCita);
                
                return pst.executeUpdate() > 0;
            } catch (SQLException e) {
                System.out.println("Error al actualizar estado de la cita: " + e.getMessage());
                return false;
            }
        }

    // === ACTUALIZAR ===
    public boolean updateCita(Cita cita) {
        boolean state = false;
        String sql = "UPDATE cita SET id_medico=?, id_paciente=?, id_estado_cita=?, id_modalidad=?, id_portafolio=?, fecha_cita=?, hora_cita=? "
                + "WHERE id_cita=?";

        try (Connection conn = ConexionDatabase.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, cita.getIdMedico());
            pst.setInt(2, cita.getIdPaciente());
            pst.setInt(3, cita.getIdEstadoCita());
            pst.setInt(4, cita.getIdModalidad());
            pst.setInt(5, cita.getIdPortafolio());
            pst.setDate(6, new java.sql.Date(cita.getFechaCita().getTime()));
            pst.setTime(7, cita.getHoraCita());
            pst.setInt(8, cita.getIdCita());

            int res = pst.executeUpdate();
            state = res > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar cita: " + e.getMessage());
        }

        return state;
    }

    // === ELIMINAR ===
    public boolean deleteCita(int idCita) {
        boolean state = false;
        String sql = "DELETE FROM cita WHERE id_cita = ?";

        try (Connection conn = ConexionDatabase.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idCita);
            int res = pst.executeUpdate();
            state = res > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar cita: " + e.getMessage());
        }

        return state;
    }

    // === LISTAR ===
    public List<Cita> selectCita() {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM cita";

        try (Connection conn = ConexionDatabase.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Cita c = new Cita(0, 0, 0, 0, 0, 0, null, null);
                c.setIdCita(rs.getInt("id_cita"));
                c.setIdMedico(rs.getInt("id_medico"));
                c.setIdPaciente(rs.getInt("id_paciente"));
                c.setIdEstadoCita(rs.getInt("id_estado_cita"));
                c.setIdModalidad(rs.getInt("id_modalidad"));
                c.setIdPortafolio(rs.getInt("id_portafolio"));
                c.setFechaCita(rs.getDate("fecha_cita"));
                c.setHoraCita(rs.getTime("hora_cita"));
                lista.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar Cita: " + e.getMessage());
        }

        return lista;
    }

    // === OBTENER CITA POR ID ===
    public Cita obtenerCitaPorId(int idCita) {
        Cita cita = null;
        String sql = "SELECT * FROM cita WHERE id_cita = ?";

        try (Connection conn = ConexionDatabase.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idCita);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                cita = new Cita(0, 0, 0, 0, 0, 0, null, null);
                cita.setIdCita(rs.getInt("id_cita"));
                cita.setIdMedico(rs.getInt("id_medico"));
                cita.setIdPaciente(rs.getInt("id_paciente"));
                cita.setIdEstadoCita(rs.getInt("id_estado_cita"));
                cita.setIdModalidad(rs.getInt("id_modalidad"));
                cita.setIdPortafolio(rs.getInt("id_portafolio"));
                cita.setFechaCita(rs.getDate("fecha_cita"));
                cita.setHoraCita(rs.getTime("hora_cita"));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener cita: " + e.getMessage());
        }

        return cita;
    }

    // === OBTENER Cita POR PACIENTE ===
    public List<Cita> obtenerCitaPorPaciente(int idPaciente) {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM cita WHERE id_paciente = ?";

        try (Connection conn = ConexionDatabase.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idPaciente);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Cita c = new Cita(0, 0, 0, 0, 0, 0, null, null);
                c.setIdCita(rs.getInt("id_cita"));
                c.setIdMedico(rs.getInt("id_medico"));
                c.setIdPaciente(rs.getInt("id_paciente"));
                c.setIdEstadoCita(rs.getInt("id_estado_cita"));
                c.setIdModalidad(rs.getInt("id_modalidad"));
                c.setIdPortafolio(rs.getInt("id_portafolio"));
                c.setFechaCita(rs.getDate("fecha_cita"));
                c.setHoraCita(rs.getTime("hora_cita"));
                lista.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener Cita del paciente: " + e.getMessage());
        }

        return lista;
    }

    public List<Cita> obtenerCitaPorMedico(int idMedico) {
        List<Cita> lista = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conexion = ConexionDatabase.getConnection();
            String sql = "SELECT * FROM cita WHERE id_medico = ?";
            stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, idMedico);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Cita cita = new Cita(
                        rs.getInt("id_cita"),
                        rs.getInt("id_medico"),
                        rs.getInt("id_paciente"),
                        rs.getInt("id_estado_cita"),
                        rs.getInt("id_modalidad"),
                        rs.getInt("id_portafolio"),
                        rs.getDate("fecha_cita"),
                        rs.getTime("hora_cita"));
                lista.add(cita);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener Cita por médico: " + e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conexion != null)
                    conexion.close();
            } catch (SQLException ex) {
                System.err.println("Error al cerrar recursos: " + ex.getMessage());
            }
        }

        return lista;
    }

    private Cita mapCita(ResultSet rs) throws SQLException {
    return new Cita(
        rs.getInt("id_cita"),
        rs.getInt("id_medico"),
        rs.getInt("id_paciente"),
        rs.getInt("id_estado_cita"),
        rs.getInt("id_modalidad"),
        rs.getInt("id_portafolio"),
        rs.getDate("fecha_cita"),
        rs.getTime("hora_cita")
    );
}

}
