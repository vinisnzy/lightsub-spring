package com.vinisnzy.lightsub_auth_service.integrations.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.vinisnzy.lightsub_auth_service.dto.login.LoginRequest;
import com.vinisnzy.lightsub_auth_service.dto.login.LoginResponse;
import com.vinisnzy.lightsub_auth_service.dto.register.RegisterRequest;
import com.vinisnzy.lightsub_auth_service.exceptions.UsernameAlreadyExistsException;
import com.vinisnzy.lightsub_auth_service.model.UserModel;
import com.vinisnzy.lightsub_auth_service.repository.UserRepository;
import com.vinisnzy.lightsub_auth_service.service.AuthService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestPropertySource(properties = {
    "JWT_SECRET=test-secret-key"
})
class AuthServiceIT {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should login successfully and return a JWT token")
    void loginSuccess() {
        // Arrange
        UserModel user = new UserModel();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("password"));

        user = userRepository.save(user);

        LoginRequest request = new LoginRequest("user", "password");

        // Act
        LoginResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.token());
        assertNotNull(user.getId());
        assertTrue(passwordEncoder.matches("password", user.getPassword()));
    }

    @Test
    @DisplayName("Should register a new user successfully")
    void registerSuccess() {
        // Arrange
        RegisterRequest request = new RegisterRequest("newuser", "newpassword");

        // Act
        authService.register(request);

        // Assert
        assertNotNull(userRepository.findByUsername("newuser"));
    }

    @Test
    @DisplayName("Should not register a user with an existing username")
    void registerUsernameAlreadyExists() {
        // Arrange
        UserModel user = new UserModel();
        user.setUsername("existinguser");
        user.setPassword("anypassword");
        userRepository.save(user);

        RegisterRequest request = new RegisterRequest("existinguser", "password");

        // Act & Assert
        UsernameAlreadyExistsException exception = assertThrows(UsernameAlreadyExistsException.class, 
                () -> authService.register(request));
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Username already exists: existinguser");
    }
}
