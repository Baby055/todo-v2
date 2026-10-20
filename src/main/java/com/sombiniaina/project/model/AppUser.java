package com.sombiniaina.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;
import lombok.Builder;

@Builder
public record AppUser(
    UUID id, String username, String email, @JsonIgnore String passwordHash, Role role) {}
