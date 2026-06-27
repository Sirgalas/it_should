package ru.sergalas.whats_need.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import ru.sergalas.whats_need.repository.ChatRepository;
import ru.sergalas.whats_need.util.PostgresChatMemory;

@RequiredArgsConstructor
@Configuration
public class ChatClientConfig {

    private final ChatRepository chatRepository;
    private final VectorStore vectorStore;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultAdvisors(getHistoryAdvisor(),getRagAdvisor()).build();
    }

    private Advisor getRagAdvisor() {
        return QuestionAnswerAdvisor.builder(vectorStore).build();
    }

    private Advisor getHistoryAdvisor() {
        return MessageChatMemoryAdvisor.builder(getChatMemory()).build();
    }

    private ChatMemory getChatMemory() {
       return PostgresChatMemory.builder().maxMessages(2).chatRepository(chatRepository).build();
    }
}
