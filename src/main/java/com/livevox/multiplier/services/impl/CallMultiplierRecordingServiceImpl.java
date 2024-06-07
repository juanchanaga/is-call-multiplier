/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.services.impl;

import com.livevox.multiplier.domain.ClientInfo;
import com.livevox.multiplier.domain.Permission;
import com.livevox.multiplier.domain.Share;
import com.livevox.multiplier.domain.exceptions.InvalidParametersException;
import com.livevox.multiplier.domain.exceptions.MissingFieldsException;
import com.livevox.multiplier.domain.exceptions.UnauthorizedException;
import com.livevox.multiplier.services.LvRecordingRestService;
import com.livevox.multiplier.domain.lvapi.CallRecording;
import com.livevox.multiplier.utils.ShareUtil;
import com.livevox.multiplier.domain.JobRequest;
import com.livevox.multiplier.domain.RecordingRequest;
import com.livevox.multiplier.jobs.CallRecordingInfoJob;
import com.livevox.multiplier.jobs.CallRecordingLookupJob;
import com.livevox.multiplier.services.CallMultiplierServiceAbstract;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

@Service
@Slf4j
public class CallMultiplierRecordingServiceImpl extends CallMultiplierServiceAbstract {


    /**
     * Instantiates a new Call multiplier recording service.
     */
    public CallMultiplierRecordingServiceImpl() {
        super();
    }


    /**
     * Instantiates a new Call multiplier recording service.
     *
     * @param threadPoolSize the thread pool size
     */
    public CallMultiplierRecordingServiceImpl(Integer threadPoolSize) {
        this();
        executionService = Executors.newFixedThreadPool(threadPoolSize);
    }


    public List<CallRecording> getCallRecordings(final RecordingRequest req,
                                                 final String token) throws UnauthorizedException, MissingFieldsException,
            InvalidParametersException, Exception {
        log.debug("API Session ID: " + req.getApiSessionId());
        populateRequestDefaults(req);
        validateCallRecordingRequest(req, token);

        JobRequest jobReq = new JobRequest(req);
        jobReq.setConfigService(configService);
        jobReq.setClientId( isAuthenticatedAs(token).getBody().intValue() );

        List<CallRecordingLookupJob> callRecordingJobList = getConcurrentJobList(
                req.getClientId(), jobReq, CallRecordingLookupJob.class, true, Permission.PermissionType.READ).getBody();

        List<CallRecording> completeRecordingList = new ArrayList<CallRecording>();
        try {
            List<Future<List<CallRecording>>> respList = executionService.invokeAll(
                    callRecordingJobList, 300, TimeUnit.SECONDS);
            if(respList == null) {
                return completeRecordingList;
            }

            for(Future<List<CallRecording>> f : respList) {
                List<CallRecording> newRecordingList = f.get();
                if(newRecordingList != null) {
                    for(CallRecording callRecrd : newRecordingList) {
                        completeRecordingList.add(callRecrd);
                    }
                }
            }
            return completeRecordingList;
        } catch(ExecutionException e) {
            if(e.getCause() != null && e.getCause() instanceof InvalidParametersException) {
                throw (InvalidParametersException) e.getCause();
            }
            if(e.getCause() != null && e.getCause() instanceof MissingFieldsException) {
                throw (MissingFieldsException) e.getCause();
            }
            throw e;
        } catch(InvalidParametersException e) {
            throw e;
        } catch(CancellationException ce) {
            log.error("Getting a list of call recordings concurrently timed out. ", ce);
            throw ce;
        }catch(Exception e) {
            log.error("Getting a list of call recordings concurrently failed. ", e);
            throw e;
        }
    }

    public CallRecording getCallRecordingInfo(final String id, final String token) throws
            Exception {
        JobRequest jobReq = new JobRequest();
        jobReq.setConfigService(configService);
        jobReq.setClientId( isAuthenticatedAs(token).getBody().intValue() );
        jobReq.setRecordingId(id);
        jobReq.setSessoinId(token);

        List<CallRecordingInfoJob> callRecordingJobList = getConcurrentJobList(
                null, jobReq, CallRecordingInfoJob.class, false, Permission.PermissionType.READ).getBody();

        try {
            List<Future<CallRecording>> recInfoJobs = executionService.invokeAll(
                    callRecordingJobList, 300, TimeUnit.SECONDS);
            if(recInfoJobs == null) {
                return null;
            }
            for(Future<CallRecording> f : recInfoJobs) {
                try {
                    CallRecording newRecording = f.get();
                    //
                    //  This Livevox API call on failure returns a success response
                    //  with an empty body resulting in an empty object being returned.
                    //  So we need to skip all the empty ones and return the one valid
                    //  response.
                    //
                    if (newRecording != null && newRecording.getClientId() != null) {
                        return newRecording;
                    }
                } catch(Exception e) {
                    //
                    //  Since there is no way to know what client owns the call
                    //  recording all but one of the jobs will fail.   Those failures
                    //  should not be passed up stream.   We only need to pass the
                    //  one sucessful response.
                    //
                    if( !(e.getCause() != null && e.getCause() instanceof UnauthorizedException)  ) {
                        throw e;
                    }
                }
            }
            return null;
        } catch(CancellationException ce) {
            log.error("Getting call recording info concurrently timed out. ", ce);
            throw ce;
        }catch(Exception e) {
            log.error("Getting call recording info concurrently failed. ", e);
            throw e;
        }
    }

    public byte[] getCallRecordingFile(final String callId, final String objectid,
                                       final Integer ownerClientId, final String appName, final String token)
            throws UnauthorizedException, MissingFieldsException {
        Integer userClientId = isAuthenticatedAs(token).getBody().intValue();
        if( userClientId == null|| ownerClientId == null || token == null) {
            throw new MissingFieldsException();
        }
        String recordingId = selectIdToUse(userClientId, objectid, callId);
        if( ownerClientId.equals(userClientId) ) {
            try {
                return getRecordingService(userClientId).getRecording(
                        recordingId, userClientId, token);
            } catch(Exception e) {
                log.error("Getting a call recording failed for recordingId: "+recordingId, e);
                throw e;
            }
        }
        List<Share> sharedClientAccounts = null;
        try {
            sharedClientAccounts = sharedDao.getSharesForClient(userClientId);
        }catch(Exception e) {
            log.error("Looking up clients shared data for the call recording lookup failed. ", e);
        }
        if(sharedClientAccounts == null || sharedClientAccounts.isEmpty() ) {
            log.warn("No configured shares were found for clientId: "+userClientId+
                    "  which was being looked up for call recordingId: "+recordingId);
            throw new DataIntegrityViolationException("No shares configured.");
        }
        try {
            if( !ShareUtil.canRead(sharedClientAccounts, ownerClientId, appName) ) {
                log.error(" Denied access to call recording file for clinetId:  "+ownerClientId+" recordingId: "+recordingId);
                throw new UnauthorizedException("This account does not have READ access");
            }

            return getRecordingService(ownerClientId).getRecording(recordingId,
                    ownerClientId, null);
        }catch(Exception e) {
            log.error("Getting a call recording failed for recordingId: "+recordingId, e);
            throw e;
        }
    }
    private String selectIdToUse(Integer clntId, String objectId, String callId) {
        if(  objectId == null && StringUtils.isEmpty(callId) ) {
            log.error("No lookup Id provided for redording lookup.  client: {} ", clntId);
            throw new MissingFieldsException() ;
        } else if(  objectId == null && StringUtils.isNotEmpty(callId) ) {
            return callId;
        } else if(  objectId != null && StringUtils.isEmpty(callId) ) {
            return String.valueOf(objectId);
        }

        if( isBeforeObjectIdSwitchOver(clntId)  ) {
            return callId;
        } else {
            return objectId;
        }
    }
    private boolean isBeforeObjectIdSwitchOver(Integer clntId) {
        Optional<ClientInfo> clnt = configService.getClient(clntId);
        if(  !clnt.isPresent()  || StringUtils.isEmpty( clnt.get().getApiVersion() )  ) {
            log.error("No client configured.  ClientId {}  ", clntId );
            throw new IllegalStateException("No client configured.  ClientId "+clntId) ;
        }

        return ( "5.0".equals(clnt.get().getApiVersion()))  || ( "6.0".equals(clnt.get().getApiVersion()))  ||
                ( "7.0".equals(clnt.get().getApiVersion()))  || ( "8.0".equals(clnt.get().getApiVersion()))  ;
    }

    private LvRecordingRestService getRecordingService(Integer clientId) {
        Optional<ClientInfo> conf =  configService.getClient(clientId);
        LvRecordingRestServiceImpl srvc =
                new LvRecordingRestServiceImpl(configService, signInService);
        if(conf != null && conf.isPresent() ) {
            if(StringUtils.isNotEmpty(conf.get().getApiHost()) ) {
                srvc.setHostName(conf.get().getApiHost());
            }
            if(StringUtils.isNotEmpty(conf.get().getApiVersion()) ) {
                srvc.setVersion(conf.get().getApiVersion());
            }
            if(StringUtils.isNotEmpty(conf.get().getApiToken()) ) {
                srvc.setLvAuthToken(conf.get().getApiToken());
            }
        }
        return srvc;
    }




    private void validateCallRecordingRequest(final RecordingRequest req, final String token) {
        if(req == null || StringUtils.isEmpty(token) || req.getEndDate() == null ||
                req.getStartDate() == null || req.getSortBy() == null) {
            throw new MissingFieldsException();
        }
    }
}
