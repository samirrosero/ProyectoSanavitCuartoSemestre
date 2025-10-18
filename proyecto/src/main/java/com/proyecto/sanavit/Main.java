package com.proyecto.sanavit;
import javax.swing.JOptionPane;
import java.sql.Connection;

import com.proyecto.sanavit.modelo.ConexionDatabase;

public class Main {
   public static void main(String[] args) {
        Connection conexion = null;

        try {
            conexion = ConexionDatabase.getConnection();
            if (conexion != null) {
                System.out.println(" Conexión exitosa a la base de datos.");
            } else {
                JOptionPane.showMessageDialog(null, " No se pudo establecer conexión con la base de datos.");
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
            return;
        } 
    }
}