<<<<<<< HEAD
package LibroFicha_entities;

/**
 * Clase FichaBibliografica
 * Representa la ficha asociada a un libro (relación 1 a 1 unidireccional).
 * Esta clase pertenece al "lado B" de la relación (la ficha no conoce al Libro como objeto,
 * solo guarda su id para mantener la FK en la base de datos).
 */
public class FichaBibliografica {

    // --- Atributos principales ---
    private Long id;                   // Identificador único (PK)
    private boolean eliminado;         // Baja lógica: true = no visible, false = activo
    private String isbn;               // Código ISBN (único)
    private String clasificacionDewey; // Código de clasificación según Dewey
    private String estanteria;         // Ubicación física en la biblioteca
    private String idioma;             // Idioma del libro

    // --- Relación 1→1 hacia Libro ---
    // Nota de modificacion (Damian): agrego este campo porque en la tabla 'ficha_bibliografica'
    // existe la columna 'libro_id' (FK única) que vincula cada ficha con su libro.
    // Solo guardo el ID, no una referencia al objeto Libro, para mantener la unidireccionalidad.
    private Long libroId;

    // --- Constructores ---
    // Constructor vacío 
    public FichaBibliografica() { }

    // Constructor con todos los atributos (sin libroId)
    public FichaBibliografica(Long id, boolean eliminado, String isbn,
                              String clasificacionDewey, String estanteria, String idioma) {
        this.id = id;
        this.eliminado = eliminado;
        this.isbn = isbn;
        this.clasificacionDewey = clasificacionDewey;
        this.estanteria = estanteria;
        this.idioma = idioma;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean getEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }
    public boolean isEliminado() { return eliminado; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getClasificacionDewey() { return clasificacionDewey; }
    public void setClasificacionDewey(String clasificacionDewey) { this.clasificacionDewey = clasificacionDewey; }

    public String getEstanteria() { return estanteria; }
    public void setEstanteria(String estanteria) { this.estanteria = estanteria; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    // --- Getters y Setters de la relación con Libro ---
    public Long getLibroId() { return libroId; }
    public void setLibroId(Long libroId) { this.libroId = libroId; }

    // --- Representación en texto ---
    @Override
    public String toString() {
        // Nota (Damian):agregué esto, manejo valores nulos para evitar errores al imprimir.
        String isbnStr = (isbn != null && !isbn.isEmpty()) ? isbn : "sin_isbn";
        String deweyStr = (clasificacionDewey != null && !clasificacionDewey.isEmpty()) ? clasificacionDewey : "sin_clasificacion";
        String idiomaStr = (idioma != null && !idioma.isEmpty()) ? idioma : "sin_idioma";
        String libroStr = (libroId != null) ? libroId.toString() : "sin_libro";

        return "FichaBibliografica{" +
               "id=" + id +
               ", eliminado=" + eliminado +
               ", isbn='" + isbnStr + '\'' +
               ", clasificacionDewey='" + deweyStr + '\'' +
               ", estanteria='" + estanteria + '\'' +
               ", idioma='" + idiomaStr + '\'' +
               ", libroId=" + libroStr +
               '}';
    }
}
=======
package LibroFicha_entities;

/**
 * Clase FichaBibliografica
 * Representa la ficha asociada a un libro (relación 1 a 1 unidireccional).
 * Esta clase pertenece al "lado B" de la relación (la ficha no conoce al Libro como objeto,
 * solo guarda su id para mantener la FK en la base de datos).
 */
public class FichaBibliografica {

    // --- Atributos principales ---
    private Long id;                   // Identificador único (PK)
    private boolean eliminado;         // Baja lógica: true = no visible, false = activo
    private String isbn;               // Código ISBN (único)
    private String clasificacionDewey; // Código de clasificación según Dewey
    private String estanteria;         // Ubicación física en la biblioteca
    private String idioma;             // Idioma del libro

    // --- Relación 1→1 hacia Libro ---
    // Nota de modificacion (Damian): agrego este campo porque en la tabla 'ficha_bibliografica'
    // existe la columna 'libro_id' (FK única) que vincula cada ficha con su libro.
    // Solo guardo el ID, no una referencia al objeto Libro, para mantener la unidireccionalidad.
    private Long libroId;

    // --- Constructores ---
    // Constructor vacío 
    public FichaBibliografica() { }

    // Constructor con todos los atributos (sin libroId)
    public FichaBibliografica(Long id, boolean eliminado, String isbn,
                              String clasificacionDewey, String estanteria, String idioma) {
        this.id = id;
        this.eliminado = eliminado;
        this.isbn = isbn;
        this.clasificacionDewey = clasificacionDewey;
        this.estanteria = estanteria;
        this.idioma = idioma;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean getEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }
    public boolean isEliminado() { return eliminado; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getClasificacionDewey() { return clasificacionDewey; }
    public void setClasificacionDewey(String clasificacionDewey) { this.clasificacionDewey = clasificacionDewey; }

    public String getEstanteria() { return estanteria; }
    public void setEstanteria(String estanteria) { this.estanteria = estanteria; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    // --- Getters y Setters de la relación con Libro ---
    public Long getLibroId() { return libroId; }
    public void setLibroId(Long libroId) { this.libroId = libroId; }

    // --- Representación en texto ---
    @Override
    public String toString() {
        // Nota (Damian):agregué esto, manejo valores nulos para evitar errores al imprimir.
        String isbnStr = (isbn != null && !isbn.isEmpty()) ? isbn : "sin_isbn";
        String deweyStr = (clasificacionDewey != null && !clasificacionDewey.isEmpty()) ? clasificacionDewey : "sin_clasificacion";
        String idiomaStr = (idioma != null && !idioma.isEmpty()) ? idioma : "sin_idioma";
        String libroStr = (libroId != null) ? libroId.toString() : "sin_libro";

        return "FichaBibliografica{" +
               "id=" + id +
               ", eliminado=" + eliminado +
               ", isbn='" + isbnStr + '\'' +
               ", clasificacionDewey='" + deweyStr + '\'' +
               ", estanteria='" + estanteria + '\'' +
               ", idioma='" + idiomaStr + '\'' +
               ", libroId=" + libroStr +
               '}';
    }
}
>>>>>>> Damian
