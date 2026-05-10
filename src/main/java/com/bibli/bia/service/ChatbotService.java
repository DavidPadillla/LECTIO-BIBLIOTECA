package com.bibli.bia.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ChatbotService {

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final WebClient webClient;
    private final String GROQ_KEY;
    private static final String MODEL = "llama-3.3-70b-versatile";
    private final ObjectMapper mapper = new ObjectMapper();

    // ✅ Eliminado MongoTemplate del constructor
    public ChatbotService() {
        String tempKey = System.getenv("GROQ_API_KEY");

        if (tempKey == null || tempKey.isBlank()) {
            throw new IllegalStateException(
                    "❌ ERROR: GROQ_API_KEY no está configurada en las variables de entorno.\n" +
                            "Configúrala con: export GROQ_API_KEY='tu-key-aqui'"
            );
        }

        this.GROQ_KEY = tempKey;
        System.out.println("✅ API Key cargada desde variable de entorno");

        this.webClient = WebClient.builder()
                .baseUrl("https://api.groq.com")
                .defaultHeader("Authorization", "Bearer " + this.GROQ_KEY)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public void streamRespuesta(String pregunta, String username, SseEmitter emitter) {
        executor.submit(() -> {
            try {
                System.out.println("📝 Pregunta: " + pregunta);
                System.out.println("👤 Usuario: " + username);

                Map<String, Object> body = Map.of(
                        "model", MODEL,
                        "stream", true,
                        "max_tokens", 500,
                        "temperature", 0.7,
                        "messages", List.of(
                                Map.of("role", "system", "content",
                                        "Eres un asistente de biblioteca llamado Lectio. " +
                                                "Responde en español, claro y breve. Máximo 100 palabras."),
                                Map.of("role", "user", "content", pregunta)
                        )
                );

                webClient.post()
                        .uri("/openai/v1/chat/completions")
                        .bodyValue(body)
                        .retrieve()
                        .bodyToFlux(String.class)
                        .subscribe(
                                line -> {
                                    try {
                                        if (line == null || line.isBlank()) return;

                                        String jsonData = line.startsWith("data: ")
                                                ? line.substring(6).trim()
                                                : line.trim();

                                        if ("[DONE]".equals(jsonData)) {
                                            emitter.send(SseEmitter.event().data("[DONE]"));
                                            emitter.complete();
                                            return;
                                        }

                                        JsonNode root = mapper.readTree(jsonData);
                                        JsonNode delta = root
                                                .path("choices")
                                                .path(0)
                                                .path("delta")
                                                .path("content");

                                        if (!delta.isMissingNode() && !delta.isNull()) {
                                            String chunk = delta.asText();
                                            if (!chunk.isEmpty()) {
                                                System.out.print(chunk);
                                                String encoded = Base64.getEncoder()
                                                        .encodeToString(chunk.getBytes(StandardCharsets.UTF_8));
                                                emitter.send(SseEmitter.event().data(encoded));
                                            }
                                        }

                                    } catch (Exception e) {
                                        System.err.println("Error procesando chunk: " + e.getMessage());
                                    }
                                },
                                error -> {
                                    System.err.println("Error stream: " + error.getMessage());
                                    try {
                                        emitter.send(SseEmitter.event().data("[ERROR]"));
                                    } catch (Exception ignored) {}
                                    emitter.complete();
                                },
                                () -> {
                                    System.out.println("\n✅ Stream completado");
                                    try {
                                        emitter.complete();
                                    } catch (Exception ignored) {}
                                }
                        );

            } catch (Exception e) {
                System.err.println("Error general: " + e.getMessage());
                try {
                    emitter.send(SseEmitter.event().data("[ERROR]"));
                } catch (Exception ignored) {}
                emitter.complete();
            }
        });
    }
}