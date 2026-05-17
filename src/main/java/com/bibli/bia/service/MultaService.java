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

    public MultaModel crearMulta(MultaModel multa) {
        return multaRepository.save(multa);
    }


    public List<MultaModel> obtenerMultasPorUsuario(String idUsuario) {
        return multaRepository.findByUsuarioId(idUsuario);
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
                return multaRepository.save(multa);
            }
            return multa;
        }
        return null;
    }
    public long contarMultas() {
        return multaRepository.count();
    }

    public void eliminarMulta(String idMulta) {
        multaRepository.deleteById(idMulta);
        System.out.println("✅ Multa eliminada de PostgreSQL");
    }

    public List<MultaModel> obtenerTodasMultas() {
        return multaRepository.findAll();
    }

    public List<MultaModel> obtenerMultasPagadas() {
        return multaRepository.findByPagada(true);
    }
}