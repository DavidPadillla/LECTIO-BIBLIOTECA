package com.bibli.bia.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "libros_fisicos")
public class LibroFisicoModel {

    @Id
    private String id;

    private String titulo;
    private String autor;
    private String descripcion;
    private String categoria;
    private int stock = 0;      // 🔹 Valor inicial por defecto
    private int reservado = 0;


    public LibroFisicoModel() {}


    public LibroFisicoModel(String titulo, String autor, String descripcion, String categoria, int stock, int reservado) {
        this.titulo = titulo;
        this.autor = autor;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.stock = stock;
        this.reservado = reservado;

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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getReservado() {
        return reservado;
    }

    public void setReservado(int reservado) {
        this.reservado = reservado;
    }


}
