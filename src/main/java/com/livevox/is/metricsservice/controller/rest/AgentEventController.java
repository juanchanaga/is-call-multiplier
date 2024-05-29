package com.livevox.is.metricsservice.controller.rest;

import com.livevox.is.domain.metrics.request.AgentEventRequest;
import com.livevox.is.domain.metrics.request.MetricsRequest;
import com.livevox.is.domain.metrics.response.AgentEvent;
import com.livevox.is.domain.metrics.response.AgentProductivity;
import com.livevox.is.domain.util.IdClass;
import com.livevox.is.metricsservice.service.AgentEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class AgentEventController {

    @Autowired
    private AgentEventService agentEventService;

    @GetMapping(value = "/agents/active")
    public ResponseEntity<List<IdClass>> getActiveAgents(@Valid MetricsRequest request) {

        List<IdClass> activeAgents = agentEventService.getActiveAgents(request);

        if (activeAgents.isEmpty()) {
            log.info("No active agents for client id {}", request.getClientId());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(activeAgents);
    }

    @GetMapping(value = "/agents/{agentId}/events")
    public ResponseEntity<List<AgentEvent>> getAgentEvents(@Valid AgentEventRequest agentEventRequest) {

        List<AgentEvent> agentEvents = agentEventService.getAgentEvents(agentEventRequest);

        if (agentEvents.isEmpty()) {
            log.info("No events for agent id {}", agentEventRequest.getAgentId());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(agentEvents);
    }

    @GetMapping(value = "{clientId}/agents/events")
    public ResponseEntity<List<AgentEvent>> getAgentEventsByClient(@Valid AgentEventRequest agentEventRequest) {

        List<AgentEvent> agentEvents = agentEventService.getAgentEventsByClient(agentEventRequest);

        if (agentEvents.isEmpty()) {
            log.info("No events for client id {}", agentEventRequest.getAgentId());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(agentEvents);
    }

    @GetMapping(value = "/agents/{agentId}/productivity")
    public ResponseEntity<List<AgentProductivity>> getAgentProductivity(@Valid AgentEventRequest agentEventRequest) {

        List<AgentProductivity> agentProductivityList = agentEventService.getAgentProductivity(agentEventRequest);

        if (CollectionUtils.isEmpty(agentProductivityList)) {
            log.info("No productivity data for agent id {}", agentEventRequest.getAgentId());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(agentProductivityList);
    }

    @GetMapping(value = {"/actuator/health"})
    public ResponseEntity<?> healthCheck() throws IOException {
        return ResponseEntity.ok(null);
    }
}
