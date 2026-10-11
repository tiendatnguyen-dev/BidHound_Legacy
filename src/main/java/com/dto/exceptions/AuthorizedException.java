package com.dto.exceptions;

public class AuthorizedException extends RuntimeException {
  public AuthorizedException(String message) {
    super(message);
  }
}
