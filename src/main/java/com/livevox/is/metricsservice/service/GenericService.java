package com.livevox.is.metricsservice.service;

import com.livevox.is.metricsservice.domain.*;

public interface GenericService {

    String getResponse();
    GenericResponse<Agent> getAgents(AgentRequest request);
    GenericResponse<Client> getDashBoards(String appName, String token);
}
