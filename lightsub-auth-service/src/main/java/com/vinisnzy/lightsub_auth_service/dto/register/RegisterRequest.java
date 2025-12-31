package com.vinisnzy.lightsub_auth_service.dto.register;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(

    @NotBlank(message = "Username is required")
    String username,

    @NotBlank(message = "Password is required")
    String password
) {
}
