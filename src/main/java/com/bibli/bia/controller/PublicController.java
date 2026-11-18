package com.bibli.bia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {
    @GetMapping("/api/terminos")
    public String terminos() { return "terminos"; }

    @GetMapping("/api/privacidad")
    public String privacidad() { return "privacidad"; }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/intro";
    }


    @GetMapping("/intro")
    public String intro() {
        return "intro";
    }
}
