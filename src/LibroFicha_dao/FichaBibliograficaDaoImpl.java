package LibroFicha_dao;

import LibroFicha_entities.FichaBibliografica;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de FichaBibliografica (lado B).
 * Nota: respeta la relacion 1->1 con Libro via FK unica libro_id.
 * Este DAO NUNCA commitea ni cierra la Connection; eso lo maneja Service.
 */
public class FichaBibliograficaDaoImpl implements GenericDao<FichaBibliografica> {

    @Override
    public Long crear(Connection conn, FichaBibliografica ficha) {
        final String sql = "INSERT INTO ficha_bibliografica " +
                "(eliminado, isbn, clasificacionDewey, estanteria, idioma, libro_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, ficha.isEliminado());
            ps.setString(2, ficha.getIsbn());

            if (ficha.getClasificacionDewey() != null) ps.setString(3, ficha.getClasificacionDewey());
            else ps.setNull(3, Types.VARCHAR);

            if (ficha.getEstanteria() != null) ps.setString(4, ficha.getEstanteria());
            else ps.setNull(4, Types.VARCHAR);

            if (ficha.getIdioma() != null) ps.setString(5, ficha.getIdioma());
            else ps.setNull(5, Types.VARCHAR);

            if (ficha.getLibroId() != null) ps.setLong(6, ficha.getLibroId());
            else throw new SQLException("libro_id (FK) no puede ser null en ficha_bibliografica");

            int rows = ps.executeUpdate();
            if (rows != 1) throw new SQLException("No se inserto FichaBibliografica");

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    ficha.setId(id);   // tambien guardo el id en la entidad
                    return id;
                } else {
                    throw new SQLException("No se obtuvo ID generado de FichaBibliografica");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al crear FichaBibliografica: " + e.getMessage(), e);
        }
    }

    @Override
    public FichaBibliografica leer(Connection conn, long id) {
        final String sql = "SELECT id, eliminado, isbn, clasificacionDewey, estanteria, idioma, libro_id " +
                           "FROM ficha_bibliografica WHERE id = ? AND eliminado = FALSE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al leer FichaBibliografica: " + e.getMessage(), e);
        }
    }

    @Override
    public List<FichaBibliografica> leerTodos(Connection conn) {
        final String sql = "SELECT id, eliminado, isbn, clasificacionDewey, estanteria, idioma, libro_id " +
                           "FROM ficha_bibliografica WHERE eliminado = FALSE ORDER BY id";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<FichaBibliografica> out = new ArrayList<>();
            while (rs.next()) out.add(mapRow(rs));
            return out;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al listar Fichas: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Connection conn, FichaBibliografica ficha) {
        final String sql = "UPDATE ficha_bibliografica SET " +
                "eliminado = ?, isbn = ?, clasificacionDewey = ?, estanteria = ?, idioma = ?, libro_id = ? " +
                "WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, ficha.isEliminado());
            ps.setString(2, ficha.getIsbn());

            if (ficha.getClasificacionDewey() != null) ps.setString(3, ficha.getClasificacionDewey());
            else ps.setNull(3, Types.VARCHAR);

            if (ficha.getEstanteria() != null) ps.setString(4, ficha.getEstanteria());
            else ps.setNull(4, Types.VARCHAR);

            if (ficha.getIdioma() != null) ps.setString(5, ficha.getIdioma());
            else ps.setNull(5, Types.VARCHAR);

            if (ficha.getLibroId() != null) ps.setLong(6, ficha.getLibroId());
            else throw new SQLException("libro_id (FK) no puede ser null en ficha_bibliografica");

            ps.setLong(7, ficha.getId());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al actualizar FichaBibliografica: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(Connection conn, long id) {
        // eliminacion logica
        final String sql = "UPDATE ficha_bibliografica SET eliminado = TRUE WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al eliminar (logico) FichaBibliografica: " + e.getMessage(), e);
        }
    }

    /**
     * Eliminacion logica de la Ficha por el id del Libro (FK).
     * Lo usamos desde el Service cuando "eliminamos" un Libro.
     */
    public boolean eliminarPorLibroId(Connection conn, long libroId) {
        final String sql = "UPDATE ficha_bibliografica SET eliminado = TRUE WHERE libro_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, libroId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL al eliminar ficha por libro_id: " + e.getMessage(), e);
        }
    }

    // --------- Helpers ---------
    private FichaBibliografica mapRow(ResultSet rs) throws SQLException {
        FichaBibliografica f = new FichaBibliografica();
        f.setId(rs.getLong("id"));
        f.setEliminado(rs.getBoolean("eliminado"));
        f.setIsbn(rs.getString("isbn"));
        f.setClasificacionDewey(rs.getString("clasificacionDewey"));
        f.setEstanteria(rs.getString("estanteria"));
        f.setIdioma(rs.getString("idioma"));

        // libro_id es NOT NULL por esquema, pero dejo el manejo generico
        Long libroId = (Long) rs.getObject("libro_id");
        f.setLibroId(libroId);

        return f;
    }
}
