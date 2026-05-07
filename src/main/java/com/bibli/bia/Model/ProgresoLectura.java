package com.bibli.bia.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.*;

@Document(collection = "progreso_lectura")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgresoLectura {

    @Id
    private String id;

    private String username;

    private Set<String> librosCompletados = new HashSet<>();

    private Map<String, Set<Integer>> capitulosPorLibro = new HashMap<>();

    private int totalLibrosLeidos = 0;

    private LocalDateTime ultimaActualizacion;

    private int puntos = 0;

    public ProgresoLectura(String username) {
        this.username = username;
        this.librosCompletados = new HashSet<>();
        this.capitulosPorLibro = new HashMap<>();
        this.totalLibrosLeidos = 0;
        this.ultimaActualizacion = LocalDateTime.now();
        this.puntos = 0;
    }
}
