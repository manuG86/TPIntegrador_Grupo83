package LibroFicha_main;

import LibroFicha_config.DatabaseConnection;
import LibroFicha_dao.LibroDao;
import LibroFicha_dao.FichaBibliograficaDao;
import LibroFicha_entities.Libro;
import LibroFicha_entities.FichaBibliografica;
import LibroFicha_service.LibroService;

import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        try {
            // 1️⃣ Instanciar DAOs y Service
            LibroDao libroDao = new LibroDao();
            FichaBibliograficaDao fichaDao = new FichaBibliograficaDao();
            LibroService libroService = new LibroService(libroDao, fichaDao);

            // 2️⃣ Crear libro
            Libro libro = new Libro();
            libro.setEliminado(false);
            libro.setTitulo("Cien años de soledad");
            libro.setAutor("Gabriel García Márquez");
            libro.setEditorial("Editorial Sudamericana");
            libro.setAnioEdicion(1967);

            // 3️⃣ Insertar libro usando tu LibroService
            libroService.insertar(libro); // solo inserta libro

            // 4️⃣ Crear ficha y asignar libroId
            FichaBibliografica ficha = new FichaBibliografica();
            ficha.setEliminado(false);
            ficha.setIsbn("978-3-16-148411-7"); // 🔹 cambiado para evitar duplicados
            ficha.setClasificacionDewey("863.6");
            ficha.setEstanteria("Estantería A1");
            ficha.setIdioma("Español");
            ficha.setLibroId(libro.getId());

            // 5️⃣ Insertar ficha usando DatabaseConnection directamente
            try (Connection conn = DatabaseConnection.getConnection()) {
                fichaDao.crear(conn, ficha);
            }

            System.out.println("Libro insertado: " + libro);
            System.out.println("Ficha insertada: " + ficha);

            // 6️⃣ Listar todos los libros
            List<Libro> libros = libroService.getAll();
            System.out.println("\nTodos los libros:");
            for (Libro l : libros) {
                System.out.println(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
