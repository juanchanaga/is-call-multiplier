/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.controller.rest.conf;

import com.livevox.is.metricsservice.domain.Agent;
import com.livevox.is.metricsservice.domain.AgentRequest;
import com.livevox.is.metricsservice.domain.ListRequest;
import com.livevox.is.metricsservice.exceptions.InvalidParametersException;

import javax.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.List;

public interface LvAgentRestService extends Serializable {
    Agent readAgent(Integer var1, Integer var2) throws InvalidParametersException, WebServiceException;

    Agent readAgent(Integer var1, Integer var2, String var3) throws InvalidParametersException, WebServiceException;

    Agent readAgent(Integer var1, String var2) throws InvalidParametersException, WebServiceException;

    Agent readAgent(Integer var1, String var2, String var3) throws InvalidParametersException, WebServiceException;

    List<Agent> findAgents(AgentRequest var1) throws InvalidParametersException, WebServiceException;

    List<Agent> listAgents(ListRequest var1) throws InvalidParametersException, WebServiceException;
}
