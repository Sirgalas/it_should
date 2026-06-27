package ru.sergalas.whats_need.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.sergalas.whats_need.repository.LoadedDocumentRepository;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.util.DigestUtils;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.data.util.Pair;
import ru.sergalas.whats_need.entity.LoadedDocument;
import org.springframework.boot.CommandLineRunner;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadedDocumentService implements CommandLineRunner {
    private final LoadedDocumentRepository repository;
    private final ResourcePatternResolver resolver;
    private final VectorStore vectorStore;

    @SneakyThrows
    public void loadDocuments() {
        List<Resource> resources = Arrays.stream(resolver.getResources("classpath*:/knowledgebase/**/*.txt")).toList();

        resources.stream()
                .map(resource -> Pair.of(resource,getContendHash(resource)))
                .filter(pair -> !repository.existsByFilenameAndContentHash(
                        pair.getFirst().getFilename(),
                        pair.getSecond())
                )
                .forEach(pair -> {
                    List<Document> documents = new TextReader(pair.getFirst()).get();
                    TokenTextSplitter textSplitter = TokenTextSplitter.builder().withChunkSize(500).build();
                    List<Document> chunks = textSplitter.apply(documents);
                    vectorStore.accept(chunks);
                    LoadedDocument loadedDocument = LoadedDocument.builder()
                            .documentType("txt")
                            .chunkCount(chunks.size())
                            .filename(pair.getFirst().getFilename())
                            .contentHash(pair.getSecond())
                            .build();
                    repository.save(loadedDocument);

                });

    }

    @SneakyThrows
    private String getContendHash(Resource resource) {
        return DigestUtils.md5DigestAsHex(resource.getInputStream()).toUpperCase();
    }

    @Override
    public void run(String... args) throws Exception {
        loadDocuments();
    }
}
