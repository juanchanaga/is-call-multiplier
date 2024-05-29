package com.livevox.is.metricsservice.util;

import com.livevox.is.domain.api.configuration.response.LiveVoxResult;
import com.livevox.is.domain.metrics.enumeration.CampaignType;
import com.livevox.is.domain.metrics.request.ActiveServiceMetricsRequest;
import com.livevox.is.metricsservice.domain.AgentEventDAO;
import com.livevox.is.metricsservice.domain.InteractionEventDAO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Slf4j
public class MetricsUtils {

    public static ServiceMetricsBuilder initializeBuilder(ActiveServiceMetricsRequest request) {
        return ServiceMetricsBuilder
                .initialize()
                .clientId(request.getClientId())
                .callCenterId(request.getCallCenterId())
                .skillId(request.getServiceId())
                .start(request.getStart())
                .end(request.getEnd())
                .intervalDate(request.getStart());
    }

    /**
     * Calculate duration in seconds of time spent between events.
     *
     * @param start
     * @param end
     * @return int
     */
    public static int calculateDuration(Instant start, Instant end) {
        return (int) Duration.between(start, end).toMillis();
    }

    /**
     * Updates the given ServiceMetrics object with the provided AgentEventDAO data
     *
     * @param metricsBuilder
     * @param agentEvent
     * @param duration
     */
    public static void updateAgentMetrics(
            ServiceMetricsBuilder metricsBuilder,
            AgentEventDAO agentEvent,
            Integer duration) {

        switch (agentEvent.getEventType()) {
            case READY -> metricsBuilder.addReadySeconds(duration);
            case NOT_READY -> metricsBuilder.addNotReadySeconds(duration);
            case IN_CALL,
                    DIALING,
                    TRANSFERRING,
                    AGENT_RESUMED,
                    AGENT_TRANSFER_CONNECTED_SUPERVISOR,
                    AGENT_TRANSFER_CONNECTED_AGENT2,
                    AGENT_TRANSFER_CONFERENCED_SUPERVISOR,
                    AGENT_TRANSFER_CONFERENCED_AGENT2,
                    AGENT_TRANSFER_RECONNECTED_SUPERVISOR,
                    AGENT_TRANSFER_RECONNECTED_AGENT2,
                    AGENT_TRANSFER_RESUMED_SUPERVISOR,
                    AGENT_TRANSFER_RESUMED_AGENT2,
                    AGENT_TRANSFER_COMPLETE_SUPERVISOR,
                    AGENT_TRANSFER_COMPLETE_AGENT2,
                    AGENT_TRANSFER_HOLD_SUPERVISOR,
                    AGENT_TRANSFER_HOLD_AGENT2,
                    AGENT_TRANSFER_INITIATED_SUPERVISOR,
                    AGENT_TRANSFER_INITIATED_AGENT2 -> metricsBuilder.addInCallSeconds(duration);
            case WRAP_UP -> metricsBuilder.addWrapUpSeconds(duration);
            case PREVIEW_DIAL_OFFERED -> metricsBuilder.addPreviewDialSeconds(duration);
            case AGENT_HOLD -> metricsBuilder.addHoldSeconds(duration);
            default -> log.debug("Unmapped eventType: {}", agentEvent.getEventType());
        }
    }

    public static boolean isRpc(Optional<LiveVoxResult> liveVoxResult) {
        return liveVoxResult
                .filter(LiveVoxResult::isRightPartyContact)
                .isPresent();
    }

    public static void updateCallMetrics(
            ServiceMetricsBuilder metricsBuilder,
            AgentEventDAO agentEvent,
            Integer duration) {

        switch (agentEvent.getEventType()) {
            case IN_CALL -> metricsBuilder.addRightPartyInCallSeconds(duration);
            case WRAP_UP -> metricsBuilder.addRightPartyWrapUpSeconds(duration);
            default -> log.debug("Invalid choice for calcCallAgentMetrics: " + agentEvent.getEventType());
        }
    }

    public static void updateServiceMetrics(
            ServiceMetricsBuilder metricsBuilder,
            InteractionEventDAO interactionEvent) {

        Optional.ofNullable(interactionEvent.getCampaignType())
                .filter(CampaignType.INBOUND::equals)
                .ifPresent(campaignType -> metricsBuilder.addTotalInboundCalls(1));

        int roundedIvr = interactionEvent.getRoundedIvrDuration() == null ? 0 : interactionEvent.getRoundedIvrDuration();
        int transferHoldDuration = interactionEvent.getTransferHoldDuration() == null ? 0 : interactionEvent.getTransferHoldDuration();
        metricsBuilder.addCallsAttempted(1);
        metricsBuilder.addIvrSeconds(roundedIvr);
        metricsBuilder.addTransferHoldDuration(transferHoldDuration);
        metricsBuilder.addRunningAccount(interactionEvent.getAccount());

    }

    public static void mapTermCodeValues(ServiceMetricsBuilder metricsBuilder, LiveVoxResult lvResult) {

        if (lvResult.isPromiseToPay()) {
            metricsBuilder.addPromiseToPayCalls(1);
        }

        switch (lvResult.getName()) {
            case "RPC" -> metricsBuilder.addRightPartyContacts(1);
            case "Wrong Party", "WPC", "Other" -> metricsBuilder.addWrongPartyContacts(1);
            default -> {
                if (lvResult.isRightPartyContact()) {
                    metricsBuilder.addRightPartyContacts(1);
                } else if (lvResult.isWrongPartyContact()) {
                    metricsBuilder.addWrongPartyContacts(1);
                }
                log.debug("term code found that is not configured for dashboard metrics: " + lvResult.getName());
            }
        }
    }

    public static void updateOperatorTransferMetrics(
            ServiceMetricsBuilder metricsBuilder,
            InteractionEventDAO interactionEvent) {

        if (interactionEvent.isOperatorTransfer()) {
            if (interactionEvent.isOperatorTransferSuccessful()) {
                metricsBuilder.addSuccessfulOperatorTransfers(1);
            } else {
                metricsBuilder.addFailedOperatorTransfers(1);
            }
        } else if (interactionEvent.getRoundedIvrDuration() != null) {
            metricsBuilder.addConnectedCalls(1);
        } else {
            metricsBuilder.addNotConnectedCalls(1);
        }
    }

    public static ServiceMetricsBuilder updateServiceLevelThresholdMetrics(
            ServiceMetricsBuilder metricsBuilder,
            InteractionEventDAO interactionEvent) {

        if (interactionEvent.getOperatorTransfer() == null || interactionEvent.getOperatorTransfer() == 0) {
            return metricsBuilder;
        }

        if (ObjectUtils.allNotNull(
                interactionEvent.getOperatorTransferSuccessful(),
                interactionEvent.getTransferHoldDuration(),
                interactionEvent.getServiceLevelThreshold())) {

            if (interactionEvent.isOperatorTransferSuccessful()) {
                if (interactionEvent.getTransferHoldDuration() <= interactionEvent.getServiceLevelThreshold()) {
                    metricsBuilder.addSuccessfulCallsBelowServiceLevelThreshold(1);
                } else {
                    metricsBuilder.addSuccessfulCallsAfterServiceLevelThreshold(1);
                }
            } else {
                if (interactionEvent.getTransferHoldDuration() <= interactionEvent.getServiceLevelThreshold()) {
                    metricsBuilder.addAbandonedCallsBelowServiceLevelThreshold(1);
                } else {
                    metricsBuilder.addAbandonedCallsAfterServiceLevelThreshold(1);
                }
            }
        }

        return metricsBuilder;
    }
}
