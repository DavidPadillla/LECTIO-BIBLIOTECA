package com.bibli.bia.Model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "libros", indexes = {
        @Index(name = "idx_titulo", columnList = "titulo"),
        @Index(name = "idx_autor", columnList = "autor"),
        @Index(name = "idx_categoria", columnList = "categoria")
})
public class LibroModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(length = 500)
    private String url;

    @Column(nullable = false, length = 100)
    private String autor;

    @Column(length = 2000)
    private String descripcion;

    @Column(nullable = false, length = 50)
    private String categoria;

    // Relaciones inversas (opcionales, para consultas)
    @OneToMany(mappedBy = "libroId", fetch = FetchType.LAZY)
    private List<ResenaModel> resenas = new ArrayList<>();

    @OneToMany(mappedBy = "libro", fetch = FetchType.LAZY)
    private List<MultaModel> multas = new ArrayList<>();

    public LibroModel() {}

    public LibroModel(String titulo, String url, String autor, String descripcion, String categoria) {
        this.titulo = titulo;
        this.url = url;
        this.autor = autor;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public List<ResenaModel> getResenas() { return resenas; }
    public void setResenas(List<ResenaModel> resenas) { this.resenas = resenas; }

    public List<MultaModel> getMultas() { return multas; }
    public void setMultas(List<MultaModel> multas) { this.multas = multas; }
}