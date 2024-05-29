package com.livevox.is.metricsservice.service.impl;

import com.livevox.is.domain.metrics.ServiceMetrics;
import com.livevox.is.domain.metrics.request.ActiveServiceMetricsRequest;
import com.livevox.is.domain.metrics.request.ActiveServicesRequest;
import com.livevox.is.domain.metrics.request.CallMetricsRequest;
import com.livevox.is.domain.util.IdClass;
import com.livevox.is.metricsservice.domain.InteractionEventDAO;
import com.livevox.is.metricsservice.repository.crudrepository.InteractionEventCrudRepository;
import com.livevox.is.metricsservice.service.InteractionEventService;
import com.livevox.is.metricsservice.service.InteractionMetricsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InteractionEventServiceImpl implements InteractionEventService {

    private InteractionEventCrudRepository interactionEventRepository;

    private InteractionMetricsService interactionMetricsService;

    @Autowired
    public InteractionEventServiceImpl(
            InteractionEventCrudRepository interactionEventRepository,
            InteractionMetricsService interactionMetricsService) {

        this.interactionEventRepository = interactionEventRepository;
        this.interactionMetricsService = interactionMetricsService;
    }

    @Override
    public List<IdClass> getActiveServices(ActiveServicesRequest request) {
        return interactionEventRepository.getActiveServices(request);
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceMetrics getActiveServiceMetrics(ActiveServiceMetricsRequest request) {

        log.info(
                "Getting interaction events for serviceId: {} - clientId: {} - callCenterId: {}",
                request.getServiceId(), request.getClientId(), request.getCallCenterId());

        List<InteractionEventDAO> events = interactionEventRepository.getActiveServiceInteractionDetails(request);

        return interactionMetricsService.calculateMetrics(events, request);
    }

    @Override
    public Optional<ServiceMetrics> getCallMetrics(CallMetricsRequest request) {

        return interactionEventRepository.getEventBySession(request.getCallSessionId(), request.getClientId())
                .map(interactionMetricsService::calculateCallMetrics);
    }
}
