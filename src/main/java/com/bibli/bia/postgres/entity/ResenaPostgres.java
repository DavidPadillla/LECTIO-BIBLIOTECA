package com.bibli.bia.postgres.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resenas")
public class ResenaPostgres {

    @Id
    @Column(columnDefinition = "TEXT")
    private String id;

    // ❌ OPCIONAL: Mantener como backup o eliminar
    @Column(columnDefinition = "TEXT")
    private String nombre;  // Nombre del usuario (backup)

    @Column(columnDefinition = "TEXT")
    private String comentario;

    // ✅ NUEVO: Relación con usuarios
    @Column(name = "id_usuario", columnDefinition = "TEXT")
    private String idUsuario;

    // ✅ NUEVO: Relación con libros
    @Column(name = "id_libro", columnDefinition = "TEXT")
    private String idLibro;

    // ✅ NUEVO: Calificación/puntuación (útil para Power BI)
    @Column(name = "puntuacion")
    private Integer puntuacion;  // 1-5 estrellas, por ejemplo

    // ✅ NUEVO: Fecha de la reseña
    @Column(name = "fecha_resena")
    private java.time.LocalDate fechaResena;

    // Constructor 1: solo nombre y comentario (mantiene compatibilidad con código existente)
    public ResenaPostgres(String nombre, String comentario) {
        this.id = java.util.UUID.randomUUID().toString();
        this.nombre = nombre;
        this.comentario = comentario;
        this.idUsuario = null;
        this.idLibro = null;
        this.puntuacion = null;
        this.fechaResena = java.time.LocalDate.now();
    }

    // ✅ Constructor 2: con relaciones completas (recomendado)
    public ResenaPostgres(String idUsuario, String idLibro, String comentario, Integer puntuacion) {
        this.id = java.util.UUID.randomUUID().toString();
        this.idUsuario = idUsuario;
        this.idLibro = idLibro;
        this.nombre = null;  // Se puede obtener de usuarios.username si es necesario
        this.comentario = comentario;
        this.puntuacion = puntuacion;
        this.fechaResena = java.time.LocalDate.now();
    }

    // ✅ Constructor 3: incluyendo nombre como backup
    public ResenaPostgres(String idUsuario, String nombre, String idLibro, String comentario, Integer puntuacion) {
        this.id = java.util.UUID.randomUUID().toString();
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.idLibro = idLibro;
        this.comentario = comentario;
        this.puntuacion = puntuacion;
        this.fechaResena = java.time.LocalDate.now();
    }
}