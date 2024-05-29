package com.livevox.is.metricsservice.service;

import com.livevox.is.domain.metrics.request.AgentEventRequest;
import com.livevox.is.domain.metrics.request.MetricsRequest;
import com.livevox.is.domain.metrics.response.AgentEvent;
import com.livevox.is.domain.metrics.response.AgentProductivity;
import com.livevox.is.domain.util.IdClass;
import com.livevox.is.metricsservice.domain.AgentEventDAO;
import com.livevox.is.metricsservice.domain.InteractionEventDAO;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AgentEventService {

    List<IdClass> getActiveAgents(MetricsRequest request);

    List<AgentEvent> getAgentEvents(AgentEventRequest agentEventRequest);

    List<AgentEvent> getAgentEventsByClient(AgentEventRequest agentEventRequest);

    List<AgentProductivity> getAgentProductivity(AgentEventRequest agentEventRequest);

    List<AgentEventDAO> getEventsByAgentExcludingLogoff(
            InteractionEventDAO event,
            Instant start,
            Instant end);

    List<AgentEventDAO> getEventsByTransactionIdExcludingLogoff(Long transactionId);

    Optional<Instant> getNextAgentEventTimestamp(AgentEventDAO agentEventEvent);

    List<AgentEventDAO> getEventsByTransactionId(Long transactionId);
}
