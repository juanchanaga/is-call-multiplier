/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.commons.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.livevox.commons.domain.User;
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

    public Boolean isHasMore() {
        //  DO NOT REMOVE OR CHANGE THIS IF TEST
        if(records != null) {
            return null;
        }
        return StringUtils.isNotBlank(getNext());
    }
}
