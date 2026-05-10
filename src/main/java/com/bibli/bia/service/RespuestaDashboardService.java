package com.bibli.bia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bibli.bia.Model.RespuestaDashboard;
import com.bibli.bia.repository.RespuestaDashboardRepository;

@Service
public class RespuestaDashboardService {

    @Autowired
    private RespuestaDashboardRepository respuestaRepository;

    // ✅ Elimina esta línea - no va aquí:
    // @Autowired

    public RespuestaDashboard guardarRespuesta(RespuestaDashboard respuestaDashboard) {
        RespuestaDashboard saved = respuestaRepository.save(respuestaDashboard);
        return saved;
    }
}