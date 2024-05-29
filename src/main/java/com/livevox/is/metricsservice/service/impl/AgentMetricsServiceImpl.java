package com.livevox.is.metricsservice.service.impl;

import com.livevox.is.domain.api.configuration.response.Agent;
import com.livevox.is.domain.api.configuration.response.ServiceGeneral;
import com.livevox.is.domain.metrics.enumeration.AgentEventType;
import com.livevox.is.domain.metrics.enumeration.CampaignType;
import com.livevox.is.domain.metrics.request.AgentEventRequest;
import com.livevox.is.domain.metrics.response.AgentCallMetrics;
import com.livevox.is.domain.metrics.response.AgentProductivity;
import com.livevox.is.metricsservice.domain.AgentEventDAO;
import com.livevox.is.metricsservice.service.AgentMetricsService;
import com.livevox.is.metricsservice.util.AgentCallMetricsBuilder;
import com.livevox.is.metricsservice.util.AgentProductivityBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;

@Slf4j
@Service
public class AgentMetricsServiceImpl implements AgentMetricsService {

    @Override
    public List<AgentProductivity> calculateAgentProductivity(AgentEventRequest agentEventRequest, Agent agent, ServiceGeneral skill,
                                                              List<AgentEventDAO> agentEvents, boolean mergeSessions) {

        List<AgentProductivity> agentProductivityList = new ArrayList<>();
        List<Long> transactions = new ArrayList<>();
        boolean logoff = false;
        boolean lastEventInList = false;
        Instant nextTimeStamp = null;

        AgentProductivityBuilder agentProductivityBuilder = AgentProductivityBuilder.initialize().loadAgentProductivityDefaults(agentEventRequest, agent, skill);

        for (int i = 0; i < agentEvents.size(); i++) {

            AgentEventDAO agentEvent = agentEvents.get(i);
            Instant currentTimestamp = agentEvent.getTimestamp();
            logoff = AgentEventType.LOGOFF.equals(agentEvent.getEventType());
            lastEventInList = (i == agentEvents.size() - 1);

            if (agentEvent.getTransactionId() != null && !transactions.contains(agentEvent.getTransactionId())) {
                transactions.add(agentEvent.getTransactionId());
                agentProductivityBuilder.addCallsHandled(1);
            }

            if (AgentEventType.LOGON.equals(agentEvent.getEventType())) {
                agentProductivityBuilder.signIn(currentTimestamp);

            } else if (AgentEventType.LOGOFF.equals(agentEvent.getEventType())) {
                agentProductivityBuilder.signOut(currentTimestamp);

                if (!lastEventInList && !mergeSessions) {
                    agentProductivityList.add(agentProductivityBuilder.getAgentProductivity());
                    agentProductivityBuilder = AgentProductivityBuilder.initialize().loadAgentProductivityDefaults(agentEventRequest, agent, skill);
                }
            }

            if (lastEventInList) {
                nextTimeStamp = logoff ? agentEvent.getTimestamp() : agentEventRequest.getEnd();
            } else {
                nextTimeStamp = agentEvents.get(i + 1).getTimestamp();
            }

            int duration = (int) currentTimestamp.until(nextTimeStamp, SECONDS);

            calculateEventMetrics(agentProductivityBuilder, agentEvent.getEventType(), duration);

            if (agentEvent.getCampaignType() != null) {
                AgentCallMetrics agentCallMetrics = calculateAgentEventMetrics(agentEvent.getEventType(), duration);
                calculateCampaignMetrics(agentProductivityBuilder, agentCallMetrics, agentEvent.getCampaignType());
            }
        }

        if (lastEventInList || agentProductivityList.isEmpty()) {
            agentProductivityList.add(agentProductivityBuilder.getAgentProductivity());
        }

        return agentProductivityList;
    }

    private void calculateEventMetrics(AgentProductivityBuilder agentProductivityBuilder, AgentEventType agentEventType, int duration) {

        switch (agentEventType) {
            case IN_CALL -> agentProductivityBuilder.addInCallSeconds(duration);
            case AGENT_HOLD -> agentProductivityBuilder.addAgentHoldSeconds(duration);
            case WRAP_UP -> agentProductivityBuilder.addWrapUpSeconds(duration);
            case READY -> agentProductivityBuilder.addReadySeconds(duration);
            case NOT_READY -> agentProductivityBuilder.addNotReadySeconds(duration);
            case TRANSFERRING -> agentProductivityBuilder.addTransferSeconds(duration);
            default -> log.debug("Invalid choice for calculateEventMetrics: " + agentEventType);
        }
    }

    private AgentCallMetrics calculateAgentEventMetrics(AgentEventType eventType, int duration) {

        AgentCallMetricsBuilder agentCallMetricsBuilder = AgentCallMetricsBuilder.initialize();

        switch (eventType) {
            case IN_CALL -> {
                agentCallMetricsBuilder.setContactsHandled(1);
                agentCallMetricsBuilder.addAllInCallSeconds(duration);
            }
            case AGENT_HOLD -> agentCallMetricsBuilder.addAllHoldSeconds(duration);
            case WRAP_UP -> agentCallMetricsBuilder.addAllWrapUpSeconds(duration);
            default -> log.debug("Invalid choice for calculateAgentEventMetrics: " + eventType);
        }

        return agentCallMetricsBuilder.getAgentCallMetrics();
    }

    private void calculateCampaignMetrics(AgentProductivityBuilder agentProductivityBuilder, AgentCallMetrics agentCallMetrics, CampaignType campaignType) {

        switch (campaignType) {
            case INBOUND -> agentProductivityBuilder.mergeInbound(agentCallMetrics);
            case OUTBOUND -> agentProductivityBuilder.mergeOutbound(agentCallMetrics);
            case MANUAL, HCI -> agentProductivityBuilder.mergeManual(agentCallMetrics);
            case PREVIEW -> agentProductivityBuilder.mergePreview(agentCallMetrics);
            case SCHEDULE_CALL_BACK -> agentProductivityBuilder.mergeScheduled(agentCallMetrics);
            default -> log.debug("Invalid choice for calculateCampaignMetrics: " + campaignType);
        }
    }

}
