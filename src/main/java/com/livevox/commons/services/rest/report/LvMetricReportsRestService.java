/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.services.rest.report;

import com.livevox.integration.commons.domain.stats.ServiceMetrics;
import com.livevox.integration.commons.domain.stats.ServiceMetricsRequest;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.List;

public interface LvMetricReportsRestService extends Serializable {

    List<ServiceMetrics> retrieveOutboundMetrics(Integer clientId, String apiSessionId,
                                                 ServiceMetricsRequest request) throws WebServiceException;

    List<ServiceMetrics> retrieveInboundMetrics(Integer clientId, String apiSessionId,
                                                ServiceMetricsRequest request) throws WebServiceException;
}
