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
import com.livevox.is.metricsservice.domain.ListRestResponse;
import com.livevox.is.metricsservice.exceptions.InvalidParametersException;
import com.livevox.is.metricsservice.service.config.AtgConfigService;
import com.livevox.is.metricsservice.session.LvSessionRestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.List;

@Service
public class LvAgentRestServiceImpl extends LvSessionRestServiceImpl implements LvAgentRestService, Serializable {

    public static final String URI_PREFIX = "/configuration/";

    public static final String URI_SUFFIX = "/agents";

    @Autowired(
            required = false
    )
    public LvAgentRestServiceImpl(AtgConfigService atgConfigService) {
        super(atgConfigService);
    }

    public Agent readAgent(Integer agentId, Integer clientId) throws InvalidParametersException, WebServiceException {
        return this.readAgent(agentId, (String)null, clientId, (String)null);
    }

    public Agent readAgent(Integer agentId, Integer clientId, String apiVersion) throws InvalidParametersException, WebServiceException {
        return this.readAgent(agentId, (String)null, clientId, apiVersion);
    }

    public Agent readAgent(Integer agentId, String apiSessionId, String apiVersion) throws InvalidParametersException, WebServiceException {
        return this.readAgent(agentId, apiSessionId, (Integer)null, apiVersion);
    }

    public Agent readAgent(Integer agentId, String apiSessionId) throws InvalidParametersException, WebServiceException {
        return this.readAgent(agentId, apiSessionId, (Integer)null, (String)null);
    }

    public List<Agent> findAgents(AgentRequest req) throws InvalidParametersException, WebServiceException {
        log.debug("Start findAgents() ");
        if (req != null && req.valid()) {
            ListRestResponse resp;
            try {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.getURI(URI_PREFIX,  URI_SUFFIX+"/search", req.getApiVersion(), req.getClientId()));
                builder.queryParam("count", new Object[]{req.getCount() == null ? 1000 : req.getCount()});
                builder.queryParam("offset", new Object[]{req.getOffset() == null ? 0 : req.getOffset()});
                resp = (ListRestResponse)this.makeLvCall(builder.toUriString(), HttpMethod.POST, this.getCallSafeReqObj(req), ListRestResponse.class, req.getClientId(), req.getApiSessionId());
            } catch (WebServiceException var4) {
                throw new WebServiceException("Could not recover from auth failure." + var4, var4);
            } catch (HttpClientErrorException var5) {
                if (var5.getStatusCode() != null && var5.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                    return null;
                }

                log.error("Failed response on agent lookup. ", var5);
                throw new WebServiceException("Failed response on agent lookup.   HttpCode: " + var5.getStatusCode());
            } catch (Exception var6) {
                log.error("Error finding matching agents. ", var6);
                throw new WebServiceException("Error finding matching agents. ");
            }

            if (resp == null) {
                log.error("Unable to read agent. The response was invalid. ");
                throw new WebServiceException("Read agent failed. The response was invalid ");
            } else {
                log.debug("End findAgents() ");
                return resp.getAgent();
            }
        } else {
            throw new InvalidParametersException(" Required value was null ");
        }
    }

    public List<Agent> listAgents(ListRequest req) throws InvalidParametersException, WebServiceException {
        log.debug("Start listAgents() ");
        if (req == null) {
            throw new InvalidParametersException(" Required value was null ");
        } else {
            ListRestResponse resp;
            try {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.getURI(URI_PREFIX, URI_SUFFIX, req.getApiVersion(), req.getClientId()));
                builder.queryParam("count", new Object[]{req.getCount() == null ? 1000 : req.getCount()});
                builder.queryParam("offset", new Object[]{req.getOffset() == null ? 0 : req.getOffset()});
                resp = (ListRestResponse)this.makeLvCall(builder.toUriString(), HttpMethod.GET, (Object)null, ListRestResponse.class, req.getClientId(), req.getApiSessionId());
            } catch (WebServiceException var4) {
                throw new WebServiceException("Could not recover from auth failure." + var4, var4);
            } catch (HttpClientErrorException var5) {
                if (var5.getStatusCode() != null && var5.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                    return null;
                }

                log.error("Failed response on agent list. ", var5);
                throw new WebServiceException("Failed response on agent list.   HttpCode: " + var5.getStatusCode());
            } catch (Exception var6) {
                log.error("Error finding agents list. ", var6);
                throw new WebServiceException("Error finding agents list. ");
            }

            if (resp == null) {
                log.error("Unable to list agents. The response was invalid. ");
                throw new WebServiceException("List agents failed. The response was invalid ");
            } else {
                log.debug("End listAgents() ");
                return resp.getAgent();
            }
        }
    }

    private AgentRequest getCallSafeReqObj(AgentRequest req) {
        AgentRequest newReq = new AgentRequest();
        newReq.setLastName(req.getLastName());
        newReq.setLoginId(req.getLoginId());
        newReq.setPhoneNumber(req.getPhoneNumber());
        return newReq;
    }

    protected Agent readAgent(Integer agentId, String apiSessionId, Integer clientId, String apiVersion) {
        log.debug("Start readAgent() ");
        if (agentId == null) {
            throw new InvalidParametersException(" Required value was null ");
        } else {
            Agent resp;
            try {
                resp = (Agent)this.makeLvCall(this.getURI(URI_PREFIX,  URI_SUFFIX + "/" + agentId, apiVersion, clientId), HttpMethod.GET, (Object)null, Agent.class, clientId, apiSessionId);
            } catch (WebServiceException var7) {
                throw new WebServiceException("Could not recover from auth failure." + var7, var7);
            } catch (Exception var8) {
                log.error("Read user failed. ", var8);
                throw new WebServiceException("Read user failed. ");
            }

            if (resp == null) {
                log.error("Unable to read agent. The response was invalid. ");
                throw new WebServiceException("Read agent failed. The response was invalid ");
            } else {
                log.debug("End readAgent() ");
                return resp;
            }
        }
    }
}
