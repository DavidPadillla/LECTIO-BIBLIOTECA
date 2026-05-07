package com.bibli.bia.postgres.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "libros_fisicos")
public class LibroFisicoPostgres {

    @Id
    @Column(columnDefinition = "TEXT")
    private String id;

    // ✅ NUEVO: Relación con la tabla libros
    @Column(name = "id_libro", columnDefinition = "TEXT")
    private String idLibro;  // ← Foreign Key a libros(id)

    // Campos existentes (se pueden mantener o migrar a la tabla libros)
    @Column(columnDefinition = "TEXT")
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String autor;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String categoria;

    private int stock = 0;
    private int reservado = 0;
}