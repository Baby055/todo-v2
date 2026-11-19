package com.sombiniaina.project.endpoint.rest.controller;

import com.sombiniaina.project.exception.AccountAlreadyExistsException;
import com.sombiniaina.project.model.Role;
import com.sombiniaina.project.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class AuthController {
  private final AccountService accountService;

  public record RegisterRequest(
      @NotBlank(message = "Username is required") String username,
      @NotBlank(message = "Email is required") @Email(message = "Email is invalid") String email,
      @NotBlank(message = "Password is required")
          @Size(min = 8, message = "Password must be at least 8 characters")
          String password) {}

  @GetMapping("/register")
  public String register(Model model) {
    model.addAttribute("registerRequest", new RegisterRequest(null, null, null));
    return "register";
  }

  @PostMapping("/register")
  public String register(
      @ModelAttribute @Valid RegisterRequest registerRequest, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "register";
    }
    try {
      accountService.addAccount(
          registerRequest.username(),
          registerRequest.email(),
          registerRequest.password(),
          Role.USER);
    } catch (AccountAlreadyExistsException e) {
      bindingResult.reject("accountExists", e.getMessage());
      return "register";
    }
    return "redirect:/login";
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }
}
