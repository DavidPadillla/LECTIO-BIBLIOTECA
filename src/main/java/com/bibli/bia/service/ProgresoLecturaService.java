package com.bibli.bia.service;

import com.bibli.bia.Model.ProgresoLectura;
import com.bibli.bia.Model.Usuario;
import com.bibli.bia.repository.ProgresoLecturaRepository;
import com.bibli.bia.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProgresoLecturaService {

    private final ProgresoLecturaRepository progresoRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public ProgresoLecturaService(ProgresoLecturaRepository progresoRepository, UsuarioRepository usuarioRepository) {
        this.progresoRepository = progresoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ProgresoLectura obtenerProgreso(String username) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));


        Optional<ProgresoLectura> progreso = progresoRepository.findByUsuario(usuario);

        if (progreso.isPresent()) {
            return progreso.get();
        } else {
            ProgresoLectura nuevoProgreso = new ProgresoLectura(usuario);
            ProgresoLectura saved = progresoRepository.save(nuevoProgreso);
            return saved;
        }
    }

    public ProgresoLectura guardarCapitulo(String username, String libroId, int capitulo, int totalCapitulos) {
        ProgresoLectura progreso = obtenerProgreso(username);


        Map<String, Set<Integer>> capitulosPorLibro = progreso.getCapitulosPorLibro();

        if (capitulosPorLibro == null) {
            capitulosPorLibro = new HashMap<>();
        }

        if (!capitulosPorLibro.containsKey(libroId)) {
            capitulosPorLibro.put(libroId, new HashSet<>());
        }

        capitulosPorLibro.get(libroId).add(capitulo);
        progreso.setCapitulosPorLibro(capitulosPorLibro);


        if (capitulosPorLibro.get(libroId).size() == totalCapitulos) {
            Set<String> librosCompletados = progreso.getLibrosCompletados();
            if (librosCompletados == null) {
                librosCompletados = new HashSet<>();
            }

            boolean esNuevo = librosCompletados.add(libroId);
            if (esNuevo) {
                progreso.setLibrosCompletados(librosCompletados);
                progreso.setTotalLibrosLeidos(progreso.getTotalLibrosLeidos() + 1);
                progreso.setPuntos(progreso.getPuntos() + 100);
            }
        }

        progreso.setUltimaActualizacion(LocalDateTime.now());

        return progresoRepository.save(progreso);
    }

    public ProgresoLectura marcarLibroCompletado(String username, String libroId) {
        ProgresoLectura progreso = obtenerProgreso(username);

        Set<String> librosCompletados = progreso.getLibrosCompletados();
        if (librosCompletados == null) {
            librosCompletados = new HashSet<>();
        }

        boolean esNuevo = librosCompletados.add(libroId);

        if (esNuevo) {
            progreso.setLibrosCompletados(librosCompletados);
            progreso.setTotalLibrosLeidos(progreso.getTotalLibrosLeidos() + 1);
            progreso.setPuntos(progreso.getPuntos() + 100);
        }

        progreso.setUltimaActualizacion(LocalDateTime.now());

        return progresoRepository.save(progreso);
    }

    public boolean haCompletadoLibro(String username, String libroId) {
        ProgresoLectura progreso = obtenerProgreso(username);
        Set<String> librosCompletados = progreso.getLibrosCompletados();
        return librosCompletados != null && librosCompletados.contains(libroId);
    }

    public void reiniciarProgreso(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        Optional<ProgresoLectura> progreso = progresoRepository.findByUsuario(usuario);
        if (progreso.isPresent()) {
            progresoRepository.delete(progreso.get());
            System.out.println("✅ Progreso eliminado de PostgreSQL");
        }
    }
}