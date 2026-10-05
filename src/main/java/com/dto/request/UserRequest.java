package com.dto.request;

public class UserRequest {
  private Long userId;

  public Long getUserId() {
    return userId;
  }

  public UserRequest(Long userId) {
    this.userId = userId;
  }
}
