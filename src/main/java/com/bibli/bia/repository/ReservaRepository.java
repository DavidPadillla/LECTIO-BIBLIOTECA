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

    // Buscar reservas por usuario
    List<ReservaModel> findByUsuario(Usuario usuario);

    // Buscar reservas por ID de usuario
    List<ReservaModel> findByUsuarioId(String usuarioId);

    // Buscar reservas por libro
    List<ReservaModel> findByLibro(String libro);

    // Buscar reservas por categoría
    List<ReservaModel> findByCategoria(String categoria);

    // Buscar reservas por fecha
    List<ReservaModel> findByFecha(LocalDate fecha);

    // Buscar reservas entre fechas
    List<ReservaModel> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Buscar reservas futuras (fecha >= hoy)
    List<ReservaModel> findByFechaGreaterThanEqual(LocalDate fecha);
}