package com.livevox.is.metricsservice.jobs;

import com.livevox.is.metricsservice.controller.rest.conf.LvAgentRestService;
import com.livevox.is.metricsservice.controller.rest.conf.LvAgentRestServiceImpl;
import com.livevox.is.metricsservice.domain.Agent;
import com.livevox.is.metricsservice.domain.JobRequest;
import com.livevox.is.metricsservice.exceptions.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;

import javax.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
public class AgentLookupJob extends AbstractConfigWrapper<LvAgentRestService>
        implements Callable<List<Agent>>, ClientId, Serializable {


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
