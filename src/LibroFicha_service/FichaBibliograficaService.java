package LibroFicha_service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import LibroFicha_config.DatabaseConnection;
import LibroFicha_dao.FichaBibliograficaDaoImpl;
import LibroFicha_entities.FichaBibliografica;

/**
 * Service de FichaBibliografica
 * - Encapsula la lógica de negocio y transacciones.
 * - Se apoya en FichaBibliograficaDaoImpl para acceder a BD.
 * - Implementa el CRUD genérico (GenericService).
 * 
 * Nota: esta capa no manipula objetos Libro, solo trabaja sobre FichaBibliografica.
 */
public class FichaBibliograficaService implements GenericService<FichaBibliografica> {

    private final FichaBibliograficaDaoImpl fichaDao;

    // Constructor por defecto
    public FichaBibliograficaService() {
        this.fichaDao = new FichaBibliograficaDaoImpl();
    }

    // Constructor alternativo (para tests / mocks)
    public FichaBibliograficaService(FichaBibliograficaDaoImpl fichaDao) {
        this.fichaDao = fichaDao;
    }

    /* =========================================================
       CRUD básico con transacciones
       ========================================================= */

    @Override
    public Long insertar(FichaBibliografica ficha) throws Exception {
        validarFicha(ficha);

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            Long id = fichaDao.crear(conn, ficha);

            conn.commit();
            return id;
        } catch (Exception e) {
            rollbackSilencioso(conn);
            throw new RuntimeException("Error insertando FichaBibliografica (rollback): " + e.getMessage(), e);
        } finally {
            restaurarYCerrar(conn);
        }
    }

    @Override
    public boolean actualizar(FichaBibliografica ficha) throws Exception {
        validarFicha(ficha);

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            boolean ok = fichaDao.actualizar(conn, ficha);

            conn.commit();
            return ok;
        } catch (Exception e) {
            rollbackSilencioso(conn);
            throw new RuntimeException("Error actualizando FichaBibliografica (rollback): " + e.getMessage(), e);
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

            fichaDao.eliminar(conn, id.longValue());

            conn.commit();
        } catch (Exception e) {
            rollbackSilencioso(conn);
            throw new RuntimeException("Error eliminando FichaBibliografica (rollback): " + e.getMessage(), e);
        } finally {
            restaurarYCerrar(conn);
        }
    }

    @Override
    public FichaBibliografica getById(Long id) throws Exception {
        if (id == null) throw new IllegalArgumentException("Id no puede ser null");

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            return fichaDao.leer(conn, id.longValue());
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo FichaBibliografica: " + e.getMessage(), e);
        } finally {
            cerrarSilencioso(conn);
        }
    }

    @Override
    public List<FichaBibliografica> getAll() throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            return fichaDao.leerTodos(conn);
        } catch (Exception e) {
            throw new RuntimeException("Error listando FichasBibliograficas: " + e.getMessage(), e);
        } finally {
            cerrarSilencioso(conn);
        }
    }

    /* =========================================================
       Validaciones mínimas
       ========================================================= */

    private void validarFicha(FichaBibliografica ficha) {
        if (ficha == null)
            throw new IllegalArgumentException("FichaBibliografica no puede ser null");

        if (ficha.getIsbn() == null || ficha.getIsbn().isBlank())
            throw new IllegalArgumentException("ISBN obligatorio");

        if (ficha.getLibroId() == null)
            throw new IllegalArgumentException("FichaBibliografica debe estar asociada a un Libro (libroId obligatorio)");
    }

    /* =========================================================
       Utilidades de conexión
       ========================================================= */

    private void rollbackSilencioso(Connection conn) {
        if (conn != null) {
            try { conn.rollback(); } catch (SQLException ignored) {}
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
