package com.bibli.bia.repository;

import com.bibli.bia.Model.LibroFisicoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LibroFisicoRepository extends JpaRepository<LibroFisicoModel, String> {

    // Buscar por categoría (ignorando mayúsculas/minúsculas)
    List<LibroFisicoModel> findByCategoriaIgnoreCase(String categoria);

    // Buscar por título (exacto)
    Optional<LibroFisicoModel> findByTitulo(String titulo);

    // Buscar por título (contiene, ignorando mayúsculas)
    List<LibroFisicoModel> findByTituloContainingIgnoreCase(String titulo);
    // En LibroFisicoRepository.java
    List<LibroFisicoModel> findByCategoria(String categoria);

    // Buscar por autor
    List<LibroFisicoModel> findByAutorContainingIgnoreCase(String autor);

    // Buscar libros con stock disponible (stock > reservado)
    @Query("SELECT l FROM LibroFisicoModel l WHERE l.stock > l.reservado")
    List<LibroFisicoModel> findLibrosDisponibles();

    // Buscar libros con stock bajo
    List<LibroFisicoModel> findByStockLessThan(int limite);

    // Búsqueda combinada por título y autor
    List<LibroFisicoModel> findByTituloContainingIgnoreCaseAndAutorContainingIgnoreCase(String titulo, String autor);
}