package com.bibli.bia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {


    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/intro";
    }


    @GetMapping("/intro")
    public String intro() {
        return "intro";
    }
}
