package com.bibli.bia.service;

import com.bibli.bia.Model.LibroFisicoModel;
import com.bibli.bia.repository.LibroFisicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LibroFisicoService {

    @Autowired
    private LibroFisicoRepository libroFisicoRepository;

    public LibroFisicoModel guardarLibroFisico(LibroFisicoModel libroFisico) {
        if (libroFisico.getStock() < 0) libroFisico.setStock(0);
        if (libroFisico.getReservado() < 0) libroFisico.setReservado(0);
        return libroFisicoRepository.save(libroFisico);
    }

    public List<LibroFisicoModel> obtenerTodosLosLibrosFisicos() {
        return libroFisicoRepository.findAll();
    }

    public Optional<LibroFisicoModel> obtenerLibroFisicoPorId(String id) {
        return libroFisicoRepository.findById(id);
    }

    public List<LibroFisicoModel> obtenerLibrosFisicosPorCategoria(String categoria) {
        return libroFisicoRepository.findByCategoriaIgnoreCase(categoria);
    }

    @Transactional
    public LibroFisicoModel obtenerParaActualizar(String id) {
        return libroFisicoRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new IllegalArgumentException("Libro físico no encontrado"));
    }

    @Transactional
    public void descontarDisponible(LibroFisicoModel libro, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        if (libro.getStock() < cantidad) {
            throw new IllegalStateException("No hay stock suficiente para completar la reserva");
        }
        libro.setStock(libro.getStock() - cantidad);
        libro.setReservado(libro.getReservado() + cantidad);
        libroFisicoRepository.save(libro);
    }

    @Transactional
    public void devolverAStock(LibroFisicoModel libro, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        if (libro.getReservado() < cantidad) {
            throw new IllegalStateException("El inventario reservado no permite liberar esa cantidad");
        }
        libro.setReservado(libro.getReservado() - cantidad);
        libro.setStock(libro.getStock() + cantidad);
        libroFisicoRepository.save(libro);
    }

    public long contarLibrosFisicos() {
        return libroFisicoRepository.count();
    }
}
