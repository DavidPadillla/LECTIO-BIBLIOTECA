package com.bibli.bia.service;

import com.bibli.bia.Model.LibroFisicoModel;
import com.bibli.bia.repository.LibroFisicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<LibroFisicoModel> obtenerLibrosFisicosPorCategoria(String categoria) {
        // ✅ Cambiado: ahora usa findByCategoriaIgnoreCase
        return libroFisicoRepository.findByCategoriaIgnoreCase(categoria);
    }

    public boolean reservarLibroFisico(String id) {
        Optional<LibroFisicoModel> libroOptional = libroFisicoRepository.findById(id);
        if (libroOptional.isEmpty()) return false;
        LibroFisicoModel libro = libroOptional.get();
        if (libro.getStock() <= 0) return false;
        libro.setStock(libro.getStock() - 1);
        libro.setReservado(libro.getReservado() + 1);
        libroFisicoRepository.save(libro);
        return true;
    }

    public boolean cancelarReservaLibroFisico(String id) {
        Optional<LibroFisicoModel> libroOptional = libroFisicoRepository.findById(id);
        if (libroOptional.isEmpty()) return false;
        LibroFisicoModel libro = libroOptional.get();
        if (libro.getReservado() <= 0) return false;
        libro.setStock(libro.getStock() + 1);
        libro.setReservado(libro.getReservado() - 1);
        libroFisicoRepository.save(libro);
        return true;
    }
}