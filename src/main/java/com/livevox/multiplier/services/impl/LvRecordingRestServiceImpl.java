/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.services.impl;

import com.livevox.multiplier.domain.CallRecordingRequest;
import com.livevox.multiplier.domain.requests.FindCallRecordingRequest;
import com.livevox.multiplier.domain.requests.RestFilter;
import com.livevox.multiplier.domain.lvapi.CallRecording;
import com.livevox.multiplier.domain.lvapi.ListRestResponse;
import com.livevox.integration.commons.domain.stats.DateRange;
import com.livevox.multiplier.domain.stats.NumberRange;
import com.livevox.multiplier.services.config.AtgConfigService;
import com.livevox.multiplier.services.LvRecordingRestService;
import com.livevox.multiplier.services.sigin.SignInService;
import com.livevox.multiplier.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

@Service
public class LvRecordingRestServiceImpl extends LvSessionRestServiceImpl
        implements LvRecordingRestService, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8870956103461012675L;

    private static final String API_URI_PREFIX = "/compliance/";

    private static final String RECORDING_URI = "/recording/";

    private static final String INFO_URI = RECORDING_URI + "info";

    @Autowired(required = false)
    public LvRecordingRestServiceImpl(AtgConfigService atgConfigService, SignInService signInService) {
        super(atgConfigService, signInService);
    }

    @Override
    public List<CallRecording> getRecordings(CallRecordingRequest callRecordingReq)
            throws WebServiceException {
        return this.getRecordings(callRecordingReq, false);
    }


    @Override
    public List<CallRecording> getAllRecordings(CallRecordingRequest callRecordingReq)
            throws WebServiceException {
        if (null == callRecordingReq.getCount())
            callRecordingReq.setCount(1000);
        if (null == callRecordingReq.getOffset())
            callRecordingReq.setOffset(0);
        return this.getRecordings(callRecordingReq, true);
    }


    @Deprecated
    public List<CallRecording> getRecordings(CallRecordingRequest callRecordingReq,
                                             Integer clientId, Integer count, Integer offset, boolean returnAll)
            throws WebServiceException {
        if(callRecordingReq == null ) {
            return null;
        }
        if( callRecordingReq.getClientId() == null ) {
            callRecordingReq.setClientId(clientId);
        }
        if( callRecordingReq.getCount() == null ) {
            callRecordingReq.setCount(count);
        }
        if( callRecordingReq.getOffset() == null ) {
            callRecordingReq.setOffset(offset);
        }
        return getRecordings(callRecordingReq, returnAll);
    }


    public List<CallRecording> getRecordings(CallRecordingRequest callRecordingReq,
                                             boolean returnAll) throws WebServiceException {
        log.debug("Start getAllRecordings() ");
        if (callRecordingReq == null || callRecordingReq.getClientId() == null ||
                callRecordingReq.getStartDate() == null || callRecordingReq.getEndDate() == null ||
                callRecordingReq.getCount() == null || callRecordingReq.getOffset() == null) {
            throw new WebServiceException(" Required value was null ");
        }
        ListRestResponse resp = null;
        try {
            FindCallRecordingRequest request = new FindCallRecordingRequest();
            if (callRecordingReq.getCallCenterIds() != null) {
                RestFilter ccFilter = new RestFilter();
                for (Long callcenter : callRecordingReq.getCallCenterIds()) {
                    ccFilter.addCallCenter(callcenter);
                }
                request.setCallCenter(ccFilter);
            }
            if (callRecordingReq.getServiceIds() != null) {
                RestFilter skillFilter = new RestFilter();
                for (Long service : callRecordingReq.getServiceIds()) {
                    skillFilter.addService(service);
                }
                request.setService(skillFilter);
            }

            if (callRecordingReq.getAgentIds() != null) {
                RestFilter agentFilter = new RestFilter();
                for (Long agent : callRecordingReq.getAgentIds()) {
                    agentFilter.addAgent(agent);
                }
                request.setAgent(agentFilter);
            }

            if (callRecordingReq.getCampaignId() != null) {
                RestFilter campFilter = new RestFilter();
                campFilter.addCampaign(callRecordingReq.getCampaignId());
                request.setCampaign(campFilter);
            }

            if (callRecordingReq.getTermCodes() != null) {
                RestFilter termCodeFilter = new RestFilter();
                for (Long termCode : callRecordingReq.getTermCodes()) {
                    if(termCode != null) {
                        termCodeFilter.addResult(termCode.intValue());
                    }
                }
                request.setResults(termCodeFilter);
            }

            if (callRecordingReq.getDurationMax() != null
                    && callRecordingReq.getDurationMin() != null) {
                NumberRange range = new NumberRange();
                range.setTo(callRecordingReq.getDurationMax());
                range.setFrom(callRecordingReq.getDurationMin());
                request.setDurationRange(range);
            }

            if (null != callRecordingReq.getOriginalAccountNumber()) {
                request.setOriginalAccountNumber(callRecordingReq.getOriginalAccountNumber());
            }

            DateRange dateRange = new DateRange();
            dateRange.setTo(callRecordingReq.getEndDate());
            dateRange.setFrom(callRecordingReq.getStartDate());
            request.setDateRange(dateRange);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString( getURI(API_URI_PREFIX, RECORDING_URI,
                            callRecordingReq.getApiVersion(),
                            callRecordingReq.getClientId() ) + "search");
            builder.queryParam("offset", callRecordingReq.getOffset());
            builder.queryParam("count", callRecordingReq.getCount());

            if (callRecordingReq.getAccount() != null) {
                builder.queryParam("account", callRecordingReq.getAccount());
            }
            if (callRecordingReq.getPhone() != null) {
                builder.queryParam("phone", callRecordingReq.getPhone());
            }
            resp = makeLvCall(URLDecoder.decode(builder.toUriString(), "UTF-8"), HttpMethod.POST, request,
                    ListRestResponse.class, callRecordingReq.getClientId(), callRecordingReq.getApiSessionId());
        } catch (WebServiceException wsExp) {
            throw new WebServiceException("Could not recover from auth failure." + wsExp, wsExp);
        } catch (Exception e) {
            log.error("List call recordings failed. ", e);
            throw new WebServiceException("List call recordings  failed. ");
        }

        if (resp == null) {
            log.error("Unable to retrieve call recordings . The response was invalid. ");
            throw new WebServiceException(
                    "List call recordings  failed. The response was invalid ");
        }

        if (returnAll && resp.isHasMore()) {

            // The new call recording request should be updated with the new offset and count prior to the recursive call
            // This is causing an infernal loop bug without it by starting at the same offset for every call.
            // (eg. next="compliance/v3.3/recording/search?count=5&offset=5")
            String next = resp.getNext();
            log.debug("List call recordings next value["+next+']');
            Map<String, List<String>> params = CommonUtils.getQueryParams(next);
            if(  !CollectionUtils.isEmpty(params.keySet()) ) {

                Integer count = null;
                List<String> countList = params.get("count");
                if( !CollectionUtils.isEmpty(countList) ) {
                    count = Integer.parseInt(countList.get(0));
                }

                Integer offset = null;
                List<String> offsetList = params.get("offset");
                if( !CollectionUtils.isEmpty(offsetList) ) {
                    offset = Integer.parseInt(offsetList.get(0));
                }

                if (null != count && null != offset) {
                    callRecordingReq.setCount(count);
                    callRecordingReq.setOffset(offset);

                    List<CallRecording> more = getRecordings(callRecordingReq, returnAll);
                    if (more != null) {
                        resp.getCallRecording().addAll(more);
                    }
                }
                else {
                    log.warn("unable to locate next offset and count from response, next["+next+"], found count["+count+"] and offset["+offset+']');
                }

            }
            else {
                log.warn("invalid next value from getRecordings, aborting.  next["+next+']');
            }
        }

        log.debug("End getAllRecordings() ");
        return resp.getCallRecording();
    }


    @Override
    public CallRecording getRecordingInfo(String id, Integer clientId) throws WebServiceException {
        return getRecordingInfo(id, clientId, null);
    }


    @Override
    public CallRecording getRecordingInfo(String id, Integer clientId, String apiSessionId) throws WebServiceException {
        return getRecordingInfo(id, clientId, apiSessionId, null);
    }


    @Override
    public CallRecording getRecordingInfo(String id, Integer clientId, String apiSessionId, String apiVersion) throws WebServiceException {
        log.debug("Start getRecordingInfo() ");
        if (clientId == null || id == null) {
            throw new WebServiceException(" Required value was null ");
        }
        CallRecording resp;
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(
                    getURI(API_URI_PREFIX, INFO_URI, apiVersion, clientId ) );
            builder.queryParam("recording", id);
            resp = makeLvCall(builder.toUriString(), HttpMethod.GET, null, CallRecording.class, clientId, apiSessionId);
        } catch (WebServiceException wsExp) {
            throw new WebServiceException("Could not recover from auth failure." + wsExp, wsExp);
        } catch (Exception e) {
            log.error("Read recording failed. ", e);
            throw new WebServiceException("Read recording failed. ");
        }
        if (resp == null) {
            log.error("Unable to read recording. The response was invalid. ");
            throw new WebServiceException("Read account recording. The response was invalid ");
        }
        log.debug("End getRecordingInfo() ");
        return resp;
    }


    @Override
    public boolean deleteRecording(String id, Integer clientId) throws WebServiceException {
        return  deleteRecording( id, clientId, null);
    }


    @Override
    public boolean deleteRecording(String id, Integer clientId, String apiSessionId)
            throws WebServiceException {
        return deleteRecording(id, clientId, apiSessionId, null);
    }


    @Override
    public boolean deleteRecording(String id, Integer clientId, String apiSessionId, String apiVersion)
            throws WebServiceException {
        log.debug("Start deleteRecording() ");
        if (clientId == null || id == null) {
            throw new WebServiceException(" Required value was null ");
        }
        try {
            makeLvCall(getURI(API_URI_PREFIX, RECORDING_URI, apiVersion, clientId) + id, HttpMethod.DELETE, null, String.class,
                    clientId, apiSessionId);
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
     * Seems that returning this as a byte array is the most versatile, as that
     * will allow it to be saved as a file or send back over a rest API
     */
    @Override
    public byte[] getRecording(String id, Integer clientId) throws WebServiceException {
        return  getRecording( id, clientId, null);
    }

    @Override
    public byte[] getRecording(String id, Integer clientId, String apiSessionId)
            throws WebServiceException {
        return getRecording(id, clientId, apiSessionId, null);
    }


    /**
     * Seems that returning this as a byte array is the most versatile, as that
     * will allow it to be saved as a file or send back over a rest API
     */
    @Override
    public byte[] getRecording(String id, Integer clientId, String apiSessionId, String apiVersion)
            throws WebServiceException {
        return this.getRecordingResource(id, clientId, apiSessionId, apiVersion).getByteArray();
    }

    @Override
    public ByteArrayResource getRecordingResource(String id, Integer clientId)
            throws WebServiceException {
        return this.getRecordingResource(id, clientId, null, null);
    }

    @Override
    public ByteArrayResource getRecordingResource(String id, Integer clientId,
                                                  String apiSessionId) throws WebServiceException {
        return this.getRecordingResource(id, clientId, apiSessionId, null);
    }

    @Override
    public ByteArrayResource getRecordingResource(String id, Integer clientId, String apiSessionId, String apiVersion)
            throws WebServiceException {
        log.debug("Start getRecording() ");
        if (clientId == null || id == null) {
            throw new WebServiceException(" Required value was null ");
        }
        ByteArrayResource resp;
        try {
            resp = makeLvCall( getURI(API_URI_PREFIX, RECORDING_URI, apiVersion, clientId) + id, HttpMethod.GET, null,
                    ByteArrayResource.class, MediaType.ALL, clientId, null, apiSessionId);
        } catch (WebServiceException wsExp) {
            throw new WebServiceException("Could not recover from auth failure." + wsExp, wsExp);
        } catch (Exception e) {
            log.error("Read recording failed. ", e);
            throw new WebServiceException("Read recording failed. ");
        }
        if (resp == null) {
            log.error("Unable to read recording. The response was invalid. ");
            throw new WebServiceException("Read account recording. The response was invalid ");
        }
        log.debug("End getRecording() ");
        return resp;
    }

}
