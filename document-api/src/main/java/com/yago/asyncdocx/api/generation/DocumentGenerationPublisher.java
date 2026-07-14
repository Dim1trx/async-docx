package com.yago.asyncdocx.api.generation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DocumentGenerationPublisher {

	private final KafkaTemplate<String, DocumentGenerationRequestedEvent> kafkaTemplate;
	private final String topic;

	public DocumentGenerationPublisher(
			KafkaTemplate<String, DocumentGenerationRequestedEvent> kafkaTemplate,
			@Value("${app.kafka.document-generation-topic}") String topic) {
		this.kafkaTemplate = kafkaTemplate;
		this.topic = topic;
	}

	public void publish(DocumentGeneration generation) {
		DocumentGenerationRequestedEvent event = new DocumentGenerationRequestedEvent(generation.getId());
		kafkaTemplate.send(topic, generation.getId().toString(), event);
	}
}
