/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.services.rest.compliance;

import com.livevox.commons.domain.CallRecordingRequest;
import com.livevox.integration.commons.domain.lvapi.CallRecording;
import org.springframework.core.io.ByteArrayResource;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.List;

public interface LvRecordingRestService  extends Serializable {


    List<CallRecording> getRecordings(CallRecordingRequest callRecordingReq) throws WebServiceException;

    List<CallRecording> getAllRecordings(CallRecordingRequest callRecordingReq) throws WebServiceException;

    CallRecording getRecordingInfo(String id, Integer clientId) throws WebServiceException;

    CallRecording getRecordingInfo(String id, Integer clientId, String apiSessionId) throws WebServiceException;

    CallRecording getRecordingInfo(String id, Integer clientId, String apiSessionId, String apiVersion) throws WebServiceException;

    boolean deleteRecording(String id,  Integer clientId) throws WebServiceException;

    boolean deleteRecording(String id,  Integer clientId, String apiSessionId) throws WebServiceException;

    boolean deleteRecording(String id,  Integer clientId, String apiSessionId, String apiVersion) throws WebServiceException;

    byte[] getRecording(String id, Integer clientId) throws WebServiceException;

    byte[] getRecording(String id, Integer clientId, String apiSessionId) throws WebServiceException;

    byte[] getRecording(String id, Integer clientId, String apiSessionId, String apiVersion) throws WebServiceException;

    ByteArrayResource getRecordingResource(String id, Integer clientId, String apiSessionId, String apiVersion)
            throws WebServiceException;

    ByteArrayResource getRecordingResource(String id, Integer clientId)
            throws WebServiceException;

    ByteArrayResource getRecordingResource(String id, Integer clientId, String apiSessionId)
            throws WebServiceException;

}

