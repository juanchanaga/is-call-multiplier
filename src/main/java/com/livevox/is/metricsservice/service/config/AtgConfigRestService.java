/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.service.config;

import com.livevox.is.metricsservice.domain.config.ClientsAndPropertiesResponse;

import javax.xml.ws.WebServiceException;
import java.io.Serializable;

public interface AtgConfigRestService extends Serializable {


    ClientsAndPropertiesResponse getAppConfigInfo() throws WebServiceException;


    String getApiSession(Integer clientId) throws WebServiceException;


}

