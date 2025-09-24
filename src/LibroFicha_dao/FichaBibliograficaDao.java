package LibroFicha_dao;

import LibroFicha_entities.FichaBibliografica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FichaBibliograficaDao {

    public void crear(Connection conn, FichaBibliografica ficha) throws SQLException {
        String sql = "INSERT INTO ficha_bibliografica (eliminado, isbn, clasificacion_dewey, estanteria, idioma, libro_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setBoolean(1, ficha.isEliminado());
            stmt.setString(2, ficha.getIsbn());
            stmt.setString(3, ficha.getClasificacionDewey());
            stmt.setString(4, ficha.getEstanteria());
            stmt.setString(5, ficha.getIdioma());
            if (ficha.getLibroId() != null) {
                stmt.setLong(6, ficha.getLibroId());
            } else {
                stmt.setNull(6, Types.BIGINT);
            }

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    ficha.setId(keys.getLong(1));
                }
            }
        }
    }

    public void actualizar(Connection conn, FichaBibliografica ficha) throws SQLException {
        String sql = "UPDATE ficha_bibliografica SET eliminado=?, isbn=?, clasificacion_dewey=?, estanteria=?, idioma=?, libro_id=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, ficha.isEliminado());
            stmt.setString(2, ficha.getIsbn());
            stmt.setString(3, ficha.getClasificacionDewey());
            stmt.setString(4, ficha.getEstanteria());
            stmt.setString(5, ficha.getIdioma());
            if (ficha.getLibroId() != null) {
                stmt.setLong(6, ficha.getLibroId());
            } else {
                stmt.setNull(6, Types.BIGINT);
            }
            stmt.setLong(7, ficha.getId());

            stmt.executeUpdate();
        }
    }

    public void eliminarPorLibroId(Connection conn, Long libroId) throws SQLException {
        String sql = "DELETE FROM ficha_bibliografica WHERE libro_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, libroId);
            stmt.executeUpdate();
        }
    }

    public FichaBibliografica getById(Connection conn, Long id) throws SQLException {
        String sql = "SELECT * FROM ficha_bibliografica WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    FichaBibliografica ficha = new FichaBibliografica();
                    ficha.setId(rs.getLong("id"));
                    ficha.setEliminado(rs.getBoolean("eliminado"));
                    ficha.setIsbn(rs.getString("isbn"));
                    ficha.setClasificacionDewey(rs.getString("clasificacion_dewey"));
                    ficha.setEstanteria(rs.getString("estanteria"));
                    ficha.setIdioma(rs.getString("idioma"));
                    ficha.setLibroId(rs.getLong("libro_id"));
                    return ficha;
                }
            }
        }
        return null;
    }

    public List<FichaBibliografica> getAll(Connection conn) throws SQLException {
        List<FichaBibliografica> lista = new ArrayList<>();
        String sql = "SELECT * FROM ficha_bibliografica";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                FichaBibliografica ficha = new FichaBibliografica();
                ficha.setId(rs.getLong("id"));
                ficha.setEliminado(rs.getBoolean("eliminado"));
                ficha.setIsbn(rs.getString("isbn"));
                ficha.setClasificacionDewey(rs.getString("clasificacion_dewey"));
                ficha.setEstanteria(rs.getString("estanteria"));
                ficha.setIdioma(rs.getString("idioma"));
                ficha.setLibroId(rs.getLong("libro_id"));
                lista.add(ficha);
            }
        }
        return lista;
    }
}
