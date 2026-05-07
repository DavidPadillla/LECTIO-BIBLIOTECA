package com.bibli.bia.controller;

import com.bibli.bia.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatbotService chatbotService;

    @Autowired
    public ChatController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @GetMapping(value = "/groq", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter preguntarGroq(@RequestParam String pregunta) {

        String username = obtenerUsernameActual();

        // Timeout infinito
        SseEmitter emitter = new SseEmitter(0L);

        emitter.onCompletion(() -> System.out.println("✅ SSE completado"));

        emitter.onTimeout(() -> {
            System.out.println("⏰ Timeout SSE");
            emitter.complete();
        });

        emitter.onError((e) -> {
            System.out.println("🔥 Error SSE: " + e.getMessage());
            e.printStackTrace();
            emitter.completeWithError(e);
        });

        chatbotService.streamRespuesta(pregunta, username, emitter);

        return emitter;
    }

    private String obtenerUsernameActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }

        return null;
    }
}