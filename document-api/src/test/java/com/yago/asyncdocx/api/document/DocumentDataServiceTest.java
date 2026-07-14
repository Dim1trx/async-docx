package com.yago.asyncdocx.api.document;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class DocumentDataServiceTest {

	private final DocumentDataRepository repository = Mockito.mock(DocumentDataRepository.class);
	private final Clock clock = Clock.fixed(Instant.parse("2026-07-13T12:00:00Z"), ZoneOffset.UTC);
	private final DocumentDataService service = new DocumentDataService(repository, clock);

	@Test
	void createsDocumentDataWithValidatedBusinessFields() {
		when(repository.save(any(DocumentData.class))).thenAnswer(invocation -> invocation.getArgument(0));
		CreateDocumentDataRequest request = new CreateDocumentDataRequest(
				"Acme Corp",
				"Async DOCX LLC",
				"Generate monthly service reports.",
				LocalDate.parse("2026-08-01"),
				new BigDecimal("199.90"));

		DocumentData created = service.create(request);

		ArgumentCaptor<DocumentData> captor = ArgumentCaptor.forClass(DocumentData.class);
		verify(repository).save(captor.capture());
		assertThat(created.getId()).isNotNull();
		assertThat(captor.getValue().getClientName()).isEqualTo("Acme Corp");
		assertThat(captor.getValue().getProviderName()).isEqualTo("Async DOCX LLC");
		assertThat(captor.getValue().getServiceDescription()).isEqualTo("Generate monthly service reports.");
		assertThat(captor.getValue().getEffectiveDate()).isEqualTo(LocalDate.parse("2026-08-01"));
		assertThat(captor.getValue().getFeeAmount()).isEqualByComparingTo("199.90");
		assertThat(captor.getValue().getCreatedAt()).isEqualTo(Instant.parse("2026-07-13T12:00:00Z"));
	}
}
