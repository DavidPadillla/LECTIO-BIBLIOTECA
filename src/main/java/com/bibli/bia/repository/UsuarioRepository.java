package com.bibli.bia.repository;

import com.bibli.bia.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    // Buscar por username (único)
    Optional<Usuario> findByUsername(String username);

    // Verificar si existe un username
    boolean existsByUsername(String username);

    // Buscar por username ignorando mayúsculas
    Optional<Usuario> findByUsernameIgnoreCase(String username);
}