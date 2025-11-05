<<<<<<< HEAD
package LibroFicha_main;

import java.util.List;
import java.util.Scanner;

import LibroFicha_entities.FichaBibliografica;
import LibroFicha_entities.Libro;
import LibroFicha_service.LibroService;

/**
 * AppMenu (capa de presentación)
 * ------------------------------
 * - Este menú interactúa SOLO con la capa Service (NO con DAOs ni SQL).
 * - El Service aplica validaciones y maneja transacciones; aquí solo pedimos datos
 *   y mostramos resultados/mensajes para el usuario.
 */
public class AppMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final LibroService service = new LibroService();

    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEnteroMenu();
            try {
                switch (opcion) {
                    case 1 -> opcionListarLibros();
                    case 2 -> opcionInsertarLibroConFicha();
                    case 3 -> opcionActualizarLibro();
                    case 4 -> opcionEliminarLibro();
                    case 5 -> opcionBuscarPorId();
                    case 6 -> opcionBuscarPorIsbn();
                    case 0 -> System.out.println("Saliendo...");
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                // Mostramos un mensaje claro si algo falla (validación, SQL, etc.)
                System.out.println("¡Ups! Ocurrió un error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    void mostrarMenu() {
        System.out.println("\n==============================");
        System.out.println("   Sistema Libro–Ficha (TFI)   ");
        System.out.println("==============================");
        System.out.println("1) Listar Libros activos");
        System.out.println("2) Insertar Libro + Ficha (transacción A→B)");
        System.out.println("3) Actualizar Libro");
        System.out.println("4) Eliminar Libro (baja lógica + su Ficha)");
        System.out.println("5) Buscar Libro por ID");
        System.out.println("6) Buscar Libro por ISBN  (JOIN con Ficha)");
        System.out.println("0) Salir");
        System.out.print("Elija una opción: ");
    }

    // -------------------------------------------------------------
    // OPCIONES DEL MENÚ
    // -------------------------------------------------------------

    /** Opción 1: Listar libros activos (Service.getAll) */
    private void opcionListarLibros() throws Exception {
        System.out.println("\n== Libros activos ==");
        List<Libro> libros = service.getAll();
        if (libros == null || libros.isEmpty()) {
            System.out.println("(sin libros)");
            return;
        }
        // Mostramos cada libro; (Equipo: podemos sumar luego el ISBN consultando por Service.buscarPorIsbn)
        for (Libro l : libros) {
            System.out.println(l);
        }
    }

    /**
     * Opción 2: Insertar Libro + Ficha en UNA transacción (A→B)
     * Explicación:
     *  - Cargamos los datos del Libro (A).
     *  - Cargamos los datos de la Ficha (B).
     *  - Llamamos a service.insertarConFicha(...) que hace:
     *      setAutoCommit(false), crear Libro -> setear libroId en Ficha -> crear Ficha, y commit.
     *  - Si algo falla, rollback (atomicidad).
     */
    private void opcionInsertarLibroConFicha() throws Exception {
        System.out.println("\n== Insertar Libro + Ficha (transacción A→B) ==");

        // 1) Pedimos los datos del Libro
        Libro libro = new Libro();
        libro.setEliminado(false); // por defecto, activo
        System.out.print("Título (obligatorio): ");
        libro.setTitulo(leerTextoObligatorio());
        System.out.print("Autor (obligatorio): ");
        libro.setAutor(leerTextoObligatorio());
        System.out.print("Editorial (opcional): ");
        libro.setEditorial(leerTextoOpcional());
        System.out.print("Año de edición (opcional, ENTER para omitir): ");
        Integer anio = leerEnteroOpcional();
        libro.setAnioEdicion(anio);

        // 2) Pedimos los datos de la Ficha
        FichaBibliografica ficha = new FichaBibliografica();
        ficha.setEliminado(false);
        System.out.print("ISBN (obligatorio, 10/13 dígitos, admite guiones): ");
        ficha.setIsbn(leerTextoObligatorio());
        System.out.print("Clasificación Dewey (opcional): ");
        ficha.setClasificacionDewey(leerTextoOpcional());
        System.out.print("Estantería (opcional): ");
        ficha.setEstanteria(leerTextoOpcional());
        System.out.print("Idioma (opcional): ");
        ficha.setIdioma(leerTextoOpcional());

        // 3) Llamamos al Service para que haga todo atómico
        service.insertarConFicha(libro, ficha);
        System.out.println("OK: Libro y Ficha insertados en una sola transacción.");
    }

    /** Opción 3: Actualizar datos básicos de un Libro (Service.actualizar) */
    private void opcionActualizarLibro() throws Exception {
        System.out.println("\n== Actualizar Libro ==");
        System.out.print("ID del libro a actualizar: ");
        Long id = leerLongObligatorio();

        // Traemos el libro actual (si no existe, avisamos)
        Libro existente = service.getById(id);
        if (existente == null) {
            System.out.println("No existe un libro activo con id=" + id);
            return;
        }

        // Pedimos nuevos datos (ENTER para dejar iguales donde sea opcional)
        System.out.println("Valores actuales: " + existente);
        System.out.print("Nuevo título (obligatorio): ");
        existente.setTitulo(leerTextoObligatorio());
        System.out.print("Nuevo autor (obligatorio): ");
        existente.setAutor(leerTextoObligatorio());
        System.out.print("Nueva editorial (opcional): ");
        existente.setEditorial(leerTextoOpcional());
        System.out.print("Nuevo año (opcional, ENTER para omitir): ");
        existente.setAnioEdicion(leerEnteroOpcional());

        boolean ok = service.actualizar(existente);
        System.out.println(ok ? "OK: Libro actualizado." : "No se pudo actualizar el Libro.");
    }

    /**
     * Opción 4: Eliminar Libro (baja lógica) + su Ficha asociada.
     * Explicación:
     *  - El Service resuelve eliminar primero la Ficha por libro_id y luego el Libro.
     *  - Todo dentro de una misma transacción para mantener consistencia.
     */
    private void opcionEliminarLibro() throws Exception {
        System.out.println("\n== Eliminar Libro (baja lógica) ==");
        System.out.print("ID del libro a eliminar: ");
        Long id = leerLongObligatorio();

        service.eliminar(id);
        System.out.println("OK: Libro y Ficha marcados como eliminados.");
    }

    /** Opción 5: Buscar un Libro por su ID (Service.getById) */
    private void opcionBuscarPorId() throws Exception {
        System.out.println("\n== Buscar Libro por ID ==");
        System.out.print("ID: ");
        Long id = leerLongObligatorio();

        Libro l = service.getById(id);
        if (l == null) System.out.println("(no encontrado)");
        else System.out.println("Encontrado: " + l);
    }

    /**
     * Opción 6 (NUEVA): Buscar Libro por ISBN.
     * Explicación:
     *  - Le pedimos el ISBN al usuario.
     *  - Llamamos a Service.buscarPorIsbn(...) que hace el JOIN en DAO con ficha_bibliografica.
     *  - Si existe y está activo, lo mostramos.
     */
    private void opcionBuscarPorIsbn() throws Exception {
        System.out.println("\n== Buscar Libro por ISBN ==");
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine().trim();

        Libro l = service.buscarPorIsbn(isbn);
        if (l == null) System.out.println("(no se encontró un Libro activo con ese ISBN)");
        else System.out.println("Encontrado: " + l);
    }

    // -------------------------------------------------------------
    // HELPERS de entrada (IO)
    // -------------------------------------------------------------

    /** Lee una opción del menú. Si no es número, pide de nuevo. */
    private int leerEnteroMenu() {
        while (true) {
            String s = scanner.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número de opción: ");
            }
        }
    }

    /** Lee un Long obligatorio (ID). */
    private Long leerLongObligatorio() {
        while (true) {
            String s = scanner.nextLine().trim();
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                System.out.print("Número inválido. Intente de nuevo: ");
            }
        }
    }

    /** Lee texto obligatorio (no vacío). */
    private String leerTextoObligatorio() {
        while (true) {
            String s = scanner.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.print("Obligatorio. Ingrese nuevamente: ");
        }
    }

    /** Lee texto opcional (puede quedar vacío). */
    private String leerTextoOpcional() {
        return scanner.nextLine().trim();
    }

    /** Lee un entero opcional; si se deja vacío o no es número, devuelve null. */
    private Integer leerEnteroOpcional() {
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) return null;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("(no es un número; se deja sin año)");
            return null;
        }
    }
}

=======
package LibroFicha_main;

import java.util.List;
import java.util.Scanner;

import LibroFicha_entities.FichaBibliografica;
import LibroFicha_entities.Libro;
import LibroFicha_service.LibroService;

/**
 * AppMenu (capa de presentación)
 * ------------------------------
 * - Este menú interactúa SOLO con la capa Service (NO con DAOs ni SQL).
 * - El Service aplica validaciones y maneja transacciones; aquí solo pedimos datos
 *   y mostramos resultados/mensajes para el usuario.
 */
public class AppMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final LibroService service = new LibroService();

    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEnteroMenu();
            try {
                switch (opcion) {
                    case 1 -> opcionListarLibros();
                    case 2 -> opcionInsertarLibroConFicha();
                    case 3 -> opcionActualizarLibro();
                    case 4 -> opcionEliminarLibro();
                    case 5 -> opcionBuscarPorId();
                    case 6 -> opcionBuscarPorIsbn();
                    case 0 -> System.out.println("Saliendo...");
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                // Mostramos un mensaje claro si algo falla (validación, SQL, etc.)
                System.out.println("¡Ups! Ocurrió un error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    void mostrarMenu() {
        System.out.println("\n==============================");
        System.out.println("   Sistema Libro–Ficha (TFI)   ");
        System.out.println("==============================");
        System.out.println("1) Listar Libros activos");
        System.out.println("2) Insertar Libro + Ficha (transacción A→B)");
        System.out.println("3) Actualizar Libro");
        System.out.println("4) Eliminar Libro (baja lógica + su Ficha)");
        System.out.println("5) Buscar Libro por ID");
        System.out.println("6) Buscar Libro por ISBN  (JOIN con Ficha)");
        System.out.println("0) Salir");
        System.out.print("Elija una opción: ");
    }

    // -------------------------------------------------------------
    // OPCIONES DEL MENÚ
    // -------------------------------------------------------------

    /** Opción 1: Listar libros activos (Service.getAll) */
    private void opcionListarLibros() throws Exception {
        System.out.println("\n== Libros activos ==");
        List<Libro> libros = service.getAll();
        if (libros == null || libros.isEmpty()) {
            System.out.println("(sin libros)");
            return;
        }
        // Mostramos cada libro; (Equipo: podemos sumar luego el ISBN consultando por Service.buscarPorIsbn)
        for (Libro l : libros) {
            System.out.println(l);
        }
    }

    /**
     * Opción 2: Insertar Libro + Ficha en UNA transacción (A→B)
     * Explicación:
     *  - Cargamos los datos del Libro (A).
     *  - Cargamos los datos de la Ficha (B).
     *  - Llamamos a service.insertarConFicha(...) que hace:
     *      setAutoCommit(false), crear Libro -> setear libroId en Ficha -> crear Ficha, y commit.
     *  - Si algo falla, rollback (atomicidad).
     */
    private void opcionInsertarLibroConFicha() throws Exception {
        System.out.println("\n== Insertar Libro + Ficha (transacción A→B) ==");

        // 1) Pedimos los datos del Libro
        Libro libro = new Libro();
        libro.setEliminado(false); // por defecto, activo
        System.out.print("Título (obligatorio): ");
        libro.setTitulo(leerTextoObligatorio());
        System.out.print("Autor (obligatorio): ");
        libro.setAutor(leerTextoObligatorio());
        System.out.print("Editorial (opcional): ");
        libro.setEditorial(leerTextoOpcional());
        System.out.print("Año de edición (opcional, ENTER para omitir): ");
        Integer anio = leerEnteroOpcional();
        libro.setAnioEdicion(anio);

        // 2) Pedimos los datos de la Ficha
        FichaBibliografica ficha = new FichaBibliografica();
        ficha.setEliminado(false);
        System.out.print("ISBN (obligatorio, 10/13 dígitos, admite guiones): ");
        ficha.setIsbn(leerTextoObligatorio());
        System.out.print("Clasificación Dewey (opcional): ");
        ficha.setClasificacionDewey(leerTextoOpcional());
        System.out.print("Estantería (opcional): ");
        ficha.setEstanteria(leerTextoOpcional());
        System.out.print("Idioma (opcional): ");
        ficha.setIdioma(leerTextoOpcional());

        // 3) Llamamos al Service para que haga todo atómico
        service.insertarConFicha(libro, ficha);
        System.out.println("OK: Libro y Ficha insertados en una sola transacción.");
    }

    /** Opción 3: Actualizar datos básicos de un Libro (Service.actualizar) */
    private void opcionActualizarLibro() throws Exception {
        System.out.println("\n== Actualizar Libro ==");
        System.out.print("ID del libro a actualizar: ");
        Long id = leerLongObligatorio();

        // Traemos el libro actual (si no existe, avisamos)
        Libro existente = service.getById(id);
        if (existente == null) {
            System.out.println("No existe un libro activo con id=" + id);
            return;
        }

        // Pedimos nuevos datos (ENTER para dejar iguales donde sea opcional)
        System.out.println("Valores actuales: " + existente);
        System.out.print("Nuevo título (obligatorio): ");
        existente.setTitulo(leerTextoObligatorio());
        System.out.print("Nuevo autor (obligatorio): ");
        existente.setAutor(leerTextoObligatorio());
        System.out.print("Nueva editorial (opcional): ");
        existente.setEditorial(leerTextoOpcional());
        System.out.print("Nuevo año (opcional, ENTER para omitir): ");
        existente.setAnioEdicion(leerEnteroOpcional());

        boolean ok = service.actualizar(existente);
        System.out.println(ok ? "OK: Libro actualizado." : "No se pudo actualizar el Libro.");
    }

    /**
     * Opción 4: Eliminar Libro (baja lógica) + su Ficha asociada.
     * Explicación:
     *  - El Service resuelve eliminar primero la Ficha por libro_id y luego el Libro.
     *  - Todo dentro de una misma transacción para mantener consistencia.
     */
    private void opcionEliminarLibro() throws Exception {
        System.out.println("\n== Eliminar Libro (baja lógica) ==");
        System.out.print("ID del libro a eliminar: ");
        Long id = leerLongObligatorio();

        service.eliminar(id);
        System.out.println("OK: Libro y Ficha marcados como eliminados.");
    }

    /** Opción 5: Buscar un Libro por su ID (Service.getById) */
    private void opcionBuscarPorId() throws Exception {
        System.out.println("\n== Buscar Libro por ID ==");
        System.out.print("ID: ");
        Long id = leerLongObligatorio();

        Libro l = service.getById(id);
        if (l == null) System.out.println("(no encontrado)");
        else System.out.println("Encontrado: " + l);
    }

    /**
     * Opción 6 (NUEVA): Buscar Libro por ISBN.
     * Explicación:
     *  - Le pedimos el ISBN al usuario.
     *  - Llamamos a Service.buscarPorIsbn(...) que hace el JOIN en DAO con ficha_bibliografica.
     *  - Si existe y está activo, lo mostramos.
     */
    private void opcionBuscarPorIsbn() throws Exception {
        System.out.println("\n== Buscar Libro por ISBN ==");
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine().trim();

        Libro l = service.buscarPorIsbn(isbn);
        if (l == null) System.out.println("(no se encontró un Libro activo con ese ISBN)");
        else System.out.println("Encontrado: " + l);
    }

    // -------------------------------------------------------------
    // HELPERS de entrada (IO)
    // -------------------------------------------------------------

    /** Lee una opción del menú. Si no es número, pide de nuevo. */
    private int leerEnteroMenu() {
        while (true) {
            String s = scanner.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número de opción: ");
            }
        }
    }

    /** Lee un Long obligatorio (ID). */
    private Long leerLongObligatorio() {
        while (true) {
            String s = scanner.nextLine().trim();
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                System.out.print("Número inválido. Intente de nuevo: ");
            }
        }
    }

    /** Lee texto obligatorio (no vacío). */
    private String leerTextoObligatorio() {
        while (true) {
            String s = scanner.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.print("Obligatorio. Ingrese nuevamente: ");
        }
    }

    /** Lee texto opcional (puede quedar vacío). */
    private String leerTextoOpcional() {
        return scanner.nextLine().trim();
    }

    /** Lee un entero opcional; si se deja vacío o no es número, devuelve null. */
    private Integer leerEnteroOpcional() {
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) return null;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("(no es un número; se deja sin año)");
            return null;
        }
    }
}

>>>>>>> Damian
