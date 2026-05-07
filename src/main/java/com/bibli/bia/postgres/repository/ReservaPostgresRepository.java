package com.bibli.bia.postgres.repository;

import com.bibli.bia.postgres.entity.ReservaPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaPostgresRepository extends JpaRepository<ReservaPostgres, Long> {
}