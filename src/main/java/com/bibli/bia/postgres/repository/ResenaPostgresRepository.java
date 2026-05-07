package com.bibli.bia.postgres.repository;

import com.bibli.bia.postgres.entity.ResenaPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResenaPostgresRepository extends JpaRepository<ResenaPostgres, Long> {
}