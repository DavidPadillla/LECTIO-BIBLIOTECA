package com.bibli.bia.postgres.repository;

import com.bibli.bia.postgres.entity.ProgresoLecturaPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgresoLecturaPostgresRepository extends JpaRepository<ProgresoLecturaPostgres, Long> {
}
