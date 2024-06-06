/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.commons.services.rest.domain.RecordingReportRequest;
import com.livevox.commons.services.rest.domain.RecordingReportRequest.SortBy;
import com.livevox.commons.services.rest.domain.RestFilter;
import com.livevox.integration.commons.domain.lvapi.ListRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CallRecordingRequest extends ListRequest implements Serializable {

    private static final long serialVersionUID = 38651873445112L;

    /**
     *  This is only used with the call recording COMPLIANCE API.   It will break the call recording report API.
     *
     */
    private List<Long> agentIds;

    private List<Long> callCenterIds;

    private Long campaignId;

    private List<Long> termCodes;

    private List<Long> serviceIds;

    private Date startDate;

    private Date endDate;

    private Integer durationMin;

    private Integer durationMax;

    private String account;

    private String phone;

    private RestFilter filter;

    private String originalAccountNumber;

    private SortBy sortBy;

    private String id;

    private String apiVersion;


    public CallRecordingRequest() {
    }

    public void addAgentId(Long agentId) {
        if (agentIds == null) {
            agentIds = new ArrayList<>();
        }
        agentIds.add(agentId);
    }

    public void addCallCenterId(Long callCenterId) {
        if (callCenterIds == null) {
            callCenterIds = new ArrayList<>();
        }
        callCenterIds.add(callCenterId);
    }


    public void addTermCode(Long termCode) {
        if (termCodes == null) {
            termCodes = new ArrayList<>();
        }
        termCodes.add(termCode);
    }


    public void addServiceId(Long serviceId) {
        if (serviceIds == null) {
            serviceIds = new ArrayList<>();
        }
        serviceIds.add(serviceId);
    }


    @Override
    public String getApiVersion() {
        return apiVersion;
    }

    @Override
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
}
