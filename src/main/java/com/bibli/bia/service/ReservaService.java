package com.bibli.bia.service;

import com.bibli.bia.Model.ReservaModel;
import com.bibli.bia.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public long contarReservas() {
        return reservaRepository.count();
    }

    public ReservaModel crearReserva(ReservaModel reserva) {
        return reservaRepository.save(reserva);
    }

    public ReservaModel obtenerReservaPorId(String id) {
        return reservaRepository.findById(id).orElse(null);
    }

    public List<ReservaModel> obtenerTodasReservas() {
        return reservaRepository.findAll();
    }

    public void eliminarReserva(String id) {
        Optional<ReservaModel> reserva = reservaRepository.findById(id);
        if (reserva.isPresent()) {
            reservaRepository.deleteById(id);
            System.out.println("✅ Reserva eliminada de PostgreSQL");
        } else {
            throw new IllegalArgumentException("Reserva no encontrada");
        }
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
}