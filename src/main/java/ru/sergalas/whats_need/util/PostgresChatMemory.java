package ru.sergalas.whats_need.util;

import lombok.*;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import ru.sergalas.whats_need.entity.Chat;
import ru.sergalas.whats_need.entity.EntryChat;
import ru.sergalas.whats_need.exception.ChatNotFoundException;
import ru.sergalas.whats_need.repository.ChatRepository;
import ru.sergalas.whats_need.services.EntryService;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@Data
@Builder
@RequiredArgsConstructor
public class PostgresChatMemory implements ChatMemory {

    private final ChatRepository chatRepository;
    private final EntryService entryService;

    private Integer maxMessages;

    @Override
    public void add(String conversationId, List<Message> messages) {
        Chat chat = getChat(conversationId);
        messages.forEach(message -> {
            entryService.addChatEntry(chat, message);
        });
        chatRepository.save(chat);

    }

    @Override
    public List<Message> get(String conversationId) {
        Chat chat = getChat(conversationId);
        return chat.
                getHistory()
                .stream()
                .sorted(Comparator.comparing(EntryChat::getCreatedAt).reversed())
                .map(EntryChat::toMessage)
                .limit(maxMessages)
                .toList();
    }

    @Override
    public void clear(String conversationId) {

    }

    private Chat getChat(String conversationId) {
        return chatRepository.findById(UUID.fromString(conversationId)).orElseThrow(() -> new ChatNotFoundException("чат не найден"));
    }
}

