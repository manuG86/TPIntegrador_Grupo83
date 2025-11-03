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

import SeguridadBD.integracionBDI.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * ============================================================
 *  Grupo 83 – TFI Bases de Datos I – Etapa 4
 *  AppConsultaSegura
 *
 *  Objetivos:
 *   1) Verificar conexión y existencia de tablas básicas.
 *   2) Ejecutar consulta SEGURA (PreparedStatement) sobre la VISTA
 *      pública de catálogo, filtrando por título exacto.
 *   3) Repetir la consulta con un payload malicioso ("' OR '1'='1"),
 *      y demostrar que NO devuelve filas gracias a los parámetros (?).
 *   4) (Opcional - COMENTADO) Demostración con usuario de mínima
 *      (app_min): SELECT a tablas crudas → 1142; SELECT a vista → OK.
 *
 * ============================================================
 */
public class AppConsultaSegura {

    private static final String VIEW_NAME = "vistacatalogopublico";

    // Título existente para la “consulta normal” (ajustado a uno real de nuestra BD)
    private static final String TITULO_EXISTENTE = "Libro_00996";

    // Payload malicioso típico para probar inyección
    private static final String PAYLOAD_INJECTION = "' OR '1'='1";

    public static void main(String[] args) {
        System.out.println("=== Grupo 83 – AppConsultaSegura (Etapa 4) ===");

        try (Connection conn = DatabaseConnection.getConnection()) {
            String catalog = (conn.getCatalog() != null ? conn.getCatalog() : "(catálogo no informado)");
            System.out.println("[DB] Conectado. Catálogo: " + catalog);

            // 1) Chequeo de tablas básicas (autor/libro/ficha_bibliografica)
            checkTablasBasicas(conn);

            // 2) Consulta SEGURA normal (por título exacto)
            int filasNormal = consultaSeguraPorTitulo(conn, TITULO_EXISTENTE);
            System.out.println("[OK] Filas devueltas (consulta normal): " + filasNormal);

            // 3) Consulta SEGURA con payload malicioso
            int filasPayload = consultaSeguraPorTitulo(conn, PAYLOAD_INJECTION);
            if (filasPayload == 0) {
                System.out.println("PreparedStatement neutralizó el payload de inyección. Filas: " + filasPayload);
            } else {
                System.out.println("️Se devolvieron filas con payload; revisar estrictamente que no haya concatenación.");
            }

            /*
             * ============================================================
             *   OPCIÓN ALTERNATIVA (COMENTADA): usuario de mínima (app_min)
             *   Principio de mínimo privilegio + vistas como única interfaz
             *
             *   Para ejecutar esta demostración:
             *   1) Cambiar credenciales en db.properties:
             *         db.user=app_min
             *         db.password=********
             *   2) Ejecutar este programa. Se espera:
             *      - SELECT directo a tablas crudas → Error 1142 (denied)
             *      - SELECT sobre la vista pública → OK
             *
             *   Evidencias:
             *      SHOW GRANTS FOR 'app_min'@'localhost';
             *      SELECT * FROM vistacatalgopublico LIMIT 3;
             * ============================================================
             */
             //demoUsuarioMinimo(conn); //  Dejar comentado. Usarlo para la evidencia con app_min.

        } catch (Exception e) {
            System.err.println("[ERROR] " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nFin de prueba — capturar consola y pegar en el informe de la Etapa 4.");
    }

    // ------------------------------------------------------------
    // Chequea existencia de tablas básicas en el schema conectado
    // ------------------------------------------------------------
    private static void checkTablasBasicas(Connection conn) {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT table_name " +
                "FROM information_schema.tables " +
                "WHERE table_schema = DATABASE() " +
                "AND table_name IN ('autor','libro','ficha_bibliografica')")) {

            boolean autor = false, libro = false, ficha = false;

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String t = rs.getString(1);
                    if ("autor".equalsIgnoreCase(t)) autor = true;
                    if ("libro".equalsIgnoreCase(t)) libro = true;
                    if ("ficha_bibliografica".equalsIgnoreCase(t)) ficha = true;
                }
            }

            if (autor && libro && ficha) {
                System.out.println("[Check] Tablas básicas encontradas: autor, libro, ficha_bibliografica.");
            } else {
                System.out.println("[Check] FALTAN TABLAS en el schema actual: autor=" + autor
                        + ", libro=" + libro + ", ficha_bibliografica=" + ficha);
                System.out.println("       -> Revisar db.url (schema) o ejecutar el DDL antes de la prueba.");
            }
        } catch (Exception ex) {
            System.err.println("[Check] Error verificando tablas básicas: " + ex.getMessage());
        }
    }

    // ------------------------------------------------------------
    // Consulta SEGURA a la vista pública por título exacto
    // (usa parámetros, no concatenación)
    // ------------------------------------------------------------
    private static int consultaSeguraPorTitulo(Connection conn, String titulo) {
        String sql = ""
            + "SELECT id_libro, titulo, anio_publicacion, id_autor, autor, nacionalidad, isbn_mask, dewey, idioma \n"
            + "FROM " + VIEW_NAME + " \n"
            + "WHERE titulo = ?";

        int count = 0;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, titulo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (count == 0) {
                        // Imprime el primero como ejemplo de evidencia
                        System.out.println("Ejemplo → id_libro=" + rs.getLong("id_libro")
                                + ", titulo=" + rs.getString("titulo")
                                + ", autor=" + rs.getString("autor")
                                + ", isbn_mask=" + rs.getString("isbn_mask"));
                    }
                    count++;
                }
            }
        } catch (Exception ex) {
            System.err.println("[ConsultaSegura] Error: " + ex.getMessage());
        }

        return count;
    }

    // ------------------------------------------------------------
    // (COMENTADO) Demostración con usuario de mínima (app_min)
    //   - SELECT a tabla cruda → debería fallar con 1142
    //   - SELECT a vista pública → debería funcionar
    // ------------------------------------------------------------
    @SuppressWarnings("unused")
    private static void demoUsuarioMinimo(Connection conn) {
        // Intento directo a tabla cruda (espera 1142 con app_min)
        try (Statement st = conn.createStatement()) {
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM libro")) {
                // Si llega aquí con app_min, hay un problema de permisos (no debería).
                if (rs.next()) {
                    System.out.println("app_min pudo consultar tabla 'libro' (revisar GRANTS). COUNT=" + rs.getInt(1));
                }
            }
        } catch (Exception ex) {
            System.out.println("OK (esperado): acceso a tabla 'libro' denegado para app_min → " + ex.getMessage());
        }

        // Lectura a la vista pública (debería funcionar con app_min)
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id_libro, titulo FROM " + VIEW_NAME + " LIMIT 3")) {
            try (ResultSet rs = ps.executeQuery()) {
                int c = 0;
                while (rs.next()) {
                    if (c == 0) System.out.println("Lectura en vista OK (app_min):");
                    System.out.println("  - id_libro=" + rs.getLong("id_libro")
                            + ", titulo=" + rs.getString("titulo"));
                    c++;
                }
                if (c == 0) {
                    System.out.println("Vista accesible, pero sin filas (OK igualmente para la evidencia).");
                }
            }
        } catch (Exception ex) {
            System.err.println("Error leyendo la vista con app_min: " + ex.getMessage());
        }
    }
}



