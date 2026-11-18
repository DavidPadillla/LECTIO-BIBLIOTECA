package com.bibli.bia.controller;

import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class LecturasController {
    private static final Map<String, String> PUBLICAS = Map.of(
            "hadas", "Cuentos de hadas clásicos",
            "grimm", "Cuentos de los Hermanos Grimm",
            "poe", "Cuentos de Edgar Allan Poe",
            "aventura", "Clásicos de aventura");
    private static final Set<String> LECTURAS = Set.of(
            "hadas", "grimm", "poe", "aventura", "conocimiento", "universo", "civilizaciones");

    @GetMapping("/api/muestras/{libro}")
    public String muestra(@PathVariable String libro, Model model) {
        if (!PUBLICAS.containsKey(libro)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        model.addAttribute("coleccion", PUBLICAS.get(libro));
        return "muestra-" + libro;
    }

    @GetMapping("/api/lecturas/{libro}")
    public String lectura(@PathVariable String libro, Model model, Authentication authentication) {
        if (!LECTURAS.contains(libro)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        model.addAttribute("username", authentication.getName());
        return "lectura-" + libro;
    }
}
