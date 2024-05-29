package com.livevox.is.metricsservice.controller.grpc;

import com.livevox.is.domain.metrics.request.AgentEventGrpc;
import com.livevox.is.domain.metrics.request.AgentEventRequest;
import com.livevox.is.domain.metrics.request.AgentEventRequestGrpc;
import com.livevox.is.domain.metrics.request.AgentProductivityGrpc;
import com.livevox.is.domain.metrics.request.IdClassGrpc;
import com.livevox.is.domain.metrics.request.MetricsRequest;
import com.livevox.is.domain.metrics.request.MetricsRequestGrpc;
import com.livevox.is.domain.util.IdClass;
import com.livevox.is.metricsservice.service.AgentEventService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.core.convert.ConversionService;

@Slf4j
@GrpcService
public class AgentEventGrpcController extends AgentEventControllerGrpc.AgentEventControllerImplBase {

    private final ConversionService conversionService;

    private final AgentEventService agentEventService;

    public AgentEventGrpcController(
            ConversionService conversionService,
            AgentEventService agentEventService) {

        this.conversionService = conversionService;
        this.agentEventService = agentEventService;
    }

    @Override
    public void getActiveAgents(
            MetricsRequestGrpc grpcRequest,
            StreamObserver<IdClassGrpc> observer) {

        MetricsRequest request = conversionService.convert(grpcRequest, MetricsRequest.class);
        agentEventService.getActiveAgents(request)
                .stream()
                .map(IdClass::getId)
                .map(id -> IdClassGrpc.newBuilder().setId(id).build())
                .forEach(observer::onNext);

        observer.onCompleted();
    }

    @Override
    public void getAgentEvents(
            AgentEventRequestGrpc grpcRequest,
            StreamObserver<AgentEventGrpc> observer) {

        AgentEventRequest request = conversionService.convert(grpcRequest, AgentEventRequest.class);

        agentEventService.getAgentEvents(request).stream()
                .map(event -> conversionService.convert(event, AgentEventGrpc.class))
                .forEach(observer::onNext);

        observer.onCompleted();
    }

    @Override
    public void getAgentEventsByClient(
            AgentEventRequestGrpc grpcRequest,
            StreamObserver<AgentEventGrpc> observer) {

        AgentEventRequest request = conversionService.convert(grpcRequest, AgentEventRequest.class);
        agentEventService.getAgentEventsByClient(request).stream()
                .map(event -> conversionService.convert(event, AgentEventGrpc.class))
                .forEach(observer::onNext);

        observer.onCompleted();
    }

    @Override
    public void getAgentProductivity(
            AgentEventRequestGrpc grpcRequest,
            StreamObserver<AgentProductivityGrpc> observer) {

        log.debug("Received request with parameters: ClientId: {} - Start: {} - End: {}",
                grpcRequest.getClientId(),
                grpcRequest.getStart(),
                grpcRequest.getEnd());

        AgentEventRequest request = conversionService.convert(grpcRequest, AgentEventRequest.class);

        log.debug("Parameters after conversion: ClientId: {} - Start: {} - End: {}",
                request.getClientId(),
                request.getStart(),
                request.getEnd());

        agentEventService.getAgentProductivity(request).stream()
                .map(productivity -> conversionService.convert(productivity, AgentProductivityGrpc.class))
                .forEach(observer::onNext);

        observer.onCompleted();
    }
}
