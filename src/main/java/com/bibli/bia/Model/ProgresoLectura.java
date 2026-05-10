package com.bibli.bia.Model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "progreso_lectura")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgresoLectura {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(columnDefinition = "TEXT")
    private String librosCompletados = "[]"; // Guardas como JSON: ["libro1", "libro2"]

    @Column(columnDefinition = "TEXT")
    private String capitulosPorLibro = "{}"; // Guardas como JSON: {"libro1": [1,2,3], "libro2": [1]}

    private int totalLibrosLeidos = 0;

    private LocalDateTime ultimaActualizacion;

    private int puntos = 0;

    public ProgresoLectura(Usuario usuario) {
        this.usuario = usuario;
        this.librosCompletados = "[]";
        this.capitulosPorLibro = "{}";
        this.totalLibrosLeidos = 0;
        this.ultimaActualizacion = LocalDateTime.now();
        this.puntos = 0;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    // ✅ CORREGIDO: Convierte JSON string a Set<String>
    public Set<String> getLibrosCompletados() {
        if (librosCompletados == null || librosCompletados.isEmpty() || librosCompletados.equals("[]")) {
            return new HashSet<>();
        }
        try {
            return objectMapper.readValue(librosCompletados, new TypeReference<Set<String>>() {});
        } catch (Exception e) {
            System.err.println("Error parsing librosCompletados: " + e.getMessage());
            return new HashSet<>();
        }
    }

    // ✅ CORREGIDO: Convierte Set<String> a JSON string
    public void setLibrosCompletados(Set<String> libros) {
        if (libros == null) {
            this.librosCompletados = "[]";
        } else {
            try {
                this.librosCompletados = objectMapper.writeValueAsString(libros);
            } catch (Exception e) {
                System.err.println("Error serializing librosCompletados: " + e.getMessage());
                this.librosCompletados = "[]";
            }
        }
    }

    // ✅ CORREGIDO: Convierte JSON string a Map<String, Set<Integer>>
    public Map<String, Set<Integer>> getCapitulosPorLibro() {
        if (capitulosPorLibro == null || capitulosPorLibro.isEmpty() || capitulosPorLibro.equals("{}")) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(capitulosPorLibro, new TypeReference<Map<String, Set<Integer>>>() {});
        } catch (Exception e) {
            System.err.println("Error parsing capitulosPorLibro: " + e.getMessage());
            return new HashMap<>();
        }
    }

    // ✅ CORREGIDO: Convierte Map<String, Set<Integer>> a JSON string
    public void setCapitulosPorLibro(Map<String, Set<Integer>> capitulos) {
        if (capitulos == null) {
            this.capitulosPorLibro = "{}";
        } else {
            try {
                this.capitulosPorLibro = objectMapper.writeValueAsString(capitulos);
            } catch (Exception e) {
                System.err.println("Error serializing capitulosPorLibro: " + e.getMessage());
                this.capitulosPorLibro = "{}";
            }
        }
    }

    public int getTotalLibrosLeidos() { return totalLibrosLeidos; }
    public void setTotalLibrosLeidos(int totalLibrosLeidos) { this.totalLibrosLeidos = totalLibrosLeidos; }

    public LocalDateTime getUltimaActualizacion() { return ultimaActualizacion; }
    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) { this.ultimaActualizacion = ultimaActualizacion; }

    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }
}