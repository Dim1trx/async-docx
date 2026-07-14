package com.yago.asyncdocx.api.document;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "document_data")
public class DocumentData {

	@Id
	private UUID id;

	@Column(nullable = false, length = 160)
	private String clientName;

	@Column(nullable = false, length = 160)
	private String providerName;

	@Column(nullable = false, length = 1000)
	private String serviceDescription;

	@Column(nullable = false)
	private LocalDate effectiveDate;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal feeAmount;

	@Column(nullable = false)
	private Instant createdAt;

	protected DocumentData() {
	}

	public DocumentData(UUID id, String clientName, String providerName, String serviceDescription,
			LocalDate effectiveDate, BigDecimal feeAmount, Instant createdAt) {
		this.id = id;
		this.clientName = clientName;
		this.providerName = providerName;
		this.serviceDescription = serviceDescription;
		this.effectiveDate = effectiveDate;
		this.feeAmount = feeAmount;
		this.createdAt = createdAt;
	}

	public UUID getId() {
		return id;
	}

	public String getClientName() {
		return clientName;
	}

	public String getProviderName() {
		return providerName;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	public BigDecimal getFeeAmount() {
		return feeAmount;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
}
