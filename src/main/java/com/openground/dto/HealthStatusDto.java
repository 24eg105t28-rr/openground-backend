package com.openground.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class HealthStatusDto {
    private String status;
    private String application;
    private String version;
    private LocalDateTime timestamp;
    private Map<String, Object> details;

    public HealthStatusDto() {
    }

    public HealthStatusDto(String status, String application, String version, LocalDateTime timestamp, Map<String, Object> details) {
        this.status = status;
        this.application = application;
        this.version = version;
        this.timestamp = timestamp;
        this.details = details;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getApplication() { return application; }
    public void setApplication(String application) { this.application = application; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Map<String, Object> getDetails() { return details; }
    public void setDetails(Map<String, Object> details) { this.details = details; }
}
