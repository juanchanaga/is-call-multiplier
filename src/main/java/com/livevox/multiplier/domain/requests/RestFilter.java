/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.domain.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.integration.commons.domain.stats.IdType;
import com.livevox.integration.commons.domain.stats.ResultType;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class RestFilter implements Serializable, Cloneable {

    private static final long serialVersionUID = 7409910233901640121L;
    private List<IdType> callCenter;
    private List<IdType> service;
    private List<IdType> type;
    private List<IdType> agent;
    private List<IdType> campaign;
    private String pattern;
    private List<IdType> campaignType;
    private List<RecordingType> exclusions;
    private List<ResultType> result;

    public RestFilter clone() {
        RestFilter newFilter = new RestFilter();
        newFilter.setPattern(this.getPattern());
        if (this.getCallCenter() != null) {
            newFilter.setCallCenter(new ArrayList(this.getCallCenter()));
        }

        if (this.getService() != null) {
            newFilter.setService(new ArrayList(this.getService()));
        }

        if (this.getAgent() != null) {
            newFilter.setAgent(new ArrayList(this.getAgent()));
        }

        if (this.getCampaign() != null) {
            newFilter.setCampaign(new ArrayList(this.getCampaign()));
        }

        if (this.getCampaignType() != null) {
            newFilter.setCampaignType(new ArrayList(this.getCampaignType()));
        }

        if (this.getType() != null) {
            newFilter.setType(new ArrayList(this.getType()));
        }

        if (this.getExclusions() != null) {
            newFilter.setExclusions(new ArrayList(this.getExclusions()));
        }

        if (this.getResult() != null) {
            newFilter.setResult(new ArrayList(this.getResult()));
        }

        return newFilter;
    }

    public void addCallCenter(Long id) {
        if (id != null) {
            if (this.callCenter == null) {
                this.callCenter = new ArrayList();
            }

            this.callCenter.add(new IdType(id));
        }

    }

    public void addService(Long id) {
        if (id != null) {
            if (this.service == null) {
                this.service = new ArrayList();
            }

            this.service.add(new IdType(id));
        }

    }

    public void addType(Integer id) {
        if (id != null) {
            if (this.type == null) {
                this.type = new ArrayList();
            }

            this.type.add(new IdType(id.longValue()));
        }

    }

    public void addCampaign(Long id) {
        if (id != null) {
            if (this.campaign == null) {
                this.campaign = new ArrayList();
            }

            this.campaign.add(new IdType(id));
        }

    }

    public void addAgent(Long id) {
        if (id != null) {
            if (this.agent == null) {
                this.agent = new ArrayList();
            }

            this.agent.add(new IdType(id));
        }

    }

    public void addResult(Integer id) {
        if (id != null) {
            if (this.result == null) {
                this.result = new ArrayList();
            }

            this.result.add(new ResultType(id));
        }

    }

    public void addExclusion(String id) {
        if (id != null) {
            if (this.exclusions == null) {
                this.exclusions = new ArrayList();
            }

            this.exclusions.add(new RecordingType(id));
        }

    }

    public void addCampaignType(Integer id) {
        if (id != null) {
            if (this.campaignType == null) {
                this.campaignType = new ArrayList();
            }

            this.campaignType.add(new IdType(id.longValue()));
        }

    }
}
