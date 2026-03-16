package com.edstem.ollama.controller;

import com.edstem.ollama.model.CountryCities;
import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StructuredOutputController {

    private final ChatClient chatClient;

    public StructuredOutputController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.defaultAdvisors(new SimpleLoggerAdvisor()).build();
    }

    @GetMapping("/chat-bean")
    public ResponseEntity<CountryCities> getCountries(
            @RequestParam(defaultValue = "Provide the cities in India") String message) {
        CountryCities response = chatClient.prompt(message).call().entity(CountryCities.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat-bean-object")
    public ResponseEntity<CountryCities> getCountriesObject(
            @RequestParam(defaultValue = "Provide the cities in India") String message) {
        CountryCities response =
                chatClient
                        .prompt(message)
                        .call()
                        .entity(new BeanOutputConverter<>(CountryCities.class));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat-bean-list")
    public ResponseEntity<List<String>> getCitiesList(
            @RequestParam(defaultValue = "Provide the cities in India") String message) {
        List<String> response = chatClient.prompt(message).call().entity(new ListOutputConverter());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat-bean-map")
    public ResponseEntity<Map<String, Object>> getCountriesCitiesMap(
            @RequestParam(defaultValue = "Provide the cities in India") String message) {
        Map<String, Object> response =
                chatClient.prompt(message).call().entity(new MapOutputConverter());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat-bean-object-list")
    public ResponseEntity<List<CountryCities>> getCountriesCitiesList(
            @RequestParam(defaultValue = "Provide the cities in India") String message) {
        List<CountryCities> response =
                chatClient
                        .prompt(message)
                        .call()
                        .entity(new ParameterizedTypeReference<List<CountryCities>>() {});
        return ResponseEntity.ok(response);
    }
}
