package LibroFicha_service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import LibroFicha_config.DatabaseConnection;
import LibroFicha_dao.FichaBibliograficaDaoImpl;
import LibroFicha_dao.LibroDaoImpl;
import LibroFicha_entities.FichaBibliografica;
import LibroFicha_entities.Libro;

/**
 * Service de Libro
 * - Orquesta transacciones (setAutoCommit(false)/commit/rollback)
 * - Aplica reglas de negocio/validaciones
 * - Usa SIEMPRE la MISMA Connection para que todos los DAOs participen
 *   de una sola transaccion.
 *
 * Nota al equipo: mantenemos "baja logica" (eliminado=true), por eso
 * cuando "eliminamos" un Libro tambien marcamos como eliminado su Ficha.
 */
public class LibroService implements GenericService<Libro> {

    // DAOs usados por el service
    private final LibroDaoImpl libroDao;
    private final FichaBibliograficaDaoImpl fichaDao;

    // Constructor por defecto (crea los DAO)
    public LibroService() {
        this.libroDao  = new LibroDaoImpl();
        this.fichaDao  = new FichaBibliograficaDaoImpl();
    }

    // Constructor para inyectar DAOs (tests / mock)
    public LibroService(LibroDaoImpl libroDao, FichaBibliograficaDaoImpl fichaDao) {
        this.libroDao = libroDao;
        this.fichaDao = fichaDao;
    }

    /* =========================================================
       CRUD basico de Libro (con transacciones)
       ========================================================= */

    @Override
    public Long insertar(Libro libro) throws Exception {
        validarLibro(libro);

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Creamos el libro (el DAO setea el id en la entidad y retorna el id)
            Long id = libroDao.crear(conn, libro);

            conn.commit();
            return id;
        } catch (Exception e) {
            rollbackSilencioso(conn);
            throw new RuntimeException("Error insertando Libro (rollback): " + e.getMessage(), e);
        } finally {
            restaurarYCerrar(conn);
        }
    }

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

    @Override
    public void eliminar(Long id) throws Exception {
        if (id == null) throw new IllegalArgumentException("Id no puede ser null");

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Nota: como nuestra "eliminacion" es logica (UPDATE ... SET eliminado=TRUE),
            // el ON DELETE CASCADE no aplica. Por eso primero "bajamos" la ficha del libro.
            fichaDao.eliminarPorLibroId(conn, id.longValue());

            // Luego "bajamos" el libro
            libroDao.eliminar(conn, id.longValue());

            conn.commit();
        } catch (Exception e) {
            rollbackSilencioso(conn);
            throw new RuntimeException("Error eliminando Libro (rollback): " + e.getMessage(), e);
        } finally {
            restaurarYCerrar(conn);
        }
    }

    @Override
    public Libro getById(Long id) throws Exception {
        if (id == null) throw new IllegalArgumentException("Id no puede ser null");
        Connection conn = null;
        try {
            // Para lectura simple no necesito transaccion, pero mantengo el patron
            conn = DatabaseConnection.getConnection();
            return libroDao.leer(conn, id.longValue());
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo Libro: " + e.getMessage(), e);
        } finally {
            cerrarSilencioso(conn);
        }
    }

    @Override
    public List<Libro> getAll() throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            return libroDao.leerTodos(conn);
        } catch (Exception e) {
            throw new RuntimeException("Error listando Libros: " + e.getMessage(), e);
        } finally {
            cerrarSilencioso(conn);
        }
    }

    /* =========================================================
       CASOS COMPUESTOS (Libro + Ficha) CON TRANSACCION
       ========================================================= */

    /**
     * Caso FELIZ de insercion 1->1:
     * 1) Creo Libro -> obtengo id
     * 2) Seteo libroId en Ficha
     * 3) Creo Ficha
     * 4) commit
     */
    public void insertarConFicha(Libro libro, FichaBibliografica ficha) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            validarLibro(libro);
            validarFicha(ficha);

            // 1) Creo Libro y obtengo id
            Long libroId = libroDao.crear(conn, libro);
            libro.setId(libroId);

            // 2) Vinculo Ficha con Libro (FK unica libro_id)
            ficha.setLibroId(libroId);

            // 3) Creo Ficha (el DAO setea id en la entidad)
            fichaDao.crear(conn, ficha);

            // 4) Guardar cambios
            conn.commit();

        } catch (Exception e) {
            rollbackSilencioso(conn);
            throw new RuntimeException("Fallo al insertar Libro + Ficha (rollback ejecutado): " + e.getMessage(), e);
        } finally {
            restaurarYCerrar(conn);
        }
    }

    /**
     * Caso con ROLLBACK SIMULADO (para el video):
     * hacemos todo igual que insertarConFicha pero lanzamos un error a proposito
     * al final, para demostrar que NO queda nada en BD.
     */
    public void insertarConFichaConRollbackSimulado(Libro libro, FichaBibliografica ficha) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            validarLibro(libro);
            validarFicha(ficha);

            Long libroId = libroDao.crear(conn, libro);
            libro.setId(libroId);

            ficha.setLibroId(libroId);
            fichaDao.crear(conn, ficha);

            // === ERROR SIMULADO ===
            // Nota: lanzamos esta excepcion "a mano" despues de crear ambas cosas
            // para que se vea claro el rollback en la demo.
            throw new RuntimeException("Error simulado para mostrar rollback (no debe persistir nada).");

            // conn.commit();  // <- no se ejecuta nunca en este metodo

        } catch (Exception e) {
            rollbackConMensaje(conn, "Ocurrio un error, ejecutando rollback simulado...");
            throw new RuntimeException("Rollback simulado OK: " + e.getMessage(), e);
        } finally {
            restaurarYCerrar(conn);
        }
    }

    /**
     * Actualizacion coherente de Libro + Ficha en una sola transaccion.
     * (dejado como ejemplo por si lo usamos en la defensa)
     */
    public void actualizarConFicha(Libro libro, FichaBibliografica ficha) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            validarLibro(libro);
            validarFicha(ficha);

            // Por las dudas, si no viene seteada la FK desde UI:
            if (ficha.getLibroId() == null) {
                ficha.setLibroId(libro.getId());
            }

            boolean ok1 = libroDao.actualizar(conn, libro);
            boolean ok2 = fichaDao.actualizar(conn, ficha);

            if (!ok1 || !ok2) {
                throw new RuntimeException("No se pudo actualizar Libro y/o Ficha");
            }

            conn.commit();
        } catch (Exception e) {
            rollbackSilencioso(conn);
            throw new RuntimeException("Error actualizando Libro+Ficha (rollback): " + e.getMessage(), e);
        } finally {
            restaurarYCerrar(conn);
        }
    }

    /* =========================================================
       Validaciones minimas
       ========================================================= */

    private void validarLibro(Libro libro) {
        if (libro == null) throw new IllegalArgumentException("Libro no puede ser null");
        if (libro.getTitulo() == null || libro.getTitulo().isBlank())
            throw new IllegalArgumentException("Titulo obligatorio");
        if (libro.getAutor() == null || libro.getAutor().isBlank())
            throw new IllegalArgumentException("Autor obligatorio");
        // podes sumar: anioEdicion entre 1400 y YEAR(now()), etc.
    }

    private void validarFicha(FichaBibliografica ficha) {
        if (ficha == null) throw new IllegalArgumentException("Ficha no puede ser null");
        if (ficha.getIsbn() == null || ficha.getIsbn().isBlank())
            throw new IllegalArgumentException("ISBN obligatorio");
    }

    /* =========================================================
       Utiles de transaccion/Connection (para no repetir)
       ========================================================= */

    private void rollbackSilencioso(Connection conn) {
        if (conn != null) {
            try { conn.rollback(); } catch (SQLException ignored) {}
        }
    }

    private void rollbackConMensaje(Connection conn, String msg) {
        if (conn != null) {
            try {
                System.out.println(msg);
                conn.rollback();
                System.out.println("Rollback OK: la BD quedo igual que antes.");
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
        }
    }

    private void restaurarYCerrar(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException ignored) {}
        }
    }

    private void cerrarSilencioso(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException ignored) {}
        }
    }
}
