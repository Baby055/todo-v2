package com.sombiniaina.project.endpoint.rest.controller;

import com.sombiniaina.project.service.TodoService;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class TodoController {
  private final TodoService todoService;

  public record CreateTodoRequest(@NotBlank String title, String description) {}

  public record UpdateTodoRequest(@NotBlank String title, String description) {}

  @GetMapping("/tasks")
  public String tasks(Model model, Authentication authentication) {
    model.addAttribute("todosList", todoService.getTasksForUser(authentication.getName()));
    return "tasks";
  }

  @PostMapping("/tasks/create")
  public String createTask(
          @ModelAttribute @Valid CreateTodoRequest request,
          BindingResult bindingResult,
          RedirectAttributes redirectAttributes,
          Authentication authentication) {
    if (bindingResult.hasErrors()) {
      redirectAttributes.addFlashAttribute("error", "Please correct the errors above.");
      return "redirect:/tasks";
    }
    todoService.createTodo(
            authentication.getName(),
            request.title(),
            request.description());
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
      RedirectAttributes redirectAttributes,
      @ModelAttribute @Valid UpdateTodoRequest request,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      redirectAttributes.addFlashAttribute("error", "Please correct the errors above.");
      return "redirect:/tasks";
    }
    todoService.updateTodo(id, authentication.getName(), request.title(), request.description());
    return "redirect:/tasks";
  }

  @PostMapping("/tasks/{id}/delete")
  public String deleteTask(@PathVariable("id") UUID id, Authentication authentication) {
    todoService.deleteTodoAsUser(id, authentication.getName());
    return "redirect:/tasks";
  }

  @GetMapping("/admin/dashboard")
  public String adminDashboard(Model model) {
    model.addAttribute("adminTodos", todoService.getAllTasksForAdmin());
    return "admin-dashboard";
  }

  @PostMapping("/admin/tasks/{id}/update")
  public String updateTaskAsAdmin(
          @PathVariable("id") UUID id,
          @ModelAttribute @Valid UpdateTodoRequest request,
          BindingResult bindingResult,
          RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      redirectAttributes.addFlashAttribute("error", "Please correct the errors above.");
      return "redirect:/admin/dashboard";
    }
    todoService.updateTodoAsAdmin(id, request.title(), request.description());
    return "redirect:/admin/dashboard";
  }

  @PostMapping("/admin/tasks/{id}/delete")
  public String deleteTaskAsAdmin(@PathVariable("id") UUID id) {
    todoService.deleteTodoAsAdmin(id);
    return "redirect:/admin/dashboard";
  }
}
