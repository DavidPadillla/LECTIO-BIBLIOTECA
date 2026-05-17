package com.bibli.bia.Model;

import java.util.List;

public class ResultadoOptimizacion {

    private List<LibroAsignado> librosSeleccionados;

    private double horasTotalesUtilizadas;
    private double horasTotalesDisponibles;

    private int tiempoDisponible;

    private double valorObjetivo;

    private int maxLibros;

    private String categoriaPreferida;

    private double porcentajeCategoria;

    public ResultadoOptimizacion() {
    }

    public ResultadoOptimizacion(
            List<LibroAsignado> librosSeleccionados,
            double horasTotalesUtilizadas,
            double horasTotalesDisponibles,
            int tiempoDisponible,
            double valorObjetivo,
            int maxLibros,
            String categoriaPreferida
    ) {

        this.librosSeleccionados = librosSeleccionados;
        this.horasTotalesUtilizadas = horasTotalesUtilizadas;
        this.horasTotalesDisponibles = horasTotalesDisponibles;
        this.tiempoDisponible = tiempoDisponible;
        this.valorObjetivo = valorObjetivo;
        this.maxLibros = maxLibros;
        this.categoriaPreferida = categoriaPreferida;

        calcularPorcentajeCategoria();
    }

    // =========================
    // GETTERS Y SETTERS
    // =========================

    public List<LibroAsignado> getLibrosSeleccionados() {
        return librosSeleccionados;
    }

    public void setLibrosSeleccionados(List<LibroAsignado> librosSeleccionados) {
        this.librosSeleccionados = librosSeleccionados;
    }

    public double getHorasTotalesUtilizadas() {
        return horasTotalesUtilizadas;
    }

    public void setHorasTotalesUtilizadas(double horasTotalesUtilizadas) {
        this.horasTotalesUtilizadas = horasTotalesUtilizadas;
    }

    public double getHorasTotalesDisponibles() {
        return horasTotalesDisponibles;
    }

    public void setHorasTotalesDisponibles(double horasTotalesDisponibles) {
        this.horasTotalesDisponibles = horasTotalesDisponibles;
    }

    public int getTiempoDisponible() {
        return tiempoDisponible;
    }

    public void setTiempoDisponible(int tiempoDisponible) {
        this.tiempoDisponible = tiempoDisponible;
    }

    public double getValorObjetivo() {
        return valorObjetivo;
    }

    public void setValorObjetivo(double valorObjetivo) {
        this.valorObjetivo = valorObjetivo;
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

    public double getPorcentajeCategoria() {
        return porcentajeCategoria;
    }

    public void setPorcentajeCategoria(double porcentajeCategoria) {
        this.porcentajeCategoria = porcentajeCategoria;
    }

    // =========================
    // MÉTODO AUXILIAR
    // =========================

    private void calcularPorcentajeCategoria() {

        if (categoriaPreferida == null ||
                categoriaPreferida.isBlank() ||
                librosSeleccionados == null ||
                librosSeleccionados.isEmpty()) {

            porcentajeCategoria = 0;
            return;
        }

        long cantidad = librosSeleccionados.stream()
                .filter(l ->
                        l.getLibro().getCategoria()
                                .equalsIgnoreCase(categoriaPreferida))
                .count();

        porcentajeCategoria =
                ((double) cantidad / librosSeleccionados.size()) * 100.0;
    }

    // =====================================================
    // CLASE INTERNA
    // =====================================================

    public static class LibroAsignado {

        private LibroOptimizador libro;

        private double horasAsignadas;

        private boolean terminaLibro;

        private double porcentajeAvance;

        public LibroAsignado() {
        }

        public LibroAsignado(
                LibroOptimizador libro,
                double horasAsignadas
        ) {

            this.libro = libro;
            this.horasAsignadas = horasAsignadas;

            this.terminaLibro =
                    horasAsignadas >= libro.getHorasLectura();

            this.porcentajeAvance =
                    (horasAsignadas / libro.getHorasLectura()) * 100.0;
        }

        public LibroOptimizador getLibro() {
            return libro;
        }

        public void setLibro(LibroOptimizador libro) {
            this.libro = libro;
        }

        public double getHorasAsignadas() {
            return horasAsignadas;
        }

        public void setHorasAsignadas(double horasAsignadas) {
            this.horasAsignadas = horasAsignadas;
        }

        public boolean isTerminaLibro() {
            return terminaLibro;
        }

        public void setTerminaLibro(boolean terminaLibro) {
            this.terminaLibro = terminaLibro;
        }

        public double getPorcentajeAvance() {
            return porcentajeAvance;
        }

        public void setPorcentajeAvance(double porcentajeAvance) {
            this.porcentajeAvance = porcentajeAvance;
        }
    }
}