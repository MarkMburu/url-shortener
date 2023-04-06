package dev.markmburu.urlshortner.dtos.responses;

import lombok.Builder;

@Builder
public record HealthCheckResponseDto(String message) {
}
