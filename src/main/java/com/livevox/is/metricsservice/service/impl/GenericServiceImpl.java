package com.livevox.is.metricsservice.service.impl;

import com.livevox.is.metricsservice.domain.*;
import com.livevox.is.metricsservice.service.GenericService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenericServiceImpl implements GenericService {

    public GenericServiceImpl() {
    }

    @Override
    public String getResponse() {
        return "Prueba";
    }

    @Override
    public GenericResponse<Agent> getAgents(AgentRequest request) {
        GenericResponse<Agent> resp = new GenericResponse<>();

        List<Agent> agents = new ArrayList<>();
        Agent agent = new Agent();

        agent.setActive(false);
        agent.setClientId(1008784);
        agent.setId(1065618L);
        agent.setFirstName("dialer");
        agent.setLastName("team");
        agent.setPhone("1234");
        agent.setLoginId("DIALER");

        agents.add(agent);
        resp.setData(agents);

        resp.setTotalCount(resp.getData().size());
        resp.setStatus(Response.ResponseStatus.SUCESS.getCode());
        return resp;
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
