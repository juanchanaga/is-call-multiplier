package com.livevox.is.metricsservice.service.impl;

import com.livevox.is.domain.api.configuration.response.LiveVoxResult;
import com.livevox.is.domain.api.configuration.response.ServiceGeneral;
import com.livevox.is.domain.config.ClientSetup;
import com.livevox.is.domain.metrics.ServiceMetrics;
import com.livevox.is.domain.metrics.request.ActiveServiceMetricsRequest;
import com.livevox.is.metricsservice.domain.AgentEventDAO;
import com.livevox.is.metricsservice.domain.InteractionEventDAO;
import com.livevox.is.metricsservice.service.AgentEventService;
import com.livevox.is.metricsservice.service.ConfigurationService;
import com.livevox.is.metricsservice.service.TermCodeCachedService;
import com.livevox.is.metricsservice.util.ServiceMetricsBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InteractionMetricsServiceImplTest {

    @Mock
    private RetryTemplate retryTemplate;

    @Mock
    private ConfigurationService mockService;

    @Mock
    private AgentEventService agentEventService;

    @Mock
    private TermCodeCachedService termCodeCachedService;

    @Mock(answer = Answers.RETURNS_SELF)
    private ServiceMetricsBuilder serviceMetricsBuilder;

    @InjectMocks
    private InteractionMetricsServiceImpl service;

    @Test
    public void testCalculateMetrics() {

        InteractionEventDAO event1 = getInteractionEventMock(0, null);
        InteractionEventDAO event2 = getInteractionEventMock(1, 6L);
        ActiveServiceMetricsRequest request = getActiveServiceMetricsRequestMock();

        List<InteractionEventDAO> events = Arrays.asList(event1, event2);

        ClientSetup clientSetup = new ClientSetup();
        String sessionId = "1234";
        ServiceGeneral serviceGeneral = ServiceGeneral.builder().name("name").build();

        LiveVoxResult liveVoxResult = new LiveVoxResult();
        liveVoxResult.setName("name");
        Optional<LiveVoxResult> lvResultOptional = Optional.of(liveVoxResult);

        AgentEventDAO agentEvent = new AgentEventDAO();
        AgentEventDAO agentEvent2 = new AgentEventDAO();
        agentEvent2.setAgentId(2);
        AgentEventDAO agentEvent3 = new AgentEventDAO();
        AgentEventDAO agentEvent4 = new AgentEventDAO();
        agentEvent4.setAgentId(9);
        agentEvent3.setAgentId(agentEvent2.getAgentId());

        Instant nextTimestamp = Instant.EPOCH;

        List<AgentEventDAO> agentEventsList = List.of(agentEvent);
        List<AgentEventDAO> agentEventsList2 = Arrays.asList(agentEvent2, agentEvent3, agentEvent4);

        ServiceMetrics metrics = new ServiceMetrics();

        when(mockService.getClientSetup(eq(request.getClientId())))
                .thenReturn(clientSetup);
        when(mockService.getSessionId(eq(request.getClientId())))
                .thenReturn(sessionId);
        when(retryTemplate.execute(any(RetryCallback.class)))
                .thenReturn(ResponseEntity.ok(serviceGeneral));
        when(termCodeCachedService.readLiveVoxResult(eq(clientSetup), any(), eq(sessionId)))
                .thenReturn(lvResultOptional);
        when(agentEventService.getEventsByTransactionIdExcludingLogoff(eq(event1.getTransactionId())))
                .thenReturn(agentEventsList);
        when(agentEventService.getEventsByAgentExcludingLogoff(eq(event2), eq(request.getStart()), eq(request.getEnd())))
                .thenReturn(agentEventsList2);
        when(agentEventService.getNextAgentEventTimestamp(eq(agentEvent)))
                .thenReturn(Optional.of(nextTimestamp));
        when(serviceMetricsBuilder.getMetrics())
                .thenReturn(metrics);

        try (MockedStatic<ServiceMetricsBuilder> builder = mockStatic(ServiceMetricsBuilder.class)) {

            builder.when(() -> ServiceMetricsBuilder.initialize(request)).thenReturn(serviceMetricsBuilder);
            ServiceMetrics serviceMetrics = service.calculateMetrics(events, request);
            assertEquals(metrics, serviceMetrics);
        }

        verify(serviceMetricsBuilder)
                .skillName(eq(serviceGeneral.getName()));
        verify(serviceMetricsBuilder, times(2))
                .addCustomMetric(any());
        verify(serviceMetricsBuilder, times(4))
                .buildMetricsForAgentEvent(any(), any(), any(), eq(lvResultOptional));
        verify(serviceMetricsBuilder)
                .buildMetricsForInteractionEvent(eq(event1), eq(lvResultOptional));
        verify(serviceMetricsBuilder)
                .buildMetricsForInteractionEvent(eq(event2), eq(lvResultOptional));
        verify(termCodeCachedService)
                .readLiveVoxResult(eq(clientSetup), eq(event1.getLvResultId()), eq(sessionId));
        verify(termCodeCachedService)
                .readLiveVoxResult(eq(clientSetup), eq(event2.getLvResultId()), eq(sessionId));
        verify(serviceMetricsBuilder)
                .getMetrics();

    }

    @Test
    public void testCalculateCallMetrics() {

        InteractionEventDAO event = getInteractionEventMock(0, 5L);
        AgentEventDAO agentEvent = new AgentEventDAO();
        agentEvent.setTimestamp(Instant.now());
        AgentEventDAO agentEvent2 = new AgentEventDAO();
        agentEvent2.setTimestamp(Instant.now());
        agentEvent2.setAgentId(2);

        Instant nextTimestamp = Instant.MIN;

        List<AgentEventDAO> agentEvents = Arrays.asList(agentEvent, agentEvent2);
        ServiceMetrics metrics = new ServiceMetrics();

        when(agentEventService.getEventsByTransactionId(eq(event.getTransactionId())))
                .thenReturn(agentEvents);
        when(agentEventService.getNextAgentEventTimestamp(eq(agentEvent2)))
                .thenReturn(Optional.of(nextTimestamp));
        when(serviceMetricsBuilder.getMetrics())
                .thenReturn(metrics);

        try (MockedStatic<ServiceMetricsBuilder> builder = mockStatic(ServiceMetricsBuilder.class)) {

            builder.when(ServiceMetricsBuilder::initialize).thenReturn(serviceMetricsBuilder);
            ServiceMetrics serviceMetrics = service.calculateCallMetrics(event);
            assertEquals(metrics, serviceMetrics);
        }

        verify(serviceMetricsBuilder)
                .buildMetricsForAgentEvent(
                        eq(agentEvent),
                        eq(agentEvent.getTimestamp()),
                        eq(agentEvent2.getTimestamp()),
                        eq(Optional.empty()));
        verify(serviceMetricsBuilder)
                .buildMetricsForAgentEvent(
                        eq(agentEvent2),
                        eq(agentEvent2.getTimestamp()),
                        eq(nextTimestamp),
                        eq(Optional.empty()));
        verify(serviceMetricsBuilder)
                .buildMetricsForInteractionEvent(eq(event), eq(Optional.empty()));
        verify(serviceMetricsBuilder)
                .start(eq(event.getStart()));
        verify(serviceMetricsBuilder)
                .end((eq(event.getEnd())));
        verify(serviceMetricsBuilder)
                .callCenterId(eq(event.getCallCenterId()));
        verify(serviceMetricsBuilder)
                .skillId(event.getServiceId());
        verify(serviceMetricsBuilder)
                .getMetrics();
    }

    private static InteractionEventDAO getInteractionEventMock(int operatorTransfer, Long agentId) {

        Random random = new Random();

        InteractionEventDAO event = new InteractionEventDAO();
        event.setTransactionId(random.nextLong());
        event.setOperatorTransfer(operatorTransfer);
        event.setLvResultId(random.nextInt());
        event.setStart(Instant.MIN);
        event.setEnd(Instant.MAX);
        event.setAgentId(agentId);
        event.setCallCenterId(1);
        event.setServiceId(1);

        return event;
    }

    private static ActiveServiceMetricsRequest getActiveServiceMetricsRequestMock() {
        ActiveServiceMetricsRequest request = new ActiveServiceMetricsRequest();
        request.setStart(Instant.MIN);
        request.setEnd(Instant.MAX);
        request.setClientId(1);
        return request;
    }

}