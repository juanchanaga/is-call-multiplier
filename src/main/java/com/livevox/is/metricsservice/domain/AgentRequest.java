package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.integration.commons.domain.lvapi.ListRequest;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class AgentRequest extends ListRequest implements Serializable {

    private RestFilter filter;
    private String application;
    private Date startDate;
    private Date endDate;
    private String account;
    private String originalAccountNumber;
    private String loginId;
    private String lastName;
    private String phoneNumber;

}
