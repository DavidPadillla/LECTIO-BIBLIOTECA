package com.bibli.bia.service;

import com.bibli.bia.Model.DocumentChunk;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.util.retry.Retry;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class ChatbotService {

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final WebClient webClient;
    private final String GROQ_KEY;

    /**
     * ✅ MODELO ACTUALIZADO
     * Groq retiró los modelos Llama (llama-3.3-70b-versatile y llama-3.1-8b-instant).
     * El reemplazo recomendado es "openai/gpt-oss-120b" o "openai/gpt-oss-20b".
     * Verifica siempre los modelos activos en: https://api.groq.com/openai/v1/models
     */
    private static final String MODEL = "openai/gpt-oss-120b";

    private final ObjectMapper mapper = new ObjectMapper();

    private final Map<String, List<Map<String, String>>> historialPorUsuario =
            new ConcurrentHashMap<>();

    private static final int MAX_HISTORIAL = 10;

    @Autowired
    private DatabaseQueryService dbQueryService;

    @Autowired
    private RetrievalService retrievalService;

    public ChatbotService() {

        String tempKey = System.getenv("GROQ_API_KEY");
        if (tempKey == null || tempKey.isBlank()) {
            throw new IllegalStateException("ERROR: GROQ_API_KEY no está configurada.");
        }
        this.GROQ_KEY = tempKey;
        System.out.println("✅ API Key cargada");

        // URL base correcta: incluye /openai/v1 completo
        this.webClient = WebClient.builder()
                .baseUrl("https://api.groq.com/openai/v1")
                .defaultHeader("Authorization", "Bearer " + this.GROQ_KEY)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public List<Map<String, String>> obtenerHistorial(String username) {
        return historialPorUsuario.getOrDefault(username, new ArrayList<>());
    }

    private List<Map<String, String>> getHistorial(String username) {
        return historialPorUsuario.computeIfAbsent(username, k -> new ArrayList<>());
    }

    private void agregarAlHistorial(String username, String rol, String contenido) {
        List<Map<String, String>> historial = getHistorial(username);
        Map<String, String> mensaje = new HashMap<>();
        mensaje.put("role", rol);
        mensaje.put("content", contenido);
        historial.add(mensaje);

        while (historial.size() > MAX_HISTORIAL) {
            historial.remove(0);
        }
    }

    public void limpiarHistorial(String username) {
        historialPorUsuario.remove(username);
        System.out.println("🧹 Historial limpiado para: " + username);
    }

    private String procesarConsultaLocal(String pregunta, String username) {

        String preguntaLower = pregunta.toLowerCase();

        if (preguntaLower.matches(".*(mis libros|mis reservas|qué libros tengo reservados).*")) {
            return dbQueryService.getMisReservas(username);
        }
        if (preguntaLower.matches(".*(mis multas|multas pendientes|debo dinero).*")) {
            return dbQueryService.getMisMultas(username);
        }
        if (preguntaLower.matches(".*(mi progreso|cuántos libros he leído|puntos tengo).*")) {
            return dbQueryService.getMiProgreso(username);
        }
        if (preguntaLower.matches(".*(libros disponibles|qué libros hay|libros físicos).*")) {
            return dbQueryService.getLibrosFisicosDisponibles();
        }
        if (preguntaLower.matches(".*(limpiar chat|borrar historial|resetear conversación).*")) {
            limpiarHistorial(username);
            return "He limpiado mi memoria. ¿En qué puedo ayudarte ahora?";
        }

        java.util.regex.Pattern categoriaPattern =
                java.util.regex.Pattern.compile("libros de (\\w+)");
        java.util.regex.Matcher categoriaMatcher =
                categoriaPattern.matcher(preguntaLower);

        if (categoriaMatcher.find()) {
            String categoria = categoriaMatcher.group(1);
            String primeraLetra = categoria.substring(0, 1).toUpperCase();
            String categoriaCapitalizada = primeraLetra + categoria.substring(1);
            return dbQueryService.getLibrosVirtualesPorCategoria(categoriaCapitalizada);
        }

        return null;
    }

    // ==================== STREAM PRINCIPAL ====================
    public void streamRespuesta(String pregunta, String username, SseEmitter emitter) {

        executor.submit(() -> {
            try {
                System.out.println("📩 Pregunta: " + pregunta);
                System.out.println("👤 Usuario: " + username);

                agregarAlHistorial(username, "user", pregunta);

                // 1) Consultas BD locales
                String respuestaLocal = procesarConsultaLocal(pregunta, username);
                if (respuestaLocal != null) {
                    agregarAlHistorial(username, "assistant", respuestaLocal);
                    String encoded = Base64.getEncoder()
                            .encodeToString(respuestaLocal.getBytes(StandardCharsets.UTF_8));
                    emitter.send(SseEmitter.event().data(encoded));
                    emitter.send(SseEmitter.event().data("[DONE]"));
                    emitter.complete();
                    return;
                }

                // 2) Saludos
                if (pregunta.toLowerCase().matches(
                        ".*(hola|buenos días|buenas tardes|qué tal|saludos).*")) {

                    String saludo = "✨ ¡Hola! Soy el asistente inteligente de Lectio. " +
                            "Puedo ayudarte con libros, reglamentos, reservas y preguntas de la biblioteca.";
                    agregarAlHistorial(username, "assistant", saludo);

                    String encoded = Base64.getEncoder()
                            .encodeToString(saludo.getBytes(StandardCharsets.UTF_8));
                    emitter.send(SseEmitter.event().data(encoded));
                    emitter.send(SseEmitter.event().data("[DONE]"));
                    emitter.complete();
                    return;
                }

                // 3) RAG
                List<DocumentChunk> chunks =
                        retrievalService.buscarChunksRelevantes(pregunta, 3);

                String contexto = chunks.stream()
                        .map(DocumentChunk::getContent)
                        .collect(Collectors.joining("\n\n"));

                System.out.println("📚 Contexto encontrado:");
                System.out.println(contexto);

                List<Map<String, String>> historial = getHistorial(username);

                String systemPrompt =
                        "Eres un asistente inteligente de la biblioteca virtual Lectio. " +
                                "Debes responder usando únicamente el contexto proporcionado. " +
                                "Si la respuesta no aparece en el contexto, dilo claramente. " +
                                "Responde en español, claro y breve. Máximo 120 palabras.\n\n" +
                                "CONTEXTO:\n" + contexto;

                List<Map<String, String>> mensajes = new ArrayList<>();
                mensajes.add(Map.of("role", "system", "content", systemPrompt));
                mensajes.addAll(historial);

                Map<String, Object> body = Map.of(
                        "model", MODEL,
                        "stream", true,
                        "max_tokens", 500,
                        "temperature", 0.7,
                        "messages", mensajes
                );

                llamarGroq(body, emitter, username);

            } catch (Exception e) {
                System.err.println("❌ Error general: " + e.getMessage());
                try { emitter.send(SseEmitter.event().data("[ERROR]")); } catch (Exception ignored) {}
                emitter.complete();
            }
        });
    }

    // ==================== LLAMADA A GROQ ====================
    private void llamarGroq(Map<String, Object> body,
                            SseEmitter emitter,
                            String username) {

        StringBuilder respuestaCompleta = new StringBuilder();

        System.out.println("🤖 Llamando a Groq con modelo: " + MODEL);

        webClient.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(String.class)
                .timeout(Duration.ofSeconds(30))
                .retryWhen(
                        Retry.fixedDelay(1, Duration.ofSeconds(2))
                                .filter(e -> {
                                    if (e instanceof WebClientResponseException.Unauthorized) {
                                        System.err.println("❌ API Key inválida (401). No se reintenta.");
                                        return false;
                                    }
                                    System.err.println("⚠️ Error, reintentando: " + e.getMessage());
                                    return true;
                                })
                )
                .subscribe(
                        line -> procesarLinea(line, respuestaCompleta, emitter),
                        error -> {
                            System.err.println("❌ Error stream final: " + error.getMessage());
                            try {
                                emitter.send(SseEmitter.event().data("[ERROR]"));
                                emitter.complete();
                            } catch (Exception e) {
                                emitter.completeWithError(e);
                            }
                        },
                        () -> {
                            if (respuestaCompleta.length() > 0) {
                                agregarAlHistorial(username, "assistant", respuestaCompleta.toString());
                            }
                            System.out.println("\n✅ Stream completado");
                        }
                );
    }

    private void procesarLinea(String line,
                               StringBuilder respuestaCompleta,
                               SseEmitter emitter) {
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
            JsonNode delta = root.path("choices").path(0).path("delta").path("content");

            if (!delta.isMissingNode() && !delta.isNull()) {
                String chunk = delta.asText();
                if (!chunk.isEmpty()) {
                    System.out.print(chunk);
                    respuestaCompleta.append(chunk);
                    String encoded = Base64.getEncoder()
                            .encodeToString(chunk.getBytes(StandardCharsets.UTF_8));
                    emitter.send(SseEmitter.event().data(encoded));
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error procesando chunk: " + e.getMessage());
        }
    }
}