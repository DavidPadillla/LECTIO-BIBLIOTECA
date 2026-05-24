package com.bibli.bia.controller;

import com.bibli.bia.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatbotService chatbotService;

    @Autowired
    public ChatController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    // ==========================================
    // CHAT RAG + GROQ STREAMING
    // ==========================================

    @GetMapping(
            value = "/rag",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public SseEmitter preguntarRag(
            @RequestParam String pregunta
    ) {

        String username = obtenerUsernameActual();

        if (username == null) {
            username = "anonimo";
        }

        System.out.println("==================================");
        System.out.println("NUEVA PREGUNTA RAG");
        System.out.println("Usuario: " + username);
        System.out.println(" Pregunta: " + pregunta);
        System.out.println("==================================");

        SseEmitter emitter = new SseEmitter(0L);

        emitter.onCompletion(() ->
                System.out.println(" SSE completado")
        );

        emitter.onTimeout(() -> {

            System.out.println(" Timeout SSE");

            emitter.complete();
        });

        emitter.onError((e) -> {

            System.out.println(
                    "Error SSE: " + e.getMessage()
            );

            emitter.completeWithError(e);
        });

        chatbotService.streamRespuesta(
                pregunta,
                username,
                emitter
        );

        return emitter;
    }



    @PostMapping("/limpiar")
    public ResponseEntity<?> limpiarHistorial() {

        String username = obtenerUsernameActual();

        if (username == null) {

            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "success", false,
                                    "error", "Usuario no autenticado"
                            )
                    );
        }

        chatbotService.limpiarHistorial(username);

        System.out.println(
                " Historial limpiado para usuario: "
                        + username
        );

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "mensaje", "Historial limpiado correctamente"
                )
        );
    }



    @GetMapping("/historial")
    public ResponseEntity<?> obtenerHistorial() {

        String username = obtenerUsernameActual();

        if (username == null) {

            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "success", false,
                                    "error", "Usuario no autenticado"
                            )
                    );
        }

        var historial =
                chatbotService.obtenerHistorial(username);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "usuario", username,
                        "historial", historial
                )
        );
    }

    private String obtenerUsernameActual() {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (
                auth != null
                        && auth.isAuthenticated()
                        && !"anonymousUser"
                        .equals(auth.getPrincipal())
        ) {

            return auth.getName();
        }

        return null;
    }
}