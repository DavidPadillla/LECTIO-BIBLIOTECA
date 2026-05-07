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

    @Autowired
    private SyncService syncService;

    public LibroFisicoModel guardarLibroFisico(LibroFisicoModel libroFisico) {
        if (libroFisico.getStock() < 0) libroFisico.setStock(0);
        if (libroFisico.getReservado() < 0) libroFisico.setReservado(0);
        LibroFisicoModel saved = libroFisicoRepository.save(libroFisico);
        syncService.sincronizarLibroFisico(saved);
        return saved;
    }

    public List<LibroFisicoModel> obtenerTodosLosLibrosFisicos() {
        return libroFisicoRepository.findAll();
    }

    public List<LibroFisicoModel> obtenerLibrosFisicosPorCategoria(String categoria) {
        return libroFisicoRepository.findByCategoria(categoria);
    }

    public boolean reservarLibroFisico(String id) {
        Optional<LibroFisicoModel> libroOptional = libroFisicoRepository.findById(id);
        if (libroOptional.isEmpty()) return false;
        LibroFisicoModel libro = libroOptional.get();
        if (libro.getStock() <= 0) return false;
        libro.setStock(libro.getStock() - 1);
        libro.setReservado(libro.getReservado() + 1);
        LibroFisicoModel saved = libroFisicoRepository.save(libro);
        syncService.sincronizarLibroFisico(saved);
        return true;
    }

    public boolean cancelarReservaLibroFisico(String id) {
        Optional<LibroFisicoModel> libroOptional = libroFisicoRepository.findById(id);
        if (libroOptional.isEmpty()) return false;
        LibroFisicoModel libro = libroOptional.get();
        if (libro.getReservado() <= 0) return false;
        libro.setStock(libro.getStock() + 1);
        libro.setReservado(libro.getReservado() - 1);
        LibroFisicoModel saved = libroFisicoRepository.save(libro);
        syncService.sincronizarLibroFisico(saved);
        return true;
    }
}