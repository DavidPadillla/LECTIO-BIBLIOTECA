package com.bibli.bia.repository;

import com.bibli.bia.Model.LibroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<LibroModel, String> {

    // Buscar por categoría (ignorando mayúsculas/minúsculas)
    List<LibroModel> findByCategoriaIgnoreCase(String categoria);

    // En LibroRepository.java
    List<LibroModel> findByCategoria(String categoria);



    // Buscar por título
    List<LibroModel> findByTituloContainingIgnoreCase(String titulo);

    // Buscar por autor
    List<LibroModel> findByAutorContainingIgnoreCase(String autor);

    // Buscar por categoría y autor combinados
    List<LibroModel> findByCategoriaIgnoreCaseAndAutorContainingIgnoreCase(String categoria, String autor);
}