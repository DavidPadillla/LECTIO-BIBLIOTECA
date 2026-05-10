package com.bibli.bia.repository;

import com.bibli.bia.Model.ProgresoLectura;
import com.bibli.bia.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProgresoLecturaRepository extends JpaRepository<ProgresoLectura, String> {

    // Buscar por usuario (ahora usa la relación @OneToOne)
    Optional<ProgresoLectura> findByUsuario(Usuario usuario);

    // Buscar por ID de usuario
    Optional<ProgresoLectura> findByUsuarioId(String usuarioId);
}