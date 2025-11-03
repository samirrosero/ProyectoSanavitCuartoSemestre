package com.proyecto.sanavit.controlador;

import com.proyecto.sanavit.modelo.ConexionDatabase;

import java.util.List;

import com.proyecto.sanavit.modelo.*;

public class TestApp {
    public static void main(String[] args) {
        ConexionDatabase.getConnection();
        PacienteDao pdao = new PacienteDao();
        List<Paciente> pacientes = pdao.obtenerTodosLosPacientes();
        pacientes.forEach(p -> System.out.println(p.getNombre()));

        CitaDao cdao = new CitaDao();
        List<Cita> citas = cdao.selectCita();
        citas.forEach(c -> System.out.println("Cita #" + c.getIdCita()));
    }
}
