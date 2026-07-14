package com.yago.asyncdocx.api.generation;

import com.yago.asyncdocx.api.document.DocumentData;
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

	public DocumentGeneration(UUID id, DocumentData documentData, Instant now) {
		this.id = id;
		this.documentData = documentData;
		this.status = GenerationStatus.QUEUED;
		this.createdAt = now;
		this.updatedAt = now;
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

	public String getGeneratedFilePath() {
		return generatedFilePath;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public Instant getCompletedAt() {
		return completedAt;
	}
}
