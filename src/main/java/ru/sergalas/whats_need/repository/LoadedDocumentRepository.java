package ru.sergalas.whats_need.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sergalas.whats_need.entity.LoadedDocument;

public interface LoadedDocumentRepository extends JpaRepository<LoadedDocument, Long> {
    Boolean existsByFilenameAndContentHash(String filename, String contentHash);
}