package com.bibli.bia.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "libros") // nombre de la colección en MongoDB
public class LibroModel {

    @Id
    private String id;

    private String titulo;
    private String url;
    private String autor;
    private String descripcion;
    private String categoria;


    public LibroModel() {
    }


    public LibroModel(String titulo, String url, String autor, String descripcion, String categoria) {
        this.titulo = titulo;
        this.url = url;
        this.autor = autor;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}

