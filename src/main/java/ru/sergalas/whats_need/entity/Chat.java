package ru.sergalas.whats_need.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.stringtemplate.v4.ST;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Chat {
    @Id
    @Column(name = "id", nullable = false)
    UUID id;

    String title;

    @CreationTimestamp
    LocalDateTime createdAt;

    Boolean active;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "chat_id")
    private List<EntryChat> history = new ArrayList<>();

    public void addEntry(EntryChat entry) {
        this.history.add(entry);
    }

    public void setId() {
        this.id = UUID.randomUUID();
    }

}