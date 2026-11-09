package com.bibli.bia.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    /** Ejemplares disponibles ahora (baja al reservar, sube al cancelar). */
    @Column(nullable = false)
    private int stock = 0;

    /** Ejemplares reservados actualmente. */
    @Column(nullable = false)
    private int reservado = 0;

    /** Cuándo se registró el libro (sirve como antigüedad del libro en el modelo). */
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // CORREGIDO: antes era mappedBy = "libro" (un String). Ahora apunta a la relación real.
    @JsonIgnore
    @OneToMany(mappedBy = "libroFisico", fetch = FetchType.LAZY)
    private List<ReservaModel> reservas = new ArrayList<>();

    public LibroFisicoModel() {}

    public LibroFisicoModel(String titulo, String autor, String descripcion, String categoria, int stock, int reservado) {
        this.titulo = titulo;
        this.autor = autor;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.stock = Math.max(0, stock);
        this.reservado = Math.max(0, reservado);
    }

    @PrePersist
    void antesDeGuardar() {
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        normalizar();
    }

    @PreUpdate
    void antesDeActualizar() {
        normalizar();
    }

    /** Quita espacios sobrantes para que los títulos y categorías crucen bien. */
    private void normalizar() {
        titulo = limpiar(titulo);
        autor = limpiar(autor);
        categoria = limpiar(categoria);
    }

    private static String limpiar(String s) {
        return s == null ? null : s.trim().replaceAll("\\s+", " ");
    }


    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    /** Inventario total real (disponibles + reservados). */
    public int getTotalEjemplares() { return stock + reservado; }

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
    public void setStock(int stock) { this.stock = Math.max(0, stock); }

    public int getReservado() { return reservado; }
    public void setReservado(int reservado) { this.reservado = Math.max(0, reservado); }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public List<ReservaModel> getReservas() { return reservas; }
    public void setReservas(List<ReservaModel> reservas) { this.reservas = reservas; }
}