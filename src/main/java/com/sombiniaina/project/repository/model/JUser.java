package com.sombiniaina.project.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JUser {
    @Id private UUID id;

    @NotBlank private String username;

    @NotBlank private String email;

    @NotBlank private String password;
}
