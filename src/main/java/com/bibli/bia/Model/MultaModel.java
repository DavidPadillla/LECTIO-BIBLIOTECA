package com.bibli.bia.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "multas", indexes = {
        @Index(name = "idx_usuario_id", columnList = "usuario_id"),
        @Index(name = "idx_pagada", columnList = "pagada")
})
public class MultaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String nombreUsuario;

    @Column(nullable = false, length = 200)
    private String libro;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDate fechaReserva;

    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;

    @Column(name = "dias_retraso")
    private int diasRetraso;

    @Column(name = "valor_multa", nullable = false)
    private double valorMulta;

    @Column(nullable = false)
    private boolean pagada = false;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    public MultaModel(Usuario usuario, String nombreUsuario, String libro,
                      LocalDate fechaReserva, LocalDate fechaDevolucion,
                      int diasRetraso, double valorMulta) {
        this.usuario = usuario;
        this.nombreUsuario = nombreUsuario;
        this.libro = libro;
        this.fechaReserva = fechaReserva;
        this.fechaDevolucion = fechaDevolucion;
        this.diasRetraso = diasRetraso;
        this.valorMulta = valorMulta;
        this.pagada = false;
        this.fechaPago = null;
    }
}