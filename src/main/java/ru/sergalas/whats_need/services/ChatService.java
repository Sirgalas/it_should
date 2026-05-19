package ru.sergalas.whats_need.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sergalas.whats_need.data.EditChatData;
import ru.sergalas.whats_need.data.OneData;
import ru.sergalas.whats_need.data.SidebarData;
import ru.sergalas.whats_need.entity.Chat;
import ru.sergalas.whats_need.exception.ChatNotFoundException;
import ru.sergalas.whats_need.mapper.OneChatMapper;
import ru.sergalas.whats_need.mapper.SidebarChatMapper;
import ru.sergalas.whats_need.repository.ChatRepository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final SidebarChatMapper mapper;
    private final ChatRepository repository;
    private final OneChatMapper oneMapper;

    public List<SidebarData> getSideBar() {
        return repository.findAll().stream().map(mapper::toData).toList();
    }

    public EditChatData getEditChat(UUID id) {
        return new EditChatData(repository.findById(id).orElseThrow(() -> new ChatNotFoundException("чат не найден")).getTitle());
    }

    public void editChat( UUID id,EditChatData data) {
        Chat chat = repository.findById(id).orElseThrow(() -> new ChatNotFoundException("чат не найден"));
        chat.setTitle(data.name());
        repository.save(chat);
    }

    public OneData getOneData(UUID id) {
        return oneMapper.toData(repository.findById(id).orElseThrow(() -> new ChatNotFoundException("чат не найден")));
    }

    public void deleteChat(UUID id) {
        Chat chat = repository.findById(id).orElseThrow(() -> new ChatNotFoundException("чат не найден"));
        repository.delete(chat);
    }
}
