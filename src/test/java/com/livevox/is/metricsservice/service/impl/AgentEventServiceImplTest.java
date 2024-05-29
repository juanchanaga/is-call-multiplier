package com.livevox.is.metricsservice.service.impl;

import com.livevox.is.api.configuration.service.AgentApiService;
import com.livevox.is.api.configuration.service.ServiceApiService;
import com.livevox.is.domain.api.configuration.response.Agent;
import com.livevox.is.domain.api.configuration.response.ServiceGeneral;
import com.livevox.is.domain.config.ClientSetup;
import com.livevox.is.domain.metrics.request.AgentEventRequest;
import com.livevox.is.domain.metrics.request.MetricsRequest;
import com.livevox.is.domain.metrics.response.AgentProductivity;
import com.livevox.is.metricsservice.domain.AgentEventDAO;
import com.livevox.is.metricsservice.repository.crudrepository.AgentEventCrudRepository;
import com.livevox.is.metricsservice.service.ConfigurationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgentEventServiceImplTest {

    @InjectMocks
    private AgentEventServiceImpl agentEventService;
    @Mock
    private AgentMetricsServiceImpl agentMetricsService;
    @Mock
    private AgentEventCrudRepository agentEventRepository;
    @Mock
    private ConfigurationService configurationService;
    @Mock
    private AgentApiService agentApiService;
    @Mock
    private ServiceApiService serviceApiService;
    @Mock
    private RetryTemplate retryTemplate;

    @Test
    public void getActiveAgentsTest() {
        agentEventService.getActiveAgents(mock(MetricsRequest.class));
        verify(agentEventRepository).getActiveAgents(any(MetricsRequest.class));
    }

    @Test
    public void getAgentEventsTest() {
        agentEventService.getAgentEvents(mock(AgentEventRequest.class));
        verify(agentEventRepository).getAgentEvents(any(AgentEventRequest.class));
    }

    @Test
    public void getAgentEventsByClientTest() {
        agentEventService.getAgentEventsByClient(mock(AgentEventRequest.class));
        verify(agentEventRepository).getAgentEventsByClient(any(AgentEventRequest.class));
    }

    @Test
    public void getAgentProductivity() {
        Random random = new Random();
        String sessionId = String.valueOf(random.nextLong());
        Integer agentId = random.nextInt();
        Integer skillId = random.nextInt();
        List<AgentEventDAO> agentEvents = new ArrayList<>();
        AgentEventDAO agentEvent = new AgentEventDAO();
        agentEvent.setAgentServiceId(skillId);
        agentEvents.add(agentEvent);

        AgentEventRequest agentEventRequest = mock(AgentEventRequest.class);
        ClientSetup clientSetup = mock(ClientSetup.class);
        Agent agent = spy(Agent.class);
        ServiceGeneral skill = spy(ServiceGeneral.class);
        List<AgentProductivity> agentProductivityList = spy(ArrayList.class);

        when(retryTemplate.execute(any(RetryCallback.class))).thenAnswer(invocation -> {
            RetryCallback retry = invocation.getArgument(0);
            return retry.doWithRetry(null);
        });

        when(agentEventRequest.getAgentId()).thenReturn(agentId);
        when(configurationService.getClientSetup(anyInt())).thenReturn(clientSetup);
        when(configurationService.getSessionId(anyInt())).thenReturn(sessionId);
        when(agentApiService.readAgent(clientSetup, sessionId, agentId)).thenReturn(ResponseEntity.ok(agent));
        when(agentEventRepository.getAgentEvents(agentEventRequest)).thenReturn(agentEvents);
        when(serviceApiService.readServiceGeneral(clientSetup, skillId, sessionId)).thenReturn(ResponseEntity.ok(skill));
        when(agentMetricsService.calculateAgentProductivity(agentEventRequest, agent, skill, agentEvents, false)).thenReturn(agentProductivityList);

        List<AgentProductivity> agentProductivities = agentEventService.getAgentProductivity(agentEventRequest);
        verify(agentMetricsService).calculateAgentProductivity(agentEventRequest, agent, skill, agentEvents, false);
        assertEquals(agentId, agent.getAgentId());
        assertEquals("NOT FOUND", skill.getName());
        assertTrue(agentProductivities.containsAll(agentProductivityList));
    }

}
