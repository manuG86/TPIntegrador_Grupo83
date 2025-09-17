package LibroFicha_dao;

import java.util.List;

public interface Dao<T> {
    void crear(T t) throws Exception;
    T leer(Long id) throws Exception;
    List<T> leerTodos() throws Exception;
    void actualizar(T t) throws Exception;
    void eliminar(Long id) throws Exception; // baja lógica
}
