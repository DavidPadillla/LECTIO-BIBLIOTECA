package com.bibli.bia.postgres.repository;

import com.bibli.bia.postgres.entity.UsuarioPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioPostgresRepository extends JpaRepository<UsuarioPostgres, String> {
    Optional<UsuarioPostgres> findByUsername(String username);
}