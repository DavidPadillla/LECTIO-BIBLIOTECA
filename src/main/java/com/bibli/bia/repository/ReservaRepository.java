package com.bibli.bia.repository;

import com.bibli.bia.Model.ReservaModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends MongoRepository<ReservaModel, String> {

}
