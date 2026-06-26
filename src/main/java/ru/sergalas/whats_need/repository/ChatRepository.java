package ru.sergalas.whats_need.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.sergalas.whats_need.entity.Chat;

import java.util.*;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    Optional<Chat> findByActive(Boolean active);

}