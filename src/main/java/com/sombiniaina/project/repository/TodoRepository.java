package com.sombiniaina.project.repository;

import com.sombiniaina.project.repository.model.JTodo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TodoRepository extends JpaRepository<JTodo, UUID> {
}
