package LibroFicha_dao;

import LibroFicha_entities.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Libro.
 * Nota: este DAO NUNCA cierra ni commitea la Connection.
 * Solo usa la Connection que recibe por parametro (conexion compartida por Service).
 */
public class LibroDaoImpl implements GenericDao<Libro> {

    @Override
    public Long crear(Connection conn, Libro libro) {
        final String sql = "INSERT INTO libro " +
                "(eliminado, titulo, autor, editorial, anioEdicion) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // eliminado
            ps.setBoolean(1, libro.isEliminado()); 
            // titulo, autor, editorial
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getAutor());
            if (libro.getEditorial() != null) ps.setString(4, libro.getEditorial());
            else ps.setNull(4, Types.VARCHAR);
            // anioEdicion
            if (libro.getAnioEdicion() != null) ps.setInt(5, libro.getAnioEdicion());
            else ps.setNull(5, Types.INTEGER);

            int rows = ps.executeUpdate();
            if (rows != 1) throw new SQLException("No se inserto Libro");

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    libro.setId(id);     // guardo el id tambien en la entidad
                    return id;
                } else {
                    throw new SQLException("No se obtuvo ID generado de Libro");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al crear Libro: " + e.getMessage(), e);
        }
    }

    @Override
    public Libro leer(Connection conn, long id) {
        final String sql = "SELECT id, eliminado, titulo, autor, editorial, anioEdicion " +
                           "FROM libro WHERE id = ? AND eliminado = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al leer Libro: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Libro> leerTodos(Connection conn) {
        final String sql = "SELECT id, eliminado, titulo, autor, editorial, anioEdicion " +
                           "FROM libro WHERE eliminado = FALSE ORDER BY id";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Libro> out = new ArrayList<>();
            while (rs.next()) out.add(mapRow(rs));
            return out;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al listar Libros: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Connection conn, Libro libro) {
        final String sql = "UPDATE libro SET eliminado = ?, titulo = ?, autor = ?, editorial = ?, anioEdicion = ? " +
                           "WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, libro.isEliminado()); // si tu Libro usa getEliminado(), cambia a getEliminado()
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getAutor());
            if (libro.getEditorial() != null) ps.setString(4, libro.getEditorial());
            else ps.setNull(4, Types.VARCHAR);
            if (libro.getAnioEdicion() != null) ps.setInt(5, libro.getAnioEdicion());
            else ps.setNull(5, Types.INTEGER);
            ps.setLong(6, libro.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al actualizar Libro: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(Connection conn, long id) {
        // eliminacion logica
        final String sql = "UPDATE libro SET eliminado = TRUE WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al eliminar (logico) Libro: " + e.getMessage(), e);
        }
    }

    // --------- Helpers ---------
    private Libro mapRow(ResultSet rs) throws SQLException {
        Libro l = new Libro();
        l.setId(rs.getLong("id"));
        l.setEliminado(rs.getBoolean("eliminado"));
        l.setTitulo(rs.getString("titulo"));
        l.setAutor(rs.getString("autor"));
        l.setEditorial(rs.getString("editorial"));

        // anioEdicion puede ser NULL
        Integer anio = (Integer) rs.getObject("anioEdicion");
        l.setAnioEdicion(anio);

        return l;
    }
}
