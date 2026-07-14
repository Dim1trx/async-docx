package com.yago.asyncdocx.api.generation;

import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/document-generations")
public class DocumentGenerationController {

	private final DocumentGenerationService service;
	private final DocumentDownloadService downloadService;

	public DocumentGenerationController(DocumentGenerationService service, DocumentDownloadService downloadService) {
		this.service = service;
		this.downloadService = downloadService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.ACCEPTED)
	DocumentGenerationResponse create(@Valid @RequestBody CreateDocumentGenerationRequest request) {
		return DocumentGenerationResponse.from(service.create(request));
	}

	@GetMapping("/{generationId}")
	DocumentGenerationResponse get(@PathVariable UUID generationId) {
		return DocumentGenerationResponse.from(service.get(generationId));
	}

	@GetMapping("/{generationId}/download")
	ResponseEntity<Resource> download(@PathVariable UUID generationId) {
		Resource document = downloadService.getGeneratedDocument(generationId);
		ContentDisposition contentDisposition = ContentDisposition.attachment()
				.filename("document-" + generationId + ".docx")
				.build();

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
				.contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
				.body(document);
	}
}
