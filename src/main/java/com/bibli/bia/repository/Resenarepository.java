package com.bibli.bia.repository;

import com.bibli.bia.Model.ResenaModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Resenarepository extends MongoRepository<ResenaModel, String> {


}
