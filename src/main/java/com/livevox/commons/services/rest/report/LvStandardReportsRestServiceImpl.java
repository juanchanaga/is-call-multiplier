/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.services.rest.report;

import com.livevox.commons.domain.CallRecordingRequest;
import com.livevox.commons.services.config.AtgConfigService;
import com.livevox.commons.services.rest.domain.*;
import com.livevox.commons.services.rest.session.LvSessionRestServiceImpl;
import com.livevox.integration.commons.domain.lvapi.AgentAccount;
import com.livevox.integration.commons.domain.lvapi.CallRecording;
import com.livevox.integration.commons.domain.stats.IdType;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LvStandardReportsRestServiceImpl extends LvSessionRestServiceImpl
        implements LvStandardReportsRestService, Serializable {

    private static final long serialVersionUID = 956103461012675L;

    public static final Integer MAX_SERVVICES_ALLOWED = 130;

    public static final Integer DEFAULT_MAX_RETURNED_ELEMENTS = 1000;

    private static final String STD_REPORT_URI_PREFIX = "/reporting/";

    private static final String CALL_RECORDING_URI =  "/standard/callRecording";

    private static final String AGENT_LOOKUP_URI =  "/standard/account/lookup";


    private Integer maxAllowedReturnedElements;

    @Autowired
    public LvStandardReportsRestServiceImpl(AtgConfigService atgConfigService) {
        super(atgConfigService);
        maxAllowedReturnedElements = DEFAULT_MAX_RETURNED_ELEMENTS;
    }

    @Override
    public List<CallRecording> getRecordings(final CallRecordingRequest callRecordingReq) throws WebServiceException {
        return getRecordings(callRecordingReq, null);
    }

    @Override
    public List<AgentAccount> getAgentAccounts(final AgentRequest agentReq) throws WebServiceException {
        return getAgentAccounts(agentReq, null);
    }

    @Override
    public boolean deleteRecording(final CallRecordingRequest callRecordingReq, final String id)
            throws WebServiceException {
        return deleteRecording(callRecordingReq, id, null);
    }


    @Override
    public List<CallRecording> getRecordings(final CallRecordingRequest callRecordingReq,
                                             String apiVersionOverride) throws WebServiceException {
        log.info(" Start getRecordings() ");
        if (callRecordingReq == null || callRecordingReq.getClientId() == null
                || callRecordingReq.getStartDate() == null || callRecordingReq.getEndDate() == null
                || callRecordingReq.getSortBy() == null) {
            throw new WebServiceException(" Required value was null ");
        }
        try {
            RecordingReportRequest req = new RecordingReportRequest();
            req.setFilter(new ReportFilter());
            if (callRecordingReq.getCallCenterIds() != null) {
                for (Long callcenter : callRecordingReq.getCallCenterIds()) {
                    req.addCallCenterId(callcenter);
                }
            }
            if (callRecordingReq.getServiceIds() != null) {
                for (Long service : callRecordingReq.getServiceIds()) {
                    req.addServiceId(service);
                }
            }
            if (callRecordingReq.getTermCodes() != null) {
                for (Long termId : callRecordingReq.getTermCodes()) {
                    req.addTermCodeId(termId);
                }
            }
            if(  !CollectionUtils.isEmpty(callRecordingReq.getAgentIds()) ) {
                req.getFilter().setAgent(callRecordingReq.getAgentIds().get(0).intValue());
            }
            req.setEndDate(callRecordingReq.getEndDate());
            req.setStartDate(callRecordingReq.getStartDate());
            req.setSortBy(callRecordingReq.getSortBy());
            req.getFilter().setAccount(callRecordingReq.getAccount());
            req.getFilter().setOriginalAccountNumber(callRecordingReq.getOriginalAccountNumber());
            req.getFilter().setPhoneDialed(callRecordingReq.getPhone());
            if(callRecordingReq.getDurationMax() != null || callRecordingReq.getDurationMin() != null) {
                req.getFilter().setTransferDuration( new GenericRange(callRecordingReq.getDurationMin(), callRecordingReq.getDurationMax()) );
            }
            if(callRecordingReq.getCampaignId() != null) {
                req.getFilter().setCampaign( new IdType(callRecordingReq.getCampaignId()) );
            }
            log.debug("End getRecordings() ");
            return getCallRecordingsSafely(req, callRecordingReq, apiVersionOverride);
        } catch (WebServiceException wsExp) {
            throw new WebServiceException("Could not recover from auth failure." + wsExp, wsExp);
        } catch (Exception e) {
            log.error("List call recordings failed. ", e);
            throw new WebServiceException("List call recordings  failed. ", e);
        }
    }

    @Override
    public List<AgentAccount> getAgentAccounts(final AgentRequest agentReq,
                                               String apiVersionOverride) throws WebServiceException {
        log.info(" Start getAgentAccounts() ");
        if (agentReq == null || agentReq.getStartDate() == null || agentReq.getEndDate() == null
                || ( agentReq.getAccount() == null && agentReq.getOriginalAccountNumber()== null)  ) {
            throw new WebServiceException(" Required value was null ");
        }
        List<AgentAccount> allAccounts = null;
        try {
            //
            //  Even though it is the same object create a new request object so that only the
            //  fields used in this call are passed so the call will not fail if other
            //  fields are present
            //
            AgentRequest callReqObj = agentReq.clone();
            callReqObj.setClientId( null);
            callReqObj.setApplication(null);
            callReqObj.setLastName(null);
            callReqObj.setLoginId(null);
            callReqObj.setPhoneNumber(null);
            callReqObj.setApiSessionId(null);
            callReqObj.setCount(null);
            callReqObj.setOffset(null);

            allAccounts = getAgentAccountsSafely(callReqObj, agentReq, apiVersionOverride);
        } catch (WebServiceException wsExp) {
            throw new WebServiceException("Could not recover from auth failure." + wsExp, wsExp);
            //
            //  For some reason when the the 3.4 version of the API can not find an account it
            //  will randomly return a 400 Http status, and a response body that contains a success
            //  response code but the message "1. Invalid Account".   This needs to be treated
            //  as a simple acocunt not found and return null.
            //
        } catch (HttpClientErrorException e) {
            if( e.getStatusCode().equals(HttpStatus.BAD_REQUEST) &&
                    e.getResponseBodyAsString() != null &&
                    e.getResponseBodyAsString().indexOf("1. Invalid Account")  > -1 )  {
                return null;
            }
        } catch (Exception e) {
            log.error("List agent accounts report failed. ", e);
            throw new WebServiceException("List agent accounts report failed. ", e);
        }

        log.debug("End getAgentAccounts() ");
        return allAccounts;
    }

    @Override
    public boolean deleteRecording(final CallRecordingRequest callRecordingReq, final String id,
                                   final String apiVersionOverride) throws WebServiceException {
        log.info(" Start deleteRecording() ");
        if (callRecordingReq.getClientId() == null || id == null) {
            throw new WebServiceException(" Required value was null ");
        }
        try {
            makeLvCall(getURI(CALL_RECORDING_URI, id, apiVersionOverride, callRecordingReq.getClientId()), HttpMethod.DELETE, null, String.class,
                    callRecordingReq.getClientId(), callRecordingReq.getApiSessionId());
        } catch (WebServiceException wsExp) {
            throw new WebServiceException("Could not recover from auth failure." + wsExp, wsExp);
        } catch (Exception e) {
            log.error("Delete recording failed. ", e);
            throw new WebServiceException("Delete recording failed. ");
        }
        log.debug("End deleteRecording() ");
        return true;
    }


    /**
     *   HAVE A DRINK BEFORE READING THIS ONE.
     *
     *
     *   The API only allows you to search for up to 150 services at a time.
     *   If the request has more than 150 then it has to be broken up in to
     *   multiple calls.
     *
     * @return
     */
    private List<CallRecording> getCallRecordingsSafely(
            final RecordingReportRequest reportRequest,
            final CallRecordingRequest origRecordingReq, String apiVersionOverride) {
        List<CallRecording> allRecordings = new ArrayList<>();
        if(reportRequest == null) {
            return allRecordings;
        }
        if(reportRequest.getFilter() != null && reportRequest.getFilter().getService() != null &&
                reportRequest.getFilter().getService().size() > MAX_SERVVICES_ALLOWED) {
            //
            // More than the allowed 150 so break up the calls in to sets of 150
            // services each.
            //
            int offset = 0;
            List<IdType> origSrvcList = reportRequest.getFilter().getService().subList(0,
                    reportRequest.getFilter().getService().size());
            while( (origSrvcList.size() - offset) > 0) {
                int endIdx = offset + MAX_SERVVICES_ALLOWED;
                reportRequest.getFilter().setService( origSrvcList.subList( offset,
                        (endIdx < origSrvcList.size())  ? endIdx : origSrvcList.size() ));
                ListRestResponse resp = makeLvCall(getURI(STD_REPORT_URI_PREFIX,CALL_RECORDING_URI, origRecordingReq.getClientId()),
                        HttpMethod.POST, reportRequest, ListRestResponse.class, origRecordingReq.getClientId(),
                        origRecordingReq.getApiSessionId());
                offset = endIdx;
                if( resp != null && resp.getCallRecording() != null ) {
                    allRecordings.addAll(resp.getCallRecording());
                }
                //
                //  This call only returns 1000 records, so we need to stop when we get to that number.
                //
                if(  allRecordings.size() >= maxAllowedReturnedElements ) {
                    return allRecordings.subList(0, maxAllowedReturnedElements);
                }
            }
            return allRecordings;
        } else {
            //
            //  Does NOT have more services than allowed so just do the regular call.
            //
            ListRestResponse resp = makeLvCall(getURI(STD_REPORT_URI_PREFIX, CALL_RECORDING_URI, apiVersionOverride,  origRecordingReq.getClientId()),
                    HttpMethod.POST, reportRequest, ListRestResponse.class, origRecordingReq.getClientId(),
                    origRecordingReq.getApiSessionId());
            if( resp == null || resp.getCallRecording() == null ) {
                return allRecordings;
            } else {
                return resp.getCallRecording();
            }
        }
    }

    /**
     *   HAVE A DRINK BEFORE READING THIS ONE.
     *
     *
     *   The API only allows you to search for up to 150 services at a time.
     *   If the request has more than 150 then it has to be broken up in to
     *   multiple calls.
     *
     * @return List<AgentAccount>
     */
    private List<AgentAccount> getAgentAccountsSafely(final AgentRequest clonedSafeReq,
                                                      final AgentRequest origAgentReq, String apiVersionOverride) {
        List<AgentAccount> allAccounts = new ArrayList<AgentAccount>();
        if(clonedSafeReq == null) {
            return allAccounts;
        }
        if(clonedSafeReq.getFilter() != null && clonedSafeReq.getFilter().getService() != null &&
                clonedSafeReq.getFilter().getService().size() > MAX_SERVVICES_ALLOWED) {
            //
            // More than the allowed 150 so break up the calls in to sets of 150
            // services each.
            //
            int offset = 0;
            List<IdType> origSrvcList = clonedSafeReq.getFilter().getService().subList(0,
                    clonedSafeReq.getFilter().getService().size());
            while( (origSrvcList.size() - offset) > 0) {
                int endIdx = offset + MAX_SERVVICES_ALLOWED;
                clonedSafeReq.getFilter().setService( origSrvcList.subList( offset,
                        (endIdx < origSrvcList.size())  ? endIdx : origSrvcList.size() ));
                ListRestResponse resp = makeLvCall(getURI(STD_REPORT_URI_PREFIX, AGENT_LOOKUP_URI, origAgentReq.getClientId()),
                        HttpMethod.POST, clonedSafeReq,	ListRestResponse.class, origAgentReq.getClientId(), origAgentReq.getApiSessionId());
                offset = endIdx;
                if( resp != null && resp.getResults() != null ) {
                    allAccounts.addAll(resp.getResults());
                }
                //
                //  This call only returns 1000 records, so we need to stop when we get to that number.
                //
                if( allAccounts.size() >= maxAllowedReturnedElements) {
                    return allAccounts.subList(0, maxAllowedReturnedElements);
                }
            }
            return allAccounts;
        } else {
            //
            //  Does NOT have more services than allowed so just do the regular call.
            //
            ListRestResponse resp = makeLvCall(getURI(STD_REPORT_URI_PREFIX, AGENT_LOOKUP_URI, apiVersionOverride, clonedSafeReq.getClientId()),
                    HttpMethod.POST, clonedSafeReq, ListRestResponse.class, origAgentReq.getClientId(),
                    origAgentReq.getApiSessionId());
            if( resp == null || resp.getResults() == null ) {
                return allAccounts;
            } else {
                return resp.getResults();
            }
        }

    }
}
