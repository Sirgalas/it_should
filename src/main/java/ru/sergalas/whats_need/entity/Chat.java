package ru.sergalas.whats_need.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
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
@Table(name = "chat")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Chat {
    @Id
    @Column(name = "id", nullable = false)
    UUID id;

    String title;

    @CreationTimestamp
    LocalDateTime createdAt;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EntryChat> history = new ArrayList<>();

    public void setId() {
        this.id = UUID.randomUUID();
    }
}