package ru.sergalas.whats_need.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "loaded_document")
public class LoadedDocument {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private String contentHash;

    private String documentType;

    private Integer chunkCount;

    @CreationTimestamp
    private LocalDateTime loadedAt ;
}
