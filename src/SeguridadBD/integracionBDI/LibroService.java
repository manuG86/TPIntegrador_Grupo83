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

import SeguridadBD.integracionBDI.LibroDao;
import SeguridadBD.integracionBDI.LibroDTO;
import java.sql.SQLException;
import java.util.List;

public class LibroService {

    private final LibroDao dao = new LibroDao();

    public List<LibroDTO> buscarPorTituloSeguro(String titulo) throws SQLException {
        return dao.buscarPorTituloSeguro(titulo);
    }
}

