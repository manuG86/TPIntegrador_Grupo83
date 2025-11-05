package LibroFicha_dao;

import java.sql.Connection;
import java.util.List;

/**
 * Interfaz generica DAO.
 * Nota: todos los metodos reciben una Connection externa para
 * que la capa Service maneje la transaccion (setAutoCommit/commit/rollback).
 */
public interface GenericDao<T> {

    /** Inserta la entidad y devuelve el ID generado (y setea el id en la entidad). */
    Long crear(Connection conn, T entity);

    /** Lee por id (ignorar registros con eliminado = TRUE). */
    T leer(Connection conn, long id);

    /** Lista todas las entidades activas (eliminado = FALSE). */
    List<T> leerTodos(Connection conn);

    /** Actualiza la entidad. Devuelve true si afecto 1 fila. */
    boolean actualizar(Connection conn, T entity);

    /** Eliminacion logica (eliminado = TRUE). Devuelve true si afecto 1 fila. */
    boolean eliminar(Connection conn, long id);
}
