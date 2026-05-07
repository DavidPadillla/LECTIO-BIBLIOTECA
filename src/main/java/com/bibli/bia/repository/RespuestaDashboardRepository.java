package com.bibli.bia.repository;

import com.bibli.bia.Model.RespuestaDashboard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaDashboardRepository extends MongoRepository<RespuestaDashboard, String> {

}
