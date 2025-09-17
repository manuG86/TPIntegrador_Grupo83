package LibroFicha_service;

import java.util.List;

public interface GenericService<T> {
    void insertar(T t) throws Exception;
    void actualizar(T t) throws Exception;
    void eliminar(Long id) throws Exception;
    T getById(Long id) throws Exception;
    List<T> getAll() throws Exception;
}
