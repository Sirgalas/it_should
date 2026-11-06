package ru.sergalas.whats_need.advisers.rag;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import ru.sergalas.whats_need.enums.QuestionEnum;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
public class RagAdvisor implements BaseAdvisor {


    @Builder.Default
    private static final PromptTemplate template = PromptTemplate.builder().template("""
            CONTEXT: {context}
            Question: {question}
            """).build();

    private VectorStore vectorStore;

    @Builder.Default
    private SearchRequest searchRequest = SearchRequest.builder().topK(2).similarityThreshold(0.62).build();

    @Getter
    private int order;

    public static RagAdvisorBuilder build(VectorStore vectorStore) {
        return new RagAdvisorBuilder().vectorStore(vectorStore);

    }


    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        String originalUserQuestion = chatClientRequest.prompt().getUserMessage().getText();
        String queryToRag = chatClientRequest.context().getOrDefault(QuestionEnum.ENRICHED_QUESTION.getValue(),originalUserQuestion).toString();
        List<Document> documents = vectorStore.similaritySearch(
            SearchRequest.from(searchRequest)
                .query(queryToRag)
                .topK(searchRequest.getTopK()*2)
                .similarityThreshold(searchRequest.getSimilarityThreshold())
                .build()
        );
        if(documents == null || documents.isEmpty()) {
            return chatClientRequest.mutate().context("CONTEXT", "ТУТ ПУСТО").build();
        }

        BM25RerankEngine rerankEngine = BM25RerankEngine.builder().build();
        rerankEngine.rerank(documents,queryToRag,searchRequest.getTopK());

        String llmContext = documents.stream().map(Document::getText).collect(Collectors.joining(System.lineSeparator()));
        String finalUSerPrompt = template.render(
                Map.of("context", llmContext, "question", originalUserQuestion)
        );

        return chatClientRequest.mutate().prompt(chatClientRequest.prompt().augmentSystemMessage(finalUSerPrompt)).build();
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        return chatClientResponse;
    }
}
