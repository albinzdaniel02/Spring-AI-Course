package com.edstem.ollama.controller;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/rag")
public class RAGController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("classpath:/promptTemplates/systemPromptRandomDataTemplate.st")
    Resource promptTemplate;

    public RAGController(
            ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, VectorStore vectorStore) {
        Advisor loggerAdvisor = SimpleLoggerAdvisor.builder().build();
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        this.chatClient = chatClientBuilder.defaultAdvisors(loggerAdvisor, memoryAdvisor).build();
        this.vectorStore = vectorStore;
    }

    @GetMapping
    public ResponseEntity<Flux<String>> randomChat(
            @RequestHeader String username, @RequestParam String message) {
        SearchRequest searchRequest =
                SearchRequest.builder().query(message).topK(3).similarityThreshold(0.5).build();
        List<Document> similarDocs = vectorStore.similaritySearch(searchRequest);
        String similarContext =
                similarDocs.stream()
                        .map(Document::getText)
                        .collect(Collectors.joining(System.lineSeparator()));

        Flux<String> response =
                chatClient
                        .prompt()
                        .system(
                                promptSystemSpec ->
                                        promptSystemSpec
                                                .text(promptTemplate)
                                                .param("document", similarContext))
                        .advisors(a -> a.param(CONVERSATION_ID, username))
                        .user(message)
                        .stream()
                        .content();
        return ResponseEntity.ok(response);
    }
}
