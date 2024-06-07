/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.services;

import com.livevox.multiplier.domain.lvapi.CallCenter;
import com.livevox.multiplier.domain.stats.Skill;
import com.livevox.multiplier.dao.SharedDataDao;
import com.livevox.multiplier.domain.*;
import com.livevox.multiplier.domain.exceptions.MissingFieldsException;
import com.livevox.multiplier.domain.exceptions.UnauthorizedException;
import com.livevox.multiplier.domain.requests.RecordingReportRequest.SortBy;
import com.livevox.multiplier.services.config.AtgConfigService;
import com.livevox.multiplier.services.sigin.SignInService;
import com.livevox.multiplier.utils.ShareUtil;
import com.livevox.multiplier.domain.JobRequest;
import com.livevox.multiplier.domain.RecordingRequest;
import com.livevox.multiplier.jobs.ConfigDetailsJob;
import com.livevox.multiplier.jobs.TermCodeLookupJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * The type Call multiplier service abstract.
 *
 * @autor Luis Felipe Sosa Alvarez lsosa@livevox.com
 */
@Service
@Slf4j
public class CallMultiplierServiceAbstract {

    public static final String SIGN_IN_APP_NAME = "CALL_MULTIPLIER_SERVICE";

    public static final String ASSESSOR_HOST_PROPERTY_NAME =
            "com.livevox.atg.shareddata.multiplier.service.CallMultiplierServiceImpl.Assessor.host";

    protected volatile Map<Integer, Map<String, LvSession>> cachedTokens;

    /**
     * The Config service.
     */
    protected AtgConfigService configService;

    /**
     * The Sign in service.
     */
    protected SignInService signInService;


    protected SharedDataDao sharedDao;


    /**
     * The Execution service.
     */
    protected ExecutorService executionService;




    /**
     * Instantiates a new Call multiplier service abstract.
     */
    public CallMultiplierServiceAbstract() {
        executionService = Executors.newFixedThreadPool(10);
    }


    /**
     * Is authenticated as integer.
     *
     * @param token the token
     * @return the ResponseEntity
     */
    public ResponseEntity<Integer> isAuthenticatedAs(final String token) {
        log.debug("START - isAuthenticated() ");
        if(token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        AuthResponse auth =  signInService.isAuthenticated(token);
        if(auth != null && auth.isValidToken()) {
            log.debug("END - isAuthenticated() ");
            return ResponseEntity.ok(auth.getClientId());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    protected void populateRequestDefaults(final RecordingRequest req) {
        if(req == null) {
            return;
        }
        if(req.getSortBy() == null) {
            req.setSortBy(SortBy.CALL_START_TIME);
        }
        if(req.getCount() == null) {
            req.setCount(1000);
        }
        if(req.getOffset() == null) {
            req.setOffset(0);
        }
        if(req.getEndDate() == null)  {
            req.setEndDate( new Date() );
        }
        if(req.getStartDate() == null) {
            Calendar twoWeeksAgo = Calendar.getInstance();
            twoWeeksAgo.add(Calendar.WEEK_OF_YEAR, -2);
            req.setStartDate(twoWeeksAgo.getTime());
        }
    }

    protected <T> List<T> getConcurrentJobList(final Integer filterClientId,
                                               final JobRequest req, Class<T> jobClass)
            throws MissingFieldsException, Exception {
        return getConcurrentJobList(filterClientId, req, jobClass, null);
    }


    protected <T> List<T> getConcurrentJobList(final Integer filterClientId,
                                               final JobRequest req, Class<T> jobClass, final Permission.PermissionType permission)
            throws MissingFieldsException, Exception {
        return getConcurrentJobList(filterClientId, req, jobClass, false, permission).getBody();
    }


    /**
     * Gets concurrent job list.
     *
     * @param <T>                        the type parameter
     * @param filterClientId             the filter client id
     * @param req                        the req
     * @param jobClass                   the job class
     * @param addPermissionsRestrictions the add permissions restrictions
     * @return the concurrent job list
     * @throws MissingFieldsException the missing fields exception
     * @throws Exception              the exception
     */
    protected <T> ResponseEntity<List<T>> getConcurrentJobList(final Integer filterClientId,
                                                               final JobRequest req, Class<T> jobClass, final Boolean addPermissionsRestrictions,
                                                               final Permission.PermissionType permission) throws Exception {

        if (req == null || jobClass == null || req.getClientId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Error 400 si faltan campos
        }

        List<Client> callCntrSrvcList = null;
        if (addPermissionsRestrictions != null && addPermissionsRestrictions) {
            callCntrSrvcList = getCallCentersAndSkills(null, req.getApplication(), req.getSessoinId());
        }

        List<T> jobList = new ArrayList<>();
        List<Integer> jobClientIdsAdded = new ArrayList<>();

        if (isRequestForOwner(req.getClientId(), filterClientId)) {
            JobRequest newReq = req.copy();
            if (!isJobBlockedByPermissions(req, newReq, callCntrSrvcList)) {
                jobList.add(getJobInstance(newReq, jobClass));
                jobClientIdsAdded.add(req.getClientId());
            }
        }

        List<Share> sharedClients = null;
        try {
            sharedClients = sharedDao.getSharesForClient(req.getClientId());
        } catch (Exception e) {
            log.error("Looking up clients shared data failed for clientId: " + req.getClientId(), e);
        }

        if (sharedClients == null) {
            return ResponseEntity.ok(jobList);
        }

        addJobsForSharedClients(filterClientId, req, jobClass, permission, callCntrSrvcList, jobList, sharedClients, jobClientIdsAdded);
        return ResponseEntity.ok(jobList);
    }

    /**
     * Gets call centers and skills.
     *
     * @param filterClientId the filter client id
     * @param appName        the app name
     * @param token          the token
     * @return the call centers and skills
     * @throws UnauthorizedException the unauthorized exception
     * @throws Exception             the exception
     */
    public List<Client> getCallCentersAndSkills(final Integer filterClientId,
                                                final String appName, final String token) throws Exception {
        Integer userClientId = isAuthenticatedAs(token).getBody().intValue();

        JobRequest jobReq = new JobRequest(userClientId, token, appName, configService);


        List<ConfigDetailsJob> configDetailsJobs = getConcurrentJobList(filterClientId,
                jobReq, ConfigDetailsJob.class, false, Permission.PermissionType.READ).getBody();

        configDetailsJobs.forEach(configItem->configItem.setSignInService(signInService));

        List<Client> clientList = new ArrayList<Client>();
        double start = System.currentTimeMillis();

        try {
            List<Future<Client>> respList = executionService.invokeAll(
                    configDetailsJobs, 30, TimeUnit.SECONDS);
            log.debug(" Done looking up shared call center and services for clientId: "
                    +userClientId+"   dur="+( (System.currentTimeMillis() - start) / 1000));
            if(respList == null) {
                return clientList;
            }
            for(Future<Client> f : respList) {
                Client newClient = f.get();
                newClient.sortCallCenters();
                clientList.add(newClient);
            }

            Collections.sort(clientList);
            return clientList;
        } catch(Exception e) {
            log.error("Executing call center and service lookups concurently failed. ", e);
            throw e;
        }
    }

    public List<Client> getTermCodes(final Integer filterClientId,
                                     final String appName, final String token) throws UnauthorizedException,
            MissingFieldsException, Exception {
        Integer userClientId = isAuthenticatedAs(token).getBody().intValue();
        List<TermCodeLookupJob> termCodeJobs = getConcurrentJobList(filterClientId,
                new JobRequest(userClientId, token, appName, configService),
                TermCodeLookupJob.class, Permission.PermissionType.READ);
        List<Client> clientList = new ArrayList<>();
        try {
            List<Future<Client>> respList = executionService.invokeAll(
                    termCodeJobs, 45, TimeUnit.SECONDS);
            if(respList == null) {
                return clientList;
            }
            for(Future<Client> f : respList) {
                Client newClient = f.get();
                clientList.add(newClient);
            }
            return clientList;
        } catch(MissingFieldsException e) {
            log.error("Executing the DND block calls concurrently failed.  Required fields were missing ", e);
            throw e;
        }catch(Exception e) {
            log.error("Executing the DND block calls concurrently failed. ", e);
            throw e;
        }
    }

    /**
     * Is request for owner boolean.
     *
     * @param ownerClientId     the owner client id
     * @param requestedClientId the requested client id
     * @return the boolean
     */
    protected boolean isRequestForOwner(final Integer ownerClientId,
                                        final Integer requestedClientId) {
        return  requestedClientId == null || ownerClientId == null ||
                requestedClientId.equals(ownerClientId);
    }

    private boolean isJobBlockedByPermissions(JobRequest serviceRequest,
                                              JobRequest requestWithFilteredPermissions, List<Client> callCntrSrvcList) {
        if( serviceRequest == null || requestWithFilteredPermissions == null ||
                callCntrSrvcList == null) {
            return false;
        }
        populatePermssionRestrictions(requestWithFilteredPermissions, callCntrSrvcList );
        //
        //  If the REST request did not have any permissions filtering then there is
        //  no reason to skip the job.
        //
        if( CollectionUtils.isEmpty(serviceRequest.getCallCenterIds() )
                && CollectionUtils.isEmpty(serviceRequest.getServiceIds())  ) {
            return false;
        }
        //
        //  If the request included filtering by callCenterId and none of the requested
        //  callCenterIds match the user's permissions callCenterId list
        //  then this job should not be run since there is no way for it to have any
        //  matching data.
        //
        if( !CollectionUtils.isEmpty(serviceRequest.getCallCenterIds()) &&
                CollectionUtils.isEmpty(requestWithFilteredPermissions.getCallCenterIds())  ) {
            return true;
        }
        //
        //  If the request included filtering by serviceIds and none of the requested
        //  serviceIds match the user's permissions serviceIds list
        //  then this job should not be run since there is no way for it to have any
        //  matching data.
        //
        if( !CollectionUtils.isEmpty(serviceRequest.getServiceIds() ) &&
                CollectionUtils.isEmpty(requestWithFilteredPermissions.getServiceIds() ) ) {
            return true;
        }

        return false;
    }

    private void populatePermssionRestrictions(JobRequest req, List<Client> callCntrSrvcList) {
        if(req == null  || callCntrSrvcList == null ) {
            return;
        }
        for(Client clnt : callCntrSrvcList) {
            if(clnt.getId() != null && clnt.getId().equals(req.getClientId())) {
                log.debug("Loading permissions for clientId: " + req.getClientId());

                List<Long> approvedFilterIds = getAuthorizedPermissionIds(
                        req.findCallCenterIdsAsLong(), clnt.findAllCallCenterIds() );
                log.debug("   Added callCenterIds: "+approvedFilterIds);
                if(approvedFilterIds != null) {
                    //
                    //  Since the API's lookup logic overrides the callCenterId filtering
                    //  with the serviceIds.   So we can only include serviceIds that
                    //  that are children of the callCenterIds approved or filtered for.
                    //
                    List<Integer> childServicesList = new ArrayList<>();
                    for(Long ccId : approvedFilterIds) {
                        if(ccId != null) {
                            CallCenter cc = clnt.getCallCenter(ccId);
                            if (cc != null && cc.getCallCenterId() != null && cc.getServices() != null )  {
                                for(Skill srvc : cc.getServices()) {
                                    //
                                    //  Only add the serviceId there are no serviceIds to filter
                                    //  by in the request, or the approved serviceId is also in
                                    //  matches one of the requested filter serviceId list.
                                    //
                                    if( (req.findServiceIdsAsLong() == null ||
                                            req.findServiceIdsAsLong().isEmpty()) ||
                                            ( srvc.getId() != null && req.findServiceIdsAsLong()
                                                    .contains( srvc.getId().longValue()) )   ) {
                                        childServicesList.add(srvc.getId());
                                    }
                                }
                            }
                        }
                    }
                    //
                    // Now clear the serviceIds in the cloned request and add
                    // the serviceId list from the loop above.
                    //
                    req.setCallCenterIds(null);
                    req.setServiceIds(null);
                    req.addServiceIdsAsInt(childServicesList);
                    log.debug("   Added serviceIds: " + childServicesList);
                }
                return;
            }
        }
    }

    private <T> void addJobsForSharedClients(Integer filterClientId, JobRequest req, Class<T> jobClass,
                                             Permission.PermissionType permission, List<Client> callCntrSrvcList, List<T> jobList,
                                             List<Share> sharedClients, List<Integer> jobClientIdsAdded) throws Exception {
        if ( !CollectionUtils.isEmpty(sharedClients)  ) {
            for (Share s : sharedClients) {
                if (s.getAccess() != null) {
                    for (Access acc : s.getAccess()) {
                        if (isRequestForOwner(acc.getSharedClientId(), filterClientId)) {
                            if(!isJobAlreadyAdded(jobClientIdsAdded, acc.getSharedClientId()) ) {
                                if (permission == null || ShareUtil.can(permission, acc,
                                        acc.getSharedClientId(), req.getApplication(), null)) {
                                    //
                                    //  The request needs to be cloned since some of the
                                    //  request fields have to over written with values
                                    //  specific to this shared client.
                                    //
                                    JobRequest newReq = req.copy();
                                    newReq.setClientId(acc.getSharedClientId());
                                    newReq.setSessoinId(getApiSession(acc) );
                                    if( !isJobBlockedByPermissions(req,newReq, callCntrSrvcList) ) {
                                        jobList.add(getJobInstance(newReq, jobClass));
                                        jobClientIdsAdded.add(acc.getSharedClientId());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private List<Long> getAuthorizedPermissionIds(List<Long> reqIds, List<Long> authIds) {
        if(authIds == null) {
            return null;
        }
        if( reqIds == null ) {
            return authIds;
        }
        List<Long> approvedIds = new ArrayList<>();
        for(Long id : reqIds ) {
            if(id != null && authIds.contains(id) ) {
                approvedIds.add(id);
            }
        }
        return approvedIds;
    }

    private  <T> T getJobInstance(final JobRequest req, final Class<T> jobClass)
            throws Exception {
        if(req == null) {
            throw new IllegalAccessException("Job Request was null.  Not able to generate new job instance.");
        }
        if(req.getConfigService() == null) {
            req.setConfigService(configService);
        }
        Constructor constructor = jobClass.getConstructor(JobRequest.class);
        return (T) constructor.newInstance(req);
    }

    public String getToken(final Share s, final Integer sharedClientId) {
        if(s == null || s.getAccess() == null || sharedClientId == null ) {
            return null;
        }
        for( Access acc : s.getAccess() ) {
            if(acc.getSharedClientId() != null && acc.getSharedClientId().equals(sharedClientId) ) {
                return getToken(acc);
            }
        }
        return null;
    }

    private String getApiSession(Access acc) {
        if(acc == null || acc.getUserAcct() == null) {
            return null;
        }
        try {
            log.info("Calling signInService with this information:"+acc.getUserAcct().getClientName()+","+acc.getUserAcct().getUserName()+","+
                    ""+acc.getUserAcct().getPassword()+","+SIGN_IN_APP_NAME);
            Response session = signInService.login(acc.getUserAcct().getClientName(),
                    acc.getUserAcct().getUserName(), acc.getUserAcct().getPassword(), SIGN_IN_APP_NAME);
            log.info("Auth response: "+session.toString());
            if(session != null ) {
                log.info("CallMultiplierServiceAbstract.getApiSession: "+session.getApiSessionId());
                return session.getApiSessionId();
            }
            return null;
        } catch(Exception e) {
            log.error("Geting session login token failed.", e);
        }
        return null;
    }

    public synchronized String getToken(final Access access) {
        if( access == null || access.getSharedClientId() == null ||
                access.getUserAcct() == null ||
                StringUtils.isEmpty(access.getUserAcct().getUserName())) {
            return null;
        }
        if(cachedTokens == null) {
            cachedTokens = new HashMap<Integer, Map<String, LvSession>>();
        }
        for(Integer clientId : cachedTokens.keySet() ) {
            if(  access.getSharedClientId().equals(clientId) ) {
                Map<String, LvSession> cachedUserList = cachedTokens.get(clientId);
                if(cachedUserList != null ) {
                    for (String userName : cachedUserList.keySet()) {
                        if (access.getUserAcct().getUserName().equals(userName)) {
                            LvSession header = cachedUserList.get(userName);
                            if (header != null && !StringUtils.isEmpty(header.getSessionId()) &&
                                    !isExpired(header.getTokenLastUpdated())) {
                                return header.getSessionId();
                            } else {
                                loadToken(header);
                                if(header != null) {
                                    return header.getSessionId();
                                }
                            }
                        }
                    }
                }
                //
                //  No match was found for the user name so add a header for this user
                //
                LvSession updatedHeader = loadToken(access.getUserAcct());
                if(updatedHeader == null || cachedUserList == null ) {
                    log.error("Cached token data was null after calling update method().");
                    return null;
                }
                cachedUserList.put(access.getUserAcct().getUserName(), updatedHeader);
                return updatedHeader.getSessionId();
            }
        }
        //
        //  No match was found for the clientId so add that and a header for this user.
        //
        Map<String, LvSession> cachedUsers = new HashMap<String, LvSession>();
        LvSession updatedHeader = loadToken(access.getUserAcct());
        if(updatedHeader == null) {
            return null;
        }
        cachedUsers.put(access.getUserAcct().getUserName(), updatedHeader);
        cachedTokens.put(access.getSharedClientId(), cachedUsers);
        return updatedHeader.getSessionId();
    }

    protected boolean isExpired(final Calendar savedDate) {
        if(savedDate == null) {
            return true;
        }
        Calendar expires = Calendar.getInstance();
        expires.add(Calendar.HOUR, -23);
        return savedDate.before(expires);
    }


    private LvSession loadToken(LvSession auth) {
        if(auth == null) {
            return null;
        }
        try {
            Response session = signInService.login(auth.getClientName(),
                    auth.getUserName(), auth.getPassword(), SIGN_IN_APP_NAME);
            if(session != null ) {
                auth.setSessionId(session.getApiSessionId());
                auth.setTokenLastUpdated(Calendar.getInstance());
                return auth;
            }
        } catch(Exception e) {
            log.error("Geting session login token failed.", e);
        }
        return null;
    }

    private boolean isJobAlreadyAdded(List<Integer> jobClientIdsAdded, Integer clientId) {
        if(jobClientIdsAdded == null || clientId == null) {
            return false;
        }
        for(Integer existingId : jobClientIdsAdded) {
            if(existingId.equals(clientId)) {
                return true;
            }
        }
        return false;
    }
}
