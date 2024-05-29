package com.livevox.is.metricsservice.controller.rest;

import com.livevox.is.domain.metrics.ServiceMetrics;
import com.livevox.is.domain.metrics.request.ActiveServiceMetricsRequest;
import com.livevox.is.domain.metrics.request.ActiveServicesRequest;
import com.livevox.is.domain.metrics.request.CallMetricsRequest;
import com.livevox.is.domain.util.IdClass;
import com.livevox.is.metricsservice.service.InteractionEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

@RestController
public class InteractionEventController {

    private InteractionEventService interactionEventService;

    public InteractionEventController(InteractionEventService interactionEventService) {
        this.interactionEventService = interactionEventService;
    }

    @GetMapping("/active-services")
    public ResponseEntity<List<IdClass>> getActiveServices(@Valid ActiveServicesRequest request) {

        List<IdClass> activeServiceIds = interactionEventService.getActiveServices(request);

        if (activeServiceIds.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(activeServiceIds);
    }

    @GetMapping("/active-service-metrics")
    public ResponseEntity<ServiceMetrics> getActiveServiceMetrics(@Valid ActiveServiceMetricsRequest request) {

        ServiceMetrics metrics = interactionEventService.getActiveServiceMetrics(request);

        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/call-metrics/{callSessionId}")
    public ResponseEntity<ServiceMetrics> getCallMetrics(@Valid CallMetricsRequest request) {

        return interactionEventService.getCallMetrics(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

}
