package com.yago.asyncdocx.api.generation;

import com.yago.asyncdocx.api.document.DocumentData;
import com.yago.asyncdocx.api.document.DocumentDataRepository;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class DocumentGenerationService {

	private final DocumentDataRepository documentDataRepository;
	private final DocumentGenerationRepository generationRepository;
	private final DocumentGenerationPublisher publisher;
	private final Clock clock;

	public DocumentGenerationService(DocumentDataRepository documentDataRepository,
			DocumentGenerationRepository generationRepository, DocumentGenerationPublisher publisher, Clock clock) {
		this.documentDataRepository = documentDataRepository;
		this.generationRepository = generationRepository;
		this.publisher = publisher;
		this.clock = clock;
	}

	@Transactional
	public DocumentGeneration create(CreateDocumentGenerationRequest request) {
		DocumentData documentData = documentDataRepository.findById(request.documentDataId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document data was not found."));

		DocumentGeneration generation = new DocumentGeneration(UUID.randomUUID(), documentData, Instant.now(clock));
		DocumentGeneration savedGeneration = generationRepository.save(generation);
		publisher.publish(savedGeneration);
		return savedGeneration;
	}

	@Transactional(readOnly = true)
	public DocumentGeneration get(UUID generationId) {
		return generationRepository.findById(generationId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document generation was not found."));
	}
}
