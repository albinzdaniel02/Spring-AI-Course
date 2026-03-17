package com.edstem.ollama.controller;

import com.edstem.ollama.tools.TimeTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TimeToolController {

    private final ChatClient chatClient;

    public TimeToolController(ChatClient.Builder chatClientBuilder, TimeTool timeTool) {
        this.chatClient =
                chatClientBuilder
                        .defaultAdvisors(new SimpleLoggerAdvisor())
                        .defaultTools(timeTool)
                        .build();
    }

    @GetMapping("/time")
    public ResponseEntity<String> getTime(@RequestParam String message) {
        String response = chatClient.prompt(message).call().content();
        return ResponseEntity.ok(response);
    }
}
