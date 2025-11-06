package LibroFicha_config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase encargada de establecer la conexión con la base de datos MySQL.
 * Lee los parámetros desde el archivo db.properties ubicado en la raíz del src.
 */
public class DatabaseConnection {

    // Quitar la Connection estática compartida para evitar "connection closed"
    // private static Connection connection = null;

    // Carga del driver y de propiedades
    private static String URL;
    private static String USER;
    private static String PASS;
    private static volatile boolean initialized = false;

    private static void initOnce() {
        if (initialized) return;
        synchronized (DatabaseConnection.class) {
            if (initialized) return;
            try {
                // Driver MySQL 8.x
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Cargar propiedades desde la RAÍZ del src
                Properties props = new Properties();
                try (InputStream in = DatabaseConnection.class.getResourceAsStream("/db.properties")) {
                    if (in != null) {
                        props.load(in);
                    } else {
                        // fallback si se ejecuta fuera del IDE
                        try (FileInputStream fis = new FileInputStream("src/db.properties")) {
                            props.load(fis);
                        }
                    }
                }

                URL  = props.getProperty("db.url");
                USER = props.getProperty("db.user");
                PASS = props.getProperty("db.password");
                initialized = true;
            } catch (ClassNotFoundException e) {
                System.out.println("[DB] Error: Driver de MySQL no encontrado en el classpath.");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("[DB] Error al leer db.properties.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Retorna una NUEVA conexión por cada llamada.
     * Cada DAO debe usar try-with-resources para cerrarla al terminar.
     */
    public static Connection getConnection() throws SQLException {
        initOnce();
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /**
     * Ya no se mantiene una conexión global, por lo que esto es opcional.
     * Se deja por compatibilidad, pero no hace nada.
     */
    public static void closeConnection() {
        // sin-op: cada DAO cierra su propia conexión
    }
}

