package dev.markmburu.urlshortner.controllers;

import dev.markmburu.urlshortner.dtos.responses.HealthCheckResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping(path = "/api/v1/ping")
public class PingController {

	@GetMapping
	public ResponseEntity<HealthCheckResponseDto> healthCheck() {
		return ResponseEntity.ok()
			.body(HealthCheckResponseDto
				.builder()
				.message("Hello World!!")
				.build());
	}
}
