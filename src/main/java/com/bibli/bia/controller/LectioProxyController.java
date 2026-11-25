package com.bibli.bia.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/lectio")
public class LectioProxyController {

    private static final String API = "https://lectio-ai-production.up.railway.app";
    private static final String PROXY_PATH = "/api/lectio";
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/**")
    public ResponseEntity<String> proxyGet(HttpServletRequest request) throws IOException {
        return forward(request, HttpMethod.GET);
    }

    @PostMapping("/**")
    public ResponseEntity<String> proxyPost(HttpServletRequest request) throws IOException {
        return forward(request, HttpMethod.POST);
    }

    private ResponseEntity<String> forward(HttpServletRequest request, HttpMethod method) throws IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length() + PROXY_PATH.length());
        String query = request.getQueryString();
        URI uri = URI.create(API + path + (query == null ? "" : "?" + query));

        HttpHeaders headers = new HttpHeaders();
        String accept = request.getHeader(HttpHeaders.ACCEPT);
        if (accept != null) headers.set(HttpHeaders.ACCEPT, accept);
        String contentType = request.getContentType();
        if (contentType != null) headers.setContentType(MediaType.parseMediaType(contentType));

        byte[] body = request.getInputStream().readAllBytes();
        HttpEntity<byte[]> entity = new HttpEntity<>(body.length == 0 ? null : body, headers);
        try {
            ResponseEntity<String> upstream = restTemplate.exchange(uri, method, entity, String.class);
            return relay(upstream);
        } catch (RestClientResponseException exception) {
            MediaType responseType = exception.getResponseHeaders() == null
                    ? MediaType.APPLICATION_JSON
                    : exception.getResponseHeaders().getContentType();
            return ResponseEntity.status(exception.getStatusCode())
                    .contentType(responseType == null ? MediaType.APPLICATION_JSON : responseType)
                    .body(exception.getResponseBodyAsString());
        }
    }

    private ResponseEntity<String> relay(ResponseEntity<String> response) {
        MediaType responseType = response.getHeaders().getContentType();
        return ResponseEntity.status(response.getStatusCode())
                .contentType(responseType == null ? MediaType.APPLICATION_JSON : responseType)
                .body(response.getBody());
    }
}
