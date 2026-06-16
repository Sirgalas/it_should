//package ru.sergalas.whats_need.util;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.ai.chat.memory.ChatMemory;
//import org.springframework.ai.chat.messages.Message;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import ru.sergalas.whats_need.entity.Chat;
//import ru.sergalas.whats_need.entity.EntryChat;
//import ru.sergalas.whats_need.exception.ChatNotFoundException;
//import ru.sergalas.whats_need.repository.ChatRepository;
//import ru.sergalas.whats_need.services.EntryService;
//
//import java.util.List;
//import java.util.UUID;
//
//import static ru.sergalas.whats_need.enums.RoleEnum.USER;
//import static ru.sergalas.whats_need.enums.RoleEnum.ASSISTANT;
//
//@RequiredArgsConstructor
//@Component
//public class PostgrerChatMemory implements ChatMemory {
//
//    private final ChatRepository chatRepository;
//    private final EntryService entryService;
//
//
//    @Override
//    @Transactional
//    public void add(String conversationId, List<Message> messages) {
//        messages.forEach(message -> {
//            Chat chat = getChat(conversationId);
//            entryService.addChatEntry(chat, message);
//        });
//
//
//    }
//
//    @Override
//    public List<Message> get(String conversationId) {
//        Chat chat = getChat(conversationId);
//        return chat.getHistory().stream().map(EntryChat::toMessage).toList();
//    }
//
//    @Override
//    public void clear(String conversationId) {
//
//    }
//
//    private Chat getChat(String conversationId) {
//        return chatRepository.findById(UUID.fromString(conversationId)).orElseThrow(() -> new ChatNotFoundException("чат не найден"));
//    }
//}
