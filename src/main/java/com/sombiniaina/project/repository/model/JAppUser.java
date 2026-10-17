package com.sombiniaina.project.repository.model;

import com.sombiniaina.project.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JAppUser {
    @Id private UUID id;

    @NotBlank private String username;

    @NotBlank private String email;

    @NotBlank private String password_hash;

    @Enumerated(EnumType.STRING)
    private Role role;
}
