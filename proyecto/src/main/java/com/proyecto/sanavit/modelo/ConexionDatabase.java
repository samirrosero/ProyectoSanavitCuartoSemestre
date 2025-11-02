package com.proyecto.sanavit.modelo;

// conexion a base de datos
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
public class ConexionDatabase {
    private static Connection connection;
 
    public static Connection getConnection() {
        System.out.println("intentando conectar...");
        if (connection == null) {
            Properties properties = new Properties();
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                properties.load(new FileInputStream("config.properties"));
 
                // variables de conexion
                String url = properties.getProperty("URL").toString();
                String user = properties.getProperty("USER").toString();
                String password = properties.getProperty("PASSWORD").toString();
 
                // ESTABLECER LA CONEXION
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Conexion exitosa a la base de datos");
            } catch (ClassNotFoundException e) {
                System.out.println("Error al cargar el driver de MySQL: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Error al conectar a la base de datos: " + e.getMessage());
            } catch (FileNotFoundException e) {
                System.out.println("Archivo de configuracion no encontrado: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Error al leer el archivo de configuracion: " + e.getMessage());
            }
        }
        return connection;
    }
 
        public static Statement getInstance() {
        try {
            return getConnection().createStatement();
        } catch (SQLException e) {
            System.out.println("Error al crear el Statement: " + e.getMessage());
            return null;
        }
    }
 
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexion cerrada");
                } catch (SQLException e) {
                    System.out.println("Error al cerrar la conexion: " + e.getMessage());
                }
            }
        }
 
}
 