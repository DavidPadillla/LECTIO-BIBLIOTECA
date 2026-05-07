package com.bibli.bia.repository;

import com.bibli.bia.Model.LibroModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends MongoRepository<LibroModel, String> {
    List<LibroModel> findByCategoria(String categoria);

}



