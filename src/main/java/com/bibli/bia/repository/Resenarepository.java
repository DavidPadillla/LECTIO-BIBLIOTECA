package com.bibli.bia.repository;

import com.bibli.bia.Model.ResenaModel;
import com.bibli.bia.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface Resenarepository extends JpaRepository<ResenaModel, String> {


    List<ResenaModel> findByUsuario(Usuario usuario);


    List<ResenaModel> findByUsuarioId(String usuarioId);


    List<ResenaModel> findByLibroId(String libroId);


    List<ResenaModel> findByNombreContainingIgnoreCase(String nombre);
}