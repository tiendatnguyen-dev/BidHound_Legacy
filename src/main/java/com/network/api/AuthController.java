package com.network.api;

import com.dto.ApiResponse;
import com.dto.request.AuthRequest;
import com.dto.response.AuthResponse;
import com.entities.User;
import com.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest loginRequest) {
    User authenticated = userService.authenticateUser(loginRequest.getId(),loginRequest.getUsername());
    AuthResponse authResponse = new AuthResponse(authenticated.getId(), authenticated.getUsername());
      return ResponseEntity.status(HttpStatus.OK)
              .body(ApiResponse.success(authResponse, "Log in successfully"));
  }

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody AuthRequest registerRequest) {
    User registered = userService.registerUser(registerRequest.getUsername());
    AuthResponse registerResponse = new AuthResponse(registered.getId(), registered.getUsername());
    return ResponseEntity.status(HttpStatus.CREATED)
              .body(ApiResponse.success(registerResponse,"Registered successfully"));
  }
}
