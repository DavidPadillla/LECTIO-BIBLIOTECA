package com.bibli.bia.repository;

import com.bibli.bia.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {


    Optional<Usuario> findByUsername(String username);


    boolean existsByUsername(String username);


    Optional<Usuario> findByUsernameIgnoreCase(String username);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.roles WHERE u.username = :username")
    Optional<Usuario> findByUsernameWithRoles(@Param("username") String username);


    long countByActivoTrue();
    long countByActivoFalse();


    List<Usuario> findByActivoTrue();
    List<Usuario> findByActivoFalse();

}