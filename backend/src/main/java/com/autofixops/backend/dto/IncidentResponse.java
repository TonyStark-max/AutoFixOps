package com.autofixops.backend.dto;

import com.autofixops.backend.entity.Incident;
import java.time.LocalDateTime;

public class IncidentResponse {
    private String incidentId;
    private String status;
    private String errorType;
    private String errorMessage;
    private String fingerprint;
    private LocalDateTime createdAt;

    public IncidentResponse(Incident incident) {
        this.incidentId = incident.getIncidentId();
        this.status = incident.getStatus();
        this.errorType = incident.getErrorType();
        this.errorMessage = incident.getErrorMessage();
        this.fingerprint = incident.getFingerprint();
        this.createdAt = incident.getCreatedAt();
    }

    public String getIncidentId() { return incidentId; }
    public String getStatus() { return status; }
    public String getErrorType() { return errorType; }
    public String getErrorMessage() { return errorMessage; }
    public String getFingerprint() { return fingerprint; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
