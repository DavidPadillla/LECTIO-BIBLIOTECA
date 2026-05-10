package com.bibli.bia.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "respuestas_dashboard", indexes = {
        @Index(name = "idx_usuario_id", columnList = "usuario_id"),
        @Index(name = "idx_fecha_registro", columnList = "fecha_registro")
})
public class RespuestaDashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Integer edad;

    @Column(nullable = false, length = 20)
    private String genero;

    @Column(length = 50)
    private String educacion;

    @Column(length = 30)
    private String frecuencia;

    @Column(name = "categoria_favorita", length = 50)
    private String categoriaFavorita;

    @Column(length = 20)
    private String formato;

    @Column(length = 30)
    private String uso;

    @Column(name = "libros_mes")
    private Integer librosMes;

    private Integer calificacion;

    @Column(length = 10)
    private String recomendacion;

    @Column(length = 100)
    private String dispositivos;

    @Column(name = "ultimo_libro", length = 200)
    private String ultimoLibro;

    @Column(length = 500)
    private String mejoras;

    @Column(length = 500)
    private String recomendaciones;

    @Column(length = 200)
    private String clubes;

    @Column(length = 200)
    private String compras;

    @Column(name = "autores_favoritos", length = 500)
    private String autoresFavoritos;

    @Column(length = 200)
    private String boletines;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    // Constructores
    public RespuestaDashboard() {}

    public RespuestaDashboard(String id, Usuario usuario, Integer edad, String genero, String educacion,
                              String frecuencia, String categoriaFavorita, String formato, String uso,
                              Integer librosMes, Integer calificacion, String recomendacion, String dispositivos,
                              String ultimoLibro, String mejoras, String recomendaciones, String clubes,
                              String compras, String autoresFavoritos, String boletines, LocalDateTime fechaRegistro) {
        this.id = id;
        this.usuario = usuario;
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
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getEducacion() { return educacion; }
    public void setEducacion(String educacion) { this.educacion = educacion; }

    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }

    public String getCategoriaFavorita() { return categoriaFavorita; }
    public void setCategoriaFavorita(String categoriaFavorita) { this.categoriaFavorita = categoriaFavorita; }

    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }

    public String getUso() { return uso; }
    public void setUso(String uso) { this.uso = uso; }

    public Integer getLibrosMes() { return librosMes; }
    public void setLibrosMes(Integer librosMes) { this.librosMes = librosMes; }

    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }

    public String getRecomendacion() { return recomendacion; }
    public void setRecomendacion(String recomendacion) { this.recomendacion = recomendacion; }

    public String getDispositivos() { return dispositivos; }
    public void setDispositivos(String dispositivos) { this.dispositivos = dispositivos; }

    public String getUltimoLibro() { return ultimoLibro; }
    public void setUltimoLibro(String ultimoLibro) { this.ultimoLibro = ultimoLibro; }

    public String getMejoras() { return mejoras; }
    public void setMejoras(String mejoras) { this.mejoras = mejoras; }

    public String getRecomendaciones() { return recomendaciones; }
    public void setRecomendaciones(String recomendaciones) { this.recomendaciones = recomendaciones; }

    public String getClubes() { return clubes; }
    public void setClubes(String clubes) { this.clubes = clubes; }

    public String getCompras() { return compras; }
    public void setCompras(String compras) { this.compras = compras; }

    public String getAutoresFavoritos() { return autoresFavoritos; }
    public void setAutoresFavoritos(String autoresFavoritos) { this.autoresFavoritos = autoresFavoritos; }

    public String getBoletines() { return boletines; }
    public void setBoletines(String boletines) { this.boletines = boletines; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}