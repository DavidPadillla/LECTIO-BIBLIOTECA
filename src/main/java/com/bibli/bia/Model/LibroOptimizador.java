package com.bibli.bia.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros_optimizador")
public class LibroOptimizador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autor;
    private String categoria;
    private int paginas;
    private double horasLectura;
    private int diasPrestamo;
    private double dificultad;
    private boolean disponible;

    public LibroOptimizador() {}

    public LibroOptimizador(String titulo, String autor, String categoria,
                 int paginas, double horasLectura, int diasPrestamo,
                 double dificultad, boolean disponible) {
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.paginas = paginas;
        this.horasLectura = horasLectura;
        this.diasPrestamo = diasPrestamo;
        this.dificultad = dificultad;
        this.disponible = disponible;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public int getPaginas() { return paginas; }
    public void setPaginas(int paginas) { this.paginas = paginas; }
    public double getHorasLectura() { return horasLectura; }
    public void setHorasLectura(double horasLectura) { this.horasLectura = horasLectura; }
    public int getDiasPrestamo() { return diasPrestamo; }
    public void setDiasPrestamo(int diasPrestamo) { this.diasPrestamo = diasPrestamo; }
    public double getDificultad() { return dificultad; }
    public void setDificultad(double dificultad) { this.dificultad = dificultad; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}