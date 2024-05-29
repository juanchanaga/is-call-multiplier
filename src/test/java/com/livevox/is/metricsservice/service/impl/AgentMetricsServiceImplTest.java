package com.livevox.is.metricsservice.service.impl;

import com.livevox.is.domain.api.configuration.response.Agent;
import com.livevox.is.domain.api.configuration.response.ServiceGeneral;
import com.livevox.is.domain.metrics.enumeration.AgentEventType;
import com.livevox.is.domain.metrics.enumeration.CampaignType;
import com.livevox.is.domain.metrics.request.AgentEventRequest;
import com.livevox.is.domain.metrics.response.AgentProductivity;
import com.livevox.is.metricsservice.domain.AgentEventDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AgentMetricsServiceImplTest {

    private AgentMetricsServiceImpl agentMetricsService;
    private AgentEventRequest agentEventRequest;
    private Agent agent;
    private ServiceGeneral skill;

    @BeforeEach
    public void initialize() {

        Random random = new Random();
        Instant start = Instant.now();
        Instant end = start.plus(35, SECONDS);

        agentMetricsService = new AgentMetricsServiceImpl();

        agentEventRequest = new AgentEventRequest();
        agentEventRequest.setStart(start);
        agentEventRequest.setEnd(end);

        agent = new Agent();
        agent.setFirstName("BOB");
        agent.setLastName("SMITH");
        agent.setLoginId("BSMITH");

        skill = new ServiceGeneral();
        skill.setServiceId(random.nextInt());
    }

    @Test
    public void calculateAgentProductivity_Inbound() {

        List<AgentEventDAO> agentEvents = loadAgentEvents(CampaignType.INBOUND, agentEventRequest.getStart());

        List<AgentProductivity> agentProductivities = agentMetricsService.calculateAgentProductivity(agentEventRequest, agent, skill, agentEvents, false);

        AgentProductivity agentProductivity = agentProductivities.get(0);
        verifyAgentProductivityData(agentProductivity);

        assertEquals(1, agentProductivity.getInbound().getContactsHandled());
        assertEquals(5, agentProductivity.getInbound().getInCallSeconds());
        assertEquals(5, agentProductivity.getInbound().getRightPartyInCallSeconds());
        assertEquals(5, agentProductivity.getInbound().getWrapUpSeconds());
        assertEquals(5, agentProductivity.getInbound().getRightPartyWrapUpSeconds());
        assertEquals(5, agentProductivity.getInbound().getHoldSeconds());
        assertEquals(5, agentProductivity.getInbound().getRightPartyHoldSeconds());
    }

    @Test
    public void calculateAgentProductivity_Outbound() {

        List<AgentEventDAO> agentEvents = loadAgentEvents(CampaignType.OUTBOUND, agentEventRequest.getStart());

        List<AgentProductivity> agentProductivities = agentMetricsService.calculateAgentProductivity(agentEventRequest, agent, skill, agentEvents, false);

        AgentProductivity agentProductivity = agentProductivities.get(0);
        verifyAgentProductivityData(agentProductivity);

        assertEquals(1, agentProductivity.getOutbound().getContactsHandled());
        assertEquals(5, agentProductivity.getOutbound().getInCallSeconds());
        assertEquals(5, agentProductivity.getOutbound().getRightPartyInCallSeconds());
        assertEquals(5, agentProductivity.getOutbound().getWrapUpSeconds());
        assertEquals(5, agentProductivity.getOutbound().getRightPartyWrapUpSeconds());
        assertEquals(5, agentProductivity.getOutbound().getHoldSeconds());
        assertEquals(5, agentProductivity.getOutbound().getRightPartyHoldSeconds());
    }

    @Test
    public void calculateAgentProductivity_Manual() {

        List<AgentEventDAO> agentEvents = loadAgentEvents(CampaignType.MANUAL, agentEventRequest.getStart());

        List<AgentProductivity> agentProductivities = agentMetricsService.calculateAgentProductivity(agentEventRequest, agent, skill, agentEvents, false);

        AgentProductivity agentProductivity = agentProductivities.get(0);
        verifyAgentProductivityData(agentProductivity);

        assertEquals(1, agentProductivity.getManual().getContactsHandled());
        assertEquals(5, agentProductivity.getManual().getInCallSeconds());
        assertEquals(5, agentProductivity.getManual().getRightPartyInCallSeconds());
        assertEquals(5, agentProductivity.getManual().getWrapUpSeconds());
        assertEquals(5, agentProductivity.getManual().getRightPartyWrapUpSeconds());
        assertEquals(5, agentProductivity.getManual().getHoldSeconds());
        assertEquals(5, agentProductivity.getManual().getRightPartyHoldSeconds());
    }

    @Test
    public void calculateAgentProductivity_HCI() {

        List<AgentEventDAO> agentEvents = loadAgentEvents(CampaignType.HCI, agentEventRequest.getStart());

        List<AgentProductivity> agentProductivities = agentMetricsService.calculateAgentProductivity(agentEventRequest, agent, skill, agentEvents, false);

        AgentProductivity agentProductivity = agentProductivities.get(0);
        verifyAgentProductivityData(agentProductivity);

        assertEquals(1, agentProductivity.getManual().getContactsHandled());
        assertEquals(5, agentProductivity.getManual().getInCallSeconds());
        assertEquals(5, agentProductivity.getManual().getRightPartyInCallSeconds());
        assertEquals(5, agentProductivity.getManual().getWrapUpSeconds());
        assertEquals(5, agentProductivity.getManual().getRightPartyWrapUpSeconds());
        assertEquals(5, agentProductivity.getManual().getHoldSeconds());
        assertEquals(5, agentProductivity.getManual().getRightPartyHoldSeconds());
    }

    @Test
    public void calculateAgentProductivity_Preview() {

        List<AgentEventDAO> agentEvents = loadAgentEvents(CampaignType.PREVIEW, agentEventRequest.getStart());

        List<AgentProductivity> agentProductivities = agentMetricsService.calculateAgentProductivity(agentEventRequest, agent, skill, agentEvents, false);

        AgentProductivity agentProductivity = agentProductivities.get(0);
        verifyAgentProductivityData(agentProductivity);

        assertEquals(1, agentProductivity.getPreview().getContactsHandled());
        assertEquals(5, agentProductivity.getPreview().getInCallSeconds());
        assertEquals(5, agentProductivity.getPreview().getRightPartyInCallSeconds());
        assertEquals(5, agentProductivity.getPreview().getWrapUpSeconds());
        assertEquals(5, agentProductivity.getPreview().getRightPartyWrapUpSeconds());
        assertEquals(5, agentProductivity.getPreview().getHoldSeconds());
        assertEquals(5, agentProductivity.getPreview().getRightPartyHoldSeconds());
    }

    @Test
    public void calculateAgentProductivity_Scheduled() {

        List<AgentEventDAO> agentEvents = loadAgentEvents(CampaignType.SCHEDULE_CALL_BACK, agentEventRequest.getStart());

        List<AgentProductivity> agentProductivities = agentMetricsService.calculateAgentProductivity(agentEventRequest, agent, skill, agentEvents, false);

        AgentProductivity agentProductivity = agentProductivities.get(0);
        verifyAgentProductivityData(agentProductivity);

        assertEquals(1, agentProductivity.getScheduled().getContactsHandled());
        assertEquals(5, agentProductivity.getScheduled().getInCallSeconds());
        assertEquals(5, agentProductivity.getScheduled().getRightPartyInCallSeconds());
        assertEquals(5, agentProductivity.getScheduled().getWrapUpSeconds());
        assertEquals(5, agentProductivity.getScheduled().getRightPartyWrapUpSeconds());
        assertEquals(5, agentProductivity.getScheduled().getHoldSeconds());
        assertEquals(5, agentProductivity.getScheduled().getRightPartyHoldSeconds());
    }

    private void verifyAgentProductivityData(AgentProductivity agentProductivity) {

        assertEquals(1, agentProductivity.getCallsHandled());
        assertEquals(5, agentProductivity.getNotReadySeconds());
        assertEquals(5, agentProductivity.getReadySeconds());
        assertEquals(5, agentProductivity.getTransferSeconds());
        assertEquals(5, agentProductivity.getInCallSeconds());
        assertEquals(5, agentProductivity.getAgentHoldSeconds());
        assertEquals(5, agentProductivity.getWrapUpSeconds());

        assertEquals(agentEventRequest.getStart(), agentProductivity.getStart());
        assertEquals(agentEventRequest.getEnd(), agentProductivity.getEnd());
        assertEquals(agentEventRequest.getStart(), agentProductivity.getSignIn());
        assertEquals(agentEventRequest.getEnd(), agentProductivity.getSignOut());

        assertEquals(agent.getLoginId(), agentProductivity.getLoginId());
        assertEquals(agent.getFirstName(), agentProductivity.getFirstName());
        assertEquals(agent.getLastName(), agentProductivity.getLastName());

        assertEquals(skill.getServiceId(), agentProductivity.getSkillId());
        assertEquals("NOT FOUND", agentProductivity.getSkillName());
    }

    private List<AgentEventDAO> loadAgentEvents(CampaignType campaignType, Instant now) {

        List<AgentEventDAO> agentEvents = new ArrayList<>();
        agentEvents.add(createAgentEvent(AgentEventType.LOGON, campaignType, now));
        agentEvents.add(createAgentEvent(AgentEventType.NOT_READY, campaignType, now.plus(5, SECONDS)));
        agentEvents.add(createAgentEvent(AgentEventType.READY, campaignType, now.plus(10, SECONDS)));
        agentEvents.add(createAgentEvent(AgentEventType.TRANSFERRING, campaignType, now.plus(15, SECONDS)));
        agentEvents.add(createAgentEvent(AgentEventType.IN_CALL, campaignType, now.plus(20, SECONDS)));
        agentEvents.add(createAgentEvent(AgentEventType.AGENT_HOLD, campaignType, now.plus(25, SECONDS)));
        agentEvents.add(createAgentEvent(AgentEventType.WRAP_UP, campaignType, now.plus(30, SECONDS)));
        agentEvents.add(createAgentEvent(AgentEventType.LOGOFF, campaignType, now.plus(35, SECONDS)));

        return agentEvents;
    }

    private AgentEventDAO createAgentEvent(AgentEventType agentEventType, CampaignType campaignType, Instant timestamp) {

        AgentEventDAO agentEvent = new AgentEventDAO();
        agentEvent.setCampaignType(campaignType);
        agentEvent.setEventType(agentEventType);
        agentEvent.setTimestamp(timestamp);

        switch (agentEventType) {
            case TRANSFERRING, IN_CALL, AGENT_HOLD, WRAP_UP -> agentEvent.setTransactionId(1229258166549l);
        }

        return agentEvent;
    }

}
