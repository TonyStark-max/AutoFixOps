package com.autofixops.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_events")
public class AuditEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "incident_id")
    private Incident incident;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public AuditEvent() {}

    public AuditEvent(Incident incident, String eventType, String message) {
        this.incident = incident;
        this.eventType = eventType;
        this.message = message;
    }

    public Long getId() { return id; }
    public Incident getIncident() { return incident; }
    public String getEventType() { return eventType; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
