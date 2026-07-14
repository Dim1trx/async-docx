package com.yago.asyncdocx.worker.generation;

import com.yago.asyncdocx.worker.document.DocumentData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "document_generation")
public class DocumentGeneration {

	@Id
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "document_data_id", nullable = false)
	private DocumentData documentData;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 32)
	private GenerationStatus status;

	@Column(length = 1000)
	private String generatedFilePath;

	@Column(length = 2000)
	private String failureReason;

	@Column(nullable = false)
	private Instant createdAt;

	@Column(nullable = false)
	private Instant updatedAt;

	private Instant completedAt;

	protected DocumentGeneration() {
	}

	public UUID getId() {
		return id;
	}

	public DocumentData getDocumentData() {
		return documentData;
	}

	public GenerationStatus getStatus() {
		return status;
	}

	public void markProcessing(Instant now) {
		if (status != GenerationStatus.QUEUED) {
			throw new IllegalStateException("Only queued generations can be processed.");
		}
		status = GenerationStatus.PROCESSING;
		updatedAt = now;
	}

	public void markCompleted(String generatedFilePath, Instant now) {
		if (status != GenerationStatus.PROCESSING) {
			throw new IllegalStateException("Only processing generations can be completed.");
		}
		status = GenerationStatus.COMPLETED;
		this.generatedFilePath = generatedFilePath;
		failureReason = null;
		updatedAt = now;
		completedAt = now;
	}

	public void markFailed(String failureReason, Instant now) {
		status = GenerationStatus.FAILED;
		this.failureReason = failureReason;
		updatedAt = now;
		completedAt = now;
	}
}
