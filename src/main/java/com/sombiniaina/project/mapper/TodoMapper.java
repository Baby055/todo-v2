package com.sombiniaina.project.mapper;

import com.sombiniaina.project.model.Todo;
import com.sombiniaina.project.repository.model.JTodo;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TodoMapper {
  public List<Todo> toModel(List<JTodo> jTodos) {
    return jTodos.stream().map(this::toModel).toList();
  }

  public Todo toModel(JTodo jTodo) {
    return Todo.builder()
        .id(jTodo.getId())
        .username(jTodo.getUsername())
        .title(jTodo.getTitle())
        .description(jTodo.getDescription())
        .completed(jTodo.getCompleted())
        .s3AttachmentKey(jTodo.getS3AttachmentKey())
        .creationDate(jTodo.getCreationDate())
        .build();
  }

  public List<JTodo> toEntity(List<Todo> todos) {
    return todos.stream().map(this::toEntity).toList();
  }

  public JTodo toEntity(Todo todo) {
    return JTodo.builder()
        .id(todo.id())
        .username(todo.username())
        .title(todo.title())
        .description(todo.description())
        .completed(todo.completed())
        .s3AttachmentKey(todo.s3AttachmentKey())
        .creationDate(todo.creationDate())
        .build();
  }
}
