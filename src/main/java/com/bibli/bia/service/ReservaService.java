package com.bibli.bia.service;

import com.bibli.bia.Model.ReservaModel;
import com.bibli.bia.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.core.Instances;

import java.io.*;
import java.util.*;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public long contarReservas() {
        return reservaRepository.count();
    }

    public ReservaModel crearReserva(ReservaModel reserva) {
        ReservaModel saved = reservaRepository.save(reserva);

        return saved;
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
            System.out.println("⚠️ Reserva eliminada de MongoDB. Neon no se actualiza automáticamente.");
        } else {
            throw new IllegalArgumentException("Reserva no encontrada");
        }
    }

    public String obtenerCategoriaMasReservada() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/reservas.arff");
        if (inputStream == null) {
            throw new RuntimeException("No se encontró reservas.arff en resources");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Instances data = new Instances(reader);
        reader.close();

        HashMap<String, Integer> categorias = new HashMap<>();
        for (int i = 0; i < data.numInstances(); i++) {
            String categoria = data.instance(i).stringValue(0);
            categorias.put(categoria, categorias.getOrDefault(categoria, 0) + 1);
        }

        return Collections.max(categorias.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public Map<String, Integer> obtenerEstadisticasCategorias() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/reservas.arff");
        if (inputStream == null) {
            throw new RuntimeException("No se encontró reservas.arff en resources");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Instances data = new Instances(reader);
        reader.close();

        HashMap<String, Integer> categorias = new HashMap<>();
        for (int i = 0; i < data.numInstances(); i++) {
            String categoria = data.instance(i).stringValue(0);
            categorias.put(categoria, categorias.getOrDefault(categoria, 0) + 1);
        }
        return categorias;
    }
}