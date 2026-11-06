package ru.sergalas.whats_need.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.sergalas.whats_need.advisers.expansion.ExpansionQueryAdviser;
import ru.sergalas.whats_need.advisers.rag.RagAdvisor;
import ru.sergalas.whats_need.repository.ChatRepository;
import ru.sergalas.whats_need.services.EntryService;
import ru.sergalas.whats_need.util.PostgresChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;

@RequiredArgsConstructor
@Configuration
public class ChatClientConfig {

    private final ChatRepository chatRepository;
    private final VectorStore vectorStore;
    private final ChatModel chatModel;
    private final EntryService entryService;

    private static final PromptTemplate SYSTEM_PROMPT = new PromptTemplate(
            """
                    Ты, Java-разработчик и эксперт по Java, Hibernate, SQl, Spring и системному дизайну.
                    Отвечай от первого лица, кратко и по делу.
                    Вопрос может быть о СЛЕДСТВИИ факта из Context.
                    ВСЕГДА связывай: факт Context → вопрос.
                    Нет связи, даже косвенной = "я не нашел это в документации"
                    Есть связь = отвечай.
            """

    );

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultAdvisors(
                ExpansionQueryAdviser.builder(chatModel).order(0).build(),
                getHistoryAdvisor(1),
                SimpleLoggerAdvisor.builder().order(2).build(),
                RagAdvisor.build(vectorStore).order(3)
                    .searchRequest(
                        SearchRequest.builder()
                        .topK(3)
                        .similarityThreshold(0.61)
                        .build()
                ).build(),
                SimpleLoggerAdvisor.builder().order(4).build()
        )
        .defaultOptions(OllamaOptions.builder()
                .temperature(0.3)
                .topP(0.7)
                .topK(4)
                .repeatPenalty(1.1)
                .build())
                .defaultSystem(SYSTEM_PROMPT.render())
        .build();
    }

    private Advisor getHistoryAdvisor(int order) {
        return MessageChatMemoryAdvisor.builder(getChatMemory()).order(order).build();
    }

    private ChatMemory getChatMemory() {
       return PostgresChatMemory.builder()
               .maxMessages(2)
               .chatRepository(chatRepository)
               .entryService(entryService)
               .build();
    }
}
