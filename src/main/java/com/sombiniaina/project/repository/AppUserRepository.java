package com.sombiniaina.project.repository;

import com.sombiniaina.project.model.Role;
import com.sombiniaina.project.repository.model.JAppUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<JAppUser, UUID> {
  Optional<JAppUser> findByUsername(String username);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  boolean existsByRole(Role role);
}
