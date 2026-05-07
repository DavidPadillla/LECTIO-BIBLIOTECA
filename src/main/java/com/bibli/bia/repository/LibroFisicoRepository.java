package com.bibli.bia.repository;

import com.bibli.bia.Model.LibroFisicoModel;
import com.bibli.bia.Model.LibroModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroFisicoRepository extends MongoRepository<LibroFisicoModel, String> {


    List<LibroFisicoModel> findByCategoria(String categoria);

    LibroFisicoModel findByTitulo(String titulo);

    Optional<LibroFisicoModel> findById(String id);
}
