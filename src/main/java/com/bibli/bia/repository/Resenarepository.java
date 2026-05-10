package com.bibli.bia.repository;

import com.bibli.bia.Model.ResenaModel;
import com.bibli.bia.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface Resenarepository extends JpaRepository<ResenaModel, String> {

    // Buscar reseñas por usuario
    List<ResenaModel> findByUsuario(Usuario usuario);

    // Buscar reseñas por ID de usuario
    List<ResenaModel> findByUsuarioId(String usuarioId);

    // Buscar reseñas por libro
    List<ResenaModel> findByLibroId(String libroId);

    // Buscar reseñas por nombre (contiene, ignorando mayúsculas)
    List<ResenaModel> findByNombreContainingIgnoreCase(String nombre);
}