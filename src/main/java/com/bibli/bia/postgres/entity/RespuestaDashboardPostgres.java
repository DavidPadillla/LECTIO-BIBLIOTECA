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
@Table(name = "respuestas_dashboard")
public class RespuestaDashboardPostgres {

    @Id
    @Column(columnDefinition = "TEXT")
    private String id;

    // ✅ NUEVO: Relación con usuarios (quién respondió la encuesta)
    @Column(name = "id_usuario", columnDefinition = "TEXT")
    private String idUsuario;

    // ✅ NUEVO: username como backup (legible para humanos)
    @Column(name = "username", columnDefinition = "TEXT")
    private String username;

    // Campos existentes de la encuesta
    private Integer edad;

    @Column(columnDefinition = "TEXT")
    private String genero;

    @Column(columnDefinition = "TEXT")
    private String educacion;

    @Column(columnDefinition = "TEXT")
    private String frecuencia;

    @Column(name = "categoria_favorita", columnDefinition = "TEXT")
    private String categoriaFavorita;

    @Column(columnDefinition = "TEXT")
    private String formato;

    @Column(columnDefinition = "TEXT")
    private String uso;

    @Column(name = "libros_mes")
    private Integer librosMes;

    private Integer calificacion;

    @Column(columnDefinition = "TEXT")
    private String recomendacion;

    @Column(columnDefinition = "TEXT")
    private String dispositivos;

    @Column(name = "ultimo_libro", columnDefinition = "TEXT")
    private String ultimoLibro;

    @Column(columnDefinition = "TEXT")
    private String mejoras;

    @Column(columnDefinition = "TEXT")
    private String recomendaciones;

    @Column(columnDefinition = "TEXT")
    private String clubes;

    @Column(columnDefinition = "TEXT")
    private String compras;

    @Column(name = "autores_favoritos", columnDefinition = "TEXT")
    private String autoresFavoritos;

    @Column(columnDefinition = "TEXT")
    private String boletines;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    // Constructor original (sin relación con usuario)
    public RespuestaDashboardPostgres(Integer edad, String genero, String educacion,
                                      String frecuencia, String categoriaFavorita, String formato,
                                      String uso, Integer librosMes, Integer calificacion,
                                      String recomendacion, String dispositivos, String ultimoLibro,
                                      String mejoras, String recomendaciones, String clubes,
                                      String compras, String autoresFavoritos, String boletines) {
        this.id = java.util.UUID.randomUUID().toString();
        this.idUsuario = null;  // Sin relación
        this.username = null;
        this.edad = edad;
        this.genero = genero;
        this.educacion = educacion;
        this.frecuencia = frecuencia;
        this.categoriaFavorita = categoriaFavorita;
        this.formato = formato;
        this.uso = uso;
        this.librosMes = librosMes;
        this.calificacion = calificacion;
        this.recomendacion = recomendacion;
        this.dispositivos = dispositivos;
        this.ultimoLibro = ultimoLibro;
        this.mejoras = mejoras;
        this.recomendaciones = recomendaciones;
        this.clubes = clubes;
        this.compras = compras;
        this.autoresFavoritos = autoresFavoritos;
        this.boletines = boletines;
        this.fechaRegistro = LocalDateTime.now();
    }

    // ✅ NUEVO Constructor con relación a usuario
    public RespuestaDashboardPostgres(String idUsuario, String username, Integer edad, String genero,
                                      String educacion, String frecuencia, String categoriaFavorita,
                                      String formato, String uso, Integer librosMes, Integer calificacion,
                                      String recomendacion, String dispositivos, String ultimoLibro,
                                      String mejoras, String recomendaciones, String clubes,
                                      String compras, String autoresFavoritos, String boletines) {
        this.id = java.util.UUID.randomUUID().toString();
        this.idUsuario = idUsuario;
        this.username = username;
        this.edad = edad;
        this.genero = genero;
        this.educacion = educacion;
        this.frecuencia = frecuencia;
        this.categoriaFavorita = categoriaFavorita;
        this.formato = formato;
        this.uso = uso;
        this.librosMes = librosMes;
        this.calificacion = calificacion;
        this.recomendacion = recomendacion;
        this.dispositivos = dispositivos;
        this.ultimoLibro = ultimoLibro;
        this.mejoras = mejoras;
        this.recomendaciones = recomendaciones;
        this.clubes = clubes;
        this.compras = compras;
        this.autoresFavoritos = autoresFavoritos;
        this.boletines = boletines;
        this.fechaRegistro = LocalDateTime.now();
    }
}