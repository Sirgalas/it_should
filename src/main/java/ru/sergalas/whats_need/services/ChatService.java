package ru.sergalas.whats_need.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.sergalas.whats_need.data.*;
import ru.sergalas.whats_need.entity.Chat;
import ru.sergalas.whats_need.exception.ChatNotFoundException;
import ru.sergalas.whats_need.mapper.ChatMapper;
import ru.sergalas.whats_need.repository.ChatRepository;


import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatMapper mapper;
    private final ChatRepository repository;
    private final ChatClient chatClient;

    public List<SidebarData> getSideBar() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream().map(mapper::toSidebarData).toList();
    }

    public EditChatData getEditChat(UUID id) {
        return new EditChatData(repository.findById(id).orElseThrow(() -> new ChatNotFoundException("чат не найден")).getTitle());
    }

    public void editChat( UUID id,EditChatData data) {
        Chat chat = repository.findById(id).orElseThrow(() -> new ChatNotFoundException("чат не найден"));
        chat.setTitle(data.title());
        repository.save(chat);
    }

    public OneData getOneData(UUID id) {
        return mapper.toOneData(repository.findById(id).orElseThrow(() -> new ChatNotFoundException("чат не найден")));
    }

    public void deleteChat(UUID id) {
        Chat chat = repository.findById(id).orElseThrow(() -> new ChatNotFoundException("чат не найден"));
        repository.delete(chat);
    }


    public UUID create(CreateChatData data) {
        Chat chat = mapper.toEntityCreate(data);
        UUID id = UUID.randomUUID();
        chat.setId(id);
        chat.setActive(true);
        repository.save(chat);
        Chat active = repository.findByActive(true).orElse(null);
        if(active != null) {
            active.setActive(false);
            repository.save(active);
        }
        return id;
    }

    public void sentQuestion(PromptData data, UUID id) {
        Chat chat = repository.findById(id).orElseThrow(() -> new ChatNotFoundException("чат не найден"));
//        entryService.addChatEntry(chat, data.prompt(),USER);
        String answer = chatClient.prompt().user(data.prompt()).call().content();
//        entryService.addChatEntry(chat, answer,ASSISTANT);
    }


    public SseEmitter sendQuestion(PromptData data, UUID id) {
        Chat chat = repository.findById(id).orElseThrow(() -> new ChatNotFoundException("чат не найден"));
//        entryService.addChatEntry(chat, data.prompt(), USER);
        StringBuilder answer = new StringBuilder();
        SseEmitter sseEmitter = new SseEmitter(0L);
        chatClient
            .prompt(data.prompt())
            .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,id))
            .stream()
            .chatResponse()
            .subscribe(response -> getToken(response, sseEmitter,answer),
                sseEmitter::completeWithError/*,
                () -> entryService.addChatEntry(chat, answer.toString(), ASSISTANT)*/
            );

        return sseEmitter;
    }

    @SneakyThrows
    private static void getToken(ChatResponse response, SseEmitter sseEmitter, StringBuilder answer){
        String output = response.getResult().getOutput().getText();
        sseEmitter.send(output);
        answer.append(output);
    }
}
