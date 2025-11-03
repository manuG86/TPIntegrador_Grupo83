package LibroFicha_main;

import java.sql.Connection;
import java.sql.SQLException;
import LibroFicha_config.DatabaseConnection;

public class Main {
    public static void main(String[] args) {

        // =========================================================
        // TEST DE CONEXIÓN (para el video / diagnóstico rápido)
        // =========================================================
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Conexión exitosa a la base de datos TPI_Libro_Ficha.");
            } else {
                System.out.println("No se pudo establecer la conexión (conn == null).");
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
        

        // Ejecución del menú principal del TFI
        new AppMenu().mostrarMenu();
    }
}

 