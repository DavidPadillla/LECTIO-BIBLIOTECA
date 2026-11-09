package com.bibli.bia.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas", indexes = {
        @Index(name = "idx_usuario_id", columnList = "usuario_id"),
        @Index(name = "idx_fecha", columnList = "fecha"),
        @Index(name = "idx_libro", columnList = "libro"),
        @Index(name = "idx_reserva_libro_fisico", columnList = "libro_fisico_id"),
        @Index(name = "idx_reserva_estado", columnList = "estado")
})
public class ReservaModel {

    public enum EstadoReserva { ACTIVA, DEVUELTA, CANCELADA }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Llave foránea real al libro físico (nullable para no romper filas antiguas). */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "libro_fisico_id", foreignKey = @ForeignKey(name = "fk_reserva_libro_fisico"))
    private LibroFisicoModel libroFisico;

    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;

    @Column(nullable = false, length = 100)
    private String correo;

    /** Copia de la categoría del libro (se sincroniza al asignar libroFisico). */
    @Column(nullable = false, length = 50)
    private String categoria;

    /** Título del libro al momento de reservar (se mantiene por compatibilidad). */
    @Column(nullable = false, length = 200)
    private String libro;

    /** Fecha que el usuario elige para la reserva (fecha de retiro). */
    @Column(nullable = false)
    private LocalDate fecha;

    /** Momento real en que se hizo la reserva. Usar ESTA columna para la demanda. */
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /** Ciclo de vida de la reserva (ya no se borra al devolver). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoReserva estado = EstadoReserva.ACTIVA;

    /** Fecha en que se devolvió el libro. */
    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;

    public ReservaModel() {}

    // Constructor original (se mantiene para no romper WebController)
    public ReservaModel(Usuario usuario, String nombreCompleto, String correo,
                        String categoria, String libro, LocalDate fecha) {
        this.usuario = usuario;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.categoria = categoria;
        this.libro = libro;
        this.fecha = fecha;
    }

    // Constructor recomendado. Toma título y categoría directamente del libro.
    public ReservaModel(Usuario usuario, String nombreCompleto, String correo,
                        LibroFisicoModel libroFisico, LocalDate fecha) {
        this.usuario = usuario;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.fecha = fecha;
        setLibroFisico(libroFisico);
    }

    @PrePersist
    void antesDeGuardar() {
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        if (estado == null) estado = EstadoReserva.ACTIVA;
        libro = limpiar(libro);
        categoria = limpiar(categoria);
    }

    private static String limpiar(String s) {
        return s == null ? null : s.trim().replaceAll("\\s+", " ");
    }

    public void marcarDevuelta(LocalDate fechaDevolucion) {
        this.estado = EstadoReserva.DEVUELTA;
        this.fechaDevolucion = fechaDevolucion;
    }

    public void cancelar() {
        this.estado = EstadoReserva.CANCELADA;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LibroFisicoModel getLibroFisico() { return libroFisico; }

    /** Al asignar el libro se sincronizan título y categoría para que nunca se desfasen. */
    public void setLibroFisico(LibroFisicoModel libroFisico) {
        this.libroFisico = libroFisico;
        if (libroFisico != null) {
            this.libro = libroFisico.getTitulo();
            this.categoria = libroFisico.getCategoria();
        }
    }

    /**
     * CORREGIDO: antes se llamaba getLibroFisicoId(), lo que hacía que Spring Data
     * interpretara "libroFisicoId" como una propiedad real y no como libroFisico.id,
     * provocando PathElementException al arrancar la app.
     *
     * Ahora se llama getLibroFisicoRef() para no colisionar con la convención de
     * Spring Data, y @JsonProperty conserva la clave "libroFisicoId" en el JSON.
     */
    @JsonProperty("libroFisicoId")
    public String getLibroFisicoRef() {
        return libroFisico != null ? libroFisico.getId() : null;
    }



    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getLibro() { return libro; }
    public void setLibro(String libro) { this.libro = libro; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }

    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
}