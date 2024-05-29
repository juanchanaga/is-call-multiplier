package com.livevox.is.metricsservice.controller.grpc;

import com.livevox.is.domain.metrics.request.AgentEventGrpc;
import com.livevox.is.domain.metrics.request.AgentEventRequest;
import com.livevox.is.domain.metrics.request.AgentEventRequestGrpc;
import com.livevox.is.domain.metrics.request.AgentProductivityGrpc;
import com.livevox.is.domain.metrics.request.IdClassGrpc;
import com.livevox.is.domain.metrics.request.MetricsRequest;
import com.livevox.is.domain.metrics.request.MetricsRequestGrpc;
import com.livevox.is.domain.metrics.response.AgentEvent;
import com.livevox.is.domain.metrics.response.AgentProductivity;
import com.livevox.is.domain.util.IdClass;
import com.livevox.is.metricsservice.service.AgentEventService;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AgentEventGrpcControllerTest {

    @Mock
    private ConversionService conversionService;

    @Mock
    private AgentEventService agentEventService;

    @Mock
    private StreamObserver<IdClassGrpc> idClassGrpcObserver;

    @Mock
    private StreamObserver<AgentEventGrpc> agentEventGrpcObserver;

    @Mock
    private StreamObserver<AgentProductivityGrpc> agentProductivityGrpcObserver;

    @InjectMocks
    private AgentEventGrpcController agentEventGrpcController;

    @Test
    public void testGetActiveAgents() {

        MetricsRequestGrpc metricsRequestGrpc = MetricsRequestGrpc.newBuilder().build();
        MetricsRequest metricsRequest = new MetricsRequest();
        List<IdClass> ids = Arrays.asList(new IdClass(1), new IdClass(2));

        when(conversionService.convert(metricsRequestGrpc, MetricsRequest.class))
                .thenReturn(metricsRequest);
        when(agentEventService.getActiveAgents(metricsRequest))
                .thenReturn(ids);

        agentEventGrpcController.getActiveAgents(metricsRequestGrpc, idClassGrpcObserver);

        verify(idClassGrpcObserver, times(2)).onNext(any(IdClassGrpc.class));
        verify(idClassGrpcObserver, times(1)).onCompleted();
    }

    @Test
    public void testGetAgentEventsByClient() {

        AgentEventRequestGrpc agentEventRequestGrpc = AgentEventRequestGrpc.newBuilder().build();
        AgentEventRequest agentEventRequest = new AgentEventRequest();
        List<AgentEvent> agentEvents = Arrays.asList(new AgentEvent(), new AgentEvent());
        AgentEventGrpc agentEventGrpc = AgentEventGrpc.newBuilder().build();

        when(conversionService.convert(agentEventRequestGrpc, AgentEventRequest.class))
                .thenReturn(agentEventRequest);
        when(agentEventService.getAgentEventsByClient(agentEventRequest))
                .thenReturn(agentEvents);
        when(conversionService.convert(any(AgentEvent.class), eq(AgentEventGrpc.class)))
                .thenReturn(agentEventGrpc);

        agentEventGrpcController.getAgentEventsByClient(agentEventRequestGrpc, agentEventGrpcObserver);

        verify(agentEventGrpcObserver, times(2)).onNext(any(AgentEventGrpc.class));
        verify(agentEventGrpcObserver, times(1)).onCompleted();
    }

    @Test
    public void testGetAgentProductivity() {

        AgentEventRequestGrpc agentEventRequestGrpc = AgentEventRequestGrpc.newBuilder().build();
        AgentEventRequest agentEventRequest = new AgentEventRequest();
        List<AgentProductivity> agentEvents = Arrays.asList(new AgentProductivity(), new AgentProductivity());
        AgentProductivityGrpc agentEventGrpc = AgentProductivityGrpc.newBuilder().build();

        when(conversionService.convert(agentEventRequestGrpc, AgentEventRequest.class))
                .thenReturn(agentEventRequest);
        when(agentEventService.getAgentProductivity(agentEventRequest))
                .thenReturn(agentEvents);
        when(conversionService.convert(any(AgentProductivity.class), eq(AgentProductivityGrpc.class)))
                .thenReturn(agentEventGrpc);

        agentEventGrpcController.getAgentProductivity(agentEventRequestGrpc, agentProductivityGrpcObserver);

        verify(agentProductivityGrpcObserver, times(2)).onNext(any(AgentProductivityGrpc.class));
        verify(agentProductivityGrpcObserver, times(1)).onCompleted();
    }

}
