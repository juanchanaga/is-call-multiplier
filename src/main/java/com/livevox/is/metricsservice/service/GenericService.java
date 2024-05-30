package com.livevox.is.metricsservice.service;

import com.livevox.is.metricsservice.domain.AgentRequest;
import com.livevox.is.metricsservice.domain.AgentsResponse;
import com.livevox.is.metricsservice.domain.DashBoardResponse;

import java.util.List;

public interface GenericService {

    String getResponse();
    AgentsResponse getAgents(AgentRequest request);
    DashBoardResponse getDashBoards(String token, Long dc);
}
