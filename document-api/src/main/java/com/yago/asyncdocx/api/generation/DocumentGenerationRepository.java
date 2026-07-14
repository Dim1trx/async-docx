package com.yago.asyncdocx.api.generation;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentGenerationRepository extends JpaRepository<DocumentGeneration, UUID> {
}
