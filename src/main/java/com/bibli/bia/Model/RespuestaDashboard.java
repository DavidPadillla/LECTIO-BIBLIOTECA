package com.bibli.bia.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "respuestas_dashboard")
public class RespuestaDashboard {

    @Id
    private String id;
    private Integer edad;
    private String genero;
    private String educacion;
    private String frecuencia;

    private String categoriaFavorita;

    private String formato;
    private String uso;

    private Integer librosMes;

    private Integer calificacion;
    private String recomendacion;
    private String dispositivos;

    private String ultimoLibro;

    private String mejoras;
    private String recomendaciones;
    private String clubes;
    private String compras;

    private String autoresFavoritos;

    private String boletines;

    private LocalDateTime fechaRegistro = LocalDateTime.now();



    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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
