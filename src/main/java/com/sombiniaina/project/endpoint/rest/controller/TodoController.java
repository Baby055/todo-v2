package com.sombiniaina.project.endpoint.rest.controller;

import com.sombiniaina.project.service.TodoService;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class TodoController {
  private final TodoService todoService;

  public record CreateTodoRequest(String title, String description) {}

  public record UpdateTodoRequest(String title, String description) {}

  @GetMapping("/tasks")
  public String tasks(Model model, Authentication authentication) {
    model.addAttribute("todosList", todoService.getTasksForUser(authentication.getName()));
    return "tasks";
  }

  @PostMapping("/tasks/create")
  public String createTask(
      @ModelAttribute CreateTodoRequest request, Authentication authentication) {
    todoService.createTodo(authentication.getName(), request.title(), request.description());
    return "redirect:/tasks";
  }

  @PostMapping("/tasks/{id}/toggle")
  public String toggleTask(@PathVariable("id") UUID id, Authentication authentication) {
    todoService.toggleTaskCompletion(id, authentication.getName());
    return "redirect:/tasks";
  }

  @PostMapping("/tasks/{id}/update")
  public String updateTask(
      @PathVariable("id") UUID id,
      Authentication authentication,
      @ModelAttribute UpdateTodoRequest request) {
    todoService.updateTodo(id, authentication.getName(), request.title(), request.description());
    return "redirect:/tasks";
  }

  @PostMapping("/tasks/{id}/delete")
  public String deleteTask(@PathVariable("id") UUID id, Authentication authentication) {
    todoService.deleteTodoAsUser(id, authentication.getName());
    return "redirect:/tasks";
  }

  @GetMapping("/admin/dashboard")
  public String adminDashboard(Model model, Authentication authentication) {
    model.addAttribute("adminTodos", todoService.getAllTasksForAdmin());
    return "admin-dashboard";
  }

  @PostMapping("/admin/tasks/{id}/update")
  public String updateTask(@PathVariable("id") UUID id, @ModelAttribute UpdateTodoRequest request) {
    todoService.updateTodoAsAdmin(id, request.title(), request.description());
    return "redirect:/admin/dashboard";
  }

  @PostMapping("/admin/tasks/{id}/delete")
  public String deleteTask(@PathVariable("id") UUID id) {
    todoService.deleteTodoAsAdmin(id);
    return "redirect:/admin/dashboard";
  }
}
