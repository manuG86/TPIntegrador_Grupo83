/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LibroFicha_config;

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
            }
        } catch (Exception e) {
            // Si no existe el archivo, usamos defaults (cambiar si hace falta)
        }

        String url = props.getProperty("db.url",
                "jdbc:mysql://localhost:3306/tpi_libro_ficha?serverTimezone=UTC");
        String user = props.getProperty("db.user", "root");
        String pass = props.getProperty("db.password", "");

        return DriverManager.getConnection(url, user, pass);
    }
}
