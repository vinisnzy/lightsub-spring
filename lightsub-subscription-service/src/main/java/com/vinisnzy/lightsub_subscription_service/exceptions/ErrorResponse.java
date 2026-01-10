package com.vinisnzy.lightsub_subscription_service.exceptions;

import java.time.Instant;

public record ErrorResponse(
    int status,
    String message,
    Instant timestamp,
    String path
) {
}
