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


    List<LibroFisicoModel> findByCategoriaIgnoreCase(String categoria);


    Optional<LibroFisicoModel> findByTitulo(String titulo);


    List<LibroFisicoModel> findByTituloContainingIgnoreCase(String titulo);

    List<LibroFisicoModel> findByCategoria(String categoria);


    List<LibroFisicoModel> findByAutorContainingIgnoreCase(String autor);


    @Query("SELECT l FROM LibroFisicoModel l WHERE l.stock > l.reservado")
    List<LibroFisicoModel> findLibrosDisponibles();


    List<LibroFisicoModel> findByStockLessThan(int limite);


    List<LibroFisicoModel> findByTituloContainingIgnoreCaseAndAutorContainingIgnoreCase(String titulo, String autor);
}