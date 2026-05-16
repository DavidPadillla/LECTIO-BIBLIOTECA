package com.bibli.bia.repository;

import com.bibli.bia.Model.MultaModel;
import com.bibli.bia.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MultaRepository extends JpaRepository<MultaModel, String> {

    // Buscar por usuario (ahora usa la relación @ManyToOne)
    List<MultaModel> findByUsuario(Usuario usuario);

    // Buscar por ID de usuario
    List<MultaModel> findByUsuarioId(String usuarioId);

    // En MultaRepository.java
    List<MultaModel> findByUsuarioAndPagada(Usuario usuario, boolean pagada);

    // Buscar por nombre de usuario (parcial, ignorando mayúsculas)
    List<MultaModel> findByNombreUsuarioContainingIgnoreCase(String nombreUsuario);

    // Buscar por estado de pago
    List<MultaModel> findByPagada(boolean pagada);

    // ❌ ELIMINA ESTA LÍNEA - ya no existe
    // List<MultaModel> findByIdUsuario(String idUsuario);

    // Buscar multas no pagadas de un usuario
    List<MultaModel> findByUsuarioIdAndPagada(String usuarioId, boolean pagada);

    // Buscar multas mayores a cierto valor
    List<MultaModel> findByValorMultaGreaterThan(double valor);
}