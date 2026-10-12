package com.openground.controller;

import com.openground.dto.HealthStatusDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "Health Check", description = "Backend availability and operational metrics")
public class HealthController {

    @GetMapping
    @Operation(summary = "Check backend and database connectivity health")
    public ResponseEntity<HealthStatusDto> checkHealth() {
        Map<String, Object> details = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();
        details.put("availableProcessors", runtime.availableProcessors());
        details.put("freeMemoryMb", runtime.freeMemory() / (1024 * 1024));
        details.put("totalMemoryMb", runtime.totalMemory() / (1024 * 1024));
        details.put("systemTime", LocalDateTime.now());

        HealthStatusDto status = new HealthStatusDto(
                "UP",
                "OpenGround Outdoor Cricket Ground Booking Backend",
                "1.0.0",
                LocalDateTime.now(),
                details
        );
        return ResponseEntity.ok(status);
    }
}
