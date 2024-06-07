/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.services;

import com.livevox.multiplier.domain.CallRecordingRequest;
import com.livevox.multiplier.domain.requests.AgentRequest;
import com.livevox.multiplier.domain.lvapi.AgentAccount;
import com.livevox.multiplier.domain.lvapi.CallRecording;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.List;

public interface LvStandardReportsRestService extends Serializable {

    List<CallRecording> getRecordings(CallRecordingRequest callRecordingReq) throws WebServiceException;

    List<AgentAccount> getAgentAccounts(AgentRequest agentReq) throws WebServiceException;

    boolean deleteRecording(CallRecordingRequest callRecordingReq, String id) throws WebServiceException;

    List<CallRecording> getRecordings(CallRecordingRequest callRecordingReq, String apiVersionOverride) throws WebServiceException;

    List<AgentAccount> getAgentAccounts(AgentRequest agentReq, String apiVersionOverride) throws WebServiceException;

    boolean deleteRecording(CallRecordingRequest callRecordingReq, String id, String apiVersionOverride) throws WebServiceException;
}
