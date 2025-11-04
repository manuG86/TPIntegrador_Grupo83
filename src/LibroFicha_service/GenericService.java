package LibroFicha_service;

import java.util.List;

/**
 * Interfaz de servicio genérico (CRUD básico).
 * Nota: declaro throws Exception para que cada Service decida
 * qué propagar (SQLException, RuntimeException, etc.).
 */
public interface GenericService<T> {

    /** Crea la entidad y devuelve su id generado. */
    Long insertar(T entidad) throws Exception;

    /** Actualiza la entidad. Devuelve true si afectó 1 fila. */
    boolean actualizar(T entidad) throws Exception;

    /** Eliminación (en nuestro caso, lógica). */
    void eliminar(Long id) throws Exception;

    /** Obtiene una entidad por id (o null si no existe/está eliminada). */
    T getById(Long id) throws Exception;

    /** Lista todas las entidades activas (eliminado = FALSE). */
    List<T> getAll() throws Exception;
}

