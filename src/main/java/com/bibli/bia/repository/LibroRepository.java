package com.bibli.bia.repository;

import com.bibli.bia.Model.LibroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<LibroModel, String> {


    List<LibroModel> findByCategoriaIgnoreCase(String categoria);


    List<LibroModel> findByCategoria(String categoria);




    List<LibroModel> findByTituloContainingIgnoreCase(String titulo);


    List<LibroModel> findByAutorContainingIgnoreCase(String autor);


    List<LibroModel> findByCategoriaIgnoreCaseAndAutorContainingIgnoreCase(String categoria, String autor);
}