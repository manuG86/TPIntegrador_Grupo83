package LibroFicha_entities;

public class FichaBibliografica {
    private Long id;
    private boolean eliminado;
    private String isbn;
    private String clasificacionDewey;
    private String estanteria;
    private String idioma;

    // Constructor vacío
    public FichaBibliografica() { }

    // Constructor con todos los atributos
    public FichaBibliografica(Long id, boolean eliminado, String isbn,
                              String clasificacionDewey, String estanteria, String idioma) {
        this.id = id;
        this.eliminado = eliminado;
        this.isbn = isbn;
        this.clasificacionDewey = clasificacionDewey;
        this.estanteria = estanteria;
        this.idioma = idioma;
    }

    // Getters y Setters
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

    @Override
    public String toString() {
        String isbnStr = (isbn != null && !isbn.isEmpty()) ? isbn : "sin_isbn";
        return "FichaBibliografica{" +
               "id=" + id +
               ", eliminado=" + eliminado +
               ", isbn='" + isbnStr + '\'' +
               ", clasificacionDewey='" + clasificacionDewey + '\'' +
               ", estanteria='" + estanteria + '\'' +
               ", idioma='" + idioma + '\'' +
               '}';
    }
}