package com.bibli.bia.postgres.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "multas")
public class MultaPostgres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_usuario")
    private String idUsuario;

    @Column(name = "nombre_usuario")
    private String nombreUsuario;

    @Column(name = "libro")
    private String libro;  // ← Mantenemos el campo original

    @Column(name = "id_libro")
    private String idLibro;

    @Column(name = "libro_titulo")
    private String libroTitulo;

    @Column(name = "fecha_reserva")
    private LocalDate fechaReserva;

    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;

    @Column(name = "dias_retraso")
    private int diasRetraso;

    @Column(name = "valor_multa")
    private double valorMulta;

    @Column(name = "pagada")
    private boolean pagada = false;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    // Constructor original (sin idLibro) - para mantener compatibilidad
    public MultaPostgres(String idUsuario, String nombreUsuario, String libro,
                         LocalDate fechaReserva, LocalDate fechaDevolucion,
                         int diasRetraso, double valorMulta) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.libro = libro;
        this.fechaReserva = fechaReserva;
        this.fechaDevolucion = fechaDevolucion;
        this.diasRetraso = diasRetraso;
        this.valorMulta = valorMulta;
        this.pagada = false;
        this.fechaPago = null;
    }

    // Constructor con idLibro
    public MultaPostgres(String idUsuario, String nombreUsuario, String libro, String idLibro, String libroTitulo,
                         LocalDate fechaReserva, LocalDate fechaDevolucion,
                         int diasRetraso, double valorMulta) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.libro = libro;
        this.idLibro = idLibro;
        this.libroTitulo = libroTitulo;
        this.fechaReserva = fechaReserva;
        this.fechaDevolucion = fechaDevolucion;
        this.diasRetraso = diasRetraso;
        this.valorMulta = valorMulta;
        this.pagada = false;
        this.fechaPago = null;
    }
}