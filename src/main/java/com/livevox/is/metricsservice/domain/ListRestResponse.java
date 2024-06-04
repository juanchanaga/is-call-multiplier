/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.livevox.integration.commons.domain.lvapi.*;
import com.livevox.integration.commons.domain.stats.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@JsonInclude(Include.NON_NULL)
@Data
public class ListRestResponse implements Serializable {

    private static final long serialVersionUID = -5168724226668328256L;

    private String next;
    private Date timeStamp;
    private String token;
    private List<AgentStats> agentStatistics;
    private List<AgentStatus> agentDetails;
    private List<ServiceStats> stats;
    private List<Skill> service;
    private List<TermCode> category;
    private List<TermCode> termCode;
    private List<LvResult> lvResult;
    private List<CallCenter> callCenter;
    private List<ScreenPop> screenPop;
    private List<AccountTransaction> transaction;
    private List<Campaign> campaign;
    private List<ManualCall> nonAttemptedManualCalls;
    private List<CallRecording> callRecording;
    private List<CallDetail> call;
    private List<AgentAccount> results;
    private List<Agent> agent;
    private List<CallHistory> callHistory;
    private List<StateChange> stateChange;
    private List<ServiceMetrics> metrics;
    private List<DialingProfile> dialingProfile;
    private List<AgentEvent> agentEvent;
    private List<IdType> idType;
    private List<AgentProductivity> agentProductivity;
    private List<Contact> findMatchingContactsDetails;
    private List<CampaignRecord> records;
    private CustomField[] customFields;
    private List<User> user;

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Boolean isHasMore() {
        //  DO NOT REMOVE OR CHANGE THIS IF TEST
        if(records != null) {
            return null;
        }
        return StringUtils.isNotBlank(getNext());
    }

    public List<AgentStats> getAgentStatistics() {
        return agentStatistics;
    }

    public void setAgentStatistics(List<AgentStats> agentStatistics) {
        this.agentStatistics = agentStatistics;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<AgentStatus> getAgentDetails() {
        return agentDetails;
    }

    public void setAgentDetails(List<AgentStatus> agentDetails) {
        this.agentDetails = agentDetails;
    }

    public List<ServiceStats> getStats() {
        return stats;
    }

    public void setStats(List<ServiceStats> stats) {
        this.stats = stats;
    }

    public List<Skill> getService() {
        return service;
    }

    public void setService(List<Skill> service) {
        this.service = service;
    }

    public List<TermCode> getCategory() {
        return category;
    }

    public void setCategory(List<TermCode> category) {
        this.category = category;
    }

    public List<TermCode> getTermCode() {
        return termCode;
    }

    public void setTermCode(List<TermCode> termCode) {
        this.termCode = termCode;
    }

    public List<LvResult> getLvResult() {
        return lvResult;
    }

    public void setLvResult(List<LvResult> lvResult) {
        this.lvResult = lvResult;
    }

    public List<CallCenter> getCallCenter() {
        return callCenter;
    }

    public void setCallCenter(List<CallCenter> callCenter) {
        this.callCenter = callCenter;
    }

    public List<AccountTransaction> getTransaction() {
        return transaction;
    }

    public void setTransaction(List<AccountTransaction> transaction) {
        this.transaction = transaction;
    }

    public List<ScreenPop> getScreenPop() {
        return screenPop;
    }

    public void setScreenPop(List<ScreenPop> screenPop) {
        this.screenPop = screenPop;
    }

    public List<Campaign> getCampaign() {
        return campaign;
    }

    public void setCampaign(List<Campaign> campaign) {
        this.campaign = campaign;
    }

    public List<ManualCall> getNonAttemptedManualCalls() {
        return nonAttemptedManualCalls;
    }

    public void setNonAttemptedManualCalls(List<ManualCall> nonAttemptedManualCalls) {
        this.nonAttemptedManualCalls = nonAttemptedManualCalls;
    }

    public List<CallRecording> getCallRecording() {
        return callRecording;
    }

    public void setCallRecording(List<CallRecording> callRecording) {
        this.callRecording = callRecording;
    }

    public List<CallDetail> getCall() {
        return call;
    }

    public void setCall(List<CallDetail> call) {
        this.call = call;
    }

    public List<AgentAccount> getResults() {
        return results;
    }

    public void setResults(List<AgentAccount> results) {
        this.results = results;
    }

    public List<Agent> getAgent() {
        return agent;
    }

    public void setAgent(List<Agent> agent) {
        this.agent = agent;
    }

    public List<CallHistory> getCallHistory() {
        return callHistory;
    }

    public void setCallHistory(List<CallHistory> callHistory) {
        this.callHistory = callHistory;
    }

    public List<StateChange> getStateChange() {
        return stateChange;
    }

    public void setStateChange(List<StateChange> stateChange) {
        this.stateChange = stateChange;
    }

    public List<ServiceMetrics> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<ServiceMetrics> metrics) {
        this.metrics = metrics;
    }

    public List<DialingProfile> getDialingProfile() {
        return dialingProfile;
    }

    public void setDialingProfile(List<DialingProfile> dialingProfile) {
        this.dialingProfile = dialingProfile;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<AgentEvent> getAgentEvent() {
        return agentEvent;
    }

    public void setAgentEvent(List<AgentEvent> agentEvent) {
        this.agentEvent = agentEvent;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<IdType> getIdType() {
        return idType;
    }

    public void setIdType(List<IdType> idType) {
        this.idType = idType;
    }

    public List<AgentProductivity> getAgentProductivity() {
        return agentProductivity;
    }

    public void setAgentProductivity(List<AgentProductivity> agentProductivity) {
        this.agentProductivity = agentProductivity;
    }

    public List<Contact> getFindMatchingContactsDetails() {
        return findMatchingContactsDetails;
    }

    public void setFindMatchingContactsDetails(List<Contact> findMatchingContactsDetails) {
        this.findMatchingContactsDetails = findMatchingContactsDetails;
    }

    public void setListContactDetails(List<Contact> contactDetails) {
        this.findMatchingContactsDetails = contactDetails;
    }

    public List<Contact> getListContactDetails() {
        return this.findMatchingContactsDetails;
    }

    public List<CampaignRecord> getRecords() {
        return records;
    }

    public void setRecords(List<CampaignRecord> records) {
        this.records = records;
    }

    public CustomField[] getCustomFields() {
        return customFields;
    }

    public void setCustomFields(CustomField[] customFields) {
        this.customFields = customFields;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

}
