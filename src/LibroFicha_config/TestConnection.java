package LibroFicha_config;

import java.sql.Connection;

/**
 * Prueba rápida para verificar la conexión con la base de datos.
 * Usa DatabaseConnection y muestra mensajes de estado.
 */
public class TestConnection {

    public static void main(String[] args) {
        System.out.println("[TEST] Iniciando prueba de conexión...");
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("[TEST] ✅ Conexión exitosa a la base de datos.");
            } else {
                System.out.println("[TEST] ⚠️ No se pudo establecer la conexión.");
            }
        } catch (Exception e) {
            System.err.println("[TEST] ❌ Error durante la conexión:");
            e.printStackTrace();
        } finally {
            // Cierra conexión si quedó abierta
            DatabaseConnection.closeConnection();
        }
    }
}

