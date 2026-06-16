package ru.sergalas.whats_need.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;
import ru.sergalas.whats_need.enums.RoleEnum;
import ru.sergalas.whats_need.enums.TypeEnum;

import javax.management.relation.Role;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "entry_chat")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EntryChat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    UUID id;

    @Column(columnDefinition = "TEXT")
    String content;

    @Enumerated(EnumType.STRING)
    private RoleEnum roleEnum;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    private Chat chat;

    public static EntryChat toEntryChat(Message message) {
        return EntryChat.builder()
            .roleEnum(RoleEnum.getRoleByName(message.getMessageType().getValue()))
            .content(message.getText())
            .build();
    }

    public Message toMessage() {
        return roleEnum.getMessage(content);
    }

}