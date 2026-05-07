package com.bibli.bia.repository;

import com.bibli.bia.Model.MultaModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MultaRepository extends MongoRepository<MultaModel, String> {

    List<MultaModel> findByIdUsuario(String idUsuario);


    List<MultaModel> findByNombreUsuarioContainingIgnoreCase(String nombreUsuario);


    List<MultaModel> findByPagada(boolean pagada);
}