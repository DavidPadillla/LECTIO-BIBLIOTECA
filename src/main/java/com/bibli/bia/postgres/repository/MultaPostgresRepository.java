package com.bibli.bia.postgres.repository;

import com.bibli.bia.postgres.entity.MultaPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultaPostgresRepository extends JpaRepository<MultaPostgres, Long> {
}