package com.bibli.bia.repository;

import com.bibli.bia.Model.ProgresoLectura;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgresoLecturaRepository extends MongoRepository<ProgresoLectura, String> {
    Optional<ProgresoLectura> findByUsername(String username);
}
