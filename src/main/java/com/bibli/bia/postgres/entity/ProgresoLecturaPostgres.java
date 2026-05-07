package com.bibli.bia.postgres.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "progreso_lectura")
public class ProgresoLecturaPostgres {

    @Id
    @Column(columnDefinition = "TEXT")
    private String id;

    // ✅ Relación con usuarios (por username)
    @Column(name = "username", columnDefinition = "TEXT", unique = true)
    private String username;

    // ✅ Opcional: Agregar id_usuario para mejor relación (FK directa)
    @Column(name = "id_usuario", columnDefinition = "TEXT")
    private String idUsuario;  // ← NUEVO: Relación directa con usuarios(id)

    // JSON fields (se mantienen igual)
    @Column(columnDefinition = "TEXT")
    private String librosCompletados;  // ["libro_id_1", "libro_id_2"]

    @Column(columnDefinition = "TEXT")
    private String capitulosPorLibro;  // {"libro_id_1":[1,2,3]}

    @Column(name = "total_libros_leidos")
    private int totalLibrosLeidos = 0;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @Column(name = "puntos")
    private int puntos = 0;

    // Constructor sin idUsuario (mantiene compatibilidad)
    public ProgresoLecturaPostgres(String id, String username) {
        this.id = id;
        this.username = username;
        this.idUsuario = null;  // Se puede buscar después
        this.librosCompletados = "[]";
        this.capitulosPorLibro = "{}";
        this.totalLibrosLeidos = 0;
        this.ultimaActualizacion = LocalDateTime.now();
        this.puntos = 0;
    }

    // Constructor con username (genera ID)
    public ProgresoLecturaPostgres(String username) {
        this.id = java.util.UUID.randomUUID().toString();
        this.username = username;
        this.idUsuario = null;
        this.librosCompletados = "[]";
        this.capitulosPorLibro = "{}";
        this.totalLibrosLeidos = 0;
        this.ultimaActualizacion = LocalDateTime.now();
        this.puntos = 0;
    }

    // ✅ NUEVO: Constructor completo con idUsuario
    public ProgresoLecturaPostgres(String id, String username, String idUsuario) {
        this.id = id;
        this.username = username;
        this.idUsuario = idUsuario;
        this.librosCompletados = "[]";
        this.capitulosPorLibro = "{}";
        this.totalLibrosLeidos = 0;
        this.ultimaActualizacion = LocalDateTime.now();
        this.puntos = 0;
    }
}