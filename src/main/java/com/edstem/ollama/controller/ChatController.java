package com.edstem.ollama.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/chat")
    public ResponseEntity<String> getChat(
            @RequestParam(defaultValue = "Hello from the chat client!") String message) {
        String response = chatClient.prompt(message).call().content();
        return ResponseEntity.ok(response);
    }
}
