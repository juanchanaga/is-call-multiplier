package com.livevox.is.metricsservice.repository;

import com.livevox.is.domain.metrics.request.AgentEventRequest;
import com.livevox.is.domain.metrics.request.MetricsRequest;
import com.livevox.is.domain.util.IdClass;
import com.livevox.is.metricsservice.domain.AgentEventDAO;
import com.livevox.is.metricsservice.domain.InteractionEventDAO;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AgentEventRepository {

    List<IdClass> getActiveAgents(MetricsRequest request);

    List<AgentEventDAO> getAgentEvents(AgentEventRequest agentEventRequest);

    List<AgentEventDAO> getAgentEventsByClient(AgentEventRequest agentEventRequest);

    List<AgentEventDAO> getEventsByAgentExcludingLogoff(
            InteractionEventDAO event,
            Instant start,
            Instant end);

    Optional<Instant> getNextAgentEventTimestamp(AgentEventDAO agentEvent);
}
