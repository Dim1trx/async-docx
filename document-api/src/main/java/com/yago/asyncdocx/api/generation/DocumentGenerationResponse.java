package com.yago.asyncdocx.api.generation;

import java.time.Instant;
import java.util.UUID;

public record DocumentGenerationResponse(
		UUID generationId,
		UUID documentDataId,
		GenerationStatus status,
		Instant createdAt,
		Instant updatedAt,
		Instant completedAt,
		String failureReason) {

	static DocumentGenerationResponse from(DocumentGeneration generation) {
		return new DocumentGenerationResponse(
				generation.getId(),
				generation.getDocumentData().getId(),
				generation.getStatus(),
				generation.getCreatedAt(),
				generation.getUpdatedAt(),
				generation.getCompletedAt(),
				generation.getFailureReason());
	}
}
