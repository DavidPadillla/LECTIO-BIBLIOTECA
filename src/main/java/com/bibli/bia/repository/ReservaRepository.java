package com.bibli.bia.repository;

import com.bibli.bia.Model.ReservaModel;
import com.bibli.bia.Model.ReservaModel.EstadoReserva;
import com.bibli.bia.Model.Usuario;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaModel, String> {

    List<ReservaModel> findByUsuario(Usuario usuario);

    List<ReservaModel> findByUsuarioId(String usuarioId);

    List<ReservaModel> findByLibro(String libro);

    List<ReservaModel> findByCategoria(String categoria);

    List<ReservaModel> findByFecha(LocalDate fecha);

    List<ReservaModel> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<ReservaModel> findByFechaGreaterThanEqual(LocalDate fecha);

    List<ReservaModel> findByEstado(EstadoReserva estado);

    // ✅ Resuelve libroFisico.id
    List<ReservaModel> findByLibroFisicoId(String libroFisicoId);

    // ✅ Resuelve libroFisico.id + estado
    List<ReservaModel> findByLibroFisicoIdAndEstado(String libroFisicoId, EstadoReserva estado);

    List<ReservaModel> findByUsuarioIdAndEstado(String usuarioId, EstadoReserva estado);

    // ✅ Resuelve usuario.id + libroFisico.id + estado
    boolean existsByUsuarioIdAndLibroFisicoIdAndEstado(String usuarioId,
                                                       String libroFisicoId,
                                                       EstadoReserva estado);

    long countByEstado(EstadoReserva estado);

    long countByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM ReservaModel r WHERE r.id = :id")
    Optional<ReservaModel> findByIdForUpdate(@Param("id") String id);

    @Query(value = "SELECT r.libro_fisico_id, " +
            "COALESCE(lf.titulo, r.libro) AS titulo, " +
            "CAST(r.fecha_creacion AS date) AS dia, " +
            "r.estado, " +
            "COUNT(*) AS total " +
            "FROM reservas r " +
            "LEFT JOIN libros_fisicos lf ON lf.id = r.libro_fisico_id " +
            "WHERE r.fecha_creacion >= :desde AND r.fecha_creacion < :hasta " +
            "GROUP BY r.libro_fisico_id, COALESCE(lf.titulo, r.libro), CAST(r.fecha_creacion AS date), r.estado " +
            "ORDER BY dia, titulo, r.estado", nativeQuery = true)
    List<Object[]> demandaPorLibroYDia(@Param("desde") LocalDateTime desde,
                                       @Param("hasta") LocalDateTime hasta);

    @Query(value = "SELECT r.libro_fisico_id, " +
            "COALESCE(lf.titulo, r.libro) AS titulo, " +
            "r.fecha_devolucion, " +
            "COUNT(*) AS total " +
            "FROM reservas r " +
            "LEFT JOIN libros_fisicos lf ON lf.id = r.libro_fisico_id " +
            "WHERE r.estado = 'DEVUELTA' AND r.fecha_devolucion BETWEEN :desde AND :hasta " +
            "GROUP BY r.libro_fisico_id, COALESCE(lf.titulo, r.libro), r.fecha_devolucion " +
            "ORDER BY r.fecha_devolucion, titulo", nativeQuery = true)
    List<Object[]> devolucionesPorLibroYDia(@Param("desde") LocalDate desde,
                                            @Param("hasta") LocalDate hasta);
}