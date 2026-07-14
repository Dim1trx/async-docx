package com.yago.asyncdocx.worker.template;

import static org.assertj.core.api.Assertions.assertThat;

import com.yago.asyncdocx.worker.document.DocumentData;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.ByteArrayResource;

class TemplateDocumentGeneratorTest {

	@TempDir
	Path tempDir;

	@Test
	void generatesDocxFromTemplatePlaceholders() throws Exception {
		String template = "Agreement for {{clientName}} with {{providerName}}\n"
				+ "{{serviceDescription}}\n"
				+ "{{effectiveDate}}\n"
				+ "{{feeAmount}}";
		TemplateDocumentGenerator generator = new TemplateDocumentGenerator(
				tempDir.toString(),
				new ByteArrayResource(template.getBytes()));
		DocumentData documentData = new DocumentData(
				UUID.randomUUID(),
				"Acme Corp",
				"Async DOCX LLC",
				"Generate monthly service reports.",
				LocalDate.parse("2026-08-01"),
				new BigDecimal("199.90"),
				Instant.parse("2026-07-13T12:00:00Z"));

		String generatedPath = generator.generate(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), documentData);

		Path output = Path.of(generatedPath);
		assertThat(output).exists();
		assertThat(output.getFileName().toString()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa.docx");
		assertThat(Files.readAllBytes(output)).startsWith(new byte[] { 'P', 'K' });
		assertThat(generator.render(template, documentData))
				.contains("Acme Corp", "Async DOCX LLC", "Generate monthly service reports.", "2026-08-01", "$199.90")
				.doesNotContain("{{");
	}
}
