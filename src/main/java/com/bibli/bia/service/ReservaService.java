package com.bibli.bia.service;

import com.bibli.bia.Model.LibroFisicoModel;
import com.bibli.bia.Model.ReservaModel;
import com.bibli.bia.Model.ReservaModel.EstadoReserva;
import com.bibli.bia.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private static final int CANTIDAD_POR_RESERVA = 1;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private LibroFisicoService libroFisicoService;

    public long contarReservas() {
        return reservaRepository.count();
    }

    @Transactional
    public ReservaModel crearReserva(ReservaModel reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva es obligatoria");
        }
        if (reserva.getLibroFisico() == null || reserva.getLibroFisico().getId() == null
                || reserva.getLibroFisico().getId().isBlank()) {
            throw new IllegalArgumentException("La reserva debe estar asociada a un libro físico");
        }
        if (reserva.getUsuario() == null) {
            throw new IllegalArgumentException("La reserva debe tener un usuario");
        }

        LibroFisicoModel libro = libroFisicoService.obtenerParaActualizar(reserva.getLibroFisico().getId());

        if (reservaRepository.existsByUsuarioIdAndLibroFisicoIdAndEstado(
                reserva.getUsuario().getId(), libro.getId(), EstadoReserva.ACTIVA)) {
            throw new IllegalStateException("Ya existe una reserva activa de este libro para el usuario");
        }

        libroFisicoService.descontarDisponible(libro, CANTIDAD_POR_RESERVA);
        reserva.setLibroFisico(libro);
        return reservaRepository.save(reserva);
    }

    public ReservaModel obtenerReservaPorId(String id) {
        return reservaRepository.findById(id).orElse(null);
    }

    public List<ReservaModel> obtenerTodasReservas() {
        return reservaRepository.findAll();
    }

    public List<ReservaModel> obtenerReservasPorEstado(EstadoReserva estado) {
        return reservaRepository.findByEstado(estado);
    }

    public List<ReservaModel> obtenerReservasActivas() {
        return reservaRepository.findByEstado(EstadoReserva.ACTIVA);
    }

    @Transactional
    public ReservaModel cancelarReserva(String id) {
        ReservaModel reserva = reservaRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        if (reserva.getEstado() != EstadoReserva.ACTIVA) {
            throw new IllegalStateException("Solo se pueden cancelar reservas activas");
        }

        liberarInventario(reserva);
        reserva.cancelar();
        return reservaRepository.save(reserva);
    }

    @Transactional
    public ReservaModel devolverReserva(String id, LocalDate fechaDevolucion) {
        ReservaModel reserva = reservaRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        if (reserva.getEstado() != EstadoReserva.ACTIVA) {
            throw new IllegalStateException("Solo se pueden devolver reservas activas");
        }

        liberarInventario(reserva);
        reserva.marcarDevuelta(fechaDevolucion != null ? fechaDevolucion : LocalDate.now());
        return reservaRepository.save(reserva);
    }

    private void liberarInventario(ReservaModel reserva) {
        if (reserva.getLibroFisico() == null) {
            return;
        }
        LibroFisicoModel libro = libroFisicoService.obtenerParaActualizar(reserva.getLibroFisico().getId());
        libroFisicoService.devolverAStock(libro, CANTIDAD_POR_RESERVA);
    }

    public String obtenerCategoriaMasReservada() {
        List<ReservaModel> reservas = reservaRepository.findAll();

        if (reservas.isEmpty()) {
            return "No hay reservas registradas";
        }

        Map<String, Long> categorias = reservas.stream()
                .filter(r -> r.getCategoria() != null && !r.getCategoria().isEmpty())
                .collect(Collectors.groupingBy(
                        ReservaModel::getCategoria,
                        Collectors.counting()
                ));

        if (categorias.isEmpty()) {
            return "No hay categorías registradas";
        }

        return Collections.max(categorias.entrySet(), Map.Entry.comparingByValue()).getKey();
    }


    public Map<String, Integer> obtenerEstadisticasCategorias() {
        List<ReservaModel> reservas = reservaRepository.findAll();

        Map<String, Integer> estadisticas = new HashMap<>();

        for (ReservaModel reserva : reservas) {
            String categoria = reserva.getCategoria();
            if (categoria != null && !categoria.isEmpty()) {
                estadisticas.put(categoria, estadisticas.getOrDefault(categoria, 0) + 1);
            }
        }


        return estadisticas.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Map<EstadoReserva, Long> obtenerConteoPorEstado() {
        Map<EstadoReserva, Long> conteo = new EnumMap<>(EstadoReserva.class);
        for (EstadoReserva estado : EstadoReserva.values()) {
            conteo.put(estado, reservaRepository.countByEstado(estado));
        }
        return conteo;
    }

    public List<Object[]> obtenerDemandaPorLibroYDia(LocalDateTime desde, LocalDateTime hasta) {
        return reservaRepository.demandaPorLibroYDia(desde, hasta);
    }

    public List<Object[]> obtenerDevolucionesPorLibroYDia(LocalDate desde, LocalDate hasta) {
        return reservaRepository.devolucionesPorLibroYDia(desde, hasta);
    }
}
