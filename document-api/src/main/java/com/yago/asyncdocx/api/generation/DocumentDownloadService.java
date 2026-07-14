package com.yago.asyncdocx.api.generation;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DocumentDownloadService {

	private final DocumentGenerationRepository repository;

	public DocumentDownloadService(DocumentGenerationRepository repository) {
		this.repository = repository;
	}

	@Transactional(readOnly = true)
	public Resource getGeneratedDocument(UUID generationId) {
		DocumentGeneration generation = repository.findById(generationId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document generation was not found."));

		if (generation.getStatus() != GenerationStatus.COMPLETED) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Document generation is not completed. Current status: " + generation.getStatus());
		}

		if (generation.getGeneratedFilePath() == null || generation.getGeneratedFilePath().isBlank()) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Generated file path is missing.");
		}

		Path generatedFile = Path.of(generation.getGeneratedFilePath()).normalize();
		if (!Files.isRegularFile(generatedFile)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Generated document file was not found.");
		}

		return new PathResource(generatedFile);
	}
}
