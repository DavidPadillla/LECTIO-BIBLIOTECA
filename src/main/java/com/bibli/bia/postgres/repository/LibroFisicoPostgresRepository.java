package com.bibli.bia.postgres.repository;

import com.bibli.bia.postgres.entity.LibroFisicoPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroFisicoPostgresRepository extends JpaRepository<LibroFisicoPostgres, Long> {
}