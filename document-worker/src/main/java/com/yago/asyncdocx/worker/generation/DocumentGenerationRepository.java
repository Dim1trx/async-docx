package com.yago.asyncdocx.worker.generation;

import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentGenerationRepository extends JpaRepository<DocumentGeneration, UUID> {

	@EntityGraph(attributePaths = "documentData")
	java.util.Optional<DocumentGeneration> findWithDocumentDataById(UUID id);
}
