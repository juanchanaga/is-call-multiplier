/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.services.rest.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.commons.services.rest.domain.ReportFilter;
import lombok.Data;

import java.util.Date;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class RecordingReportRequest {


    private Date startDate;

    private Date endDate;

    private ReportFilter filter;

    private SortBy sortBy;

    public enum SortBy {
        CALL_START_TIME,
        ACCOUNT,
        AGENT;
    }

    public void addCallCenterId(Long callCenterId) {
        if(filter == null) {
            filter = new ReportFilter();
        }
        filter.addCallCenter(callCenterId);
    }


    public void addTermCodeId(Long termCodeId) {
        if(filter == null) {
            filter = new ReportFilter();
        }
        filter.addResultCode(termCodeId);
    }


    public void addServiceId(Long serviceId) {
        if(filter == null) {
            filter = new ReportFilter();
        }
        filter.addService(serviceId);
    }

}
