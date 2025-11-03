package LibroFicha_service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Year;
import java.util.List;

import LibroFicha_config.DatabaseConnection;
import LibroFicha_dao.FichaBibliograficaDaoImpl;
import LibroFicha_dao.LibroDaoImpl;
import LibroFicha_entities.FichaBibliografica;
import LibroFicha_entities.Libro;

/**
 * Servicio de Libro (capa Service)
 * --------------------------------
 * - Aplica reglas de negocio y validaciones.
 * - Orquesta transacciones (setAutoCommit(false) / commit / rollback).
 * - Llama a los DAO, que se encargan de hablar con la base de datos.
 * 
 * Importante: la consigna pide que la lógica/validaciones estén en Service
 * y que el DAO solo haga persistencia (PreparedStatement, SQL).
 */
public class LibroService implements GenericService<Libro> {

    // DAOs concretos (podrían inyectarse con constructor si quisiéramos testear)
    private final LibroDaoImpl libroDao = new LibroDaoImpl();
    private final FichaBibliograficaDaoImpl fichaDao = new FichaBibliograficaDaoImpl();

    // =========================================================
    // VALIDACIONES DE NEGOCIO (simples y claras)
    // =========================================================

    /**
     * Valida un Libro antes de persistirlo/actualizarlo.
     * Reglas mínimas: título y autor obligatorios; año dentro de un rango razonable.
     */
    private void validarLibro(Libro libro) {
        if (libro == null) throw new IllegalArgumentException("Libro no puede ser null");

        if (libro.getTitulo() == null || libro.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Título obligatorio");
        }
        if (libro.getAutor() == null || libro.getAutor().isBlank()) {
            throw new IllegalArgumentException("Autor obligatorio");
        }

        // Año opcional, pero si viene, debe estar en rango [1450..año actual].
        if (libro.getAnioEdicion() != null) {
            int y = libro.getAnioEdicion();
            int current = Year.now().getValue();
            if (y < 1450 || y > current) {
                throw new IllegalArgumentException("Año de edición fuera de rango (1450.." + current + ")");
            }
        }
    }

    /**
     * Valida una FichaBibliografica. Para este TFI el ISBN es obligatorio y
     * validamos un formato simple: ISBN-10 o ISBN-13 (permitimos guiones/espacios).
     */
    private void validarFicha(FichaBibliografica ficha) {
        if (ficha == null) throw new IllegalArgumentException("Ficha no puede ser null");

        String raw = (ficha.getIsbn() == null ? "" : ficha.getIsbn()).replaceAll("[- ]", "");
        boolean isbn10 = raw.matches("\\d{9}[\\dX]"); // 10 (el último puede ser X)
        boolean isbn13 = raw.matches("\\d{13}");      // 13 dígitos
        if (!(isbn10 || isbn13)) {
            throw new IllegalArgumentException("ISBN debe ser ISBN-10 (10) o ISBN-13 (13) válido");
        }
    }

    // =========================================================
    // CRUD BÁSICO de LIBRO (con transacciones)
    // =========================================================

    /**
     * Inserta un Libro solo (sin ficha). La transacción es simple:
     * - autocommit=false
     * - crear libro
     * - commit
     * (si falla, rollback)
     */
    @Override
    public Long insertar(Libro libro) throws Exception {
        validarLibro(libro);

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // 1) inicio de transacción

            Long id = libroDao.crear(conn, libro);    // 2) persisto Libro

            conn.commit();                            // 3) si salió todo bien, confirmo
            return id;
        } catch (Exception e) {
            rollbackSilencioso(conn); // vuelvo todo atrás
            throw new RuntimeException("Error insertando Libro (rollback ejecutado): " + e.getMessage(), e);
        } finally {
            restaurarYCerrar(conn);   // dejo la conexión limpia
        }
    }

    /**
     * Actualiza los datos de un Libro.
     */
    @Override
    public boolean actualizar(Libro libro) throws Exception {
        validarLibro(libro);

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            boolean ok = libroDao.actualizar(conn, libro);

            conn.commit();
            return ok;
        } catch (Exception e) {
            rollbackSilencioso(conn);
            throw new RuntimeException("Error actualizando Libro (rollback): " + e.getMessage(), e);
        } finally {
            restaurarYCerrar(conn);
        }
    }

    /**
     * Eliminación lógica del Libro y su Ficha (si existe).
     * Regla: como es 1→1, cuando "borramos" el Libro, también
     * "borramos" lógicamente la Ficha asociada.
     */
    @Override
    public void eliminar(Long id) throws Exception {
        if (id == null) throw new IllegalArgumentException("Id no puede ser null");

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Primero la ficha (lado B) por libro_id
            fichaDao.eliminarPorLibroId(conn, id);

            // Luego el libro (lado A)
            libroDao.eliminar(conn, id);

            conn.commit();
        } catch (Exception e) {
            rollbackSilencioso(conn);
            throw new RuntimeException("Error al eliminar Libro/Ficha (rollback): " + e.getMessage(), e);
        } finally {
            restaurarYCerrar(conn);
        }
    }

    /**
     * Obtiene un Libro por su ID (si está activo).
     * NOTA: acá no abrimos transacción porque es una lectura simple.
     */
    @Override
    public Libro getById(Long id) throws Exception {
        if (id == null) throw new IllegalArgumentException("Id no puede ser null");
        try (Connection conn = DatabaseConnection.getConnection()) {
            return libroDao.leer(conn, id);
        }
    }

    /**
     * Lista todos los Libros activos (eliminado = FALSE).
     */
    @Override
    public List<Libro> getAll() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return libroDao.leerTodos(conn);
        }
    }

    // =========================================================
    // OPERACIONES COMPUESTAS (Libro + Ficha en la MISMA transacción)
    // =========================================================

    /**
     * Inserta Libro + Ficha en una sola transacción.
     * Orden elegido por nosotros: A→B (primero Libro, después Ficha).
     * Justificación: ambos quedan atómicamente creados, y seteamos libroId en la Ficha.
     * (En el informe explicamos que la consigna sugiere B→A, pero esta variante es válida
     *  si se hace bajo la misma transacción y se documenta.)
     */
    public void insertarConFicha(Libro libro, FichaBibliografica ficha) throws Exception {
        validarLibro(libro);
        validarFicha(ficha);

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);  // transacción única para ambas inserciones

            // 1) Creo Libro y obtengo id
            Long libroId = libroDao.crear(conn, libro);
            libro.setId(libroId);

            // 2) Vinculo Ficha con Libro (FK única libro_id)
            ficha.setLibroId(libroId);

            // 3) Creo Ficha
            fichaDao.crear(conn, ficha);

            // 4) Confirmo la transacción
            conn.commit();
        } catch (Exception e) {
            rollbackSilencioso(conn);
            throw new RuntimeException("Fallo al insertar Libro + Ficha (rollback ejecutado): " + e.getMessage(), e);
        } finally {
            restaurarYCerrar(conn);
        }
    }

    /**
     * Variante con "rollback simulado" para mostrar en el video del TFI.
     * La idea es provocar un error luego de hacer las operaciones, para ver el rollback en acción.
     * (Podemos adaptarlo para forzar una excepción en el medio y mostrar cómo NO queda nada en BD).
     */
    public void insertarConFichaConRollbackSimulado(Libro libro, FichaBibliografica ficha) {
        try {
            // Reutilizamos la operación compuesta normal
            insertarConFicha(libro, ficha);

            // Simulamos un problema lógico que obliga a "deshacer" (para el video)
            throw new RuntimeException("Simulando fallo lógico tras insertar: debe verse el rollback en la demo");
        } catch (Exception e) {
            // En una demo, capturamos y mostramos el mensaje; en un caso real, podríamos registrar logs.
            throw new RuntimeException(e);
        }
    }

    // =========================================================
    // BÚSQUEDA POR CAMPO RELEVANTE (ISBN) — pedido por la consigna
    // =========================================================

    /**
     * Devuelve el Libro activo asociado a una Ficha cuyo ISBN coincida.
     * - Valida que el ISBN venga informado.
     * - Delega la búsqueda en el DAO (JOIN l + f).
     * - No usa autocommit=false porque es lectura simple.
     */
    public Libro buscarPorIsbn(String isbn) throws Exception {
        if (isbn == null || isbn.isBlank())
            throw new IllegalArgumentException("ISBN obligatorio");

        try (Connection conn = DatabaseConnection.getConnection()) {
            return libroDao.buscarPorIsbn(conn, isbn);
        }
    }

    // =========================================================
    // UTILIDADES DE TRANSACCIÓN / CONEXIÓN (para no repetir código)
    // =========================================================

    /** Intenta hacer rollback, pero sin romper si ya se cayó la conexión. */
    private void rollbackSilencioso(Connection conn) {
        if (conn != null) {
            try { conn.rollback(); } catch (SQLException ignored) {}
        }
    }

    /** Restablece autocommit y cierra la conexión de forma segura. */
    private void restaurarYCerrar(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException ignored) {}
        }
    }
}
