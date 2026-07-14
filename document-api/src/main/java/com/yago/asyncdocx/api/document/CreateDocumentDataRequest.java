package com.yago.asyncdocx.api.document;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateDocumentDataRequest(
		@NotBlank @Size(max = 160) String clientName,
		@NotBlank @Size(max = 160) String providerName,
		@NotBlank @Size(max = 1000) String serviceDescription,
		@NotNull LocalDate effectiveDate,
		@NotNull @DecimalMin(value = "0.01") BigDecimal feeAmount) {
}
