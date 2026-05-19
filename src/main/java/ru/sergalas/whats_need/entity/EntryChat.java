package ru.sergalas.whats_need.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;
import ru.sergalas.whats_need.enums.RoleEnum;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "entry_chat")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EntryChat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    UUID id;

    String content;

    @Enumerated(EnumType.STRING)
    private RoleEnum roleEnum;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne()
    Chat chat;


}