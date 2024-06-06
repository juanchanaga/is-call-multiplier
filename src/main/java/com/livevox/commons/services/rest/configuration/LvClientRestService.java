/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.services.rest.configuration;

import com.livevox.commons.domain.ClientGeneralSettings;
import com.livevox.commons.domain.ClientInternalDetails;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;

public interface LvClientRestService extends Serializable {

    public ClientInternalDetails getClientInternalDetails(Integer clientId)
            throws WebServiceException;

    public ClientInternalDetails getClientInternalDetails(Integer clientId, String apiSessionId)
            throws WebServiceException;

    public ClientInternalDetails getClientInternalDetails(Integer clientId, String apiSessionId, String apiVersion)
            throws WebServiceException;

    public ClientGeneralSettings getClientGeneralSettings(Integer clientId, String apiSessionId)
            throws WebServiceException;

    public ClientGeneralSettings getClientGeneralSettings(Integer clientId, String apiSessionId, String apiVersion)
            throws WebServiceException;

    public ClientGeneralSettings getClientGeneralSettings(Integer clientId)
            throws WebServiceException;

}
