package ru.sergalas.whats_need.repository;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sergalas.whats_need.entity.Chat;
import ru.sergalas.whats_need.entity.EntryChat;

import java.util.*;

public interface ChatRepository extends JpaRepository<Chat, UUID>, ChatMemoryRepository {
    Optional<Chat> findByActive(boolean active);

    @Query("SELECT c FROM Chat c LEFT JOIN FETCH c.history WHERE c.id = :id")
    Optional<Chat> findByIdWithHistory(UUID id);

    @Override
    default List<String> findConversationIds(){
        return findAll().stream().map(Chat::getId).map(String::valueOf).toList();
    }

    @Override
    default List<Message> findByConversationId(String conversationId) {
        if(conversationId == ChatMemory.DEFAULT_CONVERSATION_ID){
            return new ArrayList<>();
        }
        Chat chat = findByIdWithHistory(UUID.fromString(conversationId)).orElseThrow();
        return chat.getHistory().stream().map(EntryChat::toMessage).toList();
    }

    @Override
    default void saveAll(String conversationId, List<Message> messages) {
        if(conversationId == ChatMemory.DEFAULT_CONVERSATION_ID){
            return;
        }
        Chat chat = findByIdWithHistory(UUID.fromString(conversationId)).orElseThrow();
        messages.stream().map(EntryChat::toEntryChat).forEach(chat::addEntry);
        save(chat);
    }

    @Override
    default void deleteByConversationId(String conversationId) {
    }
}