package com.autofixops.backend.repository;

import com.autofixops.backend.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    Optional<Incident> findByIncidentId(String incidentId);
    Optional<Incident> findByFingerprint(String fingerprint);
}
