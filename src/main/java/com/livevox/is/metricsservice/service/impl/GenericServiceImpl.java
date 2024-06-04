package com.livevox.is.metricsservice.service.impl;

import com.livevox.is.metricsservice.domain.*;
import com.livevox.is.metricsservice.jobs.AgentLookupJob;
import com.livevox.is.metricsservice.service.GenericService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class GenericServiceImpl extends GenericServiceAbstract implements GenericService {

    /**
     * Instantiates a new Call multiplier service.
     */
    public GenericServiceImpl() {
        super();
    }


    /**
     * Instantiates a new Call multiplier service.
     *
     * @param threadPoolSize the thread pool size
     */
    public GenericServiceImpl(Integer threadPoolSize) {
        this();
        executionService = Executors.newFixedThreadPool(threadPoolSize);
    }

    @Override
    public String getResponse() {
        return "Prueba";
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agent> getAgents(final AgentRequest agntReq) throws Exception {
        List<AgentLookupJob>  agentJobs = new ArrayList<>();

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

    @Override
    public GenericResponse<Client> getDashBoards(String appName, String token) {

        GenericResponse<Client> resp = new GenericResponse<>();

        List<Client> clients = new ArrayList<>();
        List<CallCenter> callCenters = new ArrayList<>();
        List<Skill> skills = new ArrayList<>();

        Client client = new Client();
        CallCenter callCenter = new CallCenter();
        Skill skill = new Skill();

        client.setId(1008784);

        callCenter.setName("Call Center");
        callCenter.setCallCenterId(1008784L);
        callCenter.setClientId(1003619);

        skill.setSkillId(1008784);
        skill.setServiceId(1008784);
        skill.setName("AgentReg");
        skill.setCallCenterId(1003619);

        callCenters.add(callCenter);
        skills.add(skill);

        callCenter.setServices(skills);
        client.setCallCenters(callCenters);

        clients.add(client);
        resp.setData(clients);

        resp.setTotalCount(resp.getData().size());
        resp.setStatus(Response.ResponseStatus.SUCESS.getCode());
        return resp;
    }

}
