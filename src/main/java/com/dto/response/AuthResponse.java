package com.dto.response;

public class AuthResponse {
  private Long id;
  private String username;

  public AuthResponse(Long id, String username) {
    this.id = id;
    this.username = username;
  }
}
