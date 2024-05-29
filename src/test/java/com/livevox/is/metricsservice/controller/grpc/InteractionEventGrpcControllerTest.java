package com.livevox.is.metricsservice.controller.grpc;

import com.google.common.collect.Lists;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class InteractionEventGrpcControllerTest {

    @InjectMocks
    private InteractionEventGrpcController grpcController;

    @Mock
    private InteractionEventService interactionEventService;

    @Mock
    private ConversionService conversionService;

    @Mock
    private StreamObserver<ServiceMetricsGrpc> serviceMetricsGrpcStreamObserver;

    @Mock
    private StreamObserver<IdClassGrpc> idClassGrpcStreamObserver;

    @Test
    public void testGetActiveServiceMetrics() {

        ActiveServiceMetricsGrpcRequest grpcRequest = ActiveServiceMetricsGrpcRequest.newBuilder().build();
        ActiveServiceMetricsRequest request = new ActiveServiceMetricsRequest();
        ServiceMetricsGrpc response = ServiceMetricsGrpc.newBuilder().build();
        ServiceMetrics serviceMetrics = new ServiceMetrics();

        when(conversionService.convert(eq(grpcRequest), eq(ActiveServiceMetricsRequest.class)))
                .thenReturn(request);
        when(interactionEventService.getActiveServiceMetrics(eq(request)))
                .thenReturn(serviceMetrics);
        when(conversionService.convert(eq(serviceMetrics), eq(ServiceMetricsGrpc.class)))
                .thenReturn(response);

        grpcController.getActiveServiceMetrics(grpcRequest, serviceMetricsGrpcStreamObserver);

        verify(serviceMetricsGrpcStreamObserver).onNext(response);
        verify(serviceMetricsGrpcStreamObserver).onCompleted();
    }

    @Test
    public void testGetActiveServices() {

        ActiveServicesRequestGrpc grpcRequest = ActiveServicesRequestGrpc.newBuilder().build();
        ActiveServicesRequest request = new ActiveServicesRequest();
        List<IdClass> response = Lists.newArrayList(new IdClass(1), new IdClass(2));

        when(conversionService.convert(eq(grpcRequest), eq(ActiveServicesRequest.class)))
                .thenReturn(request);
        when(interactionEventService.getActiveServices(eq(request)))
                .thenReturn(response);

        grpcController.getActiveServices(grpcRequest, idClassGrpcStreamObserver);

        verify(idClassGrpcStreamObserver, times(2)).onNext(any(IdClassGrpc.class));
        verify(idClassGrpcStreamObserver).onCompleted();
    }

    @Test
    public void testGetCallMetrics() {

        CallMetricsRequestGrpc grpcRequest = CallMetricsRequestGrpc.newBuilder().build();
        ServiceMetricsGrpc grpcResponse = ServiceMetricsGrpc.newBuilder().build();
        CallMetricsRequest request = CallMetricsRequest.builder().build();
        ServiceMetrics response = new ServiceMetrics();

        when(conversionService.convert(eq(grpcRequest), eq(CallMetricsRequest.class)))
                .thenReturn(request);
        when(interactionEventService.getCallMetrics(eq(request)))
                .thenReturn(Optional.of(response));
        when(conversionService.convert(eq(response), eq(ServiceMetricsGrpc.class)))
                .thenReturn(grpcResponse);

        grpcController.getCallMetrics(grpcRequest, serviceMetricsGrpcStreamObserver);

        verify(serviceMetricsGrpcStreamObserver).onNext(eq(grpcResponse));
        verify(serviceMetricsGrpcStreamObserver).onCompleted();
    }

}
