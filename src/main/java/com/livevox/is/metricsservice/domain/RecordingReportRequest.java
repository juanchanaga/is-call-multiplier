package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
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
