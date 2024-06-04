/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.controller.rest.conf;


import com.livevox.is.metricsservice.domain.ClientGeneralSettings;
import com.livevox.is.metricsservice.domain.ClientInternalDetails;

import javax.xml.ws.WebServiceException;
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
