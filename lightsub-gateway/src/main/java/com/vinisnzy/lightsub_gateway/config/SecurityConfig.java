package com.vinisnzy.lightsub_gateway.config;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authorizeExchange(exchange -> exchange
            .pathMatchers("/auth/**").permitAll()
            .anyExchange().authenticated())
        .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> {
        }))
        .build();
  }

  @Bean
  public ReactiveJwtDecoder jwtDecoder(@Value("${security.jwt.secret}") String secret) {
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
    return NimbusReactiveJwtDecoder
        .withSecretKey(secretKey)
        .macAlgorithm(MacAlgorithm.HS256)
        .build();
  }
}
