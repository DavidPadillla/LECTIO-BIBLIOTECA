package com.bibli.bia.Model;

import jakarta.validation.constraints.Min;

public class SolicitudOptimizacion {

    @Min(value = 1, message = "Ingrese al menos 1 día")
    private int diasDisponibles;

    @Min(value = 1, message = "Ingrese horas válidas")
    private int horasLecturaDiaria;

    @Min(value = 1, message = "Debe seleccionar mínimo 1 libro")
    private int maxLibros;

    private String categoriaPreferida;

    public SolicitudOptimizacion() {
    }

    public int getDiasDisponibles() {
        return diasDisponibles;
    }

    public void setDiasDisponibles(int diasDisponibles) {
        this.diasDisponibles = diasDisponibles;
    }

    public int getHorasLecturaDiaria() {
        return horasLecturaDiaria;
    }

    public void setHorasLecturaDiaria(int horasLecturaDiaria) {
        this.horasLecturaDiaria = horasLecturaDiaria;
    }

    public int getMaxLibros() {
        return maxLibros;
    }

    public void setMaxLibros(int maxLibros) {
        this.maxLibros = maxLibros;
    }

    public String getCategoriaPreferida() {
        return categoriaPreferida;
    }

    public void setCategoriaPreferida(String categoriaPreferida) {
        this.categoriaPreferida = categoriaPreferida;
    }
}