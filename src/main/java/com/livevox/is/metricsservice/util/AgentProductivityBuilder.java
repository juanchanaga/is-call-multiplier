package com.livevox.is.metricsservice.util;

import com.livevox.is.domain.api.configuration.response.Agent;
import com.livevox.is.domain.api.configuration.response.ServiceGeneral;
import com.livevox.is.domain.metrics.request.AgentEventRequest;
import com.livevox.is.domain.metrics.response.AgentCallMetrics;
import com.livevox.is.domain.metrics.response.AgentProductivity;

import java.time.Instant;

public class AgentProductivityBuilder {

    private AgentProductivity agentProductivity = new AgentProductivity();

    public static AgentProductivityBuilder initialize() {

        AgentProductivityBuilder agentProductivityBuilder = new AgentProductivityBuilder();
        agentProductivityBuilder.agentProductivity.setCallsHandled(0);
        agentProductivityBuilder.agentProductivity.setInCallSeconds(0);
        agentProductivityBuilder.agentProductivity.setWrapUpSeconds(0);
        agentProductivityBuilder.agentProductivity.setReadySeconds(0);
        agentProductivityBuilder.agentProductivity.setNotReadySeconds(0);
        agentProductivityBuilder.agentProductivity.setTransferSeconds(0);
        agentProductivityBuilder.agentProductivity.setAgentHoldSeconds(0);
        agentProductivityBuilder.agentProductivity.setTotalInboundCalls(0);
        agentProductivityBuilder.agentProductivity.setTotalOutboundCalls(0);
        agentProductivityBuilder.agentProductivity.setTotalAgentCalls(0);
        agentProductivityBuilder.agentProductivity.setInbound(AgentCallMetricsBuilder.initialize().getAgentCallMetrics());
        agentProductivityBuilder.agentProductivity.setOutbound(AgentCallMetricsBuilder.initialize().getAgentCallMetrics());
        agentProductivityBuilder.agentProductivity.setManual(AgentCallMetricsBuilder.initialize().getAgentCallMetrics());
        agentProductivityBuilder.agentProductivity.setPreview(AgentCallMetricsBuilder.initialize().getAgentCallMetrics());
        agentProductivityBuilder.agentProductivity.setScheduled(AgentCallMetricsBuilder.initialize().getAgentCallMetrics());
        return agentProductivityBuilder;
    }

    public AgentProductivityBuilder loadAgentProductivityDefaults(AgentEventRequest agentEventRequest, Agent agent, ServiceGeneral skill) {
        return this.start(agentEventRequest.getStart())
                .end(agentEventRequest.getEnd())
                .clientId(agentEventRequest.getClientId())
                .agentId(agentEventRequest.getAgentId())
                .loginId(agent.getLoginId())
                .firstName(agent.getFirstName())
                .lastName(agent.getLastName())
                .skillId(skill.getServiceId())
                .skillName(skill.getName());
    }

    public AgentProductivityBuilder start(Instant start) {
        this.agentProductivity.setStart(start);
        return this;
    }

    public AgentProductivityBuilder end(Instant end) {
        this.agentProductivity.setEnd(end);
        return this;
    }

    public AgentProductivityBuilder clientId(Integer clientId) {
        this.agentProductivity.setClientId(clientId);
        return this;
    }

    public AgentProductivityBuilder agentId(Integer agentId) {
        this.agentProductivity.setAgentId(agentId);
        return this;
    }

    public AgentProductivityBuilder loginId(String loginId) {
        this.agentProductivity.setLoginId(loginId);
        return this;
    }

    public AgentProductivityBuilder firstName(String firstName) {
        this.agentProductivity.setFirstName(firstName);
        return this;
    }

    public AgentProductivityBuilder lastName(String lastName) {
        this.agentProductivity.setLastName(lastName);
        return this;
    }

    public AgentProductivityBuilder skillId(Integer skillId) {
        this.agentProductivity.setSkillId(skillId);
        return this;
    }

    public AgentProductivityBuilder skillName(String skillName) {
        this.agentProductivity.setSkillName(skillName == null ? "NOT FOUND" : skillName);
        return this;
    }

    public AgentProductivityBuilder signIn(Instant signIn) {
        this.agentProductivity.setSignIn(signIn);
        return this;
    }

    public AgentProductivityBuilder signOut(Instant signOut) {
        this.agentProductivity.setSignOut(signOut);
        return this;
    }

    public AgentProductivityBuilder addCallsHandled(int calls) {
        this.agentProductivity.setCallsHandled(this.agentProductivity.getCallsHandled() + calls);
        return this;
    }

    public AgentProductivityBuilder addInCallSeconds(int seconds) {
        this.agentProductivity.setInCallSeconds(this.agentProductivity.getInCallSeconds() + seconds);
        return this;
    }

    public AgentProductivityBuilder addAgentHoldSeconds(int seconds) {
        this.agentProductivity.setAgentHoldSeconds(this.agentProductivity.getAgentHoldSeconds() + seconds);
        return this;
    }

    public AgentProductivityBuilder addWrapUpSeconds(int seconds) {
        this.agentProductivity.setWrapUpSeconds(this.agentProductivity.getWrapUpSeconds() + seconds);
        return this;
    }

    public AgentProductivityBuilder addReadySeconds(int seconds) {
        this.agentProductivity.setReadySeconds(this.agentProductivity.getReadySeconds() + seconds);
        return this;
    }

    public AgentProductivityBuilder addNotReadySeconds(int seconds) {
        this.agentProductivity.setNotReadySeconds(this.agentProductivity.getNotReadySeconds() + seconds);
        return this;
    }

    public AgentProductivityBuilder addTransferSeconds(int seconds) {
        this.agentProductivity.setTransferSeconds(this.agentProductivity.getTransferSeconds() + seconds);
        return this;
    }

    public AgentProductivityBuilder mergeInbound(AgentCallMetrics newMetrics) {
        mergeAgentCallMetrics(this.agentProductivity.getInbound(), newMetrics);
        return this;
    }

    public AgentProductivityBuilder mergeOutbound(AgentCallMetrics newMetrics) {
        mergeAgentCallMetrics(this.agentProductivity.getOutbound(), newMetrics);
        return this;
    }

    public AgentProductivityBuilder mergeManual(AgentCallMetrics newMetrics) {
        mergeAgentCallMetrics(this.agentProductivity.getManual(), newMetrics);
        return this;
    }

    public AgentProductivityBuilder mergePreview(AgentCallMetrics newMetrics) {
        mergeAgentCallMetrics(this.agentProductivity.getPreview(), newMetrics);
        return this;
    }

    public AgentProductivityBuilder mergeScheduled(AgentCallMetrics newMetrics) {
        mergeAgentCallMetrics(this.agentProductivity.getScheduled(), newMetrics);
        return this;
    }

    private void mergeAgentCallMetrics(AgentCallMetrics agentCallMetrics, AgentCallMetrics newMetrics) {

        agentCallMetrics.setInCallSeconds(agentCallMetrics.getInCallSeconds() + newMetrics.getInCallSeconds());
        agentCallMetrics.setWrapUpSeconds(agentCallMetrics.getWrapUpSeconds() + newMetrics.getWrapUpSeconds());
        agentCallMetrics.setContactsHandled(agentCallMetrics.getContactsHandled() + newMetrics.getContactsHandled());
        agentCallMetrics.setHoldSeconds(agentCallMetrics.getHoldSeconds() + newMetrics.getHoldSeconds());
        agentCallMetrics.setRightPartyInCallSeconds(agentCallMetrics.getRightPartyInCallSeconds() + newMetrics.getRightPartyInCallSeconds());
        agentCallMetrics.setRightPartyWrapUpSeconds(agentCallMetrics.getRightPartyWrapUpSeconds() + newMetrics.getRightPartyWrapUpSeconds());
        agentCallMetrics.setRightPartyContactsHandled(agentCallMetrics.getRightPartyContactsHandled() + newMetrics.getRightPartyContactsHandled());
        agentCallMetrics.setRightPartyHoldSeconds(agentCallMetrics.getRightPartyHoldSeconds() + newMetrics.getRightPartyHoldSeconds());
        agentCallMetrics.setManualDialingSeconds(agentCallMetrics.getManualDialingSeconds() + newMetrics.getManualDialingSeconds());
        agentCallMetrics.setPreviewDialingSeconds(agentCallMetrics.getPreviewDialingSeconds() + newMetrics.getPreviewDialingSeconds());
    }

    public AgentProductivity getAgentProductivity() {
        return this.agentProductivity;
    }
}
