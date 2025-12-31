package com.vinisnzy.lightsub_auth_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinisnzy.lightsub_auth_service.dto.login.LoginRequest;
import com.vinisnzy.lightsub_auth_service.dto.login.LoginResponse;
import com.vinisnzy.lightsub_auth_service.dto.register.RegisterRequest;
import com.vinisnzy.lightsub_auth_service.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService service;

  @GetMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest data) {
    LoginResponse response = service.login(data);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody RegisterRequest data) {
    service.register(data);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
