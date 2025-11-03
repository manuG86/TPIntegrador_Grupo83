package LibroFicha_main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import LibroFicha_config.DatabaseConnection;
import LibroFicha_dao.FichaBibliograficaDaoImpl;
import LibroFicha_dao.LibroDaoImpl;
import LibroFicha_entities.FichaBibliografica;
import LibroFicha_entities.Libro;
import LibroFicha_service.LibroService;

/**
 * AppMenu mínimo para cumplir la consigna:
 * 1) Crear Libro + Ficha (commit)
 * 2) Crear Libro + Ficha (rollback simulado)
 * 3) Listar Libros (activos)
 * 4) Buscar Libro por ID (muestra Ficha si existe)
 * 5) Eliminar lógico Libro (+ Ficha)
 * 0) Salir
 */
public class AppMenu {

    private final Scanner scanner = new Scanner(System.in);

    // Service y DAOs
    private final LibroService service = new LibroService();
    private final LibroDaoImpl libroDao = new LibroDaoImpl();
    private final FichaBibliograficaDaoImpl fichaDao = new FichaBibliograficaDaoImpl();

    public void mostrarMenu() {
        while (true) {
            System.out.println("\n=== TPI - Libro ↦ Ficha ===");
            System.out.println("1. Crear Libro + Ficha (commit)");
            System.out.println("2. Crear Libro + Ficha (rollback simulado)");
            System.out.println("3. Listar Libros (activos)");
            System.out.println("4. Buscar Libro por ID (y Ficha)");
            System.out.println("5. Eliminar Libro (baja lógica) (+ Ficha)");
            System.out.println("0. Salir");
            System.out.print("Opción: ");

            Integer op = leerEnteroOpcional();
            if (op == null) { System.out.println("Entrada inválida."); continue; }

            try {
                switch (op) {
                    case 1 -> opcionCrearCommit();
                    case 2 -> opcionCrearRollback();
                    case 3 -> opcionListarLibros();
                    case 4 -> opcionBuscarPorId();
                    case 5 -> opcionEliminarLogico();
                    case 0 -> { System.out.println("Saliendo..."); return; }
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ---------- Opciones ----------

    private void opcionCrearCommit() throws Exception {
        System.out.println("\n== Crear Libro + Ficha (commit) ==");
        Libro libro = pedirLibro();
        FichaBibliografica ficha = pedirFicha();
        service.insertarConFicha(libro, ficha);
        System.out.println("OK: insertado con commit. ID libro = " + libro.getId());
    }

    private void opcionCrearRollback() throws Exception {
        System.out.println("\n== Crear con ROLLBACK SIMULADO ==");
        Libro libro = pedirLibro();
        FichaBibliografica ficha = pedirFicha();
        try {
            service.insertarConFichaConRollbackSimulado(libro, ficha);
        } catch (RuntimeException ex) {
            System.out.println("Rollback ejecutado (esperado): " + ex.getMessage());
        }
    }

    private void opcionListarLibros() throws SQLException {
        System.out.println("\n== Libros activos ==");
        try (Connection conn = DatabaseConnection.getConnection()) {
            List<Libro> libros = libroDao.leerTodos(conn);
            if (libros.isEmpty()) { System.out.println("(sin libros)"); return; }
            for (Libro l : libros) {
                // muestro ISBN si hay ficha activa
                FichaBibliografica f = leerFichaActivaPorLibroId(conn, l.getId());
                String extra = (f != null ? " | ISBN=" + f.getIsbn() : "");
                System.out.println(l + extra);
            }
        }
    }

    private void opcionBuscarPorId() throws SQLException {
        System.out.println("\n== Buscar Libro por ID ==");
        System.out.print("ID: ");
        Long id = leerLongObligatorio();
        try (Connection conn = DatabaseConnection.getConnection()) {
            Libro l = libroDao.leer(conn, id);
            if (l == null) { System.out.println("No existe (o eliminado)."); return; }
            System.out.println("Libro: " + l);
            FichaBibliografica f = leerFichaActivaPorLibroId(conn, id);
            System.out.println("Ficha: " + (f != null ? f : "(no tiene ficha activa)"));
        }
    }

    private void opcionEliminarLogico() throws Exception {
        System.out.println("\n== Eliminar lógico Libro (+ Ficha) ==");
        System.out.print("ID libro: ");
        Long id = leerLongObligatorio();
        service.eliminar(id);
        System.out.println("OK: marcado como eliminado (Libro y su Ficha si existían).");
    }

    // ---------- Formularios mínimos ----------

    private Libro pedirLibro() {
        Libro l = new Libro();
        l.setEliminado(false);
        System.out.print("Título: "); l.setTitulo(leerTextoObligatorio());
        System.out.print("Autor: ");  l.setAutor(leerTextoObligatorio());
        System.out.print("Editorial (opcional): ");
        String ed = scanner.nextLine().trim();
        l.setEditorial(ed.isEmpty() ? null : ed);
        System.out.print("Año edición (opcional, ENTER = null): ");
        String anio = scanner.nextLine().trim();
        if (anio.isEmpty()) l.setAnioEdicion(null);
        else {
            try { l.setAnioEdicion(Integer.parseInt(anio)); }
            catch (NumberFormatException e) { System.out.println("Valor inválido, se deja NULL."); l.setAnioEdicion(null); }
        }
        return l;
    }

    private FichaBibliografica pedirFicha() {
        FichaBibliografica f = new FichaBibliografica();
        f.setEliminado(false);
        System.out.print("ISBN: "); f.setIsbn(leerTextoObligatorio());
        System.out.print("Clasificación Dewey (opcional): ");
        String dewey = scanner.nextLine().trim();
        f.setClasificacionDewey(dewey.isEmpty() ? null : dewey);
        System.out.print("Estantería (opcional): ");
        String est = scanner.nextLine().trim();
        f.setEstanteria(est.isEmpty() ? null : est);
        System.out.print("Idioma (opcional): ");
        String idi = scanner.nextLine().trim();
        f.setIdioma(idi.isEmpty() ? null : idi);
        return f;
    }

    // ---------- Helpers SQL lectura simple ----------

    private FichaBibliografica leerFichaActivaPorLibroId(Connection conn, Long libroId) throws SQLException {
        final String sql = "SELECT id, eliminado, isbn, clasificacionDewey, estanteria, idioma, libro_id " +
                           "FROM ficha_bibliografica WHERE libro_id = ? AND eliminado = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, libroId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                FichaBibliografica f = new FichaBibliografica();
                f.setId(rs.getLong("id"));
                f.setEliminado(rs.getBoolean("eliminado"));
                f.setIsbn(rs.getString("isbn"));
                f.setClasificacionDewey(rs.getString("clasificacionDewey"));
                f.setEstanteria(rs.getString("estanteria"));
                f.setIdioma(rs.getString("idioma"));
                f.setLibroId((Long) rs.getObject("libro_id"));
                return f;
            }
        }
    }

    // ---------- Helpers IO mínimos ----------

    private Integer leerEnteroOpcional() {
        if (!scanner.hasNextInt()) { scanner.nextLine(); return null; }
        int v = scanner.nextInt();
        scanner.nextLine();
        return v;
    }

    private Long leerLongObligatorio() {
        while (true) {
            String s = scanner.nextLine().trim();
            try { return Long.parseLong(s); }
            catch (NumberFormatException e) { System.out.print("Número inválido. Intente de nuevo: "); }
        }
    }

    private String leerTextoObligatorio() {
        while (true) {
            String s = scanner.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.print("Obligatorio. Ingrese nuevamente: ");
        }
    }
}

