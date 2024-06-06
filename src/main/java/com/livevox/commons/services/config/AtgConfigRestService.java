/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.services.config;

import com.livevox.commons.domain.config.*;
import jakarta.xml.ws.WebServiceException;

import java.io.Serializable;

public interface AtgConfigRestService extends Serializable {


    ClientsAndPropertiesResponse getAppConfigInfo() throws WebServiceException;


    String getApiSession(Integer clientId) throws WebServiceException;


}

