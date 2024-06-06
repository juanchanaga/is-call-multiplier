/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.shareddata.multiplier.service;

import com.livevox.commons.domain.Access;
import com.livevox.commons.domain.Client;
import com.livevox.commons.domain.Permission;
import com.livevox.commons.domain.Share;
import com.livevox.commons.exceptions.MissingFieldsException;
import com.livevox.commons.exceptions.UnauthorizedException;
import com.livevox.commons.services.rest.domain.AgentRequest;
import com.livevox.integration.commons.domain.lvapi.Agent;
import com.livevox.integration.commons.domain.lvapi.AgentAccount;
import com.livevox.integration.commons.utils.ShareUtil;
import com.livevox.integration.shareddata.multiplier.domain.*;
import com.livevox.integration.shareddata.multiplier.jobs.AgentAccountLookupJob;
import com.livevox.integration.shareddata.multiplier.jobs.AgentLookupJob;
import com.livevox.integration.shareddata.multiplier.jobs.AsessorCallDataJob;
import com.livevox.integration.shareddata.multiplier.jobs.CampaignLookupJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * The type Call multiplier service.
 *
 * @autor Luis Felipe Sosa Alvarez lsosa@livevox.com
 */
@Service
@Slf4j
public class CallMultiplierServiceImpl extends CallMultiplierServiceAbstract{


    /**
     * Instantiates a new Call multiplier service.
     */
    public CallMultiplierServiceImpl() {
        super();
    }


    /**
     * Instantiates a new Call multiplier service.
     *
     * @param threadPoolSize the thread pool size
     */
    public CallMultiplierServiceImpl(Integer threadPoolSize) {
        this();
        executionService = Executors.newFixedThreadPool(threadPoolSize);
    }


    /**
     * Gets agents.
     *
     * @param agntReq the agnt req
     * @return the agents
     * @throws UnauthorizedException the unauthorized exception
     * @throws Exception             the exception
     */
    public List<Agent> getAgents(final AgentRequest agntReq) throws UnauthorizedException, Exception {
        List<AgentLookupJob>  agentJobs = new ArrayList<AgentLookupJob>();
        JobRequest jobReq = new JobRequest(agntReq);
        jobReq.setConfigService(configService);
        jobReq.setClientId( isAuthenticatedAs(agntReq.getApiSessionId()) );

        try {
            agentJobs = getConcurrentJobList(agntReq.getClientId(),
                    jobReq, AgentLookupJob.class, true, Permission.PermissionType.READ);
        } catch(Exception e) {
            log.error("Generating campaign lookup jobs failed. ", e);
            throw e;
        }

        List<Agent> agentList = new ArrayList<Agent>();
        try {
            List<Future<List<Agent>>> respList = executionService.invokeAll(
                    agentJobs, 300, TimeUnit.SECONDS);
            if(respList == null) {
                return agentList;
            }
            for(Future<List<Agent>> f : respList) {
                List<Agent> newCampaigns = f.get();
                agentList.addAll(newCampaigns);
            }
            return agentList;
        } catch(Exception e) {
            log.error("Getting agents lists concurrnetly failed. ", e);
            throw e;
        }
    }

    public List<CampaignResponse> getCampaigns(final RecordingRequest recordingReq)
            throws UnauthorizedException, Exception {
        List<CampaignLookupJob>  campaignJobs = new ArrayList<>();
        JobRequest jobReq = new JobRequest(recordingReq);
        jobReq.setConfigService(configService);
        jobReq.setClientId( isAuthenticatedAs(recordingReq.getApiSessionId()) );

        try {
            campaignJobs = getConcurrentJobList(jobReq.getClientId(),
                    jobReq, CampaignLookupJob.class, true,
                    Permission.PermissionType.READ);
        } catch(Exception e) {
            log.error("Generating campaign lookup jobs failed. ", e);
            throw e;
        }

        List<CampaignResponse> campaignList = new ArrayList<>();
        try {
            List<Future<List<CampaignResponse>>> respList = executionService.invokeAll(
                    campaignJobs, 300, TimeUnit.SECONDS);

            //
            //  Only add the returned data to the final list IF the user has access to
            // the call center(s) and or skill(s) for this list.
            //
            List<Client> callCntrSrvcList = getCallCentersAndSkills(recordingReq.getClientId(),
                    recordingReq.getApplication(), recordingReq.getApiSessionId());
            for(Future<List<CampaignResponse>> f : respList) {
                List<CampaignResponse> newCampaignList = f.get();
                if (newCampaignList != null) {
                    for (CampaignResponse cmpn : newCampaignList) {
                        if (cmpn != null && cmpn.getServiceId() != null &&
                                ShareUtil.hasConfigAccess(callCntrSrvcList, null, cmpn.getServiceId().intValue() )) {
                            campaignList.add(cmpn);
                        } else {
                            log.debug(" Filtering out call campaignId " +
                                    ((cmpn != null) ? cmpn.getId() : "null") + " for not matching " +
                                    " serviceId: " + ((cmpn != null) ? cmpn.getServiceId() : "null"));
                        }
                    }
                }
            }
            return campaignList;
        } catch(Exception e) {
            log.error("Looking up campaigns concurrnetly failed. ", e);
            throw e;
        }
    }

    public List<AgentAccount> getAgentAccount(final AgentRequest request) throws
            MissingFieldsException, UnauthorizedException, Exception {
        JobRequest req = new JobRequest(request);
        req.setConfigService(configService);
        req.setClientId(isAuthenticatedAs(request.getApiSessionId()));

        List<AgentAccountLookupJob> agentLookupJobs;

        try {
            agentLookupJobs = getConcurrentJobList(request.getClientId(),
                    req, AgentAccountLookupJob.class, true, Permission.PermissionType.READ);
        } catch(Exception e) {
            throw e;
        }

        List<AgentAccount> agentList = new ArrayList<>();
        try {
            List<Future<List<AgentAccount>>> respList = executionService.invokeAll(
                    agentLookupJobs, 300, TimeUnit.SECONDS);
            if(respList == null) {
                return agentList;
            }
            for(Future<List<AgentAccount>> f : respList) {
                List<AgentAccount> newAgentAccounts = f.get();
                if(newAgentAccounts != null) {
                    for(AgentAccount agntAcct : newAgentAccounts) {
                        agentList.add(agntAcct);
                    }
                }
            }
            return agentList;
        } catch(MissingFieldsException e) {
            log.error("Getting agent account lists failed.  Required fields were missing", e);
            throw e;
        }catch(Exception e) {
            log.error("Getting agent account lists concurrnetly failed. ", e);
            throw e;
        }
    }

    public List<AssessorData> getAssessorData(final AssessorRequest req) throws
            UnauthorizedException, Exception {
        Integer userClientId = isAuthenticatedAs(req.getToken());
        List<AsessorCallDataJob> asessorJobList = new ArrayList<>();
        String host = configService.getRequiredProperty(ASSESSOR_HOST_PROPERTY_NAME);

        if( req.getClientId() == null || (req.getClientId() != null  &&
                req.getClientId().equals(userClientId) )  ) {
            req.setClientId(userClientId.longValue());
            asessorJobList.add( new AsessorCallDataJob(req, host)  );
        }
        List<Share> sharedClientAccounts = null;
        try {
            sharedClientAccounts = sharedDao.getSharesForClient(userClientId);
        }catch(Exception e) {
            log.error("Looking up clients shared data for the call recording lookup failed. ", e);
        }
        if(sharedClientAccounts != null && !sharedClientAccounts.isEmpty() ) {
            for(Share s : sharedClientAccounts) {
                if( s.getAccess() != null ) {
                    for(Access acc : s.getAccess() ) {
                        if( req.getClientId() == null ||  (req.getClientId() != null &&
                                req.getClientId().equals(acc.getSharedClientId()))  ) {
                            if( ShareUtil.canRead(acc, acc.getSharedClientId(), null)  ) {
                                req.setClientId( acc.getSharedClientId().longValue() );
                                asessorJobList.add( new AsessorCallDataJob(req, host) );
                            }
                        }
                    }
                }
            }
        }
        List<AssessorData> assessorList = new ArrayList<>();
        try {
            List<Future<List<AssessorData>>> respList = executionService.invokeAll(
                    asessorJobList, 300, TimeUnit.SECONDS);
            if(respList == null) {
                return assessorList;
            }
            //
            //  Only add the returned data to the final list IF the user has access to the call center(s) and or skill(s)
            //  for this list.
            //
            List<Client> callCntrSrvcList = getCallCentersAndSkills(null,  null, req.getToken());
            for(Future<List<AssessorData>> f : respList) {
                List<AssessorData> newAssessList = f.get();
                if(newAssessList != null) {
                    for(AssessorData data : newAssessList) {
                        if( ShareUtil.hasConfigAccess(callCntrSrvcList,
                                (data.getCallCenterId() != null ) ? Long.valueOf(data.getCallCenterId()) : null,
                                data.getServiceId().intValue())) {
                            assessorList.add(data);
                        } else {
                            log.debug(" Filtering out assesor data id: "+ data.getId() +" for not matching client: "+data.getClient()
                                    +" clientId: "+data.getClientId()+"callCenterId: "+data.getCallCenterId() +" or serviceId: "+data.getServiceId() );
                        }
                    }
                }
            }
            return assessorList;
        } catch(CancellationException ce) {
            log.error("Getting a list of assessor data concurrnetly timed out. ", ce);
            throw ce;
        }catch(Exception e) {
            log.error("Getting a list of assessor data concurrnetly failed. ", e);
            throw e;
        }
    }

}
