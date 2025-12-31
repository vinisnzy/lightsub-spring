package com.vinisnzy.lightsub_auth_service.model;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.vinisnzy.lightsub_auth_service.model.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class UserModel implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  UUID id;

  @Column(nullable = false, unique = true)
  String username;

  @Column(nullable = false)
  String password;

  @Enumerated(EnumType.STRING)
  UserRole role = UserRole.USER;

  Boolean enabled = true;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(() -> "ROLE_" + role.name());
  }
}
