package com.yago.asyncdocx.worker.template;

import com.yago.asyncdocx.worker.document.DocumentData;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class TemplateDocumentGenerator implements DocumentGenerator {

	private final Path storagePath;
	private final Resource templateResource;
	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
	private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

	public TemplateDocumentGenerator(
			@Value("${app.documents.storage-path}") String storagePath,
			@Value("${app.documents.template-path}") Resource templateResource) {
		this.storagePath = Path.of(storagePath).normalize();
		this.templateResource = templateResource;
		this.currencyFormatter.setRoundingMode(RoundingMode.HALF_UP);
	}

	@Override
	public String generate(UUID generationId, DocumentData documentData) {
		try {
			Files.createDirectories(storagePath);
			Path outputPath = storagePath.resolve(generationId + ".docx").normalize();
			String template = readTemplate();

			try (XWPFDocument document = new XWPFDocument()) {
				for (String paragraphText : render(template, documentData).split("\\R")) {
					XWPFParagraph paragraph = document.createParagraph();
					XWPFRun run = paragraph.createRun();
					run.setText(paragraphText);
				}

				try (var outputStream = Files.newOutputStream(outputPath)) {
					document.write(outputStream);
				}
			}

			return outputPath.toAbsolutePath().toString();
		}
		catch (IOException exception) {
			throw new IllegalStateException("Could not generate DOCX file.", exception);
		}
	}

	private String readTemplate() throws IOException {
		try (InputStream inputStream = templateResource.getInputStream()) {
			return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
		}
	}

	String render(String template, DocumentData documentData) {
		Map<String, String> placeholders = Map.of(
				"{{clientName}}", documentData.getClientName(),
				"{{providerName}}", documentData.getProviderName(),
				"{{serviceDescription}}", documentData.getServiceDescription(),
				"{{effectiveDate}}", dateFormatter.format(documentData.getEffectiveDate()),
				"{{feeAmount}}", currencyFormatter.format(documentData.getFeeAmount()));

		String rendered = template;
		for (Map.Entry<String, String> entry : placeholders.entrySet()) {
			rendered = rendered.replace(entry.getKey(), entry.getValue());
		}
		return rendered;
	}
}
