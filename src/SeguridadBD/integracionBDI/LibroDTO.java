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

public class LibroDTO {
    private int idLibro;
    private String titulo;
    private int anio;
    private int idAutor;
    private String autor;
    private String nacionalidad;

    // Constructor vacío
    public LibroDTO() {}

    // Getters y Setters
    public int getIdLibro() {
        return idLibro;
    }
    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAnio() {
        return anio;
    }
    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getIdAutor() {
        return idAutor;
    }
    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    @Override
    public String toString() {
        return "LibroDTO{" +
               "idLibro=" + idLibro +
               ", titulo='" + titulo + '\'' +
               ", anio=" + anio +
               ", idAutor=" + idAutor +
               ", autor='" + autor + '\'' +
               ", nacionalidad='" + nacionalidad + '\'' +
               '}';
    }
}

