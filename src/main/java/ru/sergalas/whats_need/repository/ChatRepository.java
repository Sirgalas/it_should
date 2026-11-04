package ru.sergalas.whats_need.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sergalas.whats_need.entity.Chat;

import java.util.*;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    Optional<Chat> findByActive(Boolean active);

    @Query("SELECT c FROM Chat c LEFT JOIN FETCH c.history WHERE c.id = :id")
    Optional<Chat> findByIdWithHistory(@Param("id") UUID id);

}