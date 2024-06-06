package com.livevox.commons.services.rest.campaign;


import com.livevox.commons.exceptions.InvalidParametersException;
import com.livevox.commons.exceptions.UnauthorizedException;
import com.livevox.commons.services.config.AtgConfigService;
import com.livevox.commons.services.rest.domain.CampaignsRequest;
import com.livevox.commons.services.rest.domain.ListRestResponse;
import com.livevox.commons.services.rest.domain.RestFilter;
import com.livevox.commons.services.rest.session.LvSessionRestServiceImpl;
import com.livevox.integration.commons.domain.lvapi.Campaign;
import com.livevox.integration.commons.domain.stats.DateRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.xml.ws.WebServiceException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Service
public class LvCampaignRestServiceImpl extends LvSessionRestServiceImpl implements LvCampaignRestService, Serializable {
    private static final long serialVersionUID = 2631151774641496349L;

    public static final String URI_PREFIX = "/campaign/";

    public static final String URI_SUFFIX = "/campaigns/";

    public static final String FIND_CAMPAIGNS_URI = "search";

    public static final String FIND_FINISHED_CALLS_URI = "{campaignId}/finishedCalls";

    public static final String APPEND_CAMPAIGN_RECORD_URI = "{campaignId}/transactions";

    public static final Integer MAX_APPEND_RECORDS = 10000;

    @Autowired(required = false)
    public LvCampaignRestServiceImpl(AtgConfigService atgConfigService) {
        super(atgConfigService);
        this.restTemplate = new RestTemplate(this.requestFactory(1800000, 1800000));
    }

    public List<Campaign> FindMatchingCampaigns(Integer clientId, Date startDate, Date endDate, Integer count, Integer offset, List<Integer> types, List<Long> services, List<String> states) {
        return FindMatchingCampaigns(clientId, startDate, endDate, count, offset, types, services, states, null);
    }

    public List<Campaign> FindMatchingCampaigns(Integer clientId, Date startDate, Date endDate, Integer count, Integer offset, List<Integer> types, List<Long> services, List<String> states, String apiSessionId) {
        return FindMatchingCampaigns(clientId, startDate, endDate, count, offset, types, services, states, apiSessionId, null);
    }

    public List<Campaign> FindMatchingCampaigns(Integer clientId, Date startDate, Date endDate, Integer count, Integer offset, List<Integer> types, List<Long> services, List<String> states, String apiSessionId, String apiVersion) {
        log.debug("Start FindMatchingCampaigns() ");
        if (startDate == null || endDate == null || clientId == null || count == null || offset == null)
            throw new InvalidParametersException(" Required value was null ");
        ListRestResponse resp;
        try {
            CampaignsRequest request = new CampaignsRequest();
            if (types != null) {
                RestFilter filterType = new RestFilter();
                for (Integer type : types)
                    filterType.addType(type);
                request.setType(filterType);
            }
            if (services != null) {
                RestFilter skillFilter = new RestFilter();
                for (Long srvcId : services)
                    skillFilter.addService(srvcId);
                request.setService(skillFilter);
            }
            if (states != null)
                request.setState(states);
            DateRange dateRange = new DateRange();
            dateRange.setTo(endDate);
            dateRange.setFrom(startDate);
            request.setDateRange(dateRange);

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.getURI(URI_PREFIX,URI_SUFFIX + FIND_CAMPAIGNS_URI, apiVersion, clientId));
            builder.queryParam("count", count);
            builder.queryParam("offset", offset);
            resp = this.makeLvCall(builder.toUriString(), HttpMethod.POST, request, ListRestResponse.class, clientId, apiSessionId);

        } catch (UnauthorizedException wsExp) {
            throw new UnauthorizedException("Could not recover from auth failure." + wsExp);
        } catch (Exception e) {
            log.error("List campaigns failed. ", e);
            throw new WebServiceException("List campaigns failed. "+e);
        }
        if (resp == null) {
            log.error("Unable to retrieve campaigns. The response was invalid. ");
            throw new WebServiceException("List campaigns failed. The response was invalid ");
        }
        log.debug("End FindMatchingCampaigns() ");
        return resp.getCampaign();
    }

}