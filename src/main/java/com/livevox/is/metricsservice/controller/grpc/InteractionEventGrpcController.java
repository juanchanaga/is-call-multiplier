package com.livevox.is.metricsservice.controller.grpc;

import com.livevox.is.domain.metrics.ServiceMetrics;
import com.livevox.is.domain.metrics.request.ActiveServiceMetricsGrpcRequest;
import com.livevox.is.domain.metrics.request.ActiveServiceMetricsRequest;
import com.livevox.is.domain.metrics.request.ActiveServicesRequest;
import com.livevox.is.domain.metrics.request.ActiveServicesRequestGrpc;
import com.livevox.is.domain.metrics.request.CallMetricsRequest;
import com.livevox.is.domain.metrics.request.CallMetricsRequestGrpc;
import com.livevox.is.domain.metrics.request.IdClassGrpc;
import com.livevox.is.domain.metrics.request.ServiceMetricsGrpc;
import com.livevox.is.domain.util.IdClass;
import com.livevox.is.metricsservice.service.InteractionEventService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.core.convert.ConversionService;

import java.util.Objects;

@Slf4j
@GrpcService
public class InteractionEventGrpcController extends InteractionEventControllerGrpc.InteractionEventControllerImplBase {

    private final InteractionEventService interactionEventService;

    private final ConversionService conversionService;

    public InteractionEventGrpcController(
            InteractionEventService interactionEventService,
            ConversionService conversionService) {

        this.interactionEventService = interactionEventService;
        this.conversionService = conversionService;
    }

    @Override
    public void getActiveServiceMetrics(
            ActiveServiceMetricsGrpcRequest grpcRequest,
            StreamObserver<ServiceMetricsGrpc> observer) {

        log.debug("Received request with parameters: ClientId: {} - Start: {} - End: {}",
                grpcRequest.getClientId(),
                grpcRequest.getStart(),
                grpcRequest.getEnd());

        ActiveServiceMetricsRequest request = conversionService.convert(grpcRequest, ActiveServiceMetricsRequest.class);

        log.debug("Parameters after conversion: ClientId: {} - Start: {} - End: {}",
                request.getClientId(),
                request.getStart(),
                request.getEnd());

        ServiceMetrics activeServiceMetrics = interactionEventService.getActiveServiceMetrics(request);
        ServiceMetricsGrpc response = conversionService.convert(activeServiceMetrics, ServiceMetricsGrpc.class);

        observer.onNext(response);
        observer.onCompleted();
    }

    @Override
    public void getActiveServices(
            ActiveServicesRequestGrpc grpcRequest,
            StreamObserver<IdClassGrpc> observer) {

        ActiveServicesRequest request = conversionService.convert(grpcRequest, ActiveServicesRequest.class);
        interactionEventService.getActiveServices(request).stream()
                .map(IdClass::getId)
                .filter(Objects::nonNull)
                .map(id -> IdClassGrpc.newBuilder().setId(id).build())
                .forEach(observer::onNext);

        observer.onCompleted();
    }

    @Override
    public void getCallMetrics(
            CallMetricsRequestGrpc grpcRequest,
            StreamObserver<ServiceMetricsGrpc> observer) {

        CallMetricsRequest request = conversionService.convert(grpcRequest, CallMetricsRequest.class);
        interactionEventService.getCallMetrics(request)
                .map(callMetrics -> conversionService.convert(callMetrics, ServiceMetricsGrpc.class))
                .ifPresent(callMetricsGrpc -> observer.onNext(callMetricsGrpc));

        observer.onCompleted();
    }
}
