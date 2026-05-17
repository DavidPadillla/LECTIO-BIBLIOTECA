package com.bibli.bia.repository;

import com.bibli.bia.Model.LibroOptimizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroOptimizadorRepository extends JpaRepository<LibroOptimizador, Long> {
    List<LibroOptimizador> findByDisponibleTrue();
}