package com.edstem.ollama.config;

import org.h2.server.web.JakartaWebServlet;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder.build();
    }

    @Bean
    public ServletRegistrationBean<JakartaWebServlet> h2Console() {
        return new ServletRegistrationBean<>(new JakartaWebServlet(), "/h2-console/*");
    }
}
