package com.yago.asyncdocx.api.document;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentDataRepository extends JpaRepository<DocumentData, UUID> {
}
