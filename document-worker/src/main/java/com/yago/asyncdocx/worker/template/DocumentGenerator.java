package com.yago.asyncdocx.worker.template;

import com.yago.asyncdocx.worker.document.DocumentData;
import java.util.UUID;

public interface DocumentGenerator {

	String generate(UUID generationId, DocumentData documentData);
}
