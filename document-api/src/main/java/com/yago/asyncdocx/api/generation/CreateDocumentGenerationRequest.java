package com.yago.asyncdocx.api.generation;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateDocumentGenerationRequest(@NotNull UUID documentDataId) {
}
