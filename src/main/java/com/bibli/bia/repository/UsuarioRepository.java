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

    // Buscar por username (único)
    Optional<Usuario> findByUsername(String username);

    // Verificar si existe un username
    boolean existsByUsername(String username);

    // Buscar por username ignorando mayúsculas
    Optional<Usuario> findByUsernameIgnoreCase(String username);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.roles WHERE u.username = :username")
    Optional<Usuario> findByUsernameWithRoles(@Param("username") String username);



    // ✅ Métodos para contar usuarios activos e inactivos
    long countByActivoTrue();
    long countByActivoFalse();

    // ✅ Método para obtener usuarios por estado
    List<Usuario> findByActivoTrue();
    List<Usuario> findByActivoFalse();

}