package com.autofixops.backend.controller;

import com.autofixops.backend.dto.IncidentCreateRequest;
import com.autofixops.backend.dto.IncidentResponse;
import com.autofixops.backend.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    @PostMapping
    public ResponseEntity<IncidentResponse> createIncident(@Valid @RequestBody IncidentCreateRequest request) {
        return new ResponseEntity<>(incidentService.reportIncident(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<IncidentResponse>> getIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }

    @GetMapping("/{incidentId}")
    public ResponseEntity<IncidentResponse> getIncident(@PathVariable String incidentId) {
        return ResponseEntity.ok(incidentService.getIncident(incidentId));
    }

    @PostMapping("/{incidentId}/analyze")
    public ResponseEntity<IncidentResponse> analyzeIncident(@PathVariable String incidentId) {
        return ResponseEntity.ok(incidentService.analyzeIncident(incidentId));
    }
}
