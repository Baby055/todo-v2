package com.sombiniaina.project.model;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record Todo(
    UUID id,
    String username,
    String title,
    String description,
    Boolean completed,
    String s3AttachmentKey,
    Instant creationDate) {}
