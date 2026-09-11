package com.autofixops.backend;

import com.autofixops.backend.dto.IncidentCreateRequest;
import com.autofixops.backend.dto.IncidentResponse;
import com.autofixops.backend.service.IncidentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class BackendApplicationTests {

    @Autowired
    private IncidentService incidentService;

    @Test
    void contextLoads() {
    }

    @Test
    void testIncidentCreation() {
        IncidentCreateRequest req = new IncidentCreateRequest();
        req.setErrorType("Runtime Failure");
        req.setErrorMessage("Optional.get()");
        req.setFingerprint("hash123");

        IncidentResponse response = incidentService.reportIncident(req);
        
        assertNotNull(response.getIncidentId());
        assertEquals("OPEN", response.getStatus());
        assertEquals("Runtime Failure", response.getErrorType());
        
        // Test Deduplication
        IncidentResponse duplicate = incidentService.reportIncident(req);
        assertEquals(response.getIncidentId(), duplicate.getIncidentId());
    }
}
