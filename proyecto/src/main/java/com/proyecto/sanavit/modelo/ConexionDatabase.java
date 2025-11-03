package com.proyecto.sanavit.modelo;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class ConexionDatabase {



    public static Connection getConnection() {
        System.out.println("Intentando conectar...");
        Properties properties = new Properties();
        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            properties.load(new FileInputStream("config.properties"));

            // Variables de conexión
            String url = properties.getProperty("URL").trim();
            String user = properties.getProperty("USER").trim();
            String password = properties.getProperty("PASSWORD").trim();

            // Establecer la conexión
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión exitosa a la base de datos");
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver de MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de configuración no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de configuración: " + e.getMessage());
        }

        return connection;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexión cerrada");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
