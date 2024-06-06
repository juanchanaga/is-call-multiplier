package com.livevox.is.metricsservice.service.impl;

import com.livevox.is.metricsservice.domain.*;
import com.livevox.is.metricsservice.service.GenericService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class GenericServiceImpl implements GenericService {

    public GenericServiceImpl() {
    }

    @Override
    public String getResponse() {
        return "Prueba";
    }

    @Override
    public List<Agent> getAgents(AgentRequest request) {
//        List<AgentLookupJob>  agentJobs = new ArrayList<AgentLookupJob>();
//        JobRequest jobReq = new JobRequest(agntReq);
//        jobReq.setConfigService(configService);
//        jobReq.setClientId( isAuthenticatedAs(agntReq.getApiSessionId()) );
//
//        try {
//            agentJobs = getConcurrentJobList(agntReq.getClientId(),
//                    jobReq, AgentLookupJob.class, true, Permission.PermissionType.READ);
//        } catch(Exception e) {
//            log.error("Generating campaign lookup jobs failed. ", e);
//            throw e;
//        }
//
//        List<com.livevox.integration.commons.domain.lvapi.Agent> agentList = new ArrayList<com.livevox.integration.commons.domain.lvapi.Agent>();
//        try {
//            List<Future<List<com.livevox.integration.commons.domain.lvapi.Agent>>> respList = executionService.invokeAll(
//                    agentJobs, 300, TimeUnit.SECONDS);
//            if(respList == null) {
//                return agentList;
//            }
//            for(Future<List<com.livevox.integration.commons.domain.lvapi.Agent>> f : respList) {
//                List<com.livevox.integration.commons.domain.lvapi.Agent> newCampaigns = f.get();
//                agentList.addAll(newCampaigns);
//            }
//            return agentList;
//        } catch(Exception e) {
//            log.error("Getting agents lists concurrnetly failed. ", e);
//            throw e;
//        }
        List<Agent> agents = new ArrayList<>();
        return agents;
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
        skill.setId(1008784L);
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
