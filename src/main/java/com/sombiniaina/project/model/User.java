package com.sombiniaina.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

import java.util.UUID;

@Builder
public record User(UUID id, String username, String email, @JsonIgnore String password_hash, Role role) {}
