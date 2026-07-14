package com.yago.asyncdocx.api.generation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Document Generations", description = "Queues, tracks, and downloads generated DOCX files.")
public class DocumentGenerationController {

	private final DocumentGenerationService service;
	private final DocumentDownloadService downloadService;

	public DocumentGenerationController(DocumentGenerationService service, DocumentDownloadService downloadService) {
		this.service = service;
		this.downloadService = downloadService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.ACCEPTED)
	@Operation(
			summary = "Request document generation",
			description = "Creates a queued generation request and publishes an event for the worker service.",
			responses = {
					@ApiResponse(responseCode = "202", description = "Generation request was queued."),
					@ApiResponse(responseCode = "400", description = "The request body is invalid.", content = @Content),
					@ApiResponse(responseCode = "404", description = "The referenced document data does not exist.", content = @Content)
			})
	DocumentGenerationResponse create(@Valid @RequestBody CreateDocumentGenerationRequest request) {
		return DocumentGenerationResponse.from(service.create(request));
	}

	@GetMapping("/{generationId}")
	@Operation(
			summary = "Get document generation status",
			description = "Returns the current status, timestamps, and failure details for a document generation request.",
			responses = {
					@ApiResponse(responseCode = "200", description = "Generation request was found."),
					@ApiResponse(responseCode = "404", description = "Generation request does not exist.", content = @Content)
			})
	DocumentGenerationResponse get(@PathVariable UUID generationId) {
		return DocumentGenerationResponse.from(service.get(generationId));
	}

	@GetMapping("/{generationId}/download")
	@Operation(
			summary = "Download generated DOCX",
			description = "Downloads the generated DOCX file when the generation status is completed.",
			responses = {
					@ApiResponse(responseCode = "200", description = "Generated document file."),
					@ApiResponse(responseCode = "404", description = "Generation request or generated file does not exist.", content = @Content),
					@ApiResponse(responseCode = "409", description = "Generation is not completed yet.", content = @Content),
					@ApiResponse(responseCode = "500", description = "Generation metadata is inconsistent.", content = @Content)
			})
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
