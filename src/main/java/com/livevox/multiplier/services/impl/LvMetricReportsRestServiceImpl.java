/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.services.impl;

import com.livevox.multiplier.domain.requests.ListRestResponse;
import com.livevox.integration.commons.domain.stats.ServiceMetrics;
import com.livevox.integration.commons.domain.stats.ServiceMetricsRequest;
import com.livevox.multiplier.services.config.AtgConfigService;
import com.livevox.multiplier.services.LvMetricReportsRestService;
import com.livevox.multiplier.services.RestServiceAbstract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpMethod;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.List;

public class LvMetricReportsRestServiceImpl extends LvSessionRestServiceImpl
        implements LvMetricReportsRestService, Serializable {

    private static final long serialVersionUID = -3843139356651502499L;

    private static final String URI_PREFIX = "/reporting/";

    private static final String URI_SUFFIX =  "/metrics/";

    private static final String INBOUND_URI = URI_SUFFIX + "outbound";

    private static final String OUTBOUND_URI = URI_SUFFIX + "inbound";

    @Autowired
    public LvMetricReportsRestServiceImpl(AtgConfigService atgConfigService) {
        super(atgConfigService);
    }


    @Override
    public List<ServiceMetrics> retrieveOutboundMetrics(Integer clientId, String apiSessionId, ServiceMetricsRequest request) throws WebServiceException {
        RestServiceAbstract.log.debug("Start retrieveOutboundMetrics() ");
        if (request == null || request.getInterval() == null
                || request.getService() == null ||  request.getEnd() == null ||  request.getStart() == null
                || clientId == null) {
            throw new WebServiceException("Required value is missing");
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getURI(URI_PREFIX,OUTBOUND_URI,clientId));
        ListRestResponse resp;
        try {
            resp = makeLvCall(builder.toUriString(), HttpMethod.POST, request, ListRestResponse.class, clientId,
                    apiSessionId);
        } catch (WebServiceException wsExp) {
            throw new WebServiceException("Could not recover from auth failure." + wsExp, wsExp);
        } catch (Exception e) {
            RestServiceAbstract.log.error("retrieveOutboundMetrics failed. ", e);
            throw new WebServiceException("retrieveOutboundMetrics failed. ");
        }
        if (resp == null) {
            RestServiceAbstract.log.error("Unable to read outbound metrics. The response was invalid. ");
            throw new WebServiceException("Read outbound metrics failed. The response was invalid ");
        }
        RestServiceAbstract.log.debug("End retrieveOutboundMetrics() ");
        return resp.getMetrics();
    }

    @Override
    public List<ServiceMetrics> retrieveInboundMetrics(Integer clientId, String apiSessionId, ServiceMetricsRequest request) throws WebServiceException {
        RestServiceAbstract.log.debug("Start retrieveInboundMetrics() ");
        if (request == null || request.getInterval() == null
                || request.getService() == null ||  request.getEnd() == null ||  request.getStart() == null
                || clientId == null) {
            throw new WebServiceException("Required value is missing");
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getURI(URI_PREFIX,INBOUND_URI,clientId));
        ListRestResponse resp;
        try {
            resp = makeLvCall(builder.toUriString(), HttpMethod.POST, request, ListRestResponse.class, clientId,
                    apiSessionId);
        } catch (WebServiceException wsExp) {
            throw new WebServiceException("Could not recover from auth failure." + wsExp, wsExp);
        } catch (Exception e) {
            RestServiceAbstract.log.error("retrieveInboundMetrics failed. ", e);
            throw new WebServiceException("retrieveInboundMetrics failed. ");
        }
        if (resp == null) {
            RestServiceAbstract.log.error("Unable to read inbound metrics. The response was invalid. ");
            throw new WebServiceException("Read inbound metrics failed. The response was invalid ");
        }
        RestServiceAbstract.log.debug("End retrieveInboundMetrics() ");
        return resp.getMetrics();
    }
}
