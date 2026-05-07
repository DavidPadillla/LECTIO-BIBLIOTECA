package com.bibli.bia.postgres.repository;

import com.bibli.bia.postgres.entity.LibroPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LibroPostgresRepository extends JpaRepository<LibroPostgres, String> {
    Optional<LibroPostgres> findByTitulo(String titulo);
}