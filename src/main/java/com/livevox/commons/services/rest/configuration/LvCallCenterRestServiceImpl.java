/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.services.rest.configuration;

import com.livevox.commons.services.config.AtgConfigService;
import com.livevox.commons.services.rest.domain.ListRestResponse;
import com.livevox.commons.services.rest.session.LvSessionRestServiceImpl;
import com.livevox.integration.commons.domain.lvapi.CallCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.List;

@Service
public class LvCallCenterRestServiceImpl extends LvSessionRestServiceImpl
        implements LvCallCenterRestService, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3970494692926795700L;

    public static final String URI_PREFIX = "/configuration/" ;

    public static final String URI_SUFFIX =  "/callCenters";

    @Autowired
    public LvCallCenterRestServiceImpl(AtgConfigService atgConfigService) {
        super(atgConfigService);
    }

    @Override
    public List<CallCenter> getAllCallCenters(Integer clientId) throws WebServiceException {
        return getAllCallCenters(clientId, null);
    }


    @Override
    public List<CallCenter> getAllCallCenters(Integer clientId, String apiSessionId) throws WebServiceException {
        return getCallCenters(clientId, apiSessionId, 1000, 0, true, null);
    }


    @Override
    public List<CallCenter> getAllCallCenters(Integer clientId, String apiSessionId, String apiVersion) throws WebServiceException {
        return getCallCenters(clientId, apiSessionId, 1000, 0, true, apiVersion);
    }


    @Override
    public List<CallCenter> getCallCenters(Integer clientId, Integer count, Integer offset)
            throws WebServiceException {
        return getCallCenters(clientId, count, offset, false, null);
    }


    @Override
    public List<CallCenter> getCallCenters(Integer clientId, Integer count, Integer offset, String apiVersion)
            throws WebServiceException {
        return getCallCenters(clientId, count, offset, false, apiVersion);
    }


    @Override
    public List<CallCenter> getCallCenters(Integer clientId, Integer count, Integer offset,
                                           boolean returnAll, String apiVersion) throws WebServiceException {
        return getCallCenters(clientId, null, count, offset, returnAll, apiVersion);
    }


    public List<CallCenter> getCallCenters(Integer clientId, Integer count, Integer offset,
                                           String apiSessionId, boolean returnAll) throws WebServiceException {
        return getCallCenters(clientId, apiSessionId, count, offset, returnAll, null);
    }


    @Override
    public List<CallCenter> getCallCenters(Integer clientId, Integer count, Integer offset,
                                           boolean returnAll) throws WebServiceException {
        return getCallCenters(clientId, null, count, offset, returnAll);
    }


    @Override
    public List<CallCenter> getCallCenters(Integer clientId, String apiSessionId, Integer count,
                                           Integer offset, boolean returnAll) throws WebServiceException {
        return 	getCallCenters(clientId, apiSessionId, count, offset, returnAll, null);
    }


    @Override
    public List<CallCenter> getCallCenters(Integer clientId, String apiSessionId, Integer count,
                                           Integer offset, boolean returnAll, String apiVersion) throws WebServiceException {
        log.debug("Start getAllCallCenters() for client " +  clientId);
        if (clientId == null || count == null || offset == null) {
            throw new WebServiceException(" Required value was null ");
        }
        ListRestResponse resp = null;
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(
                    getURI(URI_PREFIX,  URI_SUFFIX, apiVersion, clientId)  );
            builder.queryParam("client", clientId);
            builder.queryParam("offset", offset);
            builder.queryParam("count", count);
            resp = makeLvCall(builder.toUriString(), HttpMethod.GET, null, ListRestResponse.class, clientId, apiSessionId);
        } catch (WebServiceException wsExp) {
            throw new WebServiceException("Could not recover from auth failure." + wsExp, wsExp);
        } catch (Exception e) {
            log.error("List call centers failed. ", e);
            throw new WebServiceException("List call centers failed. ");
        }

        if (resp == null) {
            log.error("Unable to retrieve call centers. The response was invalid. ");
            throw new WebServiceException("List call centers failed. The response was invalid ");
        }

        if (returnAll && resp.isHasMore()) {
            List<CallCenter> moreCC = getCallCenters(clientId, count, offset + count, returnAll);
            if (moreCC != null) {
                resp.getCallCenter().addAll(moreCC);
            }
        }

        log.debug("End getAllCallCenters() ");
        return resp.getCallCenter();
    }
}
