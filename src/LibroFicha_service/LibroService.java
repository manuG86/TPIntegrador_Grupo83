package LibroFicha_service;

import LibroFicha_config.DatabaseConnection;
import LibroFicha_dao.FichaBibliograficaDao;
import LibroFicha_dao.LibroDao;
import LibroFicha_entities.FichaBibliografica;
import LibroFicha_entities.Libro;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class LibroService implements GenericService<Libro> {

    private final LibroDao libroDao;
    private final FichaBibliograficaDao fichaDao;

    public LibroService(LibroDao libroDao, FichaBibliograficaDao fichaDao) {
        this.libroDao = libroDao;
        this.fichaDao = fichaDao;
    }

    // Método de la interfaz
    @Override
    public void insertar(Libro libro) throws Exception {
        insertar(libro, null); // llama a la versión con ficha nula
    }

    // Sobrecarga para insertar libro + ficha
    public void insertar(Libro libro, FichaBibliografica ficha) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Insertar el libro
            libroDao.crear(conn, libro);

            // Insertar ficha asociada si existe
            if (ficha != null) {
                ficha.setLibroId(libro.getId());
                fichaDao.crear(conn, ficha);
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    // Método de la interfaz
    @Override
    public void actualizar(Libro libro) throws Exception {
        actualizar(libro, null); // llama a la versión con ficha nula
    }

    // Sobrecarga para actualizar libro + ficha
    public void actualizar(Libro libro, FichaBibliografica ficha) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            libroDao.actualizar(conn, libro);

            if (ficha != null) {
                ficha.setLibroId(libro.getId());
                fichaDao.actualizar(conn, ficha);
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public void eliminar(Long id) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Primero eliminar las fichas asociadas
            fichaDao.eliminarPorLibroId(conn, id);

            // Luego eliminar el libro
            libroDao.eliminar(conn, id);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public Libro getById(Long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return libroDao.getById(conn, id);
        }
    }

    @Override
    public List<Libro> getAll() throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return libroDao.getAll(conn);
        }
    }
}
