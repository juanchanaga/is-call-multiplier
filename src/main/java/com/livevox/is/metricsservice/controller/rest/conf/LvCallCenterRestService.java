/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.controller.rest.conf;


import com.livevox.is.metricsservice.domain.CallCenter;

import javax.xml.ws.WebServiceException;
import java.util.List;

public interface LvCallCenterRestService {

    public List<CallCenter> getAllCallCenters(Integer clientId) throws WebServiceException;

    public List<CallCenter> getAllCallCenters(Integer clientId, String apiSessionId) throws WebServiceException;

    public List<CallCenter> getAllCallCenters(Integer clientId, String apiSessionId,
                                              String apiVersion) throws WebServiceException;

    public List<CallCenter> getCallCenters(Integer clientId, Integer count, Integer offset)
            throws WebServiceException;

    public List<CallCenter> getCallCenters(Integer clientId, Integer count,
                                           Integer offset, String apiVersion) throws WebServiceException;

    public List<CallCenter> getCallCenters(Integer clientId, Integer count, Integer offset,
                                           boolean returnAll) throws WebServiceException;

    public List<CallCenter> getCallCenters(Integer clientId, Integer count, Integer offset,
                                           boolean returnAll, String apiVersion) throws WebServiceException;

    public List<CallCenter> getCallCenters(Integer clientId, String apiSessionId, Integer count, Integer offset,
                                           boolean returnAll) throws WebServiceException;

    public List<CallCenter> getCallCenters(Integer clientId, String apiSessionId, Integer count, Integer offset,
                                           boolean returnAll, String apiVersion) throws WebServiceException;
}
