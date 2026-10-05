package com.network.api;

import com.dto.request.AuthRequest;
import com.dto.response.AuthResponse;
import com.entities.User;
import com.service.UserService;
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
  public ResponseEntity<?> login(@RequestBody AuthRequest loginRequest) {
    User authenticated = userService.authenticateUser(loginRequest.getId(),loginRequest.getUsername());
    if (authenticated != null) {
      AuthResponse authResponse = new AuthResponse(authenticated.getId(), authenticated.getUsername());
      return ResponseEntity.ok(authResponse);
    } else {
      return ResponseEntity.status(401).body(Map.of("message", "Incorrect ID / Username"));
    }
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody AuthRequest registerRequest) {
    User registered = userService.registerUser(registerRequest.getUsername());
    if (registered != null) {
      AuthResponse registerResponse = new AuthResponse(registered.getId(), registered.getUsername());
      return ResponseEntity.ok(registerResponse);
    } else {
      return ResponseEntity.badRequest().body(Map.of("message","Username already exists"));
    }
  }
}
