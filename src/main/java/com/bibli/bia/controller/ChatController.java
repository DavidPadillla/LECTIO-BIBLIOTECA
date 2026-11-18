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
            @RequestParam String pregunta,
            jakarta.servlet.http.HttpServletResponse response
    ) {

        String username = obtenerUsernameActual();

        if (username == null) {
            username = "anonimo";
        }


        response.setHeader("Cache-Control", "no-cache, no-transform");
        response.setHeader("X-Accel-Buffering", "no");
        SseEmitter emitter = new SseEmitter(60000L);
        try {
            emitter.send(SseEmitter.event().comment("connected"));
        } catch (java.io.IOException error) {
            emitter.completeWithError(error);
            return emitter;
        }

        emitter.onTimeout(() -> {


            emitter.complete();
        });

        emitter.onError((e) -> {


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
