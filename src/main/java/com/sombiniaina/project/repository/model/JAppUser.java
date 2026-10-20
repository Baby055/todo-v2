package com.sombiniaina.project.repository.model;

import com.sombiniaina.project.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JAppUser {
  @Id private UUID id;

  @Column(name = "username", unique = true, nullable = false)
  @NotBlank
  private String username;

  @Column(name = "email", unique = true, nullable = false)
  @NotBlank
  private String email;

  @NotBlank private String passwordHash;

  @Enumerated(EnumType.STRING)
  @NotNull
  private Role role;
}
