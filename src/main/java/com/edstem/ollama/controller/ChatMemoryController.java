package com.edstem.ollama.controller;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChatMemoryController {

    private final ChatClient chatClient;

    public ChatMemoryController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        Advisor loggerAdvisor = SimpleLoggerAdvisor.builder().build();
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        this.chatClient = chatClientBuilder.defaultAdvisors(loggerAdvisor, memoryAdvisor).build();
    }

    @GetMapping("/chat-memory")
    public ResponseEntity<String> getChat(
            @RequestHeader String username,
            @RequestParam(defaultValue = "Hello from the chat client!") String message) {
        String response =
                chatClient
                        .prompt()
                        .user(message)
                        .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, username))
                        .call()
                        .content();
        return ResponseEntity.ok(response);
    }
}
