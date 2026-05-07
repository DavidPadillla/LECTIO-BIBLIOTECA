package com.bibli.bia.postgres.repository;

import com.bibli.bia.postgres.entity.RespuestaDashboardPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaDashboardPostgresRepository extends JpaRepository<RespuestaDashboardPostgres, Long> {
}