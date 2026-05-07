package com.bibli.bia.Model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Document(collection = "reservas")
@Data
@NoArgsConstructor
public class ReservaModel {

    @Id
    private String id;

    private String idUsuario;
    private String nombreCompleto;
    private String correo;
    private String categoria;
    private String libro;
    private LocalDate fecha;

    public ReservaModel(String idUsuario, String nombreCompleto, String correo, String categoria, String libro, LocalDate fecha) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.categoria = categoria;
        this.libro = libro;
        this.fecha = fecha;
    }
}
