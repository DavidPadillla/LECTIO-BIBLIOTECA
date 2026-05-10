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

    // Buscar respuestas por usuario
    List<RespuestaDashboard> findByUsuario(Usuario usuario);

    // Buscar respuestas por ID de usuario
    List<RespuestaDashboard> findByUsuarioId(String usuarioId);

    // Buscar respuestas por rango de edad
    List<RespuestaDashboard> findByEdadBetween(Integer edadMin, Integer edadMax);

    // Buscar respuestas por género
    List<RespuestaDashboard> findByGenero(String genero);

    // Buscar respuestas por categoría favorita
    List<RespuestaDashboard> findByCategoriaFavorita(String categoriaFavorita);

    // Buscar respuestas por fecha de registro
    List<RespuestaDashboard> findByFechaRegistroBetween(LocalDateTime inicio, LocalDateTime fin);

    // Buscar respuestas con alta calificación
    List<RespuestaDashboard> findByCalificacionGreaterThanEqual(Integer calificacion);

    // Contar respuestas por género
    @Query("SELECT r.genero, COUNT(r) FROM RespuestaDashboard r GROUP BY r.genero")
    List<Object[]> countByGenero();

    // Contar respuestas por categoría favorita
    @Query("SELECT r.categoriaFavorita, COUNT(r) FROM RespuestaDashboard r GROUP BY r.categoriaFavorita")
    List<Object[]> countByCategoriaFavorita();

    // Calcular promedio de calificaciones
    @Query("SELECT AVG(r.calificacion) FROM RespuestaDashboard r")
    Double getPromedioCalificaciones();
}