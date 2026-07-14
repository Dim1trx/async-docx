package com.yago.asyncdocx.worker.messaging;

import com.yago.asyncdocx.worker.generation.DocumentGenerationProcessor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DocumentGenerationListener {

	private final DocumentGenerationProcessor processor;

	public DocumentGenerationListener(DocumentGenerationProcessor processor) {
		this.processor = processor;
	}

	@KafkaListener(topics = "${app.kafka.document-generation-topic}")
	public void onMessage(DocumentGenerationRequestedEvent event) {
		processor.process(event.generationId());
	}
}
