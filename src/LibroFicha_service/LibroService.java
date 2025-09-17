package LibroFicha_service;

import LibroFicha_config.DatabaseConnection;
import LibroFicha_dao.FichaBibliograficaDao;
import LibroFicha_dao.LibroDao;
import LibroFicha_entities.FichaBibliografica;
import LibroFicha_entities.Libro;

import java.sql.Connection;

public class LibroService implements GenericService<Libro> {

    private final LibroDao libroDao;
    private final FichaBibliograficaDao fichaDao;

    public LibroService(LibroDao libroDao, FichaBibliograficaDao fichaDao) {
        this.libroDao = libroDao;
        this.fichaDao = fichaDao;
    }

    @Override
    public void insertar(Libro libro) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            FichaBibliografica ficha = libro.getFichaBibliografica();
            if (ficha != null) {
                // Aquí llamamos a fichaDao.crear(conn, ficha) -> la implementación JDBC debe soportar esto
                // fichaDao.crear(conn, ficha);
            }

            // libroDao.crear(conn, libro);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override public void actualizar(Libro t) throws Exception { /* TODO */ }
    @Override public void eliminar(Long id) throws Exception { /* TODO */ }
    @Override public Libro getById(Long id) throws Exception { return null; /* TODO */ }
    @Override public java.util.List<Libro> getAll() throws Exception { return null; /* TODO */ }
}
