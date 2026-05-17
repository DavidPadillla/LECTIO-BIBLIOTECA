package com.bibli.bia.repository;

import com.bibli.bia.Model.ReservaModel;
import com.bibli.bia.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaModel, String> {


    List<ReservaModel> findByUsuario(Usuario usuario);


    List<ReservaModel> findByUsuarioId(String usuarioId);


    List<ReservaModel> findByLibro(String libro);


    List<ReservaModel> findByCategoria(String categoria);


    List<ReservaModel> findByFecha(LocalDate fecha);


    List<ReservaModel> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);


    List<ReservaModel> findByFechaGreaterThanEqual(LocalDate fecha);
}