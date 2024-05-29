package com.livevox.is.metricsservice.service.impl;

import com.livevox.is.api.configuration.service.ServiceApiService;
import com.livevox.is.domain.api.configuration.response.LiveVoxResult;
import com.livevox.is.domain.api.configuration.response.ServiceGeneral;
import com.livevox.is.domain.config.ClientSetup;
import com.livevox.is.domain.metrics.CustomMetric;
import com.livevox.is.domain.metrics.ServiceMetrics;
import com.livevox.is.domain.metrics.request.ActiveServiceMetricsRequest;
import com.livevox.is.metricsservice.domain.AgentEventDAO;
import com.livevox.is.metricsservice.domain.InteractionEventDAO;
import com.livevox.is.metricsservice.service.AgentEventService;
import com.livevox.is.metricsservice.service.ConfigurationService;
import com.livevox.is.metricsservice.service.InteractionMetricsService;
import com.livevox.is.metricsservice.service.TermCodeCachedService;
import com.livevox.is.metricsservice.util.ServiceMetricsBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@Slf4j
public class InteractionMetricsServiceImpl implements InteractionMetricsService {

    private ServiceApiService serviceApiService;

    private ConfigurationService configurationService;

    private AgentEventService agentEventService;

    private TermCodeCachedService termCodeCachedService;

    private RetryTemplate retryTemplate;

    public InteractionMetricsServiceImpl(
            ServiceApiService serviceApiService,
            ConfigurationService configurationService,
            AgentEventService agentEventService,
            TermCodeCachedService termCodeCachedService,
            RetryTemplate retryTemplate) {

        this.serviceApiService = serviceApiService;
        this.configurationService = configurationService;
        this.agentEventService = agentEventService;
        this.termCodeCachedService = termCodeCachedService;
        this.retryTemplate = retryTemplate;
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceMetrics calculateMetrics(
            List<InteractionEventDAO> interactionEvents,
            ActiveServiceMetricsRequest request) {

        ServiceMetricsBuilder metricsBuilder = ServiceMetricsBuilder.initialize(request);

        ClientSetup clientSetup = configurationService.getClientSetup(request.getClientId());
        String sessionId = configurationService.getSessionId(request.getClientId());

        log.debug("Calling readServiceGeneral for serviceId: {}", request.getServiceId());
        ResponseEntity<ServiceGeneral> serviceGeneral = retryTemplate.execute(context ->
                serviceApiService.readServiceGeneral(clientSetup, request.getServiceId(), sessionId));

        Optional.ofNullable(serviceGeneral)
                .map(ResponseEntity::getBody)
                .map(ServiceGeneral::getName)
                .ifPresent(metricsBuilder::skillName);

        interactionEvents
                .forEach(interactionEvent -> {

                    Optional<LiveVoxResult> liveVoxResult = Optional.ofNullable(interactionEvent.getLvResultId())
                            .flatMap(lvResultId -> termCodeCachedService.readLiveVoxResult(clientSetup, lvResultId, sessionId));

                    liveVoxResult.ifPresent(lvResult -> {
                        CustomMetric customMetric = CustomMetric.builder()
                                .lvResultCode(lvResult.getId())
                                .count(0)
                                .build();
                        metricsBuilder.addCustomMetric(customMetric);
                    });

                    List<AgentEventDAO> agentEvents = getAgentEvents(request, interactionEvent);

                    IntStream.range(0, agentEvents.size())
                            .forEach(index -> {

                                AgentEventDAO currentAgentEvent = agentEvents.get(index);
                                Instant currentTimestamp = currentAgentEvent.getTimestamp();
                                Instant nextTimestamp = getNextTimestamp(agentEvents, index, currentAgentEvent)
                                        .orElse(currentTimestamp);

                                metricsBuilder.buildMetricsForAgentEvent(
                                        currentAgentEvent,
                                        currentTimestamp,
                                        nextTimestamp,
                                        liveVoxResult);
                            });

                    metricsBuilder.buildMetricsForInteractionEvent(interactionEvent, liveVoxResult);
                });

        return metricsBuilder.getMetrics();
    }

    @Override
    public ServiceMetrics calculateCallMetrics(InteractionEventDAO event) {

        List<AgentEventDAO> agentEvents = getAgentEventsByTransactionId(event.getTransactionId());
        ServiceMetricsBuilder metricsBuilder = ServiceMetricsBuilder.initialize();

        ListIterator<AgentEventDAO> agentEventsIterator = agentEvents.listIterator();

        while (agentEventsIterator.hasNext()) {

            AgentEventDAO currentAgentEvent = agentEventsIterator.next();
            Instant currentTimestamp = currentAgentEvent.getTimestamp();
            Instant nextTimestamp = getNextTimestamp(agentEventsIterator, currentAgentEvent)
                    .orElse(currentTimestamp);

            metricsBuilder.buildMetricsForAgentEvent(
                    currentAgentEvent,
                    currentTimestamp,
                    nextTimestamp,
                    Optional.empty());
        }

        return metricsBuilder.buildMetricsForInteractionEvent(event, Optional.empty())
                .start(event.getStart())
                .end(event.getEnd())
                .callCenterId(event.getCallCenterId())
                .clientId(event.getClientId())
                .skillId(event.getServiceId())
                .getMetrics();
    }

    private Optional<Instant> getNextTimestamp(
            ListIterator<AgentEventDAO> agentEventsIterator,
            AgentEventDAO currentAgentEvent) {

        if (agentEventsIterator.hasNext()) {
            Instant nextTimestamp = agentEventsIterator.next().getTimestamp();
            agentEventsIterator.previous();
            return Optional.ofNullable(nextTimestamp);
        } else {
            return getNextAgentEventTimestamp(currentAgentEvent);
        }
    }

    private Optional<Instant> getNextTimestamp(
            List<AgentEventDAO> agentEvents,
            int index,
            AgentEventDAO currentAgentEvent) {

        Boolean isLastAgentEvent = index == agentEvents.size() - 1;
        Boolean nextEventHasDifferentAgent = !isLastAgentEvent &&
                !currentAgentEvent.getAgentId().equals(agentEvents.get(index + 1).getAgentId());

        if (isLastAgentEvent || nextEventHasDifferentAgent) {
            return getNextAgentEventTimestamp(currentAgentEvent);
        } else {
            return Optional.ofNullable(agentEvents.get(index + 1).getTimestamp());
        }
    }

    private Optional<Instant> getNextAgentEventTimestamp(AgentEventDAO currentAgentEvent) {
        try {
            return agentEventService.getNextAgentEventTimestamp(currentAgentEvent);
        } catch (Exception e) {
            log.error("Theres was an error calling getDurationForAgentEventCarryOver for event: " + currentAgentEvent.getId(),
                    e);
            return Optional.empty();
        }
    }

    private List<AgentEventDAO> getAgentEvents(ActiveServiceMetricsRequest request, InteractionEventDAO event) {

        try {
            return Objects.isNull(event.getAgentId()) ?
                    agentEventService.getEventsByTransactionIdExcludingLogoff(event.getTransactionId()) :
                    agentEventService.getEventsByAgentExcludingLogoff(event, request.getStart(), request.getEnd());
        } catch (Exception e) {
            log.error("An error has occurred getting Agent events: ", e);
            return List.of();
        }
    }

    private List<AgentEventDAO> getAgentEventsByTransactionId(Long transactionId) {

        try {
            return agentEventService.getEventsByTransactionId(transactionId);
        } catch (Exception e) {
            log.error("An error has occurred getting Agent events: ", e);
            return List.of();
        }
    }

}
