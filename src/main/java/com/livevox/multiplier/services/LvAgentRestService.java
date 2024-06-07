/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.services;

import com.livevox.multiplier.domain.lvapi.Agent;
import com.livevox.multiplier.domain.lvapi.ListRequest;
import com.livevox.multiplier.domain.exceptions.InvalidParametersException;
import com.livevox.multiplier.domain.AgentRequest;

import jakarta.xml.ws.WebServiceException;
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
