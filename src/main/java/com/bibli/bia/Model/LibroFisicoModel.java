package com.bibli.bia.Model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "libros_fisicos", indexes = {
        @Index(name = "idx_titulo_fisico", columnList = "titulo"),
        @Index(name = "idx_autor_fisico", columnList = "autor"),
        @Index(name = "idx_categoria_fisico", columnList = "categoria")
})
public class LibroFisicoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 100)
    private String autor;

    @Column(length = 2000)
    private String descripcion;

    @Column(nullable = false, length = 50)
    private String categoria;

    @Column(nullable = false)
    private int stock = 0;

    @Column(nullable = false)
    private int reservado = 0;

    // Relación con reservas (opcional)
    @OneToMany(mappedBy = "libro", fetch = FetchType.LAZY)
    private List<ReservaModel> reservas = new ArrayList<>();

    public LibroFisicoModel() {}

    public LibroFisicoModel(String titulo, String autor, String descripcion, String categoria, int stock, int reservado) {
        this.titulo = titulo;
        this.autor = autor;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.stock = stock;
        this.reservado = reservado;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getReservado() { return reservado; }
    public void setReservado(int reservado) { this.reservado = reservado; }

    public List<ReservaModel> getReservas() { return reservas; }
    public void setReservas(List<ReservaModel> reservas) { this.reservas = reservas; }
}