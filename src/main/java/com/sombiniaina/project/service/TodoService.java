package com.sombiniaina.project.service;

import com.sombiniaina.project.exception.TaskNotFoundException;
import com.sombiniaina.project.mapper.TodoMapper;
import com.sombiniaina.project.model.Todo;
import com.sombiniaina.project.repository.TodoRepository;
import com.sombiniaina.project.repository.model.JTodo;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;

    public List<Todo> getTasksForUser(String currentUsername){
        return todoMapper.toModel(todoRepository.findByUsername(currentUsername));
    }

    public Todo createTodo(String currentUsername, String title ,String description){
        JTodo jTodo =
                JTodo.builder()
                        .id(UUID.randomUUID())
                        .username(currentUsername)
                        .title(title)
                        .description(description)
                        .completed(false)
                        .creationDate(Instant.now())
                        .build();
        return todoMapper.toModel(todoRepository.save(jTodo));
    }

    public Todo updateTodo(UUID taskId, String currentUsername, String title, String description){
        JTodo jTodo =
                todoRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        if (!jTodo.getUsername().equals(currentUsername)){
            throw new AccessDeniedException("Access denied");
        }
        jTodo.setTitle(title);
        jTodo.setDescription(description);
        return  todoMapper.toModel(todoRepository.save(jTodo));
    }

    public Todo toggleTaskCompletion(UUID taskId, String currentUsername){
        JTodo jTodo =
                todoRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        if (!jTodo.getUsername().equals(currentUsername)){
            throw new AccessDeniedException("Access denied");
        }
        jTodo.setCompleted(!jTodo.getCompleted());
        return  todoMapper.toModel(todoRepository.save(jTodo));
    }

    public Todo updateTodoAsAdmin(UUID taskId, String title, String description){
        JTodo jTodo =
                todoRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        jTodo.setTitle(title);
        jTodo.setDescription(description);
        return  todoMapper.toModel(todoRepository.save(jTodo));
    }

    public void deleteTodoAsUser(UUID taskId, String currentUsername){
        JTodo jTodo =
                todoRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        if (!jTodo.getUsername().equals(currentUsername)){
            throw new AccessDeniedException("Access denied");
        }
        todoRepository.deleteById(taskId);
    }

    public List<Todo> getAllTasksForAdmin(){
        return todoMapper.toModel(todoRepository.findAll());
    }

    public void deleteTodoAsAdmin(UUID taskId){
        JTodo jTodo =
                todoRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        todoRepository.deleteById(taskId);
    }
}
