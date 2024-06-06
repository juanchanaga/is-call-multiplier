package com.livevox.commons.services.rest.campaign;

import com.livevox.integration.commons.domain.lvapi.Campaign;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface LvCampaignRestService extends Serializable {

    public List<Campaign> FindMatchingCampaigns(Integer clientId, Date startDate, Date endDate, Integer count, Integer offset, List<Integer> types, List<Long> services, List<String> states);

    public List<Campaign> FindMatchingCampaigns(Integer clientId, Date startDate, Date endDate, Integer count, Integer offset, List<Integer> types, List<Long> services, List<String> states, String apiSessionId);

    public List<Campaign> FindMatchingCampaigns(Integer clientId, Date startDate, Date endDate, Integer count, Integer offset, List<Integer> types, List<Long> services, List<String> states, String apiSessionId, String apiVersion);

}