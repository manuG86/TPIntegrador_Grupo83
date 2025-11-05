package LibroFicha_dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import LibroFicha_entities.Libro;

/**
 * Clase DAO concreta para la entidad Libro.
 * Se encarga de realizar las operaciones CRUD directamente en la base de datos.
 * 
 * Importante: no cierra la conexión ni hace commit, ya que eso lo controla el Service.
 */
public class LibroDaoImpl implements GenericDao<Libro> {

    // ---------------------------------------------------------
    // MÉTODO: CREAR
    // ---------------------------------------------------------
    @Override
    public Long crear(Connection conn, Libro libro) {
        final String sql = "INSERT INTO libro (eliminado, titulo, autor, editorial, anioEdicion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Asignamos los valores del objeto libro a los parámetros del INSERT
            ps.setBoolean(1, libro.isEliminado());
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getAutor());
            ps.setString(4, libro.getEditorial());
            if (libro.getAnioEdicion() != null)
                ps.setInt(5, libro.getAnioEdicion());
            else
                ps.setNull(5, Types.INTEGER);

            ps.executeUpdate();

            // Recuperamos el ID autogenerado por la base de datos
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    libro.setId(id);
                    return id;
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al crear Libro: " + e.getMessage(), e);
        }
    }

    // ---------------------------------------------------------
    // MÉTODO: ACTUALIZAR
    // ---------------------------------------------------------
    @Override
    public boolean actualizar(Connection conn, Libro libro) {
        final String sql = "UPDATE libro SET eliminado = ?, titulo = ?, autor = ?, editorial = ?, anioEdicion = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, libro.isEliminado());
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getAutor());
            ps.setString(4, libro.getEditorial());
            if (libro.getAnioEdicion() != null)
                ps.setInt(5, libro.getAnioEdicion());
            else
                ps.setNull(5, Types.INTEGER);
            ps.setLong(6, libro.getId());

            // Devuelve true si se actualizó una fila
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al actualizar Libro: " + e.getMessage(), e);
        }
    }

    // ---------------------------------------------------------
    // MÉTODO: ELIMINAR (lógico)
    // ---------------------------------------------------------
    @Override
    public boolean eliminar(Connection conn, long id) {
        final String sql = "UPDATE libro SET eliminado = TRUE WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al eliminar (lógico) Libro: " + e.getMessage(), e);
        }
    }

    // ---------------------------------------------------------
    // MÉTODO: LEER POR ID
    // ---------------------------------------------------------
    @Override
    public Libro leer(Connection conn, long id) {
        final String sql = "SELECT * FROM libro WHERE id = ? AND eliminado = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs); // Convertimos el resultado a objeto Libro
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al leer Libro: " + e.getMessage(), e);
        }
    }

    // ---------------------------------------------------------
    // MÉTODO: LEER TODOS
    // ---------------------------------------------------------
    @Override
    public List<Libro> leerTodos(Connection conn) {
        final String sql = "SELECT * FROM libro WHERE eliminado = FALSE ORDER BY id";
        List<Libro> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al listar Libros: " + e.getMessage(), e);
        }
        return lista;
    }

    // ---------------------------------------------------------
    // MÉTODO AUXILIAR PARA MAPEAR UN REGISTRO A OBJETO
    // ---------------------------------------------------------
    private Libro mapRow(ResultSet rs) throws SQLException {
        Libro l = new Libro();
        l.setId(rs.getLong("id"));
        l.setEliminado(rs.getBoolean("eliminado"));
        l.setTitulo(rs.getString("titulo"));
        l.setAutor(rs.getString("autor"));
        l.setEditorial(rs.getString("editorial"));
        Integer anio = (Integer) rs.getObject("anioEdicion");
        l.setAnioEdicion(anio);
        return l;
    }

    // ---------------------------------------------------------
    // NUEVO MÉTODO: BUSCAR LIBRO POR ISBN
    // ---------------------------------------------------------
    /**
     * Busca un Libro activo (eliminado = FALSE) a partir del ISBN de su ficha.
     * Se hace un JOIN con la tabla ficha_bibliografica para obtener el libro.
     */
    public Libro buscarPorIsbn(Connection conn, String isbn) {
        final String sql =
            "SELECT l.id, l.eliminado, l.titulo, l.autor, l.editorial, l.anioEdicion " +
            "FROM libro l " +
            "JOIN ficha_bibliografica f ON f.libro_id = l.id " +
            "WHERE l.eliminado = FALSE AND f.eliminado = FALSE AND f.isbn = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al buscar Libro por ISBN: " + e.getMessage(), e);
        }
    }
}

=======
package LibroFicha_dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import LibroFicha_entities.Libro;

/**
 * Clase DAO concreta para la entidad Libro.
 * Se encarga de realizar las operaciones CRUD directamente en la base de datos.
 * 
 * Importante: no cierra la conexión ni hace commit, ya que eso lo controla el Service.
 */
public class LibroDaoImpl implements GenericDao<Libro> {

    // ---------------------------------------------------------
    // MÉTODO: CREAR
    // ---------------------------------------------------------
    @Override
    public Long crear(Connection conn, Libro libro) {
        final String sql = "INSERT INTO libro (eliminado, titulo, autor, editorial, anioEdicion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Asignamos los valores del objeto libro a los parámetros del INSERT
            ps.setBoolean(1, libro.isEliminado());
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getAutor());
            ps.setString(4, libro.getEditorial());
            if (libro.getAnioEdicion() != null)
                ps.setInt(5, libro.getAnioEdicion());
            else
                ps.setNull(5, Types.INTEGER);

            ps.executeUpdate();

            // Recuperamos el ID autogenerado por la base de datos
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    libro.setId(id);
                    return id;
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al crear Libro: " + e.getMessage(), e);
        }
    }

    // ---------------------------------------------------------
    // MÉTODO: ACTUALIZAR
    // ---------------------------------------------------------
    @Override
    public boolean actualizar(Connection conn, Libro libro) {
        final String sql = "UPDATE libro SET eliminado = ?, titulo = ?, autor = ?, editorial = ?, anioEdicion = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, libro.isEliminado());
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getAutor());
            ps.setString(4, libro.getEditorial());
            if (libro.getAnioEdicion() != null)
                ps.setInt(5, libro.getAnioEdicion());
            else
                ps.setNull(5, Types.INTEGER);
            ps.setLong(6, libro.getId());

            // Devuelve true si se actualizó una fila
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al actualizar Libro: " + e.getMessage(), e);
        }
    }

    // ---------------------------------------------------------
    // MÉTODO: ELIMINAR (lógico)
    // ---------------------------------------------------------
    @Override
    public boolean eliminar(Connection conn, long id) {
        final String sql = "UPDATE libro SET eliminado = TRUE WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al eliminar (lógico) Libro: " + e.getMessage(), e);
        }
    }

    // ---------------------------------------------------------
    // MÉTODO: LEER POR ID
    // ---------------------------------------------------------
    @Override
    public Libro leer(Connection conn, long id) {
        final String sql = "SELECT * FROM libro WHERE id = ? AND eliminado = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs); // Convertimos el resultado a objeto Libro
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al leer Libro: " + e.getMessage(), e);
        }
    }

    // ---------------------------------------------------------
    // MÉTODO: LEER TODOS
    // ---------------------------------------------------------
    @Override
    public List<Libro> leerTodos(Connection conn) {
        final String sql = "SELECT * FROM libro WHERE eliminado = FALSE ORDER BY id";
        List<Libro> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al listar Libros: " + e.getMessage(), e);
        }
        return lista;
    }

    // ---------------------------------------------------------
    // MÉTODO AUXILIAR PARA MAPEAR UN REGISTRO A OBJETO
    // ---------------------------------------------------------
    private Libro mapRow(ResultSet rs) throws SQLException {
        Libro l = new Libro();
        l.setId(rs.getLong("id"));
        l.setEliminado(rs.getBoolean("eliminado"));
        l.setTitulo(rs.getString("titulo"));
        l.setAutor(rs.getString("autor"));
        l.setEditorial(rs.getString("editorial"));
        Integer anio = (Integer) rs.getObject("anioEdicion");
        l.setAnioEdicion(anio);
        return l;
    }

    // ---------------------------------------------------------
    // NUEVO MÉTODO: BUSCAR LIBRO POR ISBN
    // ---------------------------------------------------------
    /**
     * Busca un Libro activo (eliminado = FALSE) a partir del ISBN de su ficha.
     * Se hace un JOIN con la tabla ficha_bibliografica para obtener el libro.
     */
    public Libro buscarPorIsbn(Connection conn, String isbn) {
        final String sql =
            "SELECT l.id, l.eliminado, l.titulo, l.autor, l.editorial, l.anioEdicion " +
            "FROM libro l " +
            "JOIN ficha_bibliografica f ON f.libro_id = l.id " +
            "WHERE l.eliminado = FALSE AND f.eliminado = FALSE AND f.isbn = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al buscar Libro por ISBN: " + e.getMessage(), e);
        }
    }
}

