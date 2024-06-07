/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.controller.rest;


import com.livevox.multiplier.domain.Client;
import com.livevox.multiplier.domain.GenericResponse;
import com.livevox.multiplier.domain.Response;
import com.livevox.multiplier.domain.exceptions.MissingFieldsException;
import com.livevox.multiplier.domain.exceptions.UnauthorizedException;
import com.livevox.multiplier.domain.lvapi.Agent;
import com.livevox.multiplier.domain.lvapi.AgentAccount;
import com.livevox.multiplier.domain.*;
import com.livevox.multiplier.services.impl.CallMultiplierServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The type Multipler controller.
 *
 * @autor Luis Felipe Sosa Alvarez lsosa@livevox.com
 */
@RestController
@EntityScan (basePackages = "com.livevox.multiplier.domain")
@EnableJpaRepositories(basePackages = "com.livevox.multiplier.repository")
@Slf4j
public class MultiplierController {

    private CallMultiplierServiceImpl callMultiplierServiceImpl;


    @GetMapping(value = "/callCentersAndSkills/{token}/")
    public GenericResponse<Client> getCallCentersAndSkills(@PathVariable String token,
                                                           HttpServletResponse httpResp) {
        return getCallCentersAndSkillsByAppName(null, token, httpResp);
    }


    /**
     * Gets call centers and skills by app name.
     *
     * @param appName  the app name
     * @param token    the token
     * @param httpResp the http resp
     * @return the call centers and skills by app name
     */
    @GetMapping("/callCentersAndSkills/{appName}/{token}/")
    public GenericResponse<Client> getCallCentersAndSkillsByAppName(
            @PathVariable String appName, @PathVariable String token,
            HttpServletResponse httpResp) {
        GenericResponse<Client> resp = new GenericResponse<Client>();
        try {
            resp.setData(callMultiplierServiceImpl.getCallCentersAndSkills(null, appName, token));
            resp.setStatus(Response.ResponseStatus.SUCESS.getCode());
        } catch(UnauthorizedException e) {
            log.error("getCallCentersAndSkills() call failed due to bad token. ", e);
            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setStatus(Response.ResponseStatus.AUTH_FAILED.getCode());
            resp.addErrorMessage("Access Denied");
        }  catch(MissingFieldsException e) {
            log.error("getCallCentersAndSkills() call failed due to missing fields. ", e);
            httpResp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setStatus(Response.ResponseStatus.BAD_REQUEST.getCode());
            resp.addErrorMessage("Missing Required fields");
        } catch(Exception exp) {
            log.error("getCallCentersAndSkills() Getting call center list lookup failed. ", exp);
            resp.setStatus(Response.ResponseStatus.SYSTEM.getCode());
            httpResp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
        return resp;
    }

    @GetMapping(value = "/clientCallCentersAndSkills/{filterClientId}/{token}/")
    public GenericResponse<Client> getCallCentersAndSkillsByClient(@PathVariable Integer filterClientId,
                                                                   @PathVariable String token, HttpServletResponse httpResp) {
        GenericResponse<Client> resp = new GenericResponse<Client>();
        try {
            resp.setData(callMultiplierServiceImpl.getCallCentersAndSkills(filterClientId, null, token));
            resp.setStatus(Response.ResponseStatus.SUCESS.getCode());
        } catch(UnauthorizedException e) {
            log.error("getCallCentersAndSkills() call failed due to bad token. ", e);
            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setStatus(Response.ResponseStatus.AUTH_FAILED.getCode());
            resp.addErrorMessage("Access Denied");
        }  catch(MissingFieldsException e) {
            log.error("getCallCentersAndSkills() call failed due to missing fields. ", e);
            httpResp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setStatus(Response.ResponseStatus.BAD_REQUEST.getCode());
            resp.addErrorMessage("Missing Required fields");
        } catch(Exception exp) {
            log.error("getCallCentersAndSkills() Getting call center list lookup failed. ", exp);
            resp.setStatus(Response.ResponseStatus.SYSTEM.getCode());
            httpResp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
        return resp;
    }

    @GetMapping(value = "/termCodes/{filterClientId}/{appName}/{token}/",
            produces = "application/json")
    public GenericResponse<Client> getTermCodes(@PathVariable String appName,
                                                @PathVariable Integer filterClientId, @PathVariable String token,
                                                HttpServletResponse httpResp) {
        GenericResponse<Client> resp = new GenericResponse<>();
        try {
            resp.setData(callMultiplierServiceImpl.getTermCodes(filterClientId, appName, token));
            resp.setStatus(Response.ResponseStatus.SUCESS.getCode());
        } catch(UnauthorizedException e) {
            log.error("getTermCodes() call failed due to bad token. "+ e);
            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setStatus(Response.ResponseStatus.AUTH_FAILED.getCode());
            resp.addErrorMessage("Access Denied");
        }  catch(MissingFieldsException e) {
            log.error("getTermCodes() call failed due to missing fields. ", e);
            log.error("getTermCodes() call failed due to missing fields. ", e);
            httpResp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setStatus(Response.ResponseStatus.BAD_REQUEST.getCode());
            resp.addErrorMessage("Missing Required fields");
        } catch(Exception exp) {
            log.error("getTermCodes() Getting client specific filtered termcode list lookup failed. ", exp);
            resp.setStatus(Response.ResponseStatus.SYSTEM.getCode());
            httpResp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
        return resp;
    }



    @GetMapping(value = "/termCodes/{appName}/{token}/",
            produces = "application/json")
    public GenericResponse<Client> getTermCodes(@PathVariable String appName,
                                                @PathVariable String token, HttpServletResponse httpResp) {
        GenericResponse<Client> resp = new GenericResponse<>();
        try {
            resp.setData(callMultiplierServiceImpl.getTermCodes(null, appName, token));
            resp.setStatus(Response.ResponseStatus.SUCESS.getCode());
        } catch(UnauthorizedException e) {
            log.error("getTermCodes() call failed due to bad token. "+e);
            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setStatus(Response.ResponseStatus.AUTH_FAILED.getCode());
            resp.addErrorMessage("Access Denied");
        }  catch(MissingFieldsException e) {
            log.error("getTermCodes() call failed due to missing fields. "+ e);
            httpResp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setStatus(Response.ResponseStatus.BAD_REQUEST.getCode());
            resp.addErrorMessage("Missing Required fields");
        } catch(Exception exp) {
            log.error("getTermCodes() Getting termcode list by app name lookup failed. ", exp);
            resp.setStatus(Response.ResponseStatus.SYSTEM.getCode());
            httpResp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
        return resp;
    }


    @GetMapping(value = "/termCodes/{token}/",
            produces = "application/json")
    public GenericResponse<Client> getTermCodes(@PathVariable String token,
                                                HttpServletResponse httpResp) {
        GenericResponse<Client> resp = new GenericResponse<>();
        try {
            resp.setData(callMultiplierServiceImpl.getTermCodes(null, null, token));
            resp.setStatus(Response.ResponseStatus.SUCESS.getCode());
        } catch(UnauthorizedException e) {
            log.error("getTermCodes() call failed due to bad token. "+e);
            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setStatus(Response.ResponseStatus.AUTH_FAILED.getCode());
            resp.addErrorMessage("Access Denied");
        }  catch(MissingFieldsException e) {
            log.error("getTermCodes() call failed due to missing fields. "+e);
            httpResp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setStatus(Response.ResponseStatus.BAD_REQUEST.getCode());
            resp.addErrorMessage("Missing Required fields");
        } catch(Exception exp) {
            log.error("getTermCodes() Getting termcode list lookup failed. ", exp);
            resp.setStatus(Response.ResponseStatus.SYSTEM.getCode());
            httpResp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
        return resp;
    }


    @PostMapping(value = "/campaigns/",
            produces = "application/json")
    public GenericResponse<CampaignResponse> getCampaigns(
            @RequestBody CampaignRequest req,
            HttpServletResponse httpResp) {
        GenericResponse<CampaignResponse> resp = new GenericResponse<>();
        if(req.getCount() == null) {
            req.setCount(1000);
        }
        if(req.getOffset() == null ) {
            req.setOffset(0);
        }
        try {
            resp.setData( callMultiplierServiceImpl.getCampaigns(req) );
            resp.setStatus(Response.ResponseStatus.SUCESS.getCode());
        } catch(UnauthorizedException e) {
            log.error("getCampaigns() call failed due to bad token. "+e);
            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setStatus(Response.ResponseStatus.AUTH_FAILED.getCode());
            resp.addErrorMessage("Access Denied");
        } catch(MissingFieldsException e) {
            log.error("getCampaigns() call failed due to missing fields. "+e);
            httpResp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setStatus(Response.ResponseStatus.BAD_REQUEST.getCode());
            resp.addErrorMessage("Missing Required fields");
        } catch(DataIntegrityViolationException e) {
            log.error("getCampaigns() Getting campaign list lookup failed. ", e);
            resp.setStatus(Response.ResponseStatus.BAD_REQUEST.getCode());
            httpResp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        } catch(Exception exp) {
            log.error("getCampaigns() Getting campaign list lookup failed. ", exp);
            resp.setStatus(Response.ResponseStatus.SYSTEM.getCode());
            httpResp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return resp;
    }


    /**
     * Gets agents.
     *
     * @param req      the req
     * @return the agents
     */
    @PostMapping ("/agents/")
    public ResponseEntity<GenericResponse<Agent>> getAgents(@RequestBody AgentRequest req) throws Exception {

        GenericResponse<Agent> response = new GenericResponse<>();

        if(req.getCount() == null){
            req.setCount(1000);
        }

        if(req.getCount() == null){
            req.setCount(0);
        }

        response.setData(callMultiplierServiceImpl.getAgents(req));

        if(response.getData().isEmpty()){
            log.info("No found agents");
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }

    /**
     *   The API call this call pulls data from turned out to not be usable.  This
     *   call is no longer made.
     *   2021/11/26: Call was found to be used in Prod Apache logs
     *
     * @param req
     * @param httpResp
     * @return
     * @deprecated
     */
    @PostMapping(value = "/agentAccounts/",
            produces = "application/json")
    @Deprecated
    public GenericResponse<AgentAccount> getAgentAccounts(@RequestBody AgentRequest req,
                                                          HttpServletResponse httpResp) {
        GenericResponse<AgentAccount> resp = new GenericResponse<>();
        try {
            resp.setData( callMultiplierServiceImpl.getAgentAccount(req) );
            resp.setStatus(Response.ResponseStatus.SUCESS.getCode());
        } catch(UnauthorizedException e) {
            log.error("getAgentAccounts() call failed due to bad token. "+e);
            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setStatus(Response.ResponseStatus.AUTH_FAILED.getCode());
            resp.addErrorMessage("Access Denied");
        }  catch(MissingFieldsException e) {
            log.error("getAgentAccounts() call failed due to missing fields. "+ e);
            httpResp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setStatus(Response.ResponseStatus.BAD_REQUEST.getCode());
            resp.addErrorMessage("Invalid request");
        }  catch(Exception exp) {
            log.error("getAgentAccounts() Getting Agent Account list lookup failed. ", exp);
            resp.setStatus(Response.ResponseStatus.SYSTEM.getCode());
            httpResp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return resp;
    }

    @PostMapping(value = "/assesorData/",
            produces = "application/json")
    public GenericResponse<AssessorData> getAssesorData(
            @RequestBody AssessorRequest req, HttpServletResponse httpResp) {
        GenericResponse<AssessorData> resp = new GenericResponse<>();
        try {
            resp.setData( callMultiplierServiceImpl.getAssessorData(req) );
            resp.setStatus(Response.ResponseStatus.SUCESS.getCode());
        } catch(MissingFieldsException e) {
            log.error("getAssesorData() call failed due to missing fields. "+e);
            httpResp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setStatus(Response.ResponseStatus.BAD_REQUEST.getCode());
            resp.addErrorMessage("Missing Required fields");
        } catch(UnauthorizedException e) {
            log.error("getAssesorData() call failed due to bad token. "+e);
            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setStatus(Response.ResponseStatus.AUTH_FAILED.getCode());
            resp.addErrorMessage("Access Denied");
        }  catch(Exception exp) {
            log.error("getAssesorData() Getting Agent Account list lookup failed. ", exp);
            resp.setStatus(Response.ResponseStatus.SYSTEM.getCode());
            httpResp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return resp;
    }

}