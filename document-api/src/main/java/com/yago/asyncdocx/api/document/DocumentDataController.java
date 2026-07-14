package com.yago.asyncdocx.api.document;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/document-data")
@Tag(name = "Document Data", description = "Stores service agreement input data.")
public class DocumentDataController {

	private final DocumentDataService service;

	public DocumentDataController(DocumentDataService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
			summary = "Create document data",
			description = "Stores the service agreement data that will be used later by an asynchronous document generation request.",
			responses = {
					@ApiResponse(responseCode = "201", description = "Document data was created."),
					@ApiResponse(responseCode = "400", description = "The request body is invalid.", content = @Content)
			})
	CreateDocumentDataResponse create(@Valid @RequestBody CreateDocumentDataRequest request) {
		DocumentData documentData = service.create(request);
		return new CreateDocumentDataResponse(documentData.getId());
	}
}
