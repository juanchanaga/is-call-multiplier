/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.shareddata.multiplier.jobs;

import com.livevox.integration.commons.domain.lvapi.Agent;
import com.livevox.commons.exceptions.UnauthorizedException;
import com.livevox.commons.services.rest.configuration.LvAgentRestService;
import com.livevox.commons.services.rest.configuration.LvAgentRestServiceImpl;
import com.livevox.integration.shareddata.multiplier.domain.JobRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
public class AgentLookupJob extends AbstractConfigWrapper<LvAgentRestService>
        implements Callable<List<Agent>>, ClientId, Serializable {

    private static final long serialVersionUID = 3598799672699L;


    public AgentLookupJob(JobRequest jbReq) throws Exception{
        super(jbReq, LvAgentRestServiceImpl.class);
    }



    @Override
    public List<Agent> call()  throws UnauthorizedException, DataIntegrityViolationException, Exception {
        log.debug("START AgentLookupJob.call() ");
        if (getReq() == null || getService() == null ) {
            log.error("Can not lookup agents.   The request is invalid. ");
            throw new DataIntegrityViolationException("Missing required fields.");
        }
        try {
            double callStart = System.currentTimeMillis();

            List<Agent> agents = getService().listAgents( getReq().toListRequest());
            if(agents != null) {
                for(Agent agnt: agents) {
                    agnt.setClientId(getReq().getClientId());
                }
            }
            log.warn(" Got "+ ( (agents == null) ? 0 : agents.size() ) +" agents for clientId: "+
                    getReq().getClientId()+".   dur=" + ((System.currentTimeMillis() - callStart) / 1000));
            log.debug("END AgentLookupJob.call() ");
            return agents;
        } catch (WebServiceException wsExp) {
            if(wsExp.getCause() != null && wsExp.getCause() instanceof UnauthorizedException) {
                log.error("Getting the agent list failed. ");
                throw new UnauthorizedException("Getting the agent list failed.");
            }
            log.error("Getting the agent list failed. ", wsExp);
            throw wsExp;
        }catch (Exception t) {
            log.error("Getting the agent list failed.", t );
            throw t;
        }
    }



    @Override
    public Integer getClientId() {
        if(req == null) {
            return null;
        }
        return req.getClientId();
    }



    @Override
    public void setClientId(Integer clientId) {
        if(req != null) {
            req.setClientId(clientId);
        }
    }






}
