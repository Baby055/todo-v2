package com.sombiniaina.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sombiniaina.project.exception.TaskNotFoundException;
import com.sombiniaina.project.mapper.TodoMapper;
import com.sombiniaina.project.repository.TodoRepository;
import com.sombiniaina.project.repository.model.JTodo;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

  @Mock private TodoRepository todoRepository;
  @Mock private TodoMapper todoMapper;
  @InjectMocks private TodoService todoService;

  @Test
  void toggleTaskCompletion_togglesCompletedStatus() {
    UUID id = UUID.randomUUID();
    JTodo jTodo =
        JTodo.builder().id(id).username("alice").title("t").completed(false).build();
    when(todoRepository.findById(id)).thenReturn(Optional.of(jTodo));
    when(todoRepository.save(any(JTodo.class))).thenReturn(jTodo);

    todoService.toggleTaskCompletion(id, "alice");

    assertThat(jTodo.getCompleted()).isTrue();
    verify(todoRepository).save(jTodo);
  }

  @Test
  void toggleTaskCompletion_throwsWhenTaskNotFound() {
    UUID id = UUID.randomUUID();
    when(todoRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> todoService.toggleTaskCompletion(id, "alice"))
        .isInstanceOf(TaskNotFoundException.class);

    verify(todoRepository, never()).save(any());
  }

  @Test
  void toggleTaskCompletion_throwsWhenTaskBelongsToAnotherUser() {
    UUID id = UUID.randomUUID();
    JTodo jTodo =
        JTodo.builder().id(id).username("bob").title("t").completed(false).build();
    when(todoRepository.findById(id)).thenReturn(Optional.of(jTodo));

    assertThatThrownBy(() -> todoService.toggleTaskCompletion(id, "alice"))
        .isInstanceOf(AccessDeniedException.class);

    assertThat(jTodo.getCompleted()).isFalse();
    verify(todoRepository, never()).save(any());
  }

  @Test
  void updateTodo_updatesTitleAndDescriptionForOwner() {
    UUID id = UUID.randomUUID();
    JTodo jTodo =
        JTodo.builder()
            .id(id)
            .username("alice")
            .title("old title")
            .description("old description")
            .completed(false)
            .build();
    when(todoRepository.findById(id)).thenReturn(Optional.of(jTodo));
    when(todoRepository.save(any(JTodo.class))).thenReturn(jTodo);

    todoService.updateTodo(id, "alice", "new title", "new description");

    assertThat(jTodo.getTitle()).isEqualTo("new title");
    assertThat(jTodo.getDescription()).isEqualTo("new description");
    verify(todoRepository).save(jTodo);
  }

  @Test
  void updateTodo_throwsWhenTaskNotFound() {
    UUID id = UUID.randomUUID();
    when(todoRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> todoService.updateTodo(id, "alice", "new title", "new description"))
        .isInstanceOf(TaskNotFoundException.class);

    verify(todoRepository, never()).save(any());
  }

  @Test
  void updateTodo_throwsWhenTaskBelongsToAnotherUser() {
    UUID id = UUID.randomUUID();
    JTodo jTodo =
        JTodo.builder()
            .id(id)
            .username("bob")
            .title("old title")
            .description("old description")
            .completed(false)
            .build();
    when(todoRepository.findById(id)).thenReturn(Optional.of(jTodo));

    assertThatThrownBy(() -> todoService.updateTodo(id, "alice", "new title", "new description"))
        .isInstanceOf(AccessDeniedException.class);

    assertThat(jTodo.getTitle()).isEqualTo("old title");
    assertThat(jTodo.getDescription()).isEqualTo("old description");
    verify(todoRepository, never()).save(any());
  }

  @Test
  void deleteTodoAsUser_deletesTaskForOwner() {
    UUID id = UUID.randomUUID();
    JTodo jTodo =
        JTodo.builder().id(id).username("alice").title("t").completed(false).build();
    when(todoRepository.findById(id)).thenReturn(Optional.of(jTodo));

    todoService.deleteTodoAsUser(id, "alice");

    verify(todoRepository).deleteById(id);
  }

  @Test
  void deleteTodoAsUser_throwsWhenTaskBelongsToAnotherUser() {
    UUID id = UUID.randomUUID();
    JTodo jTodo =
        JTodo.builder().id(id).username("bob").title("t").completed(false).build();
    when(todoRepository.findById(id)).thenReturn(Optional.of(jTodo));

    assertThatThrownBy(() -> todoService.deleteTodoAsUser(id, "alice"))
        .isInstanceOf(AccessDeniedException.class);

    verify(todoRepository, never()).deleteById(any());
  }
}
