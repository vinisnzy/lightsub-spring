package com.vinisnzy.lightsub_auth_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.vinisnzy.lightsub_auth_service.dto.login.LoginRequest;
import com.vinisnzy.lightsub_auth_service.dto.login.LoginResponse;
import com.vinisnzy.lightsub_auth_service.dto.register.RegisterRequest;
import com.vinisnzy.lightsub_auth_service.exceptions.UsernameAlreadyExistsException;
import com.vinisnzy.lightsub_auth_service.model.UserModel;
import com.vinisnzy.lightsub_auth_service.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(authService, "secret", "testSecretKey");
    }

    @Test
    @DisplayName("Should login successfully and return a JWT token")
    void loginSuccess() {
        // Arrange
        LoginRequest request = new LoginRequest("user", "password");
        Authentication auth = new UsernamePasswordAuthenticationToken("user", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        UserModel user = new UserModel();
        user.setId(UUID.randomUUID());
        user.setUsername("user");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        // Act
        LoginResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.token());
        assertFalse(response.token().isEmpty());
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    @DisplayName("Should register a new user successfully")
    void registerSuccess() {
        // Arrange
        RegisterRequest request = new RegisterRequest("newuser", "newpassword");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedPassword");

        // Act
        authService.register(request);

        // Assert
        verify(userRepository, times(1)).existsByUsername("newuser");
        verify(passwordEncoder, times(1)).encode("newpassword");
        verify(userRepository, times(1)).save(any(UserModel.class));
    }

    @Test
    @DisplayName("Should not register a user with an existing username")
    void registerUsernameAlreadyExists() {
        // Arrange
        RegisterRequest request = new RegisterRequest("existinguser", "password");
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        // Act & Assert
        UsernameAlreadyExistsException exception = assertThrows(UsernameAlreadyExistsException.class, () -> authService.register(request));
        assertNotNull(exception);
        verify(userRepository, times(1)).existsByUsername("existinguser");
        assertEquals(exception.getMessage(), "Username already exists: existinguser");
    }
}


