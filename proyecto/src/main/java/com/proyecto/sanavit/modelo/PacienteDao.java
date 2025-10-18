package com.proyecto.sanavit.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao {
    private PreparedStatement pst;
    private ResultSet rs;

    public boolean insertarPaciente(Paciente paciente) {
        boolean state = false;
        String sql = "INSERT INTO paciente (nombre, correo, edad, telefono, sexo, direccion, identificacion, id_usuario) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, paciente.getNombre());
            pst.setString(2, paciente.getCorreo());
            pst.setInt(3, paciente.getEdad());
            pst.setString(4, paciente.getTelefono());
            pst.setString(5, paciente.getSexo());
            pst.setString(6, paciente.getDireccion());
            pst.setString(7, paciente.getIdentificacion());
            pst.setInt(8, paciente.getIdUsuario());

            int res = pst.executeUpdate();
            if (res > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        paciente.setIdPaciente(rs.getInt(1));
                    }
                }
                state = true;
            }
        } catch (SQLException e) {
            System.out.println("Error insertarPaciente: " + e.getMessage());
        }
        return state;
    }

    public ArrayList <Paciente> selectPaciente (String filter, ArrayList <String>data){
        ArrayList<Paciente> list = new ArrayList<>();
        Paciente paciente;
        Connection connect = null;

        try{
            connect = ConexionDatabase.getConnection();
            if (connect != null){
                String sql = "";
                switch (filter) {
                    case "nombre":
                      sql= "SELECT * FROM paciente WHERE nombre REGEXP ?";
                      pst = connect.prepareStatement(sql);
                      pst.setString(1, data.get(0));
                      break;
                    case "identificacion":
                      sql= "SELECT * FROM paciente WHERE identificacion REGEXP ?";
                      pst.setInt(1, Integer.parseInt(data.get(0)));
                    default:
                      sql= "SELECT * FROM paciente WHERE 1";
                      pst = connect.prepareStatement(sql);
                        break;
                }
                rs = pst.executeQuery();
                while (rs.next()) {
                    paciente = new Paciente(0, sql, sql, 0, sql, sql, filter, sql, 0);
                    paciente.setIdPaciente(rs.getInt("id_paciente"));
                    paciente.setNombre(rs.getString("nombre"));
                    paciente.setCorreo(rs.getString("correo"));
                    paciente.setEdad(rs.getInt("edad"));
                    ;
                    paciente.setTelefono(rs.getString("telefono"));
                    paciente.setSexo(rs.getString("sexo"));
                    paciente.setDireccion(rs.getString("direccion"));
                    paciente.setIdentificacion(rs.getString("identificacion"));
                    paciente.setIdUsuario(rs.getInt("id_usuario"));

                    list.add(paciente);
                }
            } else {
                System.out.println("Conexion fallida");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ConexionDatabase.closeConnection();
                pst.close();
                rs.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return list;
    }

    public List<Paciente> listarPacientes() {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM paciente";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Paciente p = new Paciente(0, null, null, 0, null, null, null, null, 0);
                p.setIdPaciente(rs.getInt("id_paciente"));
                p.setNombre(rs.getString("nombre"));
                p.setCorreo(rs.getString("correo"));
                p.setEdad(rs.getInt("edad"));
                p.setTelefono(rs.getString("telefono"));
                p.setSexo(rs.getString("sexo"));
                p.setDireccion(rs.getString("direccion"));
                p.setIdentificacion(rs.getString("identificacion"));
                p.setIdUsuario(rs.getInt("id_usuario"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error listarPacientes: " + e.getMessage());
        }
        return lista;
    }

    public Paciente obtenerPacientePorId(int idPaciente) {
        Paciente p = null;
        String sql = "SELECT * FROM paciente WHERE id_paciente = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idPaciente);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    p = new Paciente(0, null, null, 0, null, null, null, null, 0);
                    p.setIdPaciente(rs.getInt("id_paciente"));
                    p.setNombre(rs.getString("nombre"));
                    p.setCorreo(rs.getString("correo"));
                    p.setEdad(rs.getInt("edad"));
                    p.setTelefono(rs.getString("telefono"));
                    p.setSexo(rs.getString("sexo"));
                    p.setDireccion(rs.getString("direccion"));
                    p.setIdentificacion(rs.getString("identificacion"));
                    p.setIdUsuario(rs.getInt("id_usuario"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error obtenerPacientePorId: " + e.getMessage());
        }
        return p;
    }

    public Paciente obtenerPacientePorIdUsuario(int idUsuario) {
        Paciente p = null;
        String sql = "SELECT * FROM paciente WHERE id_usuario = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idUsuario);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    p = new Paciente(0, null, null, 0, null, null, null, null, 0);
                    p.setIdPaciente(rs.getInt("id_paciente"));
                    p.setNombre(rs.getString("nombre"));
                    p.setCorreo(rs.getString("correo"));
                    p.setEdad(rs.getInt("edad"));
                    p.setTelefono(rs.getString("telefono"));
                    p.setSexo(rs.getString("sexo"));
                    p.setDireccion(rs.getString("direccion"));
                    p.setIdentificacion(rs.getString("identificacion"));
                    p.setIdUsuario(rs.getInt("id_usuario"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error obtenerPacientePorIdUsuario: " + e.getMessage());
        }
        return p;
    }

    public boolean updatePaciente(Paciente paciente) {
        boolean state = false;
        String sql = "UPDATE paciente SET nombre=?, correo=?, edad=?, telefono=?, sexo=?, direccion=?, identificacion=?, id_usuario=? WHERE id_paciente=?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, paciente.getNombre());
            pst.setString(2, paciente.getCorreo());
            pst.setInt(3, paciente.getEdad());
            pst.setString(4, paciente.getTelefono());
            pst.setString(5, paciente.getSexo());
            pst.setString(6, paciente.getDireccion());
            pst.setString(7, paciente.getIdentificacion());
            pst.setInt(8, paciente.getIdUsuario());
            pst.setInt(9, paciente.getIdPaciente());

            int res = pst.executeUpdate();
            state = res > 0;
        } catch (SQLException e) {
            System.out.println("Error updatePaciente: " + e.getMessage());
        }
        return state;
    }

    public boolean deletePaciente(int idPaciente) {
        boolean state = false;
        String sql = "DELETE FROM paciente WHERE id_paciente = ?";
        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idPaciente);
            int res = pst.executeUpdate();
            state = res > 0;
        } catch (SQLException e) {
            System.out.println("Error deletePaciente: " + e.getMessage());
        }
        return state;
    }
}
