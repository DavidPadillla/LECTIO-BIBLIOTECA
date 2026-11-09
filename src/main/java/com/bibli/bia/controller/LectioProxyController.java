package com.bibli.bia.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/lectio")
public class LectioProxyController {

    private static final String API = "https://lectio-ai-production.up.railway.app";
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/**")
    public ResponseEntity<String> proxy(HttpServletRequest request) {
        String path  = request.getRequestURI().replace("/api/lectio", "");
        String query = request.getQueryString();
        String url   = API + path + (query != null ? "?" + query : "");

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.getBody());
    }
}
