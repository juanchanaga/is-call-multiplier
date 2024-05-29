package com.livevox.is.metricsservice.util;

import com.livevox.is.domain.metrics.response.AgentCallMetrics;

public class AgentCallMetricsBuilder {

    private AgentCallMetrics agentCallMetrics = new AgentCallMetrics();

    public static AgentCallMetricsBuilder initialize() {
        AgentCallMetricsBuilder agentCallMetricsBuilder = new AgentCallMetricsBuilder();
        agentCallMetricsBuilder.agentCallMetrics.setInCallSeconds(0);
        agentCallMetricsBuilder.agentCallMetrics.setWrapUpSeconds(0);
        agentCallMetricsBuilder.agentCallMetrics.setContactsHandled(0);
        agentCallMetricsBuilder.agentCallMetrics.setHoldSeconds(0);
        agentCallMetricsBuilder.agentCallMetrics.setRightPartyInCallSeconds(0);
        agentCallMetricsBuilder.agentCallMetrics.setRightPartyWrapUpSeconds(0);
        agentCallMetricsBuilder.agentCallMetrics.setRightPartyContactsHandled(0);
        agentCallMetricsBuilder.agentCallMetrics.setRightPartyHoldSeconds(0);
        agentCallMetricsBuilder.agentCallMetrics.setManualDialingSeconds(0);
        agentCallMetricsBuilder.agentCallMetrics.setPreviewDialingSeconds(0);
        return agentCallMetricsBuilder;
    }

    public AgentCallMetricsBuilder setContactsHandled(int contacts) {
        this.agentCallMetrics.setContactsHandled(contacts);
        return this;
    }

    public AgentCallMetricsBuilder addAllInCallSeconds(int seconds) {
        this.agentCallMetrics.setInCallSeconds(this.agentCallMetrics.getInCallSeconds() + seconds);
        this.agentCallMetrics.setRightPartyInCallSeconds(this.agentCallMetrics.getRightPartyInCallSeconds() + seconds);
        return this;
    }

    public AgentCallMetricsBuilder addAllHoldSeconds(int seconds) {
        this.agentCallMetrics.setHoldSeconds(this.agentCallMetrics.getHoldSeconds() + seconds);
        this.agentCallMetrics.setRightPartyHoldSeconds(this.agentCallMetrics.getRightPartyHoldSeconds() + seconds);
        return this;
    }

    public AgentCallMetricsBuilder addAllWrapUpSeconds(int seconds) {
        this.agentCallMetrics.setWrapUpSeconds(this.agentCallMetrics.getWrapUpSeconds() + seconds);
        this.agentCallMetrics.setRightPartyWrapUpSeconds(this.agentCallMetrics.getRightPartyWrapUpSeconds() + seconds);
        return this;
    }

    public AgentCallMetrics getAgentCallMetrics() {
        return this.agentCallMetrics;
    }
}
