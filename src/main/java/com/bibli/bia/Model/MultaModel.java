package com.bibli.bia.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Document(collection = "multas")
public class MultaModel {

    @Id
    private String id;

    private String idUsuario;
    private String nombreUsuario;
    private String libro;
    private LocalDate fechaReserva;
    private LocalDate fechaDevolucion;
    private int diasRetraso;
    private double valorMulta;
    private boolean pagada;
    private LocalDate fechaPago;

    public MultaModel(String idUsuario, String nombreUsuario, String libro,
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
}