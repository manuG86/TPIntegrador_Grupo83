/**
 * NOTA IMPORTANTE (Integración TFI BD I):
 * Este archivo pertenece a ejercicios/demos del TFI de Bases de Datos I
 * (consultas seguras, pruebas con PreparedStatement, etc.).
 *
 * NO forma parte de la implementación de capas del TFI de Programación 2.
 * Lo mantenemos en el repo para mostrar la integración entre ambos trabajos,
 * pero el flujo del TFI de P2 usa exclusivamente:
 * - LibroFicha_entities/*
 * - LibroFicha_dao/*
 * - LibroFicha_service/*
 * - LibroFicha_config/*
 * - LibroFicha_main/*
 */
package SeguridadBD.integracionBDI;

import SeguridadBD.integracionBDI.LibroDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDao {

    public List<LibroDTO> buscarPorTituloSeguro(String titulo) throws SQLException {
        String sql = """
            SELECT l.idLibro, l.titulo, l.anio, a.idAutor,
                   CONCAT(a.nombre,' ',a.apellido) AS autor,
                   a.nacionalidad
            FROM libro l
            JOIN autor a ON l.idAutor = a.idAutor
            WHERE l.titulo = ?
        """;
        
//Opción 2, para ejecutar como usuario min (solo lectura)usando la vista publica
/**
 * String sql = """
    SELECT idLibro, titulo, anio, idAutor, autor, nacionalidad
    FROM vistacatalogopublico
    WHERE titulo = ?
    """;
 */

        List<LibroDTO> out = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, titulo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LibroDTO dto = new LibroDTO();
                    dto.setIdLibro(rs.getInt("idLibro"));
                    dto.setTitulo(rs.getString("titulo"));
                    dto.setAnio(rs.getInt("anio"));
                    dto.setIdAutor(rs.getInt("idAutor"));
                    dto.setAutor(rs.getString("autor"));
                    dto.setNacionalidad(rs.getString("nacionalidad"));
                    out.add(dto);
                }
            }
        }
        return out;
    }
}

