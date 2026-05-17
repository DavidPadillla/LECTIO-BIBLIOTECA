package com.bibli.bia.repository;

import com.bibli.bia.Model.RespuestaDashboard;
import com.bibli.bia.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RespuestaDashboardRepository extends JpaRepository<RespuestaDashboard, String> {


    List<RespuestaDashboard> findByUsuario(Usuario usuario);


    List<RespuestaDashboard> findByUsuarioId(String usuarioId);


    List<RespuestaDashboard> findByEdadBetween(Integer edadMin, Integer edadMax);


    List<RespuestaDashboard> findByGenero(String genero);


    List<RespuestaDashboard> findByCategoriaFavorita(String categoriaFavorita);


    List<RespuestaDashboard> findByFechaRegistroBetween(LocalDateTime inicio, LocalDateTime fin);


    List<RespuestaDashboard> findByCalificacionGreaterThanEqual(Integer calificacion);


    @Query("SELECT r.genero, COUNT(r) FROM RespuestaDashboard r GROUP BY r.genero")
    List<Object[]> countByGenero();


    @Query("SELECT r.categoriaFavorita, COUNT(r) FROM RespuestaDashboard r GROUP BY r.categoriaFavorita")
    List<Object[]> countByCategoriaFavorita();


    @Query("SELECT AVG(r.calificacion) FROM RespuestaDashboard r")
    Double getPromedioCalificaciones();
}