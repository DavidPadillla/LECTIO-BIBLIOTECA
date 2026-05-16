package com.bibli.bia.service;

import com.bibli.bia.Model.ResenaModel;
import com.bibli.bia.repository.Resenarepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ResenaService {

    @Autowired
    private Resenarepository resenarepository;

    public long contarResenas() {
        return resenarepository.count();
    }

    public void guardarResena(ResenaModel resena) {
        ResenaModel saved = resenarepository.save(resena);

    }

    public List<ResenaModel> obtenerTodasLasResenas() {
        return resenarepository.findAll();
    }
}

