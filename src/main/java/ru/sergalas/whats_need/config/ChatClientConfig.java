package ru.sergalas.whats_need.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import ru.sergalas.whats_need.advisers.expansion.ExpansionQueryAdviser;
import ru.sergalas.whats_need.repository.ChatRepository;
import ru.sergalas.whats_need.util.PostgresChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;

@RequiredArgsConstructor
@Configuration
public class ChatClientConfig {

    private final ChatRepository chatRepository;
    private final VectorStore vectorStore;

    private static final PromptTemplate PROMPT_TEMPLATE = new PromptTemplate(
            "{query}\n\n" +
                    "Контекст:\n" +
                    "---------------------\n" +
                    "{question_answer_context}\n" +
                    "---------------------\n\n" +
                    "Отвечай только на основе контекста выше. Если информации нет в контексте, сообщи, что не можешь ответить."
    );

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultAdvisors(
                ExpansionQueryAdviser.builder().order(0).build(),
                getHistoryAdvisor(1),
                SimpleLoggerAdvisor.builder().order(2).build(),
                getRagAdvisor(3),
                SimpleLoggerAdvisor.builder().order(4).build()
        )
        .defaultOptions(OllamaOptions.builder()
                .temperature(0.3)
                .topP(0.7)
                .topK(4)
                .repeatPenalty(1.1)
                .build())
        .build();
    }

    private Advisor getRagAdvisor(int order) {
        return QuestionAnswerAdvisor.builder(vectorStore)
                .promptTemplate(PROMPT_TEMPLATE)
                .searchRequest(SearchRequest.builder().topK(4).similarityThreshold(0.7).build())
                .order(order)
                .build();
    }

    private Advisor getHistoryAdvisor(int order) {
        return MessageChatMemoryAdvisor.builder(getChatMemory()).order(order).build();
    }

    private ChatMemory getChatMemory() {
       return PostgresChatMemory.builder().maxMessages(2).chatRepository(chatRepository).build();
    }
}
