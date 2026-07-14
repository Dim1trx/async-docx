package com.yago.asyncdocx.worker.messaging;

import java.util.UUID;

public record DocumentGenerationRequestedEvent(UUID generationId) {
}
