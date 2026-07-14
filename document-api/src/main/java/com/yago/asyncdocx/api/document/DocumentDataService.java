package com.yago.asyncdocx.api.document;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentDataService {

	private final DocumentDataRepository repository;
	private final Clock clock;

	public DocumentDataService(DocumentDataRepository repository, Clock clock) {
		this.repository = repository;
		this.clock = clock;
	}

	@Transactional
	public DocumentData create(CreateDocumentDataRequest request) {
		DocumentData documentData = new DocumentData(
				UUID.randomUUID(),
				request.clientName(),
				request.providerName(),
				request.serviceDescription(),
				request.effectiveDate(),
				request.feeAmount(),
				Instant.now(clock));

		return repository.save(documentData);
	}
}
