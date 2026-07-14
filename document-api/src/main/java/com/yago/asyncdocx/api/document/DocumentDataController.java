package com.yago.asyncdocx.api.document;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/document-data")
public class DocumentDataController {

	private final DocumentDataService service;

	public DocumentDataController(DocumentDataService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	CreateDocumentDataResponse create(@Valid @RequestBody CreateDocumentDataRequest request) {
		DocumentData documentData = service.create(request);
		return new CreateDocumentDataResponse(documentData.getId());
	}
}
