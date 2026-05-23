package ru.sergalas.whats_need.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sergalas.whats_need.entity.EntryChat;

import java.util.UUID;

public interface EntryChatRepository extends JpaRepository<EntryChat, UUID> {
}