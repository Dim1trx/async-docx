package com.yago.asyncdocx.worker.generation;

import com.yago.asyncdocx.worker.document.DocumentData;
import com.yago.asyncdocx.worker.template.DocumentGenerator;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentGenerationProcessor {

	private final DocumentGenerationRepository repository;
	private final DocumentGenerator documentGenerator;
	private final Clock clock;

	public DocumentGenerationProcessor(DocumentGenerationRepository repository, DocumentGenerator documentGenerator, Clock clock) {
		this.repository = repository;
		this.documentGenerator = documentGenerator;
		this.clock = clock;
	}

	@Transactional
	public void process(UUID generationId) {
		DocumentGeneration generation = repository.findWithDocumentDataById(generationId)
				.orElseThrow(() -> new IllegalArgumentException("Document generation was not found: " + generationId));

		if (generation.getStatus() == GenerationStatus.COMPLETED || generation.getStatus() == GenerationStatus.FAILED) {
			return;
		}

		try {
			generation.markProcessing(Instant.now(clock));
			DocumentData documentData = generation.getDocumentData();
			String generatedPath = documentGenerator.generate(generation.getId(), documentData);
			generation.markCompleted(generatedPath, Instant.now(clock));
		}
		catch (Exception exception) {
			String failureReason = exception.getMessage() == null
					? exception.getClass().getSimpleName()
					: exception.getMessage();
			generation.markFailed(failureReason, Instant.now(clock));
		}
	}
}
