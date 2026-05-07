package com.bibli.bia.service;

import com.bibli.bia.Model.MultaModel;
import com.bibli.bia.repository.MultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MultaService {

    @Autowired
    private MultaRepository multaRepository;

    @Autowired
    private SyncService syncService;  // ← AGREGAR ESTO

    public MultaModel crearMulta(MultaModel multa) {
        MultaModel saved = multaRepository.save(multa);
        syncService.sincronizarMulta(saved);  // ← SINCRONIZAR
        return saved;
    }

    public List<MultaModel> obtenerMultasPorUsuario(String idUsuario) {
        return multaRepository.findByIdUsuario(idUsuario);
    }

    public List<MultaModel> obtenerMultasPorNombre(String nombreUsuario) {
        return multaRepository.findByNombreUsuarioContainingIgnoreCase(nombreUsuario);
    }

    public List<MultaModel> obtenerMultasPendientes() {
        return multaRepository.findByPagada(false);
    }

    public MultaModel marcarComoPagada(String idMulta) {
        Optional<MultaModel> multaOpt = multaRepository.findById(idMulta);
        if (multaOpt.isPresent()) {
            MultaModel multa = multaOpt.get();
            if (!multa.isPagada()) {
                multa.setPagada(true);
                multa.setFechaPago(LocalDate.now());
                MultaModel saved = multaRepository.save(multa);
                syncService.sincronizarMulta(saved);  // ← SINCRONIZAR
                return saved;
            }
            return multa;
        }
        return null;
    }

    public void eliminarMulta(String idMulta) {
        multaRepository.deleteById(idMulta);
        System.out.println("⚠️ Multa eliminada de MongoDB. Neon no se actualiza automáticamente.");
    }

    public List<MultaModel> obtenerTodasMultas() {
        return multaRepository.findAll();
    }

    public List<MultaModel> obtenerMultasPagadas() {
        return multaRepository.findByPagada(true);
    }
}