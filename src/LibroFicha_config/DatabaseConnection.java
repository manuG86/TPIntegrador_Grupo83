package LibroFicha_config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// Lee db.properties (URL, usuario, password).
// Si cambiamos el puerto o el schema, editar resources/db.properties o src/db.properties.

public class DatabaseConnection {

    private static final String PROPERTIES_FILE = "db.properties";

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();

        // Carga db.properties desde el classpath (Source Packages / default package)
        try (InputStream is = DatabaseConnection.class.getClassLoader()
                                                     .getResourceAsStream(PROPERTIES_FILE)) {
            if (is != null) {
                props.load(is);
            } else {
                System.out.println("[DB] No se encontró " + PROPERTIES_FILE + " en el classpath. Se usarán valores por defecto.");
            }
        } catch (Exception e) {
            System.out.println("[DB] Error leyendo " + PROPERTIES_FILE + ": " + e.getMessage());
        }

        // Valores por defecto (por si el archivo no existe)
        String url  = props.getProperty(
                "db.url",
                "jdbc:mysql://localhost:3306/tpi_libro_ficha?useSSL=false&serverTimezone=UTC"
        );
        String user = props.getProperty("db.user", "root");
        String pass = props.getProperty("db.password", "");

        // Forzar el driver (no suele hacer falta en MySQL 8+):
        // try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (ClassNotFoundException ignored) {}

        return DriverManager.getConnection(url, user, pass);
    }
}
=======
package LibroFicha_config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// Lee db.properties (URL, usuario, password).
// Si cambiamos el puerto o el schema, editar resources/db.properties o src/db.properties.

public class DatabaseConnection {

    private static final String PROPERTIES_FILE = "db.properties";

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();

        // Carga db.properties desde el classpath (Source Packages / default package)
        try (InputStream is = DatabaseConnection.class.getClassLoader()
                                                     .getResourceAsStream(PROPERTIES_FILE)) {
            if (is != null) {
                props.load(is);
            } else {
                System.out.println("[DB] No se encontró " + PROPERTIES_FILE + " en el classpath. Se usarán valores por defecto.");
            }
        } catch (Exception e) {
            System.out.println("[DB] Error leyendo " + PROPERTIES_FILE + ": " + e.getMessage());
        }

        // Valores por defecto (por si el archivo no existe)
        String url  = props.getProperty(
                "db.url",
                "jdbc:mysql://localhost:3306/tpi_libro_ficha?useSSL=false&serverTimezone=UTC"
        );
        String user = props.getProperty("db.user", "root");
        String pass = props.getProperty("db.password", "");

        // Forzar el driver (no suele hacer falta en MySQL 8+):
        // try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (ClassNotFoundException ignored) {}

        return DriverManager.getConnection(url, user, pass);
    }
}
