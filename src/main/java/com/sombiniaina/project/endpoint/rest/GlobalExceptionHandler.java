package com.sombiniaina.project.endpoint.rest;

import com.sombiniaina.project.exception.AccountAlreadyExistsException;
import com.sombiniaina.project.exception.TaskNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(TaskNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleTaskNotFoundException(TaskNotFoundException e, Model model) {
    model.addAttribute("message", e.getMessage());
    return "error/404";
  }

  @ExceptionHandler(AccountAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public String handleAccountAlreadyExistsException(AccountAlreadyExistsException e, Model model) {
    model.addAttribute("message", e.getMessage());
    return "error/409";
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public String handleAccessDeniedException(AccessDeniedException e, Model model) {
    model.addAttribute("message", e.getMessage());
    return "error/403";
  }
}
