<<<<<<< HEAD
package LibroFicha_entities;

public class Libro {
    private Long id;
    private boolean eliminado;
    private String titulo;
    private String autor;
    private String editorial;
    private Integer anioEdicion;

    // Relación 1→1 unidireccional (A contiene B)
    private FichaBibliografica fichaBibliografica;

    // Constructor vacío
    public Libro() { }

    // Constructor sin ficha
    public Libro(Long id, boolean eliminado, String titulo, String autor,
                 String editorial, Integer anioEdicion) {
        this.id = id;
        this.eliminado = eliminado;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.anioEdicion = anioEdicion;
    }

    // Constructor con ficha
    public Libro(Long id, boolean eliminado, String titulo, String autor,
                 String editorial, Integer anioEdicion, FichaBibliografica fichaBibliografica) {
        this(id, eliminado, titulo, autor, editorial, anioEdicion);
        this.fichaBibliografica = fichaBibliografica;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean getEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }
    public boolean isEliminado() { return eliminado; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }

    public Integer getAnioEdicion() { return anioEdicion; }
    public void setAnioEdicion(Integer anioEdicion) { this.anioEdicion = anioEdicion; }

    public FichaBibliografica getFichaBibliografica() { return fichaBibliografica; }
    public void setFichaBibliografica(FichaBibliografica fichaBibliografica) {
        this.fichaBibliografica = fichaBibliografica;
    }

    @Override
    public String toString() {
        String isbnStr = (fichaBibliografica != null && fichaBibliografica.getIsbn() != null
                          && !fichaBibliografica.getIsbn().isEmpty())
                         ? fichaBibliografica.getIsbn()
                         : "null";
        return "Libro{" +
               "id=" + id +
               ", eliminado=" + eliminado +
               ", titulo='" + titulo + '\'' +
               ", autor='" + autor + '\'' +
               ", editorial='" + editorial + '\'' +
               ", anioEdicion=" + anioEdicion +
               ", ficha=" + isbnStr +
               '}';
    }
}

=======
package LibroFicha_entities;

public class Libro {
    private Long id;
    private boolean eliminado;
    private String titulo;
    private String autor;
    private String editorial;
    private Integer anioEdicion;

    // Relación 1→1 unidireccional (A contiene B)
    private FichaBibliografica fichaBibliografica;

    // Constructor vacío
    public Libro() { }

    // Constructor sin ficha
    public Libro(Long id, boolean eliminado, String titulo, String autor,
                 String editorial, Integer anioEdicion) {
        this.id = id;
        this.eliminado = eliminado;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.anioEdicion = anioEdicion;
    }

    // Constructor con ficha
    public Libro(Long id, boolean eliminado, String titulo, String autor,
                 String editorial, Integer anioEdicion, FichaBibliografica fichaBibliografica) {
        this(id, eliminado, titulo, autor, editorial, anioEdicion);
        this.fichaBibliografica = fichaBibliografica;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean getEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }
    public boolean isEliminado() { return eliminado; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }

    public Integer getAnioEdicion() { return anioEdicion; }
    public void setAnioEdicion(Integer anioEdicion) { this.anioEdicion = anioEdicion; }

    public FichaBibliografica getFichaBibliografica() { return fichaBibliografica; }
    public void setFichaBibliografica(FichaBibliografica fichaBibliografica) {
        this.fichaBibliografica = fichaBibliografica;
    }

    @Override
    public String toString() {
        String isbnStr = (fichaBibliografica != null && fichaBibliografica.getIsbn() != null
                          && !fichaBibliografica.getIsbn().isEmpty())
                         ? fichaBibliografica.getIsbn()
                         : "null";
        return "Libro{" +
               "id=" + id +
               ", eliminado=" + eliminado +
               ", titulo='" + titulo + '\'' +
               ", autor='" + autor + '\'' +
               ", editorial='" + editorial + '\'' +
               ", anioEdicion=" + anioEdicion +
               ", ficha=" + isbnStr +
               '}';
    }
}

>>>>>>> Damian
