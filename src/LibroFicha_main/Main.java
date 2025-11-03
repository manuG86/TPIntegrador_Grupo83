package LibroFicha_main;

import java.sql.Connection;
import java.sql.SQLException;
import LibroFicha_config.DatabaseConnection;

/**
 * Main del TFI de Programación 2 (Grupo 83)
 * -----------------------------------------
 * - Objetivo: iniciar la aplicación y (opcional) mostrar una prueba breve de conexión.
 * - La app usa arquitectura por capas:
 *      entities -> dao -> service -> main
 * - Este Main NO hace lógica de negocio: sólo verifica conexión y llama al menú (AppMenu).
 *
 * Nota de integración:
 * - El paquete SeguridadBD.integracionBDI pertenece al TFI de Bases de Datos I (consultas seguras, DTO, etc.).
 *   En este TFI de Programación 2 no usamos esas clases; se conservan sólo para mostrar integración entre materias.
 */
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
            // Opción A: continuar igual, porque cada opción del menú abre su propia conexión.
            System.out.println("Advertencia: Falló el test de conexión: " + e.getMessage());
            // Opción B (si preferimos salir al fallar):
            // System.out.println("Error de conexión. Revise db.properties y los scripts SQL.");
            // return;
        }

        // =========================================================
        // EJECUCIÓN DEL MENÚ PRINCIPAL DEL TFI (capa de presentación)
        // =========================================================
        new AppMenu().iniciar();
    }
}

 
