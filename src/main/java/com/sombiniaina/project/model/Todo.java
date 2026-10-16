package com.sombiniaina.project.model;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Todo(
    UUID id,
    String username,
    String title,
    String description,
    Boolean completed,
    String s3AttachmentKey,
    Instant creationDate) {}
