package com.livevox.commons.services.rest.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.livevox.integration.commons.domain.stats.DateRange;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@JsonInclude(Include.NON_NULL)
@Data
@ToString( callSuper = true)
public class CampaignsRequest implements Serializable {
    private DateRange dateRange;
    private RestFilter service;
    private RestFilter type;
    private Date windowEnd;
    private Date windowStart;
    private List<String> state;

    public CampaignsRequest() {
    }
}