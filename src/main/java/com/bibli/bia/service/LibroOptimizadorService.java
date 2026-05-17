package com.bibli.bia.service;

import com.bibli.bia.Model.LibroOptimizador;
import com.bibli.bia.repository.LibroOptimizadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroOptimizadorService {

    @Autowired
    private LibroOptimizadorRepository libroRepositoryOptimizador;

    public List<LibroOptimizador> listarTodos() {
        return libroRepositoryOptimizador.findAll();
    }

    public List<LibroOptimizador> listarDisponibles() {
        return libroRepositoryOptimizador.findByDisponibleTrue();
    }

    public LibroOptimizador guardar(LibroOptimizador libro) {
        return libroRepositoryOptimizador.save(libro);
    }

    // ✅ FIX: el ID es Long (igual que en LibroOptimizador y JpaRepository<LibroOptimizador, Long>)
    public void eliminar(Long id) {
        libroRepositoryOptimizador.deleteById(id);
    }

    public long contarDisponibles() {
        return libroRepositoryOptimizador.count();
    }
}