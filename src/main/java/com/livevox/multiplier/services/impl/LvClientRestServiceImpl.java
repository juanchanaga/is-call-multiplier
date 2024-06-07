/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.services.impl;

import com.livevox.multiplier.domain.ClientGeneralSettings;
import com.livevox.multiplier.domain.ClientInternalDetails;
import com.livevox.multiplier.services.config.AtgConfigService;
import com.livevox.multiplier.services.LvClientRestService;
import com.livevox.multiplier.services.RestServiceAbstract;
import jakarta.xml.ws.WebServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class LvClientRestServiceImpl extends LvSessionRestServiceImpl
        implements LvClientRestService, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4375041737722191172L;

    public static final String URI_BASE_PREFIX = "/configuration/" ;

    public static final String URI_BASE_SUFFIX = "/clients/";

    @Autowired
    public LvClientRestServiceImpl(AtgConfigService atgConfigService) {
        super(atgConfigService);
    }

    @Override
    public ClientInternalDetails getClientInternalDetails(Integer clientId)
            throws WebServiceException {
        return getClientInternalDetails(clientId, null, null);
    }


    @Override
    public ClientInternalDetails getClientInternalDetails(Integer clientId, String apiSessionId)
            throws WebServiceException {
        return getClientInternalDetails(clientId, apiSessionId, null);
    }


    @Override
    public ClientInternalDetails getClientInternalDetails(Integer clientId, String apiSessionId,
                                                          String apiVersion) throws WebServiceException {
        RestServiceAbstract.log.debug("Start getClientInternalDetails() ");
        if (clientId == null) {
            throw new WebServiceException(" Required value was null ");
        }
        ClientInternalDetails resp = null;
        try {
            resp = makeLvCall(
                    getURI(URI_BASE_PREFIX, URI_BASE_SUFFIX + "/" + clientId + "/internal", apiVersion, clientId),
                    HttpMethod.GET, null, ClientInternalDetails.class, clientId, apiSessionId);
        } catch (WebServiceException wsExp) {
            throw new WebServiceException("Could not recover from auth failure." + wsExp, wsExp);
        } catch (Exception e) {
            RestServiceAbstract.log.error("Get client internal properties failed. ", e);
            throw new WebServiceException("Get  client internal properties failed. ");
        }

        if (resp == null) {
            RestServiceAbstract.log.error("Get client internal properties failed. The response was invalid. ");
            throw new WebServiceException("Get client internal properties failed. The response was invalid ");
        }
        RestServiceAbstract.log.debug("End getClientInternalDetails() ");
        return resp;
    }


    @Override
    public ClientGeneralSettings getClientGeneralSettings(Integer clientId)
            throws WebServiceException {
        return getClientGeneralSettings(clientId, null, null);
    }


    @Override
    public ClientGeneralSettings getClientGeneralSettings(Integer clientId, String apiSessionId)
            throws WebServiceException {
        return getClientGeneralSettings(clientId, apiSessionId, null);
    }


    @Override
    public ClientGeneralSettings getClientGeneralSettings(Integer clientId, String apiSessionId, String apiVersion)
            throws WebServiceException {
        RestServiceAbstract.log.debug("Start getClientGeneralSettings() ");
        if (clientId == null) {
            throw new WebServiceException(" Required value was null ");
        }
        ClientGeneralSettings resp = null;
        try {
            resp = makeLvCall(
                    getURI(URI_BASE_PREFIX, URI_BASE_SUFFIX + "/" + clientId + "/settings", apiVersion, clientId),
                    HttpMethod.GET, null, ClientGeneralSettings.class, clientId, apiSessionId);
        } catch (WebServiceException wsExp) {
            throw new WebServiceException("Could not recover from auth failure." + wsExp, wsExp);
        } catch (Exception e) {
            RestServiceAbstract.log.error("Get client general properties failed. ", e);
            throw new WebServiceException("Get  client general properties failed. ");
        }

        if (resp == null) {
            RestServiceAbstract.log.error("Get client general properties failed. The response was invalid. ");
            throw new WebServiceException("Get client general properties failed. The response was invalid ");
        }
        RestServiceAbstract.log.debug("End getClientGeneralSettings() ");
        return resp;
    }

}
