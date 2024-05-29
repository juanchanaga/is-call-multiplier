package com.livevox.is.metricsservice.util;

import com.livevox.is.domain.api.configuration.response.LiveVoxResult;
import com.livevox.is.domain.metrics.CustomMetric;
import com.livevox.is.domain.metrics.ServiceMetrics;
import com.livevox.is.domain.metrics.request.ActiveServiceMetricsRequest;
import com.livevox.is.metricsservice.domain.AgentEventDAO;
import com.livevox.is.metricsservice.domain.InteractionEventDAO;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Builder for {@link ServiceMetrics} class
 *
 * @author fbastos
 */
public class ServiceMetricsBuilder {

    private ServiceMetrics serviceMetrics = new ServiceMetrics();

    public static ServiceMetricsBuilder initialize() {

        ServiceMetricsBuilder builder = new ServiceMetricsBuilder();
        builder.serviceMetrics.setCustomMetrics(new ArrayList<>());
        builder.serviceMetrics.setHoldSeconds(0);
        builder.serviceMetrics.setIvrSeconds(0);
        builder.serviceMetrics.setSuccessfulOperatorTransfers(0);
        builder.serviceMetrics.setFailedOperatorTransfers(0);
        builder.serviceMetrics.setInCallSeconds(0);
        builder.serviceMetrics.setWrapUpSeconds(0);
        builder.serviceMetrics.setReadySeconds(0);
        builder.serviceMetrics.setPreviewDialSeconds(0);
        builder.serviceMetrics.setTransferHoldDuration(0);
        builder.serviceMetrics.setAccountsAttempted(0);
        builder.serviceMetrics.setCallsAttempted(0);
        builder.serviceMetrics.setRightPartyInCallSeconds(0);
        builder.serviceMetrics.setRightPartyWrapUpSeconds(0);
        builder.serviceMetrics.setTotalInboundCalls(0);
        builder.serviceMetrics.setConnectedCalls(0);
        builder.serviceMetrics.setNotConnectedCalls(0);
        builder.serviceMetrics.setNotReadySeconds(0);
        builder.serviceMetrics.setPtpCalls(0);
        builder.serviceMetrics.setRightPartyContacts(0);
        builder.serviceMetrics.setWrongPartyContacts(0);
        builder.serviceMetrics.setSuccessfulCallsBelowServiceLevelThreshold(0);
        builder.serviceMetrics.setSuccessfulCallsAfterServiceLevelThreshold(0);
        builder.serviceMetrics.setAbandonedCallsBelowServiceLevelThreshold(0);
        builder.serviceMetrics.setAbandonedCallsAfterServiceLevelThreshold(0);
        builder.serviceMetrics.setRunningAccountList(new ArrayList<>());
        return builder;
    }

    public static ServiceMetricsBuilder initialize(ActiveServiceMetricsRequest request) {
        return MetricsUtils.initializeBuilder(request);
    }

    public ServiceMetricsBuilder clientId(Integer clientId) {
        this.serviceMetrics.setClientId(clientId);
        return this;
    }


    public ServiceMetricsBuilder callCenterId(Integer serviceId) {
        this.serviceMetrics.setCallCenterId(serviceId);
        return this;
    }

    public ServiceMetricsBuilder skillId(Integer skillId) {
        this.serviceMetrics.setSkillId(skillId);
        return this;
    }

    public ServiceMetricsBuilder start(Instant start) {
        this.serviceMetrics.setStart(start);
        return this;
    }

    public ServiceMetricsBuilder end(Instant end) {
        this.serviceMetrics.setEnd(end);
        return this;
    }

    public ServiceMetricsBuilder intervalDate(Instant intervalDate) {
        this.serviceMetrics.setIntervalDate(intervalDate);
        return this;
    }

    public ServiceMetricsBuilder skillName(String skillName) {
        this.serviceMetrics.setSkillName(skillName);
        return this;
    }

    public ServiceMetricsBuilder addCustomMetric(CustomMetric customMetric) {
        this.serviceMetrics.getCustomMetrics().add(customMetric);
        return this;
    }

    public ServiceMetricsBuilder addReadySeconds(int seconds) {
        this.serviceMetrics.setReadySeconds(this.serviceMetrics.getReadySeconds() + seconds);
        return this;
    }

    public ServiceMetricsBuilder addNotReadySeconds(int seconds) {
        this.serviceMetrics.setNotReadySeconds(this.serviceMetrics.getNotReadySeconds() + seconds);
        return this;
    }

    public ServiceMetricsBuilder addInCallSeconds(int seconds) {
        this.serviceMetrics.setInCallSeconds(this.serviceMetrics.getInCallSeconds() + seconds);
        return this;
    }

    public ServiceMetricsBuilder addWrapUpSeconds(int seconds) {
        this.serviceMetrics.setWrapUpSeconds(this.serviceMetrics.getWrapUpSeconds() + seconds);
        return this;
    }

    public ServiceMetricsBuilder addPreviewDialSeconds(int seconds) {
        this.serviceMetrics.setPreviewDialSeconds(this.serviceMetrics.getPreviewDialSeconds() + seconds);
        return this;
    }

    public ServiceMetricsBuilder addHoldSeconds(int seconds) {
        this.serviceMetrics.setHoldSeconds(this.serviceMetrics.getHoldSeconds() + seconds);
        return this;
    }

    public ServiceMetricsBuilder addRightPartyInCallSeconds(int seconds) {
        this.serviceMetrics.setRightPartyInCallSeconds(this.serviceMetrics.getRightPartyInCallSeconds() + seconds);
        return this;
    }

    public ServiceMetricsBuilder addRightPartyWrapUpSeconds(int seconds) {
        this.serviceMetrics.setRightPartyWrapUpSeconds(this.serviceMetrics.getRightPartyWrapUpSeconds() + seconds);
        return this;
    }

    public ServiceMetricsBuilder addTotalInboundCalls(int value) {
        this.serviceMetrics.setTotalInboundCalls(this.serviceMetrics.getTotalInboundCalls() + value);
        return this;
    }

    public ServiceMetricsBuilder addCallsAttempted(int value) {
        this.serviceMetrics.setCallsAttempted(this.serviceMetrics.getCallsAttempted() + value);
        return this;
    }

    public ServiceMetricsBuilder addIvrSeconds(int value) {
        this.serviceMetrics.setIvrSeconds(this.serviceMetrics.getIvrSeconds() + value);
        return this;
    }

    public ServiceMetricsBuilder addTransferHoldDuration(int value) {
        this.serviceMetrics.setTransferHoldDuration(this.serviceMetrics.getTransferHoldDuration() + value);
        return this;
    }

    public ServiceMetricsBuilder addSuccessfulOperatorTransfers(int value) {
        this.serviceMetrics.setSuccessfulOperatorTransfers(this.serviceMetrics.getSuccessfulOperatorTransfers() + value);
        return this;
    }

    public ServiceMetricsBuilder addFailedOperatorTransfers(int value) {
        this.serviceMetrics.setFailedOperatorTransfers(this.serviceMetrics.getFailedOperatorTransfers() + value);
        return this;
    }

    public ServiceMetricsBuilder addConnectedCalls(int value) {
        this.serviceMetrics.setConnectedCalls(this.serviceMetrics.getConnectedCalls() + value);
        return this;
    }

    public ServiceMetricsBuilder addNotConnectedCalls(int value) {
        this.serviceMetrics.setNotConnectedCalls(this.serviceMetrics.getNotConnectedCalls() + value);
        return this;
    }

    public ServiceMetricsBuilder addPromiseToPayCalls(int value) {
        this.serviceMetrics.setPtpCalls(this.serviceMetrics.getPtpCalls() + value);
        return this;
    }

    public ServiceMetricsBuilder addRightPartyContacts(int value) {
        this.serviceMetrics.setRightPartyContacts(this.serviceMetrics.getRightPartyContacts() + value);
        return this;
    }

    public ServiceMetricsBuilder addWrongPartyContacts(int value) {
        this.serviceMetrics.setWrongPartyContacts(this.serviceMetrics.getWrongPartyContacts() + value);
        return this;
    }

    public ServiceMetricsBuilder addSuccessfulCallsBelowServiceLevelThreshold(int value) {
        this.serviceMetrics.setSuccessfulCallsBelowServiceLevelThreshold(
                this.serviceMetrics.getSuccessfulCallsBelowServiceLevelThreshold() + value);
        return this;
    }

    public ServiceMetricsBuilder addSuccessfulCallsAfterServiceLevelThreshold(int value) {
        this.serviceMetrics.setSuccessfulCallsAfterServiceLevelThreshold(
                this.serviceMetrics.getSuccessfulCallsAfterServiceLevelThreshold() + value);
        return this;
    }

    public ServiceMetricsBuilder addAbandonedCallsBelowServiceLevelThreshold(int value) {
        this.serviceMetrics.setAbandonedCallsBelowServiceLevelThreshold(
                this.serviceMetrics.getAbandonedCallsBelowServiceLevelThreshold() + value);
        return this;
    }

    public ServiceMetricsBuilder addAbandonedCallsAfterServiceLevelThreshold(int value) {
        this.serviceMetrics.setAbandonedCallsAfterServiceLevelThreshold(
                this.serviceMetrics.getAbandonedCallsAfterServiceLevelThreshold() + value);
        return this;
    }

    public ServiceMetricsBuilder addRunningAccount(String accountValue) {
        if (!this.serviceMetrics.getRunningAccountList().contains(accountValue)) {
            this.serviceMetrics.setAccountsAttempted(this.serviceMetrics.getAccountsAttempted() + 1);
            this.serviceMetrics.getRunningAccountList().add(accountValue);
        }
        return this;
    }

    public ServiceMetrics getMetrics() {
        return this.serviceMetrics;
    }

    public ServiceMetricsBuilder buildMetricsForAgentEvent(
            AgentEventDAO agentEvent,
            Instant currentTimestamp,
            Instant nextTimestamp,
            Optional<LiveVoxResult> liveVoxResult) {

        Integer duration = MetricsUtils.calculateDuration(currentTimestamp, nextTimestamp);
        MetricsUtils.updateAgentMetrics(this, agentEvent, duration);
        if (MetricsUtils.isRpc(liveVoxResult)) {
            MetricsUtils.updateCallMetrics(this, agentEvent, duration);
        }

        return this;
    }

    public ServiceMetricsBuilder buildMetricsForInteractionEvent(
            InteractionEventDAO interactionEvent,
            Optional<LiveVoxResult> liveVoxResult) {

        MetricsUtils.updateOperatorTransferMetrics(this, interactionEvent);
        liveVoxResult.ifPresent(lvResult -> {
            MetricsUtils.mapTermCodeValues(this, lvResult);
            this.serviceMetrics.getCustomMetrics()
                    .stream()
                    .filter(customMetric -> customMetric.getLvResultCode().equals(lvResult.getId()))
                    .forEach(customMetric -> customMetric.addCount(1));
        });
        MetricsUtils.updateServiceMetrics(this, interactionEvent);
        MetricsUtils.updateServiceLevelThresholdMetrics(this, interactionEvent);

        return this;
    }
}
