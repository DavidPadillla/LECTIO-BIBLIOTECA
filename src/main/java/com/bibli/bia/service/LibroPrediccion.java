package com.bibli.bia.service; // o model, según donde lo pongas

public class LibroPrediccion {
    private String libroId;
    private double prediccion;

    public LibroPrediccion(String libroId, double prediccion) {
        this.libroId = libroId;
        this.prediccion = prediccion;
    }

    public String getLibroId() { return libroId; }
    public double getPrediccion() { return prediccion; }
}
