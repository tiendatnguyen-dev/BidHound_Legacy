package com.dto.request;

public class AuthRequest {
  private String username;
  private Long id;

  public AuthRequest(String username, Long id) {
    this.username = username;
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }
}
