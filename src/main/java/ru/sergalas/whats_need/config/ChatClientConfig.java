package ru.sergalas.whats_need.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.sergalas.whats_need.repository.ChatRepository;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class ChatClientConfig {

    private final ChatRepository chatRepository;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultAdvisors(getAdvisor()).build();
    }

    private Advisor getAdvisor() {
        return MessageChatMemoryAdvisor.builder(getChatMemory()).build();
    }

    private ChatMemory getChatMemory() {
       return MessageWindowChatMemory.builder().maxMessages(2).chatMemoryRepository(chatRepository).build();
    }
}
