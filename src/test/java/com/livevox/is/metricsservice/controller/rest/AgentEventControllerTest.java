package com.livevox.is.metricsservice.controller.rest;

import com.livevox.is.domain.metrics.request.AgentEventRequest;
import com.livevox.is.domain.metrics.response.AgentEvent;
import com.livevox.is.domain.metrics.response.AgentProductivity;
import com.livevox.is.domain.util.IdClass;
import com.livevox.is.metricsservice.service.AgentEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AgentEventControllerTest {

    @InjectMocks
    private AgentEventController agentEventController;
    @Mock
    private AgentEventService agentEventService;

    @Test
    public void getActiveAgentsTest() {

        List<IdClass> activeAgents = mock(ArrayList.class);
        when(agentEventService.getActiveAgents(any(AgentEventRequest.class))).thenReturn(activeAgents);

        ResponseEntity<List<IdClass>> response = agentEventController.getActiveAgents(mock(AgentEventRequest.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activeAgents, response.getBody());

        when(activeAgents.isEmpty()).thenReturn(true);
        response = agentEventController.getActiveAgents(mock(AgentEventRequest.class));
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void getAgentEventsTest() {

        List<AgentEvent> agentEvents = mock(ArrayList.class);
        when(agentEventService.getAgentEvents(any(AgentEventRequest.class))).thenReturn(agentEvents);

        ResponseEntity<List<AgentEvent>> response = agentEventController.getAgentEvents(mock(AgentEventRequest.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        when(agentEvents.isEmpty()).thenReturn(true);
        response = agentEventController.getAgentEvents(mock(AgentEventRequest.class));
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void getAgentEventsByClientTest() {

        List<AgentEvent> agentEvents = mock(ArrayList.class);
        when(agentEventService.getAgentEventsByClient(any(AgentEventRequest.class))).thenReturn(agentEvents);

        ResponseEntity<List<AgentEvent>> response = agentEventController.getAgentEventsByClient(mock(AgentEventRequest.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        when(agentEvents.isEmpty()).thenReturn(true);
        response = agentEventController.getAgentEventsByClient(mock(AgentEventRequest.class));
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void getAgentProductivityTest() {

        List<AgentProductivity> agentProductivityList = mock(ArrayList.class);
        when(agentEventService.getAgentProductivity(any(AgentEventRequest.class))).thenReturn(agentProductivityList);

        ResponseEntity<List<AgentProductivity>> response = agentEventController.getAgentProductivity(mock(AgentEventRequest.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(agentProductivityList, response.getBody());

        when(agentProductivityList.isEmpty()).thenReturn(true);
        response = agentEventController.getAgentProductivity(mock(AgentEventRequest.class));
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    }
}
