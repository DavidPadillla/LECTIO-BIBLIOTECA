package com.bibli.bia.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservas", indexes = {
        @Index(name = "idx_usuario_id", columnList = "usuario_id"),
        @Index(name = "idx_fecha", columnList = "fecha"),
        @Index(name = "idx_libro", columnList = "libro")
})
public class ReservaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;

    @Column(nullable = false, length = 100)
    private String correo;

    @Column(nullable = false, length = 50)
    private String categoria;

    @Column(nullable = false, length = 200)
    private String libro;

    @Column(nullable = false)
    private LocalDate fecha;


    public ReservaModel() {}

    public ReservaModel(Usuario usuario, String nombreCompleto, String correo, String categoria, String libro, LocalDate fecha) {
        this.usuario = usuario;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.categoria = categoria;
        this.libro = libro;
        this.fecha = fecha;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

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
}