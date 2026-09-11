package com.autofixops.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "incident_id", unique = true, nullable = false)
    private String incidentId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "error_type", nullable = false)
    private String errorType;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "fingerprint", unique = true, nullable = false)
    private String fingerprint;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Incident() {}

    public Incident(String incidentId, String status, String errorType, String errorMessage, String fingerprint) {
        this.incidentId = incidentId;
        this.status = status;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.fingerprint = fingerprint;
    }

    public Long getId() { return id; }
    public String getIncidentId() { return incidentId; }
    public String getStatus() { return status; }
    public String getErrorType() { return errorType; }
    public String getErrorMessage() { return errorMessage; }
    public String getFingerprint() { return fingerprint; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setStatus(String status) { this.status = status; this.updatedAt = LocalDateTime.now(); }
}
