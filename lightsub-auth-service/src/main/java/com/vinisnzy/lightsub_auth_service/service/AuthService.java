package com.vinisnzy.lightsub_auth_service.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.vinisnzy.lightsub_auth_service.dto.login.LoginRequest;
import com.vinisnzy.lightsub_auth_service.dto.login.LoginResponse;
import com.vinisnzy.lightsub_auth_service.dto.register.RegisterRequest;
import com.vinisnzy.lightsub_auth_service.exceptions.UsernameAlreadyExistsException;
import com.vinisnzy.lightsub_auth_service.model.UserModel;
import com.vinisnzy.lightsub_auth_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  @Value("${security.jwt.secret}")
  private String secret;

  private final AuthenticationManager authenticationManager;

  private final UserRepository repository;

  private final PasswordEncoder passwordEncoder;

  public LoginResponse login(LoginRequest data) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            data.username(),
            data.password()));

    List<String> roles = auth.getAuthorities().stream()
        .map(grantedAuthority -> grantedAuthority.getAuthority())
        .toList();

    Algorithm algorithm = Algorithm.HMAC256(secret);

    String token = JWT.create()
        .withIssuer("lightsub-auth")
        .withSubject(auth.getName())
        .withClaim("roles", roles)
        .withClaim("userId", auth.getPrincipal() instanceof UserModel user ? user.getId().toString() : null)
        .withIssuedAt(Instant.now())
        .withExpiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
        .sign(algorithm);

    return new LoginResponse(token);
  }

  public void register(RegisterRequest data) {
    if (repository.existsByUsername(data.username())) {
      throw new UsernameAlreadyExistsException("Username already exists: " + data.username());
    }
    UserModel user = new UserModel();
    user.setUsername(data.username());
    user.setPassword(passwordEncoder.encode(data.password()));
    repository.save(user);
  }
}
