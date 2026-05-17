package com.bibli.bia.repository;

import com.bibli.bia.Model.MultaModel;
import com.bibli.bia.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MultaRepository extends JpaRepository<MultaModel, String> {


    List<MultaModel> findByUsuario(Usuario usuario);


    List<MultaModel> findByUsuarioId(String usuarioId);


    List<MultaModel> findByUsuarioAndPagada(Usuario usuario, boolean pagada);


    List<MultaModel> findByNombreUsuarioContainingIgnoreCase(String nombreUsuario);


    List<MultaModel> findByPagada(boolean pagada);


    List<MultaModel> findByUsuarioIdAndPagada(String usuarioId, boolean pagada);


    List<MultaModel> findByValorMultaGreaterThan(double valor);
}