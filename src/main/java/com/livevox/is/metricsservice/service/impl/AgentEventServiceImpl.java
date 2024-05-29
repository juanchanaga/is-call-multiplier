package com.livevox.is.metricsservice.service.impl;

import com.livevox.is.api.configuration.service.AgentApiService;
import com.livevox.is.api.configuration.service.ServiceApiService;
import com.livevox.is.domain.api.configuration.response.Agent;
import com.livevox.is.domain.api.configuration.response.ServiceGeneral;
import com.livevox.is.domain.config.ClientSetup;
import com.livevox.is.domain.metrics.request.AgentEventRequest;
import com.livevox.is.domain.metrics.request.MetricsRequest;
import com.livevox.is.domain.metrics.response.AgentEvent;
import com.livevox.is.domain.metrics.response.AgentProductivity;
import com.livevox.is.domain.util.IdClass;
import com.livevox.is.metricsservice.domain.AgentEventDAO;
import com.livevox.is.metricsservice.domain.InteractionEventDAO;
import com.livevox.is.metricsservice.repository.crudrepository.AgentEventCrudRepository;
import com.livevox.is.metricsservice.service.AgentEventService;
import com.livevox.is.metricsservice.service.AgentMetricsService;
import com.livevox.is.metricsservice.service.ConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AgentEventServiceImpl implements AgentEventService {

    private AgentEventCrudRepository agentEventRepository;
    private AgentMetricsService agentMetricsService;
    private AgentApiService agentApiService;
    private ServiceApiService serviceApiService;
    private ConfigurationService configurationService;
    private RetryTemplate retryTemplate;

    private ModelMapper modelMapper;

    public AgentEventServiceImpl(AgentEventCrudRepository agentEventRepository,
                                 AgentMetricsService agentMetricsService,
                                 AgentApiService agentApiService,
                                 ServiceApiService serviceApiService,
                                 ConfigurationService configurationService,
                                 RetryTemplate retryTemplate,
                                 ModelMapper modelMapper) {

        this.agentEventRepository = agentEventRepository;
        this.agentMetricsService = agentMetricsService;
        this.agentApiService = agentApiService;
        this.serviceApiService = serviceApiService;
        this.configurationService = configurationService;
        this.retryTemplate = retryTemplate;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<IdClass> getActiveAgents(MetricsRequest request) {
        return agentEventRepository.getActiveAgents(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentEvent> getAgentEvents(AgentEventRequest agentEventRequest) {

        return agentEventRepository.getAgentEvents(agentEventRequest)
                .stream()
                .map(event -> modelMapper.map(event, AgentEvent.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentEvent> getAgentEventsByClient(AgentEventRequest agentEventRequest) {

        return agentEventRepository.getAgentEventsByClient(agentEventRequest)
                .stream()
                .map(event -> modelMapper.map(event, AgentEvent.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentProductivity> getAgentProductivity(AgentEventRequest agentEventRequest) {

        List<AgentProductivity> agentProductivities = new ArrayList<>();

        ClientSetup clientSetup = configurationService.getClientSetup(agentEventRequest.getClientId());
        String sessionId = configurationService.getSessionId(agentEventRequest.getClientId());

        Agent agent = retryTemplate.execute(context ->
                agentApiService.readAgent(clientSetup, sessionId, agentEventRequest.getAgentId()).getBody());

        agent.setAgentId(agentEventRequest.getAgentId());

        List<AgentEventDAO> agentEvents = agentEventRepository.getAgentEvents(agentEventRequest);

        Map<Integer, List<AgentEventDAO>> agentEventsMap = agentEvents.stream().collect(Collectors.groupingBy(AgentEventDAO::getAgentServiceId));

        for (Map.Entry<Integer, List<AgentEventDAO>> entry : agentEventsMap.entrySet()) {

            ServiceGeneral skill = retryTemplate.execute(context ->
                    serviceApiService.readServiceGeneral(clientSetup, entry.getKey(), sessionId).getBody());

            if (skill.getName() == null) {
                skill.setName("NOT FOUND");
            }

            List<AgentProductivity> agentProductivityList = agentMetricsService.calculateAgentProductivity(agentEventRequest,
                    agent, skill, entry.getValue(), false);

            agentProductivities.addAll(agentProductivityList);

        }

        return agentProductivities;
    }

    @Transactional(readOnly = true)
    public List<AgentEventDAO> getEventsByAgentExcludingLogoff(
            InteractionEventDAO event,
            Instant start,
            Instant end) {

        log.debug("Getting events by agent for agentId: {}", event.getAgentId());
        return agentEventRepository.getEventsByAgentExcludingLogoff(event, start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentEventDAO> getEventsByTransactionIdExcludingLogoff(Long transactionId) {

        log.debug("Getting eventsByTransactionId for transactionId: {}", transactionId);
        return agentEventRepository.getEventsByTransactionIdExcludingLogoff(transactionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Instant> getNextAgentEventTimestamp(AgentEventDAO agentEvent) {

        log.debug("Getting next agent event timestamp for agent: {}", agentEvent.getAgentId());
        return agentEventRepository.getNextAgentEventTimestamp(agentEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentEventDAO> getEventsByTransactionId(Long transactionId) {
        return agentEventRepository.getEventsByTransactionId(transactionId);
    }

}
