package com.bibli.bia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {

    // Muestra la intro primero
    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/intro";
    }

    // Página de introducción
    @GetMapping("/intro")
    public String intro() {
        return "intro"; // archivo intro.html en /templates/
    }
}
