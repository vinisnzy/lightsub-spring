package com.vinisnzy.lightsub_auth_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinisnzy.lightsub_auth_service.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

  Optional<UserModel> findByUsername(String username);

  Boolean existsByUsername(String username);
}
