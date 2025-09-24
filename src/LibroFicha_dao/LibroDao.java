package LibroFicha_dao;

import LibroFicha_entities.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDao {

    public void crear(Connection conn, Libro libro) throws SQLException {
        String sql = "INSERT INTO libro (eliminado, titulo, autor, editorial, anio_edicion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setBoolean(1, libro.isEliminado());
            stmt.setString(2, libro.getTitulo());
            stmt.setString(3, libro.getAutor());
            stmt.setString(4, libro.getEditorial());
            if (libro.getAnioEdicion() != null) {
                stmt.setInt(5, libro.getAnioEdicion());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    libro.setId(keys.getLong(1));
                }
            }
        }
    }

    public void actualizar(Connection conn, Libro libro) throws SQLException {
        String sql = "UPDATE libro SET eliminado=?, titulo=?, autor=?, editorial=?, anio_edicion=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, libro.isEliminado());
            stmt.setString(2, libro.getTitulo());
            stmt.setString(3, libro.getAutor());
            stmt.setString(4, libro.getEditorial());
            if (libro.getAnioEdicion() != null) {
                stmt.setInt(5, libro.getAnioEdicion());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setLong(6, libro.getId());

            stmt.executeUpdate();
        }
    }

    public void eliminar(Connection conn, Long id) throws SQLException {
        String sql = "DELETE FROM libro WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public Libro getById(Connection conn, Long id) throws SQLException {
        String sql = "SELECT * FROM libro WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Libro libro = new Libro();
                    libro.setId(rs.getLong("id"));
                    libro.setEliminado(rs.getBoolean("eliminado"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setAutor(rs.getString("autor"));
                    libro.setEditorial(rs.getString("editorial"));
                    libro.setAnioEdicion(rs.getInt("anio_edicion"));
                    return libro;
                }
            }
        }
        return null;
    }

    public List<Libro> getAll(Connection conn) throws SQLException {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM libro";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Libro libro = new Libro();
                libro.setId(rs.getLong("id"));
                libro.setEliminado(rs.getBoolean("eliminado"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setEditorial(rs.getString("editorial"));
                libro.setAnioEdicion(rs.getInt("anio_edicion"));
                lista.add(libro);
            }
        }
        return lista;
    }
}
