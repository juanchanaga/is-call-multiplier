package com.livevox.is.metricsservice.service.impl;

import com.livevox.is.domain.metrics.ServiceMetrics;
import com.livevox.is.domain.metrics.request.ActiveServiceMetricsRequest;
import com.livevox.is.domain.metrics.request.ActiveServicesRequest;
import com.livevox.is.domain.metrics.request.CallMetricsRequest;
import com.livevox.is.domain.util.IdClass;
import com.livevox.is.metricsservice.domain.InteractionEventDAO;
import com.livevox.is.metricsservice.repository.crudrepository.InteractionEventCrudRepository;
import com.livevox.is.metricsservice.service.InteractionMetricsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InteractionEventServiceImplTest {

    @Mock
    private InteractionEventCrudRepository interactionEventRepository;

    @Mock
    private InteractionMetricsService interactionMetricsService;

    @InjectMocks
    private InteractionEventServiceImpl interactionEventService;

    @Test
    public void testGetActiveServices() {

        ActiveServicesRequest request = new ActiveServicesRequest();
        List<IdClass> expectedResponse = new ArrayList<>();

        when(interactionEventRepository.getActiveServices(eq(request)))
                .thenReturn(expectedResponse);

        List<IdClass> response = interactionEventService.getActiveServices(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetActiveServiceMetrics() {

        ActiveServiceMetricsRequest request = new ActiveServiceMetricsRequest();
        List<InteractionEventDAO> events = new ArrayList<>();
        ServiceMetrics metrics = new ServiceMetrics();

        when(interactionEventRepository.getActiveServiceInteractionDetails(eq(request)))
                .thenReturn(events);
        when(interactionMetricsService.calculateMetrics(eq(events), eq(request)))
                .thenReturn(metrics);

        ServiceMetrics response = interactionEventService.getActiveServiceMetrics(request);

        assertEquals(metrics, response);
    }

    @Test
    public void testGetCallMetrics() {

        CallMetricsRequest request = CallMetricsRequest.builder()
                .callSessionId("sessionId")
                .clientId(123)
                .build();
        ServiceMetrics metrics = new ServiceMetrics();
        InteractionEventDAO event = new InteractionEventDAO();

        when(interactionEventRepository.getEventBySession(eq(request.getCallSessionId()), eq(request.getClientId())))
                .thenReturn(Optional.of(event));
        when(interactionMetricsService.calculateCallMetrics(eq(event)))
                .thenReturn(metrics);

        Optional<ServiceMetrics> response = interactionEventService.getCallMetrics(request);

        assertTrue(response.isPresent());
        assertEquals(metrics, response.get());
    }

}