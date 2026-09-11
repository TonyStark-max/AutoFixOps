package com.autofixops.backend.service;

import com.autofixops.backend.dto.IncidentCreateRequest;
import com.autofixops.backend.dto.IncidentResponse;
import com.autofixops.backend.entity.AuditEvent;
import com.autofixops.backend.entity.Incident;
import com.autofixops.backend.repository.AuditEventRepository;
import com.autofixops.backend.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private AuditEventRepository auditEventRepository;

    @Transactional
    public IncidentResponse reportIncident(IncidentCreateRequest request) {
        Optional<Incident> existing = incidentRepository.findByFingerprint(request.getFingerprint());
        
        Incident incident;
        if (existing.isPresent()) {
            incident = existing.get();
            // Deduplication logic: If it's already being investigated, just return it.
            if ("OPEN".equals(incident.getStatus()) || "INVESTIGATING".equals(incident.getStatus())) {
                auditEventRepository.save(new AuditEvent(incident, "DUPLICATE_REPORT", "Received duplicate incident report. Ignored."));
                return new IncidentResponse(incident);
            } else {
                // Reopen the incident if it was fixed but happened again
                incident.setStatus("OPEN");
                incident = incidentRepository.save(incident);
                auditEventRepository.save(new AuditEvent(incident, "REOPENED", "Incident occurred again and was reopened."));
            }
        } else {
            String newId = "INC-2026-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            incident = new Incident(newId, "OPEN", request.getErrorType(), request.getErrorMessage(), request.getFingerprint());
            incident = incidentRepository.save(incident);
            auditEventRepository.save(new AuditEvent(incident, "CREATED", "Incident reported."));
        }
        
        return new IncidentResponse(incident);
    }

    @Transactional(readOnly = true)
    public List<IncidentResponse> getAllIncidents() {
        return incidentRepository.findAll().stream()
                .map(IncidentResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public IncidentResponse getIncident(String incidentId) {
        return incidentRepository.findByIncidentId(incidentId)
                .map(IncidentResponse::new)
                .orElseThrow(() -> new IllegalArgumentException("Incident not found"));
    }

    @Transactional
    public IncidentResponse analyzeIncident(String incidentId) {
        Incident incident = incidentRepository.findByIncidentId(incidentId)
                .orElseThrow(() -> new IllegalArgumentException("Incident not found"));
        
        incident.setStatus("INVESTIGATING");
        incident = incidentRepository.save(incident);
        auditEventRepository.save(new AuditEvent(incident, "ANALYSIS_STARTED", "Agent started analysis of the incident."));
        
        return new IncidentResponse(incident);
    }
}
