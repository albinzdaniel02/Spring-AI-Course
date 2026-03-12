package com.edstem.ollama.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PromptTemplateController {

    private final ChatClient chatClient;

    public PromptTemplateController(ChatClient.Builder chatClientBuilder) {
        this.chatClient =
                chatClientBuilder
                        .defaultSystem(
                                """
                        You are an helpful assistant only providing positive, useful and technical feedback
                        related to Company Policies.
                        """)
                        .build();
    }

    @Value("classpath:/promptTemplates/userPromptTemplate.st")
    Resource promptTemplate;

    @GetMapping("/email")
    public ResponseEntity<String> getEmailResponse(
            @RequestParam(defaultValue = "How can you help me?") String customerName,
            @RequestParam(defaultValue = "How can you help me?") String customerMessage) {

        String response =
                chatClient
                        .prompt()
                        .system(
                                """
                        You are a professional customer support person which helps drafting
                        email responses to improve the efficiency of the team.
                        """)
                        .user(
                                promptUserSpec ->
                                        promptUserSpec
                                                .text(promptTemplate)
                                                .param("customerName", customerName)
                                                .param("customerMessage", customerMessage))
                        .call()
                        .content();
        return ResponseEntity.ok(response);
    }
}
