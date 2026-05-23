package ru.sergalas.whats_need.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sergalas.whats_need.entity.Chat;
import ru.sergalas.whats_need.entity.EntryChat;
import ru.sergalas.whats_need.enums.RoleEnum;
import ru.sergalas.whats_need.repository.EntryChatRepository;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EntryService {

    private final EntryChatRepository repository;

    public void addChatEntry(Chat chat, String prompt, RoleEnum roleEnum) {
        EntryChat entryChat = EntryChat.builder().chat(chat).content(prompt).roleEnum(roleEnum).build();
        repository.save(entryChat);
    }
}
