package com.edstem.ollama.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatOptionsController {

    private final ChatClient chatClient;

    public ChatOptionsController(ChatClient.Builder chatClientBuilder) {
        ChatOptions chatOptions =
                ChatOptions.builder()
                        .maxTokens(200)
                        .frequencyPenalty(0.5)
                        .temperature(1.0)
                        .topK(100)
                        .build();

        this.chatClient =
                chatClientBuilder
                        .defaultOptions(chatOptions)
                        .defaultSystem(
                                """
                                You are supposed to only answer questions related to countries and their capitals.
                                Decline politely when the conversation is not related. """)
                        .build();
    }

    @GetMapping("/chat-options")
    public ResponseEntity<String> getResponse(@RequestParam String message) {

        String response = chatClient.prompt().user(message).call().content();
        return ResponseEntity.ok(response);
    }
}
