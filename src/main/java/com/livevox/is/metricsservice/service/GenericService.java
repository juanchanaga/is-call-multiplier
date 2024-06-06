package com.livevox.is.metricsservice.service;

import com.livevox.is.metricsservice.domain.*;

import java.util.List;

public interface GenericService {

    String getResponse();
    List<Agent> getAgents(AgentRequest request);
    GenericResponse<Client> getDashBoards(String appName, String token);
}
