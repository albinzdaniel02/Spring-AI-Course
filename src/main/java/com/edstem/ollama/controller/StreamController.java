package com.edstem.ollama.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class StreamController {

    private final ChatClient chatClient;

    public StreamController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    // Hit from browser since it's streaming the text instead of waiting for the complete response
    // to be built
    @GetMapping("/stream")
    public ResponseEntity<Flux<String>> getChat(
            @RequestParam(defaultValue = "Hello from the chat client!") String message) {
        Flux<String> response = chatClient.prompt(message).stream().content();
        return ResponseEntity.ok(response);
    }
}
