package com.sombiniaina.project.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "todo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JTodo {
  @Id private UUID id;

  @NotBlank private String username;
  @NotBlank private String title;

  private String description;

  @NotNull private Boolean completed;
  private String s3AttachmentKey;

  private Instant creationDate;
}
