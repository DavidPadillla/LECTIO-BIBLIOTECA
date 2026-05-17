package com.bibli.bia.service;


import com.bibli.bia.Model.LibroOptimizador;
import com.bibli.bia.Model.ResultadoOptimizacion;
import com.bibli.bia.Model.SolicitudOptimizacion;
import com.bibli.bia.repository.LibroOptimizadorRepository;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OptimizadorService {

    // ── Parámetros del modelo ─────────────────────────────
    private static final double BONUS_CATEGORIA  = 0.4;
    private static final double ALPHA_DIFICULTAD = 0.15;
    private static final double BONUS_TERMINAR   = 5.0;
    private static final double GAMMA_USO_MINIMO = 0.40;
    private static final double DELTA_MIN_LIBRO  = 0.20;

    @Autowired
    private LibroOptimizadorRepository libroRepository;

    public ResultadoOptimizacion optimizar(SolicitudOptimizacion solicitud) {

        // ── Obtener libros disponibles ────────────────────
        List<LibroOptimizador> libros = libroRepository.findByDisponibleTrue();

        int n = libros.size();

        // TOTAL HORAS DISPONIBLES
        double T_h =
                solicitud.getDiasDisponibles()
                        * solicitud.getHorasLecturaDiaria();

        int M = solicitud.getMaxLibros();

        String catPref =
                solicitud.getCategoriaPreferida() == null
                        ? ""
                        : solicitud.getCategoriaPreferida().trim();

        boolean hayPref = !catPref.isBlank();

        // ── Modelo ────────────────────────────────────────
        ExpressionsBasedModel model =
                new ExpressionsBasedModel();

        Variable[] x = new Variable[n];
        Variable[] h = new Variable[n];
        Variable[] y = new Variable[n];

        // ───────────────── VARIABLES ──────────────────────
        for (int j = 0; j < n; j++) {

            LibroOptimizador libro = libros.get(j);

            double Hj = libro.getHorasLectura();

            double wj =
                    1.0
                            + (
                            hayPref &&
                                    catPref.equalsIgnoreCase(
                                            libro.getCategoria()
                                    )
                                    ? BONUS_CATEGORIA
                                    : 0.0
                    )
                            - ALPHA_DIFICULTAD
                            * libro.getDificultad();

            // xj → selección
            x[j] = model.newVariable("x" + j)
                    .binary()
                    .weight(0.0);

            // hj → horas asignadas
            h[j] = model.newVariable("h" + j)
                    .lower(0.0)
                    .upper(Hj)
                    .weight(wj);

            // yj → termina libro
            y[j] = model.newVariable("y" + j)
                    .binary()
                    .weight(BONUS_TERMINAR);
        }

        // ───────────────── R1 ─────────────────────────────
        // ∑ hj ≤ T_h

        var r1 = model.newExpression("R1")
                .upper(T_h);

        for (int j = 0; j < n; j++) {
            r1.set(h[j], 1.0);
        }

        // ───────────────── R2 ─────────────────────────────
        // hj ≤ Hj * xj

        for (int j = 0; j < n; j++) {

            double Hj = libros.get(j)
                    .getHorasLectura();

            var r2 = model.newExpression("R2_" + j)
                    .upper(0.0);

            r2.set(h[j], 1.0);
            r2.set(x[j], -Hj);
        }

        // ───────────────── R3 ─────────────────────────────
        // ∑ xj ≤ M

        var r3 = model.newExpression("R3")
                .upper(M);

        for (int j = 0; j < n; j++) {
            r3.set(x[j], 1.0);
        }

        // ───────────────── R4 ─────────────────────────────
        // Al menos 1 libro categoría preferida

        if (hayPref) {

            boolean existeCategoria =
                    libros.stream()
                            .anyMatch(l ->
                                    catPref.equalsIgnoreCase(
                                            l.getCategoria()
                                    )
                            );

            if (existeCategoria) {

                var r4 = model.newExpression("R4")
                        .lower(1.0);

                for (int j = 0; j < n; j++) {

                    if (
                            catPref.equalsIgnoreCase(
                                    libros.get(j)
                                            .getCategoria()
                            )
                    ) {
                        r4.set(x[j], 1.0);
                    }
                }
            }
        }

        // ───────────────── R5 ─────────────────────────────
        // Hj*yj - hj ≤ 0

        for (int j = 0; j < n; j++) {

            double Hj = libros.get(j)
                    .getHorasLectura();

            var r5 = model.newExpression("R5_" + j)
                    .upper(0.0);

            r5.set(y[j], Hj);
            r5.set(h[j], -1.0);
        }

        // ───────────────── R6 ─────────────────────────────
        // yj ≤ xj

        for (int j = 0; j < n; j++) {

            var r6 = model.newExpression("R6_" + j)
                    .upper(0.0);

            r6.set(y[j], 1.0);
            r6.set(x[j], -1.0);
        }

        // ───────────────── R7 ─────────────────────────────
        // ∑ hj ≥ 40% del tiempo

        var r7 = model.newExpression("R7")
                .lower(GAMMA_USO_MINIMO * T_h);

        for (int j = 0; j < n; j++) {
            r7.set(h[j], 1.0);
        }

        // ───────────────── R8 ─────────────────────────────
        // hj ≥ 20% Hj si seleccionado

        for (int j = 0; j < n; j++) {

            double Hj = libros.get(j)
                    .getHorasLectura();

            var r8 = model.newExpression("R8_" + j)
                    .upper(0.0);

            r8.set(x[j],
                    DELTA_MIN_LIBRO * Hj);

            r8.set(h[j], -1.0);
        }

        // ───────────────── SOLVER ─────────────────────────
        Optimisation.Result resultado =
                model.maximise();

        System.out.println("══════════════════════════════");
        System.out.println("Estado: " +
                resultado.getState());

        System.out.println("Objetivo Z: " +
                resultado.getValue());

        System.out.println("══════════════════════════════");

        // ───────────────── RESULTADOS ─────────────────────
        List<ResultadoOptimizacion.LibroAsignado>
                seleccionados = new ArrayList<>();

        double horasUsadas = 0.0;

        for (int j = 0; j < n; j++) {

            double xVal =
                    x[j].getValue() != null
                            ? x[j].getValue()
                              .doubleValue()
                            : 0.0;

            double hVal =
                    h[j].getValue() != null
                            ? h[j].getValue()
                              .doubleValue()
                            : 0.0;

            System.out.printf(
                    "[%s] x=%.1f h=%.2f%n",
                    libros.get(j).getTitulo(),
                    xVal,
                    hVal
            );

            if (xVal > 0.5) {

                double horasRed =
                        Math.round(hVal * 10.0)
                                / 10.0;

                seleccionados.add(
                        new ResultadoOptimizacion
                                .LibroAsignado(
                                libros.get(j),
                                horasRed
                        )
                );

                horasUsadas += hVal;
            }
        }

        double horasFinales =
                Math.round(horasUsadas * 10.0)
                        / 10.0;

        double zFinal =
                Math.round(resultado.getValue()
                        * 10.0) / 10.0;

        return new ResultadoOptimizacion(
                seleccionados,
                horasFinales,
                T_h,
                solicitud.getDiasDisponibles(),
                zFinal,
                M,
                catPref
        );
    }
}