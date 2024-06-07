package com.livevox.multiplier.jobs;

import com.livevox.multiplier.domain.exceptions.MissingFieldsException;
import com.livevox.multiplier.domain.exceptions.UnauthorizedException;
import com.livevox.multiplier.services.impl.LvStandardReportsRestServiceImpl;
import com.livevox.multiplier.domain.lvapi.AgentAccount;
import com.livevox.multiplier.domain.JobRequest;
import lombok.extern.slf4j.Slf4j;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
public class AgentAccountLookupJob extends AbstractConfigWrapper<LvStandardReportsRestServiceImpl>
        implements Callable<List<AgentAccount>>, ClientId, Serializable {

    private static final long serialVersionUID = 92345799672699L;




    public AgentAccountLookupJob(JobRequest req) throws Exception{
        super(req, LvStandardReportsRestServiceImpl.class);
    }



    @Override
    public List<AgentAccount> call()  throws UnauthorizedException,
            MissingFieldsException, Exception {
        log.debug("START AgentAccountLookupJob.call() ");
        if (req == null || getService() == null ||
                (req.getAccount() == null && req.getOriginalAccountNumber() == null) ||
                req.getStartDate() == null || req.getEndDate() == null) {
            log.error("Can not lookup agent accounts.   The request is missing required fields. ");
            throw new MissingFieldsException();
        }
        try {
            double callStart = System.currentTimeMillis();
            List<AgentAccount> agentAccounts = getService()
                    .getAgentAccounts(req.toAgentRequest());
            log.warn(" Found "+ ( (agentAccounts == null) ? 0 : agentAccounts.size() ) +
                    " agents for clientId: "+ req.getClientId()+".   dur=" +
                    ((System.currentTimeMillis() - callStart) / 1000));
            log.debug("END AgentAccountLookupJob.call() ");
            return agentAccounts;
        } catch (WebServiceException wsExp) {
            if(wsExp.getCause() != null && wsExp.getCause() instanceof UnauthorizedException) {
                log.error("Getting the agent account list failed. ");
                throw new UnauthorizedException("Getting the agent account list failed.");
            }
            log.error("Getting the agent account list failed. ", wsExp);
            throw wsExp;
        }catch (Exception t) {
            log.error("Getting the agent account list failed.", t );
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
