package com.sombiniaina.project.repository;

import com.sombiniaina.project.repository.model.JTodo;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<JTodo, UUID> {}
