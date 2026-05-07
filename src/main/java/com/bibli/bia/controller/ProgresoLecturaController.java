package com.bibli.bia.controller;

import com.bibli.bia.Model.ProgresoLectura;
import com.bibli.bia.service.ProgresoLecturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/progreso")
@CrossOrigin(origins = "*")
public class ProgresoLecturaController {

    private final ProgresoLecturaService progresoService;

    public ProgresoLecturaController(ProgresoLecturaService progresoService) {
        this.progresoService = progresoService;
    }

    @GetMapping("/mi-progreso")
    public ResponseEntity<?> obtenerMiProgreso(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("No autenticado");
        }

        String username = authentication.getName();
        ProgresoLectura progreso = progresoService.obtenerProgreso(username);

        return ResponseEntity.ok(progreso);
    }

    @PostMapping("/guardar-capitulo")
    public ResponseEntity<?> guardarCapitulo(
            @RequestBody Map<String, Object> body,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("No autenticado");
        }

        String username = authentication.getName();
        String libroId = (String) body.get("libroId");
        int capitulo = (Integer) body.get("capitulo");
        int totalCapitulos = (Integer) body.get("totalCapitulos");

        if (libroId == null || libroId.isEmpty()) {
            return ResponseEntity.badRequest().body("libroId es requerido");
        }

        ProgresoLectura progreso = progresoService.guardarCapitulo(username, libroId, capitulo, totalCapitulos);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Capítulo guardado");
        response.put("progreso", progreso);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/completar")
    public ResponseEntity<?> completarLibro(
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("No autenticado");
        }

        String username = authentication.getName();
        String libroId = body.get("libroId");

        if (libroId == null || libroId.isEmpty()) {
            return ResponseEntity.badRequest().body("libroId es requerido");
        }

        ProgresoLectura progreso = progresoService.marcarLibroCompletado(username, libroId);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "¡Libro completado!");
        response.put("progreso", progreso);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/verificar/{libroId}")
    public ResponseEntity<?> verificarLibro(
            @PathVariable String libroId,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("No autenticado");
        }

        String username = authentication.getName();
        boolean completado = progresoService.haCompletadoLibro(username, libroId);

        Map<String, Object> response = new HashMap<>();
        response.put("completado", completado);
        response.put("libroId", libroId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reiniciar")
    public ResponseEntity<?> reiniciarProgreso(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("No autenticado");
        }

        String username = authentication.getName();
        progresoService.reiniciarProgreso(username);

        return ResponseEntity.ok("Progreso reiniciado");
    }
}
