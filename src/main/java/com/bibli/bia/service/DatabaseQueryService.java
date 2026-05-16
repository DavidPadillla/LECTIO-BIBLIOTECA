package com.bibli.bia.service;

import com.bibli.bia.Model.*;
import com.bibli.bia.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseQueryService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private LibroFisicoRepository libroFisicoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private MultaRepository multaRepository;

    @Autowired
    private ProgresoLecturaRepository progresoLecturaRepository;

    // Obtener información del usuario actual
    public String getUsuarioInfo(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);
        if (usuario == null) return "Usuario no encontrado";
        return "Usuario: " + usuario.getUsername() + ", Roles: " + usuario.getRoles();
    }

    // Obtener libros virtuales por categoría
    public String getLibrosVirtualesPorCategoria(String categoria) {
        List<LibroModel> libros = libroRepository.findByCategoriaIgnoreCase(categoria);
        if (libros.isEmpty()) return "No hay libros virtuales en la categoría '" + categoria + "'";
        return "Libros virtuales de " + categoria + ": " +
                libros.stream().map(LibroModel::getTitulo).collect(Collectors.joining(", "));
    }

    // Obtener libros físicos disponibles (con stock > 0)
    public String getLibrosFisicosDisponibles() {
        List<LibroFisicoModel> libros = libroFisicoRepository.findAll().stream()
                .filter(l -> l.getStock() > 0)
                .limit(10)
                .collect(Collectors.toList());
        if (libros.isEmpty()) return "No hay libros físicos disponibles actualmente";
        return "Libros físicos disponibles: " +
                libros.stream().map(l -> l.getTitulo() + " (" + l.getStock() + " unidades)")
                        .collect(Collectors.joining(", "));
    }

    // Obtener reservas del usuario actual
    public String getMisReservas(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);
        if (usuario == null) return "Usuario no encontrado";
        List<ReservaModel> reservas = reservaRepository.findByUsuario(usuario);
        if (reservas.isEmpty()) return "No tienes reservas activas";
        return "Tus reservas: " +
                reservas.stream().map(r -> r.getLibro() + " (fecha: " + r.getFecha() + ")")
                        .collect(Collectors.joining(", "));
    }

    // Obtener multas del usuario actual (no pagadas)
    public String getMisMultas(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);
        if (usuario == null) return "Usuario no encontrado";
        List<MultaModel> multas = multaRepository.findByUsuarioAndPagada(usuario, false);
        if (multas.isEmpty()) return "No tienes multas pendientes";
        double total = multas.stream().mapToDouble(MultaModel::getValorMulta).sum();
        return "Tienes " + multas.size() + " multa(s) pendiente(s). Total: $" + total + " COP. " +
                multas.stream().map(m -> m.getLibro() + " ($" + m.getValorMulta() + ")")
                        .collect(Collectors.joining(", "));
    }

    // Obtener progreso de lectura del usuario
    public String getMiProgreso(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);
        if (usuario == null) return "Usuario no encontrado";
        var progreso = progresoLecturaRepository.findByUsuario(usuario);
        if (progreso.isEmpty()) return "Aún no has comenzado a leer ningún libro";
        ProgresoLectura p = progreso.get();
        return "Has leído " + p.getTotalLibrosLeidos() + " libro(s) completamente. Tienes " + p.getPuntos() + " puntos acumulados.";
    }

    // Buscar libros por título
    public String buscarLibro(String titulo) {
        List<LibroModel> virtuales = libroRepository.findByTituloContainingIgnoreCase(titulo);
        List<LibroFisicoModel> fisicos = libroFisicoRepository.findByTituloContainingIgnoreCase(titulo);

        StringBuilder resultado = new StringBuilder();
        if (!virtuales.isEmpty()) {
            resultado.append("Libros virtuales: ").append(virtuales.stream().map(LibroModel::getTitulo).collect(Collectors.joining(", ")));
        }
        if (!fisicos.isEmpty()) {
            if (resultado.length() > 0) resultado.append(" | ");
            resultado.append("Libros físicos: ").append(fisicos.stream().map(LibroFisicoModel::getTitulo).collect(Collectors.joining(", ")));
        }
        if (resultado.length() == 0) return "No se encontraron libros con '" + titulo + "'";
        return resultado.toString();
    }
}