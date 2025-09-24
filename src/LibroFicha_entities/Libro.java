package LibroFicha_entities;

public class Libro {
    private Long id;
    private Boolean eliminado;
    private String titulo;
    private String autor;
    private String editorial;
    private Integer anioEdicion;

    // Constructor vacío
    public Libro() {
    }

    // Constructor con todos los atributos
    public Libro(Long id, Boolean eliminado, String titulo, String autor, String editorial, Integer anioEdicion) {
        this.id = id;
        this.eliminado = eliminado;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.anioEdicion = anioEdicion;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public Integer getAnioEdicion() {
        return anioEdicion;
    }

    public void setAnioEdicion(Integer anioEdicion) {
        this.anioEdicion = anioEdicion;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", editorial='" + editorial + '\'' +
                ", anioEdicion=" + anioEdicion +
                '}';
    }

    public boolean isEliminado() {
        return eliminado; 
    }
}
