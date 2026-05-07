package com.bibli.bia.service;

import com.bibli.bia.Model.LibroModel;
import com.bibli.bia.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private SyncService syncService;

    public LibroModel guardarLibro(LibroModel libro) {
        LibroModel saved = libroRepository.save(libro);
        syncService.sincronizarLibro(saved);
        return saved;
    }

    public List<LibroModel> obtenerTodosLosLibros() {
        return libroRepository.findAll();
    }

    public List<LibroModel> obtenerLibrosPorCategoria(String categoria) {
        return libroRepository.findByCategoria(categoria);
    }

    public void eliminarLibro(String id) {
        libroRepository.deleteById(id);
    }
}