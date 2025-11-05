<<<<<<< HEAD
/**
 * NOTA IMPORTANTE (Integración TFI BD I):
 * Este archivo pertenece a ejercicios/demos del TFI de Bases de Datos I
 * (consultas seguras, pruebas con PreparedStatement, etc.).
 *
 * NO forma parte de la implementación de capas del TFI de Programación 2.
 * Lo mantenemos en el repo para mostrar la integración entre ambos trabajos,
 * pero el flujo del TFI de P2 usa exclusivamente:
 * - LibroFicha_entities/*
 * - LibroFicha_dao/*
 * - LibroFicha_service/*
 * - LibroFicha_config/*
 * - LibroFicha_main/*
 */
package SeguridadBD.integracionBDI;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final String PROPERTIES_FILE = "db.properties";

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        try (InputStream is = DatabaseConnection.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (is != null) {
                props.load(is);
            } else {
                System.out.println("[DB] db.properties no encontrado, usando valores por defecto.");
            }
        } catch (Exception e) {
            System.out.println("[DB] No se pudo leer db.properties: " + e.getMessage());
        }

       
        String url = props.getProperty(
                "db.url",
                "jdbc:mysql://localhost:3306/tfi_base_de_datos?serverTimezone=UTC"
        );
        String user = props.getProperty("db.user", "root"); //Aquí se deben poner las credenciales (generalmente root o app_min para nuestro proyecto)
        String pass = props.getProperty("db.password", "");

        Connection conn = DriverManager.getConnection(url, user, pass);
        System.out.println("[DB] Conectado al catálogo: " + conn.getCatalog());
        return conn;
    }
}




=======
/**
 * NOTA IMPORTANTE (Integración TFI BD I):
 * Este archivo pertenece a ejercicios/demos del TFI de Bases de Datos I
 * (consultas seguras, pruebas con PreparedStatement, etc.).
 *
 * NO forma parte de la implementación de capas del TFI de Programación 2.
 * Lo mantenemos en el repo para mostrar la integración entre ambos trabajos,
 * pero el flujo del TFI de P2 usa exclusivamente:
 * - LibroFicha_entities/*
 * - LibroFicha_dao/*
 * - LibroFicha_service/*
 * - LibroFicha_config/*
 * - LibroFicha_main/*
 */
package SeguridadBD.integracionBDI;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final String PROPERTIES_FILE = "db.properties";

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        try (InputStream is = DatabaseConnection.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (is != null) {
                props.load(is);
            } else {
                System.out.println("[DB] db.properties no encontrado, usando valores por defecto.");
            }
        } catch (Exception e) {
            System.out.println("[DB] No se pudo leer db.properties: " + e.getMessage());
        }

       
        String url = props.getProperty(
                "db.url",
                "jdbc:mysql://localhost:3306/tfi_base_de_datos?serverTimezone=UTC"
        );
        String user = props.getProperty("db.user", "root"); //Aquí se deben poner las credenciales (generalmente root o app_min para nuestro proyecto)
        String pass = props.getProperty("db.password", "");

        Connection conn = DriverManager.getConnection(url, user, pass);
        System.out.println("[DB] Conectado al catálogo: " + conn.getCatalog());
        return conn;
    }
}




>>>>>>> Damian
