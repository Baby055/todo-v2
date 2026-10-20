package com.sombiniaina.project.exception;

public class AccountAlreadyExistsException extends RuntimeException {
  public AccountAlreadyExistsException(String message) {
    super(message);
  }
}
