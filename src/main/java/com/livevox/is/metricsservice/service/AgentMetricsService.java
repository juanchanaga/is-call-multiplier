package com.livevox.is.metricsservice.service;

import com.livevox.is.domain.api.configuration.response.Agent;
import com.livevox.is.domain.api.configuration.response.ServiceGeneral;
import com.livevox.is.domain.metrics.request.AgentEventRequest;
import com.livevox.is.domain.metrics.response.AgentProductivity;
import com.livevox.is.metricsservice.domain.AgentEventDAO;

import java.util.List;

public interface AgentMetricsService {

    List<AgentProductivity> calculateAgentProductivity(AgentEventRequest agentEventRequest, Agent agent, ServiceGeneral skill,
                                                       List<AgentEventDAO> agentEvents, boolean mergeSessions);
}
