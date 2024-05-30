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
    public AgentsResponse getAgents(AgentRequest request) {
        Agent mockAgent = new Agent();
        mockAgent.setActive(true);
        mockAgent.setClientId(1);
        mockAgent.setId(1);
        mockAgent.setFirstName("Prueba");
        mockAgent.setLastName("Test");
        mockAgent.setPhone("123456789");
        mockAgent.setLoginId("987");

        AgentsResponse agentsResponse = new AgentsResponse();

        List<Agent> agents = new ArrayList<>();
        agents.add(mockAgent);

        agentsResponse.setData(agents);

        agentsResponse.setStatus(0);
        agentsResponse.setTotalCount(agents.size());

        return agentsResponse;
    }

    @Override
    public DashBoardResponse getDashBoards(String token, Long dc) {
        CallCenter callCenter = new CallCenter();
        callCenter.setClientId(1);
        callCenter.setName("Prueba");
        callCenter.setId(1);
        callCenter.setCallCenterId(1);

        DashBoardResponse dashBoardResponse = new DashBoardResponse();
        DashBoard dashBoard = new DashBoard();

        List<CallCenter> callCenters = new ArrayList<>();
        List<DashBoard> dashBoards = new ArrayList<>();

        callCenters.add(callCenter);

        dashBoard.setCallCenters(callCenters);
        dashBoard.setId(1);

        dashBoards.add(dashBoard);

        dashBoardResponse.setData(dashBoards);
        dashBoardResponse.setStatus(0);
        dashBoardResponse.setTotalCount(dashBoards.size());

        return dashBoardResponse;
    }

}
